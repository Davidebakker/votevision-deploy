package com.election.backendjava.dto;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReplyDTO {
    // Getters en setters
    private Integer replyId;
    private String replyText;
    private LocalDateTime createdAt;
    private String userName; // De naam van de gebruiker die de reply plaatste
    private List<ReplyDTO> childReplies; // De lijst met nested replies

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setChildReplies(List<ReplyDTO> childReplies) {
        this.childReplies = childReplies;
    }
}
