package org.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<PostImage> images;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Like> likes;

    @ManyToOne(fetch = FetchType.LAZY) // Thiết lập mối quan hệ với User
    @JoinColumn(name = "user_id") // Tên cột trong bảng Post
    private User user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private boolean edited;

    @Column(name = "is_hidden", nullable = false, columnDefinition = "boolean default false")
    private boolean hidden = false;

    @Column(name = "admin_hidden", nullable = false, columnDefinition = "boolean default false")
    private boolean adminHidden = false;

    @Column(name = "admin_permanently_hidden", nullable = false, columnDefinition = "boolean default false")
    private boolean adminPermanentlyHidden = false;

    @Column
    private String hideReason;

    public boolean isVisible() {
        return !hidden && !adminHidden && !adminPermanentlyHidden;
    }

    private String description;

    private Long IdShare;

    private String category;

}
