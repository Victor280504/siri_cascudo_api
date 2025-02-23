package com.progweb.siri_cascudo_api.dto.Product;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

    private long maxFileSize;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        this.maxFileSize = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null) {
            return true; // Considerar nulo como válido, ajuste conforme necessário
        }
        return file.getSize() <= maxFileSize;
    }
}
