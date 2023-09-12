package com.mycompany.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  String title;
    private String content;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="post_id")
    private Set<Comment> comments;

    private int rating;
}
