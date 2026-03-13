package com.blog.blog_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (author == null || author.isEmpty()) {
            author = "匿名用户";
        }
    }
}
