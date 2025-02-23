package com.progweb.siri_cascudo_api;

import com.progweb.siri_cascudo_api.exception.CustomException;
import com.progweb.siri_cascudo_api.util.LocalStorageService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LocalStorageServiceTest {

    @Autowired
    private LocalStorageService localStorageService;

    final String uploadDir = "uploads/";
    final String baseUrl = "http://localhost:8080";

    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get(uploadDir);
        Files.createDirectories(path);
    }

    @Test
    void saveImage_ValidImage_ReturnsPath() {
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[1024]);
        String savedPath = localStorageService.saveImage(multipartFile, "test");
        assertTrue(savedPath.startsWith(baseUrl + "/" + uploadDir));
    }

    @Test
    void saveImage_InvalidFileType_ThrowsException() {
        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", new byte[1024]);
        CustomException exception = assertThrows(CustomException.class, () -> localStorageService.saveImage(multipartFile));
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), exception.getStatus());
    }

    @Test
    void saveImage_TooLargeFile_ThrowsException() {
        byte[] largeFile = new byte[11 * 1024 * 1024];
        MultipartFile multipartFile = new MockMultipartFile("file", "test.png", "image/png", largeFile);
        CustomException exception = assertThrows(CustomException.class, () -> localStorageService.saveImage(multipartFile));
        assertEquals(HttpStatus.PAYLOAD_TOO_LARGE.value(), exception.getStatus());
    }

    @Test
    void deleteImage_FileExists_ReturnsTrue() {
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[1024]);
        String savedPath = localStorageService.saveImage(multipartFile, "test");
        assertTrue(localStorageService.deleteImage(savedPath));
    }

    @Test
    void deleteImage_FileDoesNotExist_ThrowsException() {
        String fileName = "non_existent.jpg";
        String fileUrl = baseUrl + "/" + uploadDir + fileName;
        CustomException exception = assertThrows(CustomException.class, () -> localStorageService.deleteImage(fileUrl));
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatus());
    }

    @AfterAll
    void unmount() {
        Path imagePath = localStorageService.getFilePathByName("test.jpg");
        String imageUrl = baseUrl + "/" + uploadDir + "test.jpg";
        if (Files.exists(imagePath)) {
            System.out.println("Cleaning Test");
            localStorageService.deleteImage(imageUrl);
        }
    }
}
