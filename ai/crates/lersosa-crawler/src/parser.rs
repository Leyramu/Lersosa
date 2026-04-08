use scraper::{Html, Selector};
use url::Url;

pub struct ParsedHtml {
    pub title: Option<String>,
    pub text: String,
    pub links: Vec<String>,
}

pub fn parse_html(base_url: &Url, body: &str) -> ParsedHtml {
    let document = Html::parse_document(body);

    let title = Selector::parse("title")
        .ok()
        .and_then(|selector| document.select(&selector).next())
        .map(|node| node.text().collect::<String>().trim().to_string())
        .filter(|title| !title.is_empty());

    let text = Selector::parse("body")
        .ok()
        .and_then(|selector| document.select(&selector).next())
        .map(|node| {
            node.text()
                .map(str::trim)
                .filter(|segment| !segment.is_empty())
                .collect::<Vec<_>>()
                .join("\n")
        })
        .unwrap_or_default();

    let mut links = Vec::new();
    if let Ok(link_selector) = Selector::parse("a[href]") {
        for link in document.select(&link_selector) {
            if let Some(raw_href) = link.value().attr("href") {
                if let Ok(url) = base_url.join(raw_href) {
                    if matches!(url.scheme(), "http" | "https") {
                        links.push(url.to_string());
                    }
                }
            }
        }
    }

    links.sort();
    links.dedup();

    ParsedHtml { title, text, links }
}

#[cfg(test)]
mod tests {
    use super::parse_html;

    #[test]
    fn parse_html_extracts_title_text_and_links() {
        let base = url::Url::parse("https://example.com/docs/").unwrap();
        let html = r#"
        <html>
          <head><title>Test Page</title></head>
          <body>
            Hello <strong>world</strong>
            <a href="/a">A</a>
            <a href="https://example.com/b">B</a>
          </body>
        </html>
        "#;

        let parsed = parse_html(&base, html);

        assert_eq!(parsed.title.as_deref(), Some("Test Page"));
        assert!(parsed.text.contains("Hello"));
        assert_eq!(parsed.links.len(), 2);
        assert!(parsed.links.contains(&"https://example.com/a".to_string()));
    }
}

