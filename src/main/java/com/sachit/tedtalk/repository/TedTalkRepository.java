package com.sachit.tedtalk.repository;

import com.sachit.tedtalk.entity.TedTalk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TedTalkRepository extends CrudRepository<TedTalk, Long>, JpaSpecificationExecutor<TedTalk> {

    Optional<TedTalk> findByTitle(String title);

    @Override
    Page<TedTalk> findAll(Specification<TedTalk> spec, Pageable pageable);

    Page<TedTalk> findAll(Pageable pageable);
}
