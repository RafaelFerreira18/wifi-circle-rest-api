package com.wificircle.api.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.wificircle.api.models.Comment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class CommentRepository {

    private final Firestore firestore;
    private final CollectionReference commentsCollection;

    public CommentRepository(Firestore firestore) {
        this.firestore = firestore;
        this.commentsCollection = firestore.collection("comments");
    }

    public Comment save(Comment comment) throws ExecutionException, InterruptedException {
        if (comment.getId() == null || comment.getId().isEmpty()) {
            ApiFuture<DocumentReference> result = commentsCollection.add(comment);
            comment.setId(result.get().getId());
        } else {
            ApiFuture<WriteResult> result = commentsCollection.document(comment.getId()).set(comment);
            result.get();
        }
        return comment;
    }

    public Optional<Comment> findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = commentsCollection.document(id);
        DocumentSnapshot document = docRef.get().get();
        if (document.exists()) {
            return Optional.ofNullable(document.toObject(Comment.class));
        }
        return Optional.empty();
    }

    public List<Comment> findAll() throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = commentsCollection.get().get().getDocuments();
        List<Comment> comments = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            comments.add(doc.toObject(Comment.class));
        }
        return comments;
    }

    public List<Comment> findByPostId(String postId) throws ExecutionException, InterruptedException {
        Query query = commentsCollection.whereEqualTo("postId", postId);
        List<QueryDocumentSnapshot> documents = query.get().get().getDocuments();
        List<Comment> comments = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            comments.add(doc.toObject(Comment.class));
        }
        return comments;
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        commentsCollection.document(id).delete().get();
    }
}
