package com.likelion.dev_community.domain.question.repository;

import com.likelion.dev_community.domain.question.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}