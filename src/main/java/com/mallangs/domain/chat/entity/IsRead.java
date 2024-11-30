package com.mallangs.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IsRead {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long isReadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id")
    @JsonIgnore
    private ChatMessage chatMessage;

    private String sender;

    @Builder.Default
    @Column(name = "read_check", nullable = false)
    private Boolean readCheck = false;

    public void changeChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

}
