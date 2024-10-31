package com.sachit.tedtalk.repository;

import com.sachit.tedtalk.entity.TedTalk;
import org.springframework.data.jpa.domain.Specification;

public class TedTalkSpecification {

    public static Specification<TedTalk> setAuthorIfPresent(String author){
        return (root,query,criteraBuilder)->
            author==null?null:criteraBuilder.equal(root.get("author"), author);
    }

    public static Specification<TedTalk> matchTitleIfPresent(String title){
        return (root,query,criteraBuilder)->
                title==null?null:criteraBuilder.like(root.get("title"), "%"+title+"%");
    }

    public static Specification<TedTalk> setMinViewsIfPresent(Long minViews){
        return (root,query,criteraBuilder)->
                minViews==null?null:criteraBuilder.greaterThanOrEqualTo(root.get("views"), minViews);
    }

    public static Specification<TedTalk> setMinLikesIfPresent(Long minLikes){
        return (root,query,criteraBuilder)->
                minLikes==null?null:criteraBuilder.greaterThanOrEqualTo(root.get("likes"), minLikes);
    }
}
