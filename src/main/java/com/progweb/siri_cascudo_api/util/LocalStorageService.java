package com.progweb.siri_cascudo_api.util;

import com.progweb.siri_cascudo_api.config.AppConfig;
import com.progweb.siri_cascudo_api.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class LocalStorageService {

    private final String uploadDir;
    private final String baseUrl;
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");
    private static final long MAX_SIZE = 2 * 1024 * 1024; // 2MB

    @Autowired
    public LocalStorageService(AppConfig appConfig) {
        this.uploadDir = appConfig.getUploadDir();
        this.baseUrl = appConfig.getBaseUrl();

        try {
            Files.createDirectories(Paths.get(uploadDir));
            System.out.println("Diretório de uploads criado em: " + Paths.get(uploadDir).toAbsolutePath());
        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Erro ao criar diretório", "Não foi possível criar o diretório de uploads.");
        }
    }

    public String saveImage(MultipartFile file) {
        validateImage(file);

        try {
            // Gera um nome único para a imagem
            String fileExtension = getFileExtension(file.getContentType());
            String fileName = UUID.randomUUID() + "." + fileExtension;

            Path filePath = Paths.get(uploadDir, fileName);
            System.out.println("Salvando arquivo em: " + filePath.toAbsolutePath());
            Files.write(filePath, file.getBytes());

            // Retorna o caminho completo da imagem
            String fullPath = baseUrl + "/" + uploadDir + fileName;
            System.out.println("Caminho completo da imagem: " + fullPath);
            return fullPath;

        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Erro ao salvar imagem", "Não foi possível armazenar a imagem no servidor.");
        }
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST.value(),
                    "Arquivo inválido", "O arquivo não pode ser nulo ou vazio.");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new CustomException(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                    "Formato de imagem não suportado", "Apenas imagens JPEG ou PNG são permitidas.");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new CustomException(HttpStatus.PAYLOAD_TOO_LARGE.value(),
                    "Tamanho excedido", "A imagem deve ter no máximo 2MB.");
        }
    }

    private String getFileExtension(String contentType) {
        if (contentType == null) {
            throw new CustomException(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                    "Formato de imagem não suportado", "O tipo de conteúdo da imagem é inválido.");
        }

        return switch (contentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            default -> throw new CustomException(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                    "Formato de imagem não suportado", "Apenas imagens JPEG ou PNG são permitidas.");
        };
    }

    public void deleteImage(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new CustomException(HttpStatus.NOT_FOUND.value(),
                        "Imagem não encontrada", "A imagem especificada não foi encontrada.");
            }
        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Erro ao excluir imagem", "Ocorreu um problema ao deletar a imagem.");
        }
    }
}