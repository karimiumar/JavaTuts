package com.umar.apps.hibernate.lazy;

import com.umar.apps.hibernate.infra.dao.core.GenericJpaDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PostDao extends GenericJpaDao<Post, Long> {

    public PostDao() {
        super(Post.class, "postCommentPU");
    }

    @Override
    public Collection<Post> findAll() {
        Collection<Post> trades = new ArrayList<>(Collections.emptyList());
        executeInTransaction(entityManager -> {
            List<?> result = entityManager.createQuery("SELECT p from Post p").getResultList();
            result.forEach(row ->{
                trades.add((Post) row);
            });
        });
        return trades;
    }
}
