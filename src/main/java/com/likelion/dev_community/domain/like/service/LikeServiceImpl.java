package com.likelion.dev_community.domain.like.service;

import com.likelion.dev_community.common.exception.ErrorCode;
import com.likelion.dev_community.common.exception.ResourceNotFoundException;
import com.likelion.dev_community.domain.answer.entity.Answer;
import com.likelion.dev_community.domain.answer.repository.AnswerRepository;
import com.likelion.dev_community.domain.like.entity.LikeHistory;
import com.likelion.dev_community.domain.like.entity.LikeTargetType;
import com.likelion.dev_community.domain.like.repository.LikeHistoryRepository;
import com.likelion.dev_community.domain.question.entity.Question;
import com.likelion.dev_community.domain.question.repository.QuestionRepository;
import com.likelion.dev_community.domain.user.entity.User;
import com.likelion.dev_community.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// ⑤ likeCount 갱신 트랜잭션 경계
// insert/delete(likes)와 Question·Answer.likeCount 증감을
// 같은 @Transactional 메서드 안에서 처리한다.
// 이렇게 하지 않으면, 두 작업 사이에 서버가 죽거나 예외가 나는 경우
// "좋아요 기록은 있는데 카운트는 안 늘어난" 상태로 데이터가 어긋난다.
// 클래스 레벨에 @Transactional을 걸어 이 클래스의 모든 메서드가
// 기본적으로 하나의 트랜잭션으로 묶이게 한다.
@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {

    private final LikeHistoryRepository likeHistoryRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    public boolean toggleLike(Long userId, LikeTargetType targetType, Long targetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NOT_FOUND.getMessage()));

        return likeHistoryRepository
                .findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId)
                .map(existing -> {
                    likeHistoryRepository.delete(existing);
                    decreaseCount(targetType, targetId);
                    return false;
                })
                .orElseGet(() -> {
                    // UNIQUE(user_id, target_type, target_id) 제약이 최종 방어선이므로
                    // 동시 요청으로 두 번 눌려도 DB가 두 번째 insert를 막아준다.
                    likeHistoryRepository.save(
                            LikeHistory.builder()
                                    .user(user)
                                    .targetType(targetType)
                                    .targetId(targetId)
                                    .build()
                    );
                    increaseCount(targetType, targetId);
                    return true;
                });
    }

    private void increaseCount(LikeTargetType targetType, Long targetId) {
        if (targetType == LikeTargetType.QUESTION) {
            Question question = questionRepository.findById(targetId)
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NOT_FOUND.getMessage()));
            question.increaseLikeCount();
        } else {
            Answer answer = answerRepository.findById(targetId)
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NOT_FOUND.getMessage()));
            answer.increaseLikeCount();
        }
    }

    private void decreaseCount(LikeTargetType targetType, Long targetId) {
        if (targetType == LikeTargetType.QUESTION) {
            questionRepository.findById(targetId).ifPresent(Question::decreaseLikeCount);
        } else {
            answerRepository.findById(targetId).ifPresent(Answer::decreaseLikeCount);
        }
    }
}
