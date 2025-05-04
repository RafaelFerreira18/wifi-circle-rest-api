package com.wificircle.api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore; // Importação correta para o Admin SDK
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient; // Importar FirestoreClient
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class FirestoreConfig {

    @Value("classpath:wifiCircle.json")
    Resource serviceAccount;

    @Value("${spring.cloud.gcp.firestore.project-id}")
    private String projectId;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        if (!firebaseApps.isEmpty()) {
             System.out.println("Firebase Admin SDK já inicializado.");
             return firebaseApps.get(0);
        }

        InputStream serviceAccountStream = serviceAccount.getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .setProjectId(projectId)
                .build();

        System.out.println("Inicializando Firebase Admin SDK...");
        return FirebaseApp.initializeApp(options);
    }
    @Bean
    public Firestore firestore(FirebaseApp firebaseApp) {
        System.out.println("Criando Bean do Firestore...");
        return FirestoreClient.getFirestore(firebaseApp, "(default)");
    }
}
