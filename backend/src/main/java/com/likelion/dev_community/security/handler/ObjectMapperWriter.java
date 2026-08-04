package com.likelion.dev_community.security.handler;

import tools.jackson.databind.ObjectMapper;
import com.likelion.dev_community.common.ApiResponse;
import com.likelion.dev_community.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ObjectMapperWriter {
    private final ObjectMapper objectMapper;

    public void write(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ApiResponse<Void> error = ApiResponse.error(errorCode.getCode(), errorCode.getMessage());

        objectMapper.writeValue(response.getWriter(), error);
    }
}
