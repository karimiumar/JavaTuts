package com.umar.apps.hibernate.lazy;

public class PostCommentDTO {
    private final Long id;
    private final String review;
    private final String title;

    public PostCommentDTO(Long id, String review, String title) {
        this.id = id;
        this.title = title;
        this.review = review;
    }

    public String getReview() {
        return review;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "PostCommentDTO{" +
                "id=" + id +
                ", review='" + review + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
