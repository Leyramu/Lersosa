use std::collections::HashMap;

use url::Url;

#[derive(Debug, Clone)]
struct Rule {
    allow: bool,
    pattern: String,
}

#[derive(Debug, Clone, Default)]
pub struct RobotsPolicy {
    rules_by_agent: HashMap<String, Vec<Rule>>,
}

impl RobotsPolicy {
    pub fn allow_all() -> Self {
        Self::default()
    }

    pub fn parse(raw: &str) -> Self {
        let mut policy = Self::default();
        let mut current_agents: Vec<String> = Vec::new();
        let mut saw_rule_for_group = false;

        for line in raw.lines() {
            let line = line.split('#').next().unwrap_or("").trim();
            if line.is_empty() {
                continue;
            }

            let Some((key, value)) = line.split_once(':') else {
                continue;
            };
            let key = key.trim().to_ascii_lowercase();
            let value = value.trim();

            match key.as_str() {
                "user-agent" => {
                    if saw_rule_for_group {
                        current_agents.clear();
                        saw_rule_for_group = false;
                    }
                    let agent = value.to_ascii_lowercase();
                    if !current_agents.contains(&agent) {
                        current_agents.push(agent);
                    }
                }
                "allow" | "disallow" => {
                    if current_agents.is_empty() {
                        continue;
                    }
                    saw_rule_for_group = true;
                    let pattern = value.trim().to_string();
                    for agent in &current_agents {
                        policy.rules_by_agent.entry(agent.clone()).or_default().push(Rule {
                            allow: key == "allow",
                            pattern: pattern.clone(),
                        });
                    }
                }
                _ => {}
            }
        }

        policy
    }

    pub fn is_allowed(&self, user_agent: &str, url: &Url) -> bool {
        let path = match url.query() {
            Some(query) => format!("{}?{}", url.path(), query),
            None => url.path().to_string(),
        };

        self.is_allowed_for_path(user_agent, &path)
    }

    pub fn is_allowed_for_path(&self, user_agent: &str, path: &str) -> bool {
        let normalized_ua = user_agent.to_ascii_lowercase();
        let mut candidate_rules: Vec<&Rule> = Vec::new();

        for (agent, rules) in &self.rules_by_agent {
            if agent == "*" || normalized_ua.contains(agent) {
                candidate_rules.extend(rules.iter());
            }
        }

        if candidate_rules.is_empty() {
            return true;
        }

        let mut best: Option<(usize, bool)> = None;

        for rule in candidate_rules {
            if rule.pattern.is_empty() {
                if !rule.allow {
                    continue;
                }
            }
            if !matches_pattern(&rule.pattern, path) {
                continue;
            }
            let len = rule.pattern.len();
            best = match best {
                Some((best_len, best_allow)) if best_len > len => Some((best_len, best_allow)),
                Some((best_len, best_allow)) if best_len == len => {
                    Some((best_len, best_allow || rule.allow))
                }
                _ => Some((len, rule.allow)),
            };
        }

        best.map(|(_, allow)| allow).unwrap_or(true)
    }
}

fn matches_pattern(pattern: &str, path: &str) -> bool {
    if pattern.is_empty() {
        return true;
    }

    let anchored_end = pattern.ends_with('$');
    let core = if anchored_end {
        &pattern[..pattern.len().saturating_sub(1)]
    } else {
        pattern
    };

    if core == "/" {
        return true;
    }

    wildcard_match(core, path, anchored_end)
}

fn wildcard_match(pattern: &str, input: &str, anchored_end: bool) -> bool {
    if !anchored_end {
        let mut non_anchored = String::with_capacity(pattern.len() + 1);
        non_anchored.push_str(pattern);
        non_anchored.push('*');
        return wildcard_match(&non_anchored, input, true);
    }

    let p = pattern.as_bytes();
    let s = input.as_bytes();

    let mut pi = 0usize;
    let mut si = 0usize;
    let mut star_pi: Option<usize> = None;
    let mut star_si: usize = 0;

    while si < s.len() {
        if pi < p.len() && (p[pi] == s[si]) {
            pi += 1;
            si += 1;
        } else if pi < p.len() && p[pi] == b'*' {
            star_pi = Some(pi);
            pi += 1;
            star_si = si;
        } else if let Some(star_index) = star_pi {
            pi = star_index + 1;
            star_si += 1;
            si = star_si;
        } else {
            return false;
        }
    }

    while pi < p.len() && p[pi] == b'*' {
        pi += 1;
    }

    pi == p.len()
}

#[cfg(test)]
mod tests {
    use super::RobotsPolicy;

    #[test]
    fn disallow_private_for_all() {
        let robots = r#"
        User-agent: *
        Disallow: /private
        "#;

        let policy = RobotsPolicy::parse(robots);
        assert!(!policy.is_allowed_for_path("LersosaCrawler/1.0", "/private/data"));
        assert!(policy.is_allowed_for_path("LersosaCrawler/1.0", "/public"));
    }

    #[test]
    fn allow_rule_wins_on_same_prefix_length() {
        let robots = r#"
        User-agent: *
        Disallow: /docs/
        Allow: /docs/public/
        "#;

        let policy = RobotsPolicy::parse(robots);
        assert!(policy.is_allowed_for_path("LersosaCrawler/1.0", "/docs/public/a"));
        assert!(!policy.is_allowed_for_path("LersosaCrawler/1.0", "/docs/private/a"));
    }

    #[test]
    fn wildcard_and_end_anchor_supported() {
        let robots = r#"
        User-agent: *
        Disallow: /*.pdf$
        "#;

        let policy = RobotsPolicy::parse(robots);
        assert!(!policy.is_allowed_for_path("LersosaCrawler/1.0", "/doc/readme.pdf"));
        assert!(policy.is_allowed_for_path("LersosaCrawler/1.0", "/doc/readme.pdf?download=1"));
        assert!(policy.is_allowed_for_path("LersosaCrawler/1.0", "/doc/readme.txt"));
    }

    #[test]
    fn user_agent_groups_do_not_leak() {
        let robots = r#"
        User-agent: crawler-a
        Disallow: /a

        User-agent: crawler-b
        Disallow: /b
        "#;

        let policy = RobotsPolicy::parse(robots);
        assert!(!policy.is_allowed_for_path("crawler-a", "/a/path"));
        assert!(policy.is_allowed_for_path("crawler-a", "/b/path"));
        assert!(!policy.is_allowed_for_path("crawler-b", "/b/path"));
    }
}

