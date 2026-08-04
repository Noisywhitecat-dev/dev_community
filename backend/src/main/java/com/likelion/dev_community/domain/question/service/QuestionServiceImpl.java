package com.likelion.dev_community.domain.question.service;

import com.likelion.dev_community.common.exception.CustomException;
import com.likelion.dev_community.common.exception.ErrorCode;
import com.likelion.dev_community.common.xss.XssSanitizer;
import com.likelion.dev_community.domain.question.dto.QuestionRequest;
import com.likelion.dev_community.domain.question.dto.QuestionResponse;
import com.likelion.dev_community.domain.question.entity.Question;
import com.likelion.dev_community.domain.question.entity.QuestionTag;
import com.likelion.dev_community.domain.question.entity.Tag;
import com.likelion.dev_community.domain.question.repository.QuestionRepository;
import com.likelion.dev_community.domain.question.repository.QuestionTagRepository;
import com.likelion.dev_community.domain.question.repository.TagRepository;
import com.likelion.dev_community.domain.user.entity.User;
import com.likelion.dev_community.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    private final QuestionTagRepository questionTagRepository;
    private final UserRepository userRepository;
    private final XssSanitizer xssSanitizer;

    @Override
    public QuestionResponse createQuestion(Long userId, QuestionRequest request) {

        // userId로 작성자 조회
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));

        // title, content는 XSS 이스케이프 처리
        String Title = xssSanitizer.sanitize(request.getTitle());
        String Content = xssSanitizer.sanitize(request.getContent());

        Question question = Question.builder()
                .author(author)
                .title(Title)
                .content(Content)
                .build();

        questionRepository.save(question);

        return QuestionResponse.from(question, Collections.emptyList());
    }


}