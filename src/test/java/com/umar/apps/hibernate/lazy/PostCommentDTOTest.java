package com.umar.apps.hibernate.lazy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * What if you don’t even want entities in the first place.
 * If you don’t need to modify the data that’s being read,
 * why would you want to fetch an entity in the first place?
 * A DTO projection allows you to fetch fewer columns
 * and you won’t risk any <code>LazyInitializationException</code>
 */
public class PostCommentDTOTest {

    private final PostDao postDao = new PostDao();
    private static final Logger log = LogManager.getLogger(JoinFetchTest.class);

    @Test
    public void when_dto_projection_then_LazyInitializationException_not_thrown(){
        List<PostCommentDTO> postDTOComments = postDao.doInJPA(entityManager -> entityManager.createQuery("select new com.umar.apps.hibernate.lazy.PostCommentDTO(pc.id, pc.review, p.title)" 
                + "from PostComment pc join pc.post p where review = :review", PostCommentDTO.class)
                .setParameter("review", "Good").getResultList());

        postDTOComments.forEach(comment->log.info("The post title is '{}'", comment));
    }

}
