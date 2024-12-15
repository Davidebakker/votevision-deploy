package com.election.backendjava.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ReplyDTO {
    // Getters en setters
    private long replyId;
    private String replyText;
    private LocalDateTime createdAt;
    private String userName; // De naam van de gebruiker die de reply plaatste
    private List<ReplyDTO> childReplies = new ArrayList<>(); // De lijst met nested replies

}
