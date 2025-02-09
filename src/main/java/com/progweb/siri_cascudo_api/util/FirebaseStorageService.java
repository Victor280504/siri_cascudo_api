package com.progweb.siri_cascudo_api.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseStorageService {

    public FirebaseStorageService() {
        try {
            ClassPathResource resource = new ClassPathResource("firebase-key.json");

            // Verifica se o arquivo firebase-key.json existe antes de tentar carregar
            if (!resource.exists()) {
                System.out.println("Arquivo firebase-key.json não encontrado. FirebaseStorageService não será inicializado.");
                return; // Sai do construtor sem inicializar o Firebase
            }

            InputStream serviceAccount = resource.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase inicializado com sucesso.");
            }

        } catch (IOException e) {
            System.err.println("Erro ao inicializar Firebase: " + e.getMessage());
        }
    }

    // Código abaixo está comentado para evitar erros enquanto o Firebase não estiver configurado.
    // Quando criar o projeto no Firebase e adicionar firebase-key.json, descomente e implemente.

    /*
    public String uploadFile(MultipartFile file) {
        try {
            String bucketName = "SEU_BUCKET_DO_FIREBASE";
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            Bucket bucket = StorageClient.getInstance().bucket(bucketName);
            Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());

            return blob.getMediaLink(); // Retorna a URL pública do arquivo
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar arquivo para o Firebase Storage", e);
        }
    }
    */
}
