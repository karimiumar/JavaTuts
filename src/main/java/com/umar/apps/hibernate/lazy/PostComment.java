package com.umar.apps.hibernate.lazy;

import com.umar.apps.hibernate.common.WorkItem;

import javax.persistence.*;

@Entity
@Table(name = "post_comments")
public class PostComment implements WorkItem<Long> {

    PostComment(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private String review;

    public PostComment(String review, Post post) {
        this.review = review;
        this.post = post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public String getReview() {
        return review;
    }

    @Override
    public String toString() {
        return "PostComment{" +
                "id=" + id +
                ", post=" + post +
                ", review='" + review + '\'' +
                '}';
    }
}
