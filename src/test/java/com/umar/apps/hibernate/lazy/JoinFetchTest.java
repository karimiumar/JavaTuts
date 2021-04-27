package com.umar.apps.hibernate.lazy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 *
 * Entities are only needed when the current running application-level
 * transaction needs to modify the entities that are being fetched.
 * Because of the automatic dirty checking mechanism,
 * Hibernate makes it very easy to translate entity state transitions into SQL statements.
 *
 * Considering that we need to modify the <code>PostComment</code> entities,
 * and we also need the <code>Post</code> entities as well,
 * we just need to use the <code>JOIN FETCH</code> directive like in the following:
 */
public class JoinFetchTest {
    private final PostDao postDao = new PostDao();
    private static final Logger log = LogManager.getLogger(JoinFetchTest.class);

    @Test
    public void when_join_fetch_post_with_postComment_then_LazyInitializationException_not_thrown() {
        List<PostComment> comments = null;
        var em = postDao.getEMF().createEntityManager();
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            var query = em.createQuery("select pc from PostComment pc join fetch pc.post where pc.review = :review", PostComment.class).setParameter("review", "Good");
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
        finalComments.forEach(comment->log.info("The post title is '{}'", comment.getPost().getTitle()));

    }
}
