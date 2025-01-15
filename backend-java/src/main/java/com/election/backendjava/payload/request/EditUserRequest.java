package com.election.backendjava.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditUserRequest {

    @NotBlank
    private Long userId;

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String Name;

    @NotBlank
    private String region;


}
