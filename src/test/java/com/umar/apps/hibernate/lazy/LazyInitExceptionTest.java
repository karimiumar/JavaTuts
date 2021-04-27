package com.umar.apps.hibernate.lazy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Hibernate is going to throw a <code>LazyInitializationException</code> because
 * the <code>PostComment</code> entity did not fetch
 * the <code>Post</code> association while the <code>EntityManager</code> was still opened,
 * and the <code>Post</code> relationship was marked with <code>FetchType.LAZY</code>:
 * <code>
 *     @ManyToOne(fetch = FetchType.LAZY)
 *     private Post post;
 * </code>
 */

public class LazyInitExceptionTest {

    private final PostDao postDao = new PostDao();
    private static final Logger log = LogManager.getLogger(LazyInitExceptionTest.class);

    @Test
    public void when_entityManager_open_and_relationship_marked_FetchType$LAZY_PostComment_did_not_fetch_Post() {
        List<PostComment> comments = null;
        var em = postDao.getEMF().createEntityManager();
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            var query = em.createQuery("select pc from PostComment pc where pc.review = :review", PostComment.class).setParameter("review", "Good");
            comments = query.getResultList();
            transaction.commit();
        }catch (Throwable e) {
            if(null != transaction && transaction.isActive()) {
                transaction.rollback();
                throw e;
            }
        }finally {
            em.close();
        }

        List<PostComment> finalComments = comments;
        assertThrows(LazyInitializationException.class,
                () -> finalComments.forEach(comment->log.info("The post title is '{}'", comment.getPost().getTitle())));

    }
}
