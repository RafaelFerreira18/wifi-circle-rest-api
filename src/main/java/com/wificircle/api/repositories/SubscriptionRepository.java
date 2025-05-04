package com.wificircle.api.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.wificircle.api.models.Subscription;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class SubscriptionRepository {

    private final Firestore firestore;
    private final CollectionReference subscriptionsCollection;

    public SubscriptionRepository(Firestore firestore) {
        this.firestore = firestore;
        this.subscriptionsCollection = firestore.collection("subscriptions");
    }

    public Subscription save(Subscription subscription) throws ExecutionException, InterruptedException {
        if (subscription.getId() == null || subscription.getId().isEmpty()) {
            ApiFuture<DocumentReference> result = subscriptionsCollection.add(subscription);
            subscription.setId(result.get().getId());
        } else {
            ApiFuture<WriteResult> result = subscriptionsCollection.document(subscription.getId()).set(subscription);
            result.get();
        }
        return subscription;
    }

    public Optional<Subscription> findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = subscriptionsCollection.document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return Optional.ofNullable(document.toObject(Subscription.class));
        } else {
            return Optional.empty();
        }
    }

    public List<Subscription> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = subscriptionsCollection.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Subscription> subscriptions = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            subscriptions.add(doc.toObject(Subscription.class));
        }
        return subscriptions;
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> deleteResult = subscriptionsCollection.document(id).delete();
        deleteResult.get();
    }

    public Optional<Subscription> findByUserId(String userId) throws ExecutionException, InterruptedException {
        Query query = subscriptionsCollection.whereEqualTo("userId", userId).limit(1);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            return Optional.of(documents.get(0).toObject(Subscription.class));
        } else {
            return Optional.empty();
        }
    }
}
