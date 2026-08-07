package com.likelion.dev_community.domain.question.repository;

import com.likelion.dev_community.domain.question.entity.QQuestion;
import com.likelion.dev_community.domain.question.entity.QQuestionTag;
import com.likelion.dev_community.domain.question.entity.Question;
import com.likelion.dev_community.domain.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final QQuestion question = QQuestion.question;
    private static final QUser author = QUser.user;
    private static final QQuestionTag questionTag = QQuestionTag.questionTag;

    // F-17
    @Override
    public Page<Question> searchKeyword(String keyword, Pageable pageable) {

        BooleanBuilder condition = new BooleanBuilder();
        condition.or(question.title.containsIgnoreCase(keyword));
        condition.or(question.content.containsIgnoreCase(keyword));
        condition.or(author.nickname.containsIgnoreCase(keyword));

        List<Question> content = queryFactory
                .selectFrom(question)
                .join(question.author, author).fetchJoin()
                .where(condition)
                .orderBy(question.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(question.count())
                .from(question)
                .join(question.author, author)
                .where(condition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    // F-18
    @Override
    public Page<Question> searchTag(Long tagId, Pageable pageable) {

        List<Question> content = queryFactory
                .selectFrom(question)
                .join(question.author, author).fetchJoin()
                .join(question.questionTags, questionTag)
                .where(questionTag.tag.id.eq(tagId))
                .orderBy(question.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(question.count())
                .from(question)
                .join(question.questionTags, questionTag)
                .where(questionTag.tag.id.eq(tagId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}