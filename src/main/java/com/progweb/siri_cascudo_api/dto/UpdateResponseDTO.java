package com.progweb.siri_cascudo_api.dto;

import com.progweb.siri_cascudo_api.exception.ExceptionFlags;
import lombok.Data;

@Data
public class UpdateResponseDTO<T> {
    private String id;
    private String message;
    private T data;
    private ExceptionFlags flag;

    public UpdateResponseDTO(String id, String message, T data) {
        this.id = id;
        this.message = message;
        this.data = data;
        this.flag = ExceptionFlags.SUCCESS;
    }
}
