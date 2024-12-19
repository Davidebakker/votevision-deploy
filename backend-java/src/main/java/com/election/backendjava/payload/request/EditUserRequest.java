package com.election.backendjava.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditUserRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String region;

    @NotBlank
    private String bio;

}
