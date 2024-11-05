package com.sachit.tedtalk.repository;

import com.sachit.tedtalk.entity.TedTalk;
import org.springframework.data.jpa.domain.Specification;

public class TedTalkSpecification {

    /**
     * This method adds criteria condition for author check
     *
     * @param author of the ted talk
     * @return Specification of ted talk with author condition
     */
    public static Specification<TedTalk> setAuthorIfPresent(String author){
        return (root,query,criteraBuilder)->
            author==null?null:criteraBuilder.equal(root.get("author"), author);
    }


    /**
     * This method adds criteria condition for title check
     *
     * @param title of the ted talk
     * @return Specification of ted talk with title like condition
     */
    public static Specification<TedTalk> matchTitleIfPresent(String title){
        return (root,query,criteraBuilder)->
                title==null?null:criteraBuilder.like(root.get("title"), "%"+title+"%");
    }


    /**
     * This method adds criteria condition for minViews check
     *
     * @param minViews of the ted talk
     * @return Specification of ted talk with minViews condition
     */
    public static Specification<TedTalk> setMinViewsIfPresent(Long minViews){
        return (root,query,criteraBuilder)->
                minViews==null?null:criteraBuilder.greaterThanOrEqualTo(root.get("views"), minViews);
    }


    /**
     * This method adds criteria condition for minLiked check
     *
     * @param minLikes of the ted talk
     * @return Specification of ted talk with minLikes condition
     */
    public static Specification<TedTalk> setMinLikesIfPresent(Long minLikes){
        return (root,query,criteraBuilder)->
                minLikes==null?null:criteraBuilder.greaterThanOrEqualTo(root.get("likes"), minLikes);
    }
}
