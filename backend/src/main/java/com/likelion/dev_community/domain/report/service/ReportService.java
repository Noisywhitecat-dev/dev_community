package com.likelion.dev_community.domain.report.service;

import com.likelion.dev_community.common.exception.CustomException;
import com.likelion.dev_community.common.exception.ErrorCode;
import com.likelion.dev_community.domain.answer.entity.Answer;
import com.likelion.dev_community.domain.answer.repository.AnswerRepository;
import com.likelion.dev_community.domain.question.entity.Question;
import com.likelion.dev_community.domain.question.repository.QuestionRepository;
import com.likelion.dev_community.domain.report.dto.ReportRequest;
import com.likelion.dev_community.domain.report.dto.ReportResponse;
import com.likelion.dev_community.domain.report.entity.Report;
import com.likelion.dev_community.domain.report.entity.ReportTargetType;
import com.likelion.dev_community.domain.report.repository.ReportRepository;
import com.likelion.dev_community.domain.user.entity.User;
import com.likelion.dev_community.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    // 신고 접수
    @Transactional
    public ReportResponse report(Long userId, ReportRequest reportRequest){
        User user = userRepository.findById(userId).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND, "유저를 찾을 수 없습니다. "+userId));

        Long targetUserId;
        String targetUserNickname;

        if(reportRequest.getTargetType() == ReportTargetType.QUESTION){
            Question question = questionRepository.findById(reportRequest.getTargetId()).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND, "질문을 찾을 수 없습니다."));

            if(question.getAuthor().getId().equals(user.getId())){
                throw new CustomException(ErrorCode.SELF_REPORT_NOT_ALLOWED);
            }

            targetUserId = question.getAuthor().getId();
            targetUserNickname = question.getAuthor().getNickname();
        } else {
            Answer answer = answerRepository.findById(reportRequest.getTargetId()).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND, "답변을 찾을 수 없습니다."));

            if(answer.getAuthor().getId().equals(user.getId())){
                throw new CustomException(ErrorCode.SELF_REPORT_NOT_ALLOWED);
            }

            targetUserId = answer.getAuthor().getId();
            targetUserNickname = answer.getAuthor().getNickname();
        }

        Report report = reportRepository.save(Report.builder()
                .reporter(user)
                .targetType(reportRequest.getTargetType())
                .targetId(reportRequest.getTargetId())
                .targetUserId(targetUserId)
                .reason(reportRequest.getReason())
                .build());

        return ReportResponse.from(report, targetUserNickname);
    }
}
