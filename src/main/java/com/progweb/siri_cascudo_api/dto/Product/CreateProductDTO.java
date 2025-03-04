package com.progweb.siri_cascudo_api.dto.Product;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateProductDTO {
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String name;

    @NotBlank(message = "A descrição não pode ser vazia.")
    @Size(max = 500, message = "A descrição pode ter no máximo 500 caracteres.")
    private String description;

    @NotNull(message = "A Imagem é obrigatória.")
    private MultipartFile image;

    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
    private double price;

    @NotNull(message = "A categoria do produto é obrigatória.")
    private Long idCategory;
}
