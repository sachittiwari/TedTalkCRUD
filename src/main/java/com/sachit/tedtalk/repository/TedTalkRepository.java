package com.sachit.tedtalk.repository;

import com.sachit.tedtalk.entity.TedTalk;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TedTalkRepository extends CrudRepository<TedTalk, Long> {

    Optional<TedTalk> findByTitle(String title);



}
