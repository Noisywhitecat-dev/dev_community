package com.likelion.dev_community.domain.answer.service;

import com.likelion.dev_community.common.exception.CustomException;
import com.likelion.dev_community.common.exception.ErrorCode;
import com.likelion.dev_community.common.xss.XssSanitizer;
import com.likelion.dev_community.domain.answer.dto.AnswerRequest;
import com.likelion.dev_community.domain.answer.dto.AnswerResponse;
import com.likelion.dev_community.domain.answer.entity.Answer;
import com.likelion.dev_community.domain.answer.repository.AnswerRepository;
import com.likelion.dev_community.domain.question.entity.Question;
import com.likelion.dev_community.domain.question.repository.QuestionRepository;
import com.likelion.dev_community.domain.user.entity.Role;
import com.likelion.dev_community.domain.user.entity.User;
import com.likelion.dev_community.domain.user.entity.UserStatus;
import com.likelion.dev_community.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    private AnswerServiceImpl answerService;

    @BeforeEach
    void setUp() {
        answerService = new AnswerServiceImpl(answerRepository, questionRepository, userRepository, new XssSanitizer());
    }

    @Test
    void 정상적으로_답변을_작성한다() {
        User author = createUser(1L, "answerer");
        Question question = createQuestion(10L, createUser(2L, "asker"));
        AnswerRequest request = new AnswerRequest("정말 좋은 질문이네요.");

        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(questionRepository.findById(10L)).thenReturn(Optional.of(question));

        AnswerResponse response = answerService.createAnswer(1L, 10L, request);

        assertThat(response.getContent()).isEqualTo("정말 좋은 질문이네요.");
        assertThat(response.getQuestionId()).isEqualTo(10L);
        assertThat(response.getAuthorId()).isEqualTo(1L);
        assertThat(response.getAuthorNickname()).isEqualTo("answerer");
        assertThat(response.isAdopted()).isFalse();
        verify(answerRepository).save(any());
    }

    @Test
    void 존재하지_않는_질문이면_예외가_발생한다() {
        User author = createUser(1L, "answerer");
        AnswerRequest request = new AnswerRequest("내용");

        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(questionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> answerService.createAnswer(1L, 999L, request))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> assertThat(((CustomException) e).getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND));
    }

    @Test
    void 정상적으로_답변_목록을_조회한다() {
        User asker = createUser(2L, "asker");
        Question question = createQuestion(10L, asker);
        Answer answer1 = createAnswer(1L, question, createUser(3L, "answerer1"), "첫 번째 답변");
        Answer answer2 = createAnswer(2L, question, createUser(4L, "answerer2"), "두 번째 답변");

        when(questionRepository.existsById(10L)).thenReturn(true);
        when(answerRepository.findByQuestionIdOrderByCreatedAtAsc(10L)).thenReturn(List.of(answer1, answer2));

        List<AnswerResponse> responses = answerService.readAnswers(10L);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getContent()).isEqualTo("첫 번째 답변");
        assertThat(responses.get(1).getContent()).isEqualTo("두 번째 답변");
    }

    @Test
    void 답변이_없는_질문은_빈_목록을_반환한다() {
        when(questionRepository.existsById(10L)).thenReturn(true);
        when(answerRepository.findByQuestionIdOrderByCreatedAtAsc(10L)).thenReturn(List.of());

        List<AnswerResponse> responses = answerService.readAnswers(10L);

        assertThat(responses).isEmpty();
    }

    @Test
    void 답변_목록_조회_시_존재하지_않는_질문이면_예외가_발생한다() {
        when(questionRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> answerService.readAnswers(999L))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> assertThat(((CustomException) e).getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND));
    }

    private User createUser(Long id, String nickname) {
        User user = User.builder()
                .username(nickname)
                .password("encoded-password")
                .nickname(nickname)
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();
        setId(user, id);
        return user;
    }

    private Question createQuestion(Long id, User author) {
        Question question = Question.builder()
                .author(author)
                .title("제목")
                .content("내용")
                .build();
        setId(question, id);
        return question;
    }

    private Answer createAnswer(Long id, Question question, User author, String content) {
        Answer answer = Answer.builder()
                .question(question)
                .author(author)
                .content(content)
                .build();
        setId(answer, id);
        return answer;
    }

    private void setId(Object entity, Long id) {
        try {
            Field field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
