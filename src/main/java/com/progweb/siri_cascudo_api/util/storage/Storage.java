package com.progweb.siri_cascudo_api.util.storage;

import org.springframework.web.multipart.MultipartFile;

public interface Storage {
    String saveImage(MultipartFile file);

    void validateImage(MultipartFile file);

    boolean deleteImage(String fileUrl);

    boolean updateImage(String fileUrl);
}
