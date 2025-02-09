package com.progweb.siri_cascudo_api.util;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class LocalStorageService {

    private static final String UPLOAD_DIR = "uploads/";
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");
    private static final long MAX_SIZE = 2 * 1024 * 1024; // 2MB

    public String saveImage(MultipartFile file) throws IOException {
        validateImage(file);

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + fileName;
    }

    private void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo não pode estar vazio.");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Formato de imagem não suportado. Use JPEG ou PNG.");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("A imagem excede o tamanho máximo de 2MB.");
        }
    }

    public void deleteImage(String imageUrl) {
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("Imagem deletada: " + filePath);
            } else {
                System.out.println("Imagem não encontrada: " + filePath);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao excluir a imagem: " + e.getMessage(), e);
        }
    }
}