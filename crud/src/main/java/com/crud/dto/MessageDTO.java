package com.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MessageDTO {

    private String status;
    private String body;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MessageDTO(String body, String status) {
        this.body = body;
        this.status = status;
    }

    public MessageDTO() {
    }
}

