package com.mallangs.domain.image.entity;

import com.mallangs.domain.chat.entity.ChatMessage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "image")
public class Image {
    @Id
    @Column(nullable = false, unique = true)
    private String url;

    private String originalFileName;

    private String contentType;

    @Column(nullable = false)
    private Integer width;

    @Column(nullable = false)
    private Integer height;




}
