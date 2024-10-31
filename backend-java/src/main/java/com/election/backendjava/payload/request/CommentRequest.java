package com.election.backendjava.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String commentText;
}
