package com.progweb.siri_cascudo_api.exception;

import com.progweb.siri_cascudo_api.exception.responses.ErrorResponse;
import com.progweb.siri_cascudo_api.exception.responses.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxSizeException(MaxUploadSizeExceededException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.PAYLOAD_TOO_LARGE.value());
        body.put("error", "Payload Too Large");
        body.put("message", "O arquivo enviado excede o limite permitido de 5MB.");
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, WebRequest request) {
        // Obtém o status HTTP da exceção
        HttpStatus status = HttpStatus.valueOf(ex.getStatus());

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), // Timestamp
                ex.getStatus(), // Status HTTP (customizado)
                status.getReasonPhrase(), // Nome do status (ex: "Bad Request")
                ex.getMessage(), // Mensagem da exceção
                ex.getDetails(), // Detalhes personalizados
                request.getDescription(false).replace("uri=", "") // Caminho da requisição
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    // Captura a ValidationException
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(ValidationException ex, WebRequest request) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                LocalDateTime.now(), // Timestamp
                HttpStatus.BAD_REQUEST.value(), // Status HTTP (400)
                "Bad Request", // Tipo de erro
                ex.getMessage(), // Mensagem da exceção
                ex.getErrors(), // Lista de erros de validação
                request.getDescription(false).replace("uri=", "") // Caminho da requisição
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Captura exceções de validação (MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        // Extrai os erros de validação
        List<ValidationError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        // Cria a resposta de erro
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                LocalDateTime.now(), // Timestamp
                HttpStatus.BAD_REQUEST.value(), // Status HTTP (400)
                "Bad Request", // Tipo de erro
                "Validation failed", // Mensagem da exceção
                errors, // Lista de erros de validação
                request.getDescription(false).replace("uri=", "") // Caminho da requisição
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), // Timestamp
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // Status HTTP (500)
                "Internal Server Error", // Tipo de erro
                "An unexpected error occurred", // Mensagem padrão
                "Please contact the administrator.", // Detalhes padrão
                request.getDescription(false).replace("uri=", "") // Caminho da requisição
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
