package com.likelion.dev_community.domain.user.repository;


import com.likelion.dev_community.domain.user.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshRepository extends CrudRepository<RefreshToken,Long> {
}
