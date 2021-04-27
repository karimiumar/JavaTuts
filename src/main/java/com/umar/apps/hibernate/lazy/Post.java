package com.umar.apps.hibernate.lazy;


import com.umar.apps.hibernate.common.WorkItem;

import javax.persistence.*;

@Entity
@Table(name = "posts")
public class Post implements WorkItem<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    Post(){

    }

    Post(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
