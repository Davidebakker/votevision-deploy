package com.election.backendjava.dto;

import java.time.LocalDateTime;
import com.election.backendjava.dto.ReplyDTO;
import java.util.List;
import lombok.Getter;

@Getter
public class CommentDTO {
    private long commentId;
    private String commentText;
    private String commentTitle;
    private LocalDateTime createdAt;
    private String userName; // De naam van de gebruiker die de comment plaatste
    private List<ReplyDTO> replies; // De lijst met replies

    // Getters en setters
    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentTitle() {
        return commentTitle;
    }

    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<ReplyDTO> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyDTO> replies) {
        this.replies = replies;
    }
}
