package com.wificircle.api.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.wificircle.api.models.Community;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class CommunityRepository {

    private final Firestore firestore;
    private final CollectionReference communitiesCollection;

    public CommunityRepository(Firestore firestore) {
        this.firestore = firestore;
        this.communitiesCollection = firestore.collection("communities");
    }

    public Community save(Community community) throws ExecutionException, InterruptedException {
        if (community.getId() == null || community.getId().isEmpty()) {
            ApiFuture<DocumentReference> result = communitiesCollection.add(community);
            community.setId(result.get().getId());
            System.out.println("Comunidade criada com ID: " + community.getId());
        } else {
            ApiFuture<WriteResult> result = communitiesCollection.document(community.getId()).set(community);
            result.get();
            System.out.println("Comunidade atualizada com ID: " + community.getId());
        }
        return community;
    }

    public Optional<Community> findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = communitiesCollection.document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Community community = document.toObject(Community.class);
            return Optional.ofNullable(community);
        } else {
            System.out.println("Nenhuma comunidade encontrada com ID: " + id);
            return Optional.empty();
        }
    }

    public List<Community> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = communitiesCollection.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Community> communities = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            communities.add(document.toObject(Community.class));
        }
        System.out.println("Encontradas " + communities.size() + " comunidades.");
        return communities;
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> deleteResult = communitiesCollection.document(id).delete();
        deleteResult.get();
        System.out.println("Comunidade com ID " + id + " deletada.");
    }

    public Optional<Community> findByName(String name) throws ExecutionException, InterruptedException {
        Query query = communitiesCollection.whereEqualTo("name", name).limit(1);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            Community community = documents.get(0).toObject(Community.class);
            System.out.println("Comunidade encontrada por name: " + name);
            return Optional.ofNullable(community);
        } else {
            System.out.println("Nenhuma comunidade encontrada com name: " + name);
            return Optional.empty();
        }
    }
}
