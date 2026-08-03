package com.likelion.dev_community.common.xss;

import org.springframework.stereotype.Component;

@Component
public class XssSanitizer {

    public String sanitize(String input) {
        if (input == null) {
            return null;
        }
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                .replace("/", "&#x2F;");
    }
}
