package com.example.bookhubbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CheckIdResponse {
    private boolean exists;

    public CheckIdResponse(boolean exists) {
        this.exists = exists;
    }
}
