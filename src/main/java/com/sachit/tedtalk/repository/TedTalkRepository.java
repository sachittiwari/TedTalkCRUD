package com.sachit.tedtalk.repository;

import com.sachit.tedtalk.entity.TedTalk;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TedTalkRepository extends CrudRepository<TedTalk, Long> {





}
