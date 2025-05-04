package com.wificircle.api.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.wificircle.api.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {

    private final Firestore firestore;
    private final CollectionReference usersCollection;

    public UserRepository(Firestore firestore) {
        this.firestore = firestore;
        this.usersCollection = firestore.collection("users");
    }
    public User save(User user) throws ExecutionException, InterruptedException {
        if (user.getUid() == null || user.getUid().isEmpty()) {
            ApiFuture<DocumentReference> result = usersCollection.add(user);
            user.setUid(result.get().getId());
            System.out.println("Usuário criado com ID: " + user.getUid());
        } else {
            ApiFuture<WriteResult> result = usersCollection.document(user.getUid()).set(user);
            result.get();
            System.out.println("Usuário atualizado com ID: " + user.getUid());
        }
        return user;
    }
    public Optional<User> findById(String uid) throws ExecutionException, InterruptedException {
        DocumentReference docRef = usersCollection.document(uid);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            System.out.println("Documento encontrado!");
            User user = document.toObject(User.class);
            return Optional.ofNullable(user);
        } else {
            System.out.println("Nenhum documento encontrado com UID: " + uid);
            return Optional.empty();
        }
    }

    public List<User> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = usersCollection.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<User> users = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            users.add(document.toObject(User.class));
        }
        System.out.println("Encontrados " + users.size() + " usuários.");
        return users;
    }

    public void deleteById(String uid) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> deleteResult = usersCollection.document(uid).delete();
        deleteResult.get();
        System.out.println("Usuário com UID " + uid + " deletado.");
    }

    public Optional<User> findByEmail(String email) throws ExecutionException, InterruptedException {
        Query query = usersCollection.whereEqualTo("email", email).limit(1);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            User user = documents.get(0).toObject(User.class);
            System.out.println("Usuário encontrado por email: " + email);
            return Optional.ofNullable(user);
        } else {
            System.out.println("Nenhum usuário encontrado com email: " + email);
            return Optional.empty();
        }
    }
}
