package com.progweb.siri_cascudo_api.dto;

import com.progweb.siri_cascudo_api.exception.ExceptionFlags;
import lombok.Data;

@Data
public class CreateResponseDTO {
    private String id;
    private String message;
    private ExceptionFlags flag;

    public CreateResponseDTO(String id, String message) {
        this.id = id;
        this.message = message;
        this.flag = ExceptionFlags.SUCCESS;
    }
}
