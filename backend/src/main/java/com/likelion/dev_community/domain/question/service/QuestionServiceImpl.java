package com.likelion.dev_community.domain.question.service;

import com.likelion.dev_community.common.exception.CustomException;
import com.likelion.dev_community.common.exception.ErrorCode;
import com.likelion.dev_community.common.xss.XssSanitizer;
import com.likelion.dev_community.domain.question.dto.QuestionListResponse;
import com.likelion.dev_community.domain.question.dto.QuestionRequest;
import com.likelion.dev_community.domain.question.dto.QuestionResponse;
import com.likelion.dev_community.domain.question.entity.Question;
import com.likelion.dev_community.domain.question.entity.QuestionSortType;
import com.likelion.dev_community.domain.question.repository.QuestionRepository;
import com.likelion.dev_community.domain.question.repository.QuestionTagRepository;
import com.likelion.dev_community.domain.user.entity.User;
import com.likelion.dev_community.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionTagRepository questionTagRepository;
    private final UserRepository userRepository;
    private final XssSanitizer xssSanitizer;

    // F-06
    @Override
    public QuestionResponse createQuestion(Long userId, QuestionRequest request) {

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));

        String title = xssSanitizer.sanitize(request.getTitle());
        String content = xssSanitizer.sanitize(request.getContent());

        Question question = Question.builder()
                .author(author)
                .title(title)
                .content(content)
                .build();

        questionRepository.save(question);

        // 태그 저장 로직은 F-10에서 추가
        return QuestionResponse.from(question, Collections.emptyList());
    }

    // F-07
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionListResponse> readQuestions(int page, int size, String sort) {

        if (page < 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "page는 0 이상이어야 합니다.");
        }
        if (size < 1 || size > 100) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "size는 1~100 사이여야 합니다.");
        }

        Pageable pageable = PageRequest.of(page, size);
        QuestionSortType sortType = QuestionSortType.from(sort);

        Page<Question> questions = switch (sortType) {
            case LIKE -> questionRepository.findAllByOrderByLikeCount(pageable);
            case UNRESOLVED -> questionRepository.findAllOrderByUnresolvedFirst(pageable);
            case LATEST -> questionRepository.findAllOrderByLatest(pageable);
        };

        if (questions.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> questionIds = questions.getContent().stream()
                .map(Question::getId)
                .toList();

        Map<Long, List<String>> tagMap = questionTagRepository.findByQuestionIdIn(questionIds).stream()
                .collect(Collectors.groupingBy(
                        qt -> qt.getQuestion().getId(),
                        Collectors.mapping(qt -> qt.getTag().getName(), Collectors.toList())
                ));

        return questions.map(question -> QuestionListResponse.of(
                question,
                0, // 답변 작성 구현 후에 수정
                tagMap.getOrDefault(question.getId(), Collections.emptyList())
        ));
    }
}