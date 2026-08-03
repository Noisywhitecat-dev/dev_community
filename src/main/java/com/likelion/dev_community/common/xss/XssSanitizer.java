package com.likelion.dev_community.common.xss;

import org.springframework.stereotype.Component;

/**
 * 질문/답변 제목·본문 sanitizing (MVP1)
 *
 * MVP1 에디터는 마크다운/리치텍스트가 아닌 순수 텍스트를 가정하므로,
 * 태그를 선별적으로 허용하는 대신 HTML 특수문자를 전부 엔티티로
 * escape한다 — 어떤 태그 조합이 들어와도 브라우저가 마크업으로
 * 해석하지 않게 되어 스크립트 삽입 자체가 불가능해진다.
 * (별도 HTML sanitizer 라이브러리 의존성 추가는 build.gradle 변경이
 * 필요해 이번 작업 범위 밖이라 사용하지 않았다.)
 */
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
