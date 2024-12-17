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
    private String userName;
    private List<ReplyDTO> childReplies = new ArrayList<>();

    // Voeg hier ook upvotes toe
    private Integer upvotes;
}
