package com.election.backendjava.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ReplyDTO {
    private Integer replyId;
    private String replyText;
    private LocalDateTime createdAt;
    private String userName; // De naam van de gebruiker die de reply plaatste
    private Long userId; // Voeg ID van de gebruiker toe
    private List<ReplyDTO> childReplies = new ArrayList<>();
    private Integer upvotes;// Voeg hier ook upvotes toe
}
