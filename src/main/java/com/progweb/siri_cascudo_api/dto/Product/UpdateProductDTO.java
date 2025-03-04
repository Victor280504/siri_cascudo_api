package com.progweb.siri_cascudo_api.dto.Product;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProductDTO {
    private Long id;

    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String name;

    @Size(max = 500, message = "A descrição pode ter no máximo 500 caracteres.")
    private String description;

    //@NotNull(message = "A Imagem é obrigatória.")
      @FileSize(max = 5 * 1024 * 1024, message = "O tamanho do arquivo não pode exceder 5MB.")
    private MultipartFile image;

    //@DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
    private double price;

    private Long idCategory;

    @AssertTrue(message = "A imagem não pode ser vazia.")
    public boolean isValidImage() {
        return image == null || !image.isEmpty();
    }
}