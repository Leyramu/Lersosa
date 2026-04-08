use std::fmt::Write as _;

use crate::{
    dto::chat::{ChatMessage, ChatRole, ChatToolChoice, ChatToolDefinition},
    error::ServiceError,
};

#[derive(Debug, Clone, Copy)]
pub enum SpecialToken {
    Pad,
    Bos,
    Eos,
    Unk,
    System,
    User,
    Assistant,
    Tool,
    ToolChoice,
    EndOfTools,
    EndOfTurn,
}

impl SpecialToken {
    pub fn id(self) -> i64 {
        match self {
            Self::Pad => 0,
            Self::Bos => 1,
            Self::Eos => 2,
            Self::Unk => 3,
            Self::System => 4,
            Self::User => 5,
            Self::Assistant => 6,
            Self::Tool => 7,
            Self::ToolChoice => 8,
            Self::EndOfTools => 9,
            Self::EndOfTurn => 10,
        }
    }
}

#[derive(Debug, Clone)]
pub struct ChatTokenizer {
    byte_offset: i64,
}

impl Default for ChatTokenizer {
    fn default() -> Self {
        Self { byte_offset: 256 }
    }
}

impl ChatTokenizer {
    pub fn byte_offset(&self) -> i64 {
        self.byte_offset
    }

    pub fn role_token_id(&self, role: ChatRole) -> i64 {
        match role {
            ChatRole::System => SpecialToken::System.id(),
            ChatRole::User => SpecialToken::User.id(),
            ChatRole::Assistant => SpecialToken::Assistant.id(),
        }
    }

    pub fn default_stop_token_id(&self) -> i64 {
        SpecialToken::EndOfTurn.id()
    }

    pub fn encode_bytes(&self, text: &str) -> Vec<i64> {
        text.as_bytes()
            .iter()
            .map(|byte| i64::from(*byte) + self.byte_offset)
            .collect()
    }

    pub fn decode_bytes(&self, tokens: &[i64]) -> String {
        let mut bytes = Vec::new();

        for &token in tokens {
            if token == SpecialToken::EndOfTurn.id() || token == SpecialToken::Eos.id() {
                break;
            }
            if token >= self.byte_offset && token <= self.byte_offset + 255 {
                bytes.push((token - self.byte_offset) as u8);
            }
        }

        String::from_utf8_lossy(&bytes).to_string()
    }

    pub fn decode_stream_token(&self, token: i64, pending_bytes: &mut Vec<u8>) -> Option<String> {
        if token == SpecialToken::EndOfTurn.id() || token == SpecialToken::Eos.id() {
            if pending_bytes.is_empty() {
                return None;
            }
            let text = String::from_utf8_lossy(pending_bytes).to_string();
            pending_bytes.clear();
            return Some(text);
        }

        if token < self.byte_offset || token > self.byte_offset + 255 {
            return None;
        }

        pending_bytes.push((token - self.byte_offset) as u8);
        match core::str::from_utf8(pending_bytes.as_slice()) {
            Ok(text) => {
                let out = text.to_string();
                pending_bytes.clear();
                Some(out)
            }
            Err(_) => None,
        }
    }

    pub fn encode_chat_prompt(
        &self,
        system: Option<&str>,
        messages: &[ChatMessage],
        tools: &[ChatToolDefinition],
        tool_choice: Option<&ChatToolChoice>,
    ) -> Result<Vec<i64>, ServiceError> {
        validate_messages(system, messages)?;
        validate_tools(tools, tool_choice)?;

        let mut tokens = Vec::new();
        tokens.push(SpecialToken::Bos.id());

        if let Some(system_text) = system {
            if !system_text.trim().is_empty() {
                self.push_turn(&mut tokens, ChatRole::System, system_text.trim());
            }
        }

        self.push_tools_block_tokens(&mut tokens, tools, tool_choice);

        for message in messages {
            self.push_turn(&mut tokens, message.role.clone(), message.content.trim());
        }

        // Generation starts at assistant role token.
        tokens.push(SpecialToken::Assistant.id());
        Ok(tokens)
    }

    pub fn render_prompt(
        &self,
        system: Option<&str>,
        messages: &[ChatMessage],
        tools: &[ChatToolDefinition],
        tool_choice: Option<&ChatToolChoice>,
    ) -> Result<String, ServiceError> {
        validate_messages(system, messages)?;
        validate_tools(tools, tool_choice)?;

        let mut prompt = String::new();

        if let Some(system_text) = system {
            if !system_text.trim().is_empty() {
                let _ = writeln!(&mut prompt, "<|system|>");
                let _ = writeln!(&mut prompt, "{}", system_text.trim());
            }
        }

        self.render_tools_block(&mut prompt, tools, tool_choice);

        for message in messages {
            let role = match message.role {
                ChatRole::System => "system",
                ChatRole::User => "user",
                ChatRole::Assistant => "assistant",
            };
            let _ = writeln!(&mut prompt, "<|{role}|>");
            let _ = writeln!(&mut prompt, "{}", message.content.trim());
        }

        let _ = writeln!(&mut prompt, "<|assistant|>");
        Ok(prompt)
    }

    fn push_turn(&self, tokens: &mut Vec<i64>, role: ChatRole, content: &str) {
        tokens.push(self.role_token_id(role));
        tokens.extend(self.encode_bytes(content));
        tokens.push(SpecialToken::EndOfTurn.id());
    }

    fn push_tools_block_tokens(
        &self,
        tokens: &mut Vec<i64>,
        tools: &[ChatToolDefinition],
        tool_choice: Option<&ChatToolChoice>,
    ) {
        if tools.is_empty() && tool_choice.is_none() {
            return;
        }

        tokens.push(SpecialToken::Tool.id());
        for tool in tools {
            tokens.extend(self.encode_bytes(format!("name={}", tool.name).as_str()));
            tokens.push(SpecialToken::EndOfTurn.id());
            if let Some(description) = &tool.description {
                tokens.extend(self.encode_bytes(format!("description={}", description.trim()).as_str()));
                tokens.push(SpecialToken::EndOfTurn.id());
            }
            if let Some(parameters_json) = &tool.parameters_json {
                tokens.extend(self.encode_bytes(format!("parameters={}", parameters_json.trim()).as_str()));
                tokens.push(SpecialToken::EndOfTurn.id());
            }
        }
        if let Some(choice) = tool_choice {
            tokens.push(SpecialToken::ToolChoice.id());
            tokens.extend(self.encode_bytes(render_tool_choice(choice).as_str()));
            tokens.push(SpecialToken::EndOfTurn.id());
        }
        tokens.push(SpecialToken::EndOfTools.id());
    }

    fn render_tools_block(
        &self,
        prompt: &mut String,
        tools: &[ChatToolDefinition],
        tool_choice: Option<&ChatToolChoice>,
    ) {
        if tools.is_empty() && tool_choice.is_none() {
            return;
        }

        let _ = writeln!(prompt, "<|tools|>");
        for tool in tools {
            let _ = writeln!(prompt, "- name: {}", tool.name);
            if let Some(description) = &tool.description {
                let _ = writeln!(prompt, "  description: {}", description.trim());
            }
            if let Some(parameters_json) = &tool.parameters_json {
                let _ = writeln!(prompt, "  parameters: {}", parameters_json.trim());
            }
        }
        if let Some(choice) = tool_choice {
            let _ = writeln!(prompt, "<|tool_choice|>");
            let _ = writeln!(prompt, "{}", render_tool_choice(choice));
        }
        let _ = writeln!(prompt, "<|end_tools|>");
    }
}

fn render_tool_choice(choice: &ChatToolChoice) -> String {
    match choice {
        ChatToolChoice::Auto => "auto".to_string(),
        ChatToolChoice::None => "none".to_string(),
        ChatToolChoice::Required => "required".to_string(),
        ChatToolChoice::Named(name) => format!("named:{name}"),
    }
}

pub fn validate_messages(system: Option<&str>, messages: &[ChatMessage]) -> Result<(), ServiceError> {
    if messages.is_empty() {
        return Err(ServiceError::Validation("messages must not be empty".to_string()));
    }

    if let Some(system_text) = system {
        if system_text.trim().is_empty() {
            return Err(ServiceError::Validation("system must not be empty when provided".to_string()));
        }
        if matches!(messages.first().map(|message| &message.role), Some(ChatRole::System)) {
            return Err(ServiceError::Validation(
                "use either top-level system or leading system message, not both".to_string(),
            ));
        }
    }

    for (index, message) in messages.iter().enumerate() {
        if message.content.trim().is_empty() {
            return Err(ServiceError::Validation(format!(
                "message at index {index} must not be empty"
            )));
        }

        if index > 0 && matches!(message.role, ChatRole::System) {
            return Err(ServiceError::Validation(
                "system role is only allowed as the first message".to_string(),
            ));
        }
    }

    let dialogue_start = if matches!(messages.first().map(|message| &message.role), Some(ChatRole::System)) {
        1
    } else {
        0
    };

    if dialogue_start >= messages.len() {
        return Err(ServiceError::Validation(
            "conversation must include at least one user message".to_string(),
        ));
    }

    if !matches!(messages[dialogue_start].role, ChatRole::User) {
        return Err(ServiceError::Validation(
            "conversation must start with a user message after optional system prompt".to_string(),
        ));
    }

    for index in (dialogue_start + 1)..messages.len() {
        let previous = &messages[index - 1].role;
        let current = &messages[index].role;
        if !matches!(current, ChatRole::User | ChatRole::Assistant) || previous == current {
            return Err(ServiceError::Validation(
                "messages must strictly alternate between user and assistant".to_string(),
            ));
        }
    }

    if !matches!(messages.last().map(|message| &message.role), Some(ChatRole::User)) {
        return Err(ServiceError::Validation(
            "the last chat message must come from the user".to_string(),
        ));
    }

    Ok(())
}

pub fn validate_tools(
    tools: &[ChatToolDefinition],
    tool_choice: Option<&ChatToolChoice>,
) -> Result<(), ServiceError> {
    let mut names = std::collections::BTreeSet::new();
    for tool in tools {
        if tool.name.trim().is_empty() {
            return Err(ServiceError::Validation(
                "tool name must not be empty".to_string(),
            ));
        }
        let name = tool.name.trim().to_string();
        if !names.insert(name.clone()) {
            return Err(ServiceError::Validation(format!(
                "duplicate tool name '{name}'"
            )));
        }
    }

    if let Some(choice) = tool_choice {
        match choice {
            ChatToolChoice::Auto | ChatToolChoice::None => {}
            ChatToolChoice::Required => {
                if tools.is_empty() {
                    return Err(ServiceError::Validation(
                        "tool_choice=required needs at least one tool".to_string(),
                    ));
                }
            }
            ChatToolChoice::Named(name) => {
                if !names.contains(name) {
                    return Err(ServiceError::Validation(format!(
                        "tool_choice named tool '{name}' is not in tools"
                    )));
                }
            }
        }
    }

    Ok(())
}

#[cfg(test)]
mod tests {
    use super::*;

    fn msg(role: ChatRole, content: &str) -> ChatMessage {
        ChatMessage {
            role,
            content: content.to_string(),
        }
    }

    #[test]
    fn tokenizer_roundtrip_bytes() {
        let tokenizer = ChatTokenizer::default();
        let text = "hello, tokenizer";
        let encoded = tokenizer.encode_bytes(text);
        let decoded = tokenizer.decode_bytes(&encoded);
        assert_eq!(decoded, text);
    }

    #[test]
    fn encode_prompt_includes_special_tokens() {
        let tokenizer = ChatTokenizer::default();
        let tools = vec![ChatToolDefinition {
            name: "lookup_weather".to_string(),
            description: Some("query weather by city".to_string()),
            parameters_json: Some("{\"type\":\"object\"}".to_string()),
        }];
        let tokens = tokenizer
            .encode_chat_prompt(
                Some("be concise"),
                &[
                    msg(ChatRole::User, "hi"),
                    msg(ChatRole::Assistant, "hello"),
                    msg(ChatRole::User, "how are you"),
                ],
                &tools,
                Some(&ChatToolChoice::Required),
            )
            .expect("prompt should encode");

        assert_eq!(tokens[0], SpecialToken::Bos.id());
        assert_eq!(tokens.last().copied(), Some(SpecialToken::Assistant.id()));
        assert!(tokens.contains(&SpecialToken::EndOfTurn.id()));
        assert!(tokens.contains(&SpecialToken::Tool.id()));
    }

    #[test]
    fn rejects_invalid_turn_order() {
        let err = validate_messages(
            None,
            &[msg(ChatRole::User, "u1"), msg(ChatRole::User, "u2")],
        )
        .expect_err("must reject duplicate user turns");

        assert!(err.to_string().contains("alternate"));
    }

    #[test]
    fn rejects_named_choice_for_missing_tool() {
        let tools = vec![ChatToolDefinition {
            name: "a".to_string(),
            description: None,
            parameters_json: None,
        }];

        let err = validate_tools(&tools, Some(&ChatToolChoice::Named("b".to_string())))
            .expect_err("missing named tool should be rejected");
        assert!(err.to_string().contains("not in tools"));
    }
}

