package com.wificircle.api.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.wificircle.api.models.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class PostRepository {

    private final Firestore firestore;
    private final CollectionReference postsCollection;

    public PostRepository(Firestore firestore) {
        this.firestore = firestore;
        this.postsCollection = firestore.collection("posts");
    }

    public Post save(Post post) throws ExecutionException, InterruptedException {
        if (post.getId() == null || post.getId().isEmpty()) {
            ApiFuture<DocumentReference> result = postsCollection.add(post);
            post.setId(result.get().getId());
            System.out.println("Post criado com ID: " + post.getId());
        } else {
            ApiFuture<WriteResult> result = postsCollection.document(post.getId()).set(post);
            result.get();
            System.out.println("Post atualizado com ID: " + post.getId());
        }
        return post;
    }

    public Optional<Post> findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = postsCollection.document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Post post = document.toObject(Post.class);
            return Optional.ofNullable(post);
        } else {
            return Optional.empty();
        }
    }

    public List<Post> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = postsCollection.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Post> posts = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            posts.add(document.toObject(Post.class));
        }
        return posts;
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> deleteResult = postsCollection.document(id).delete();
        deleteResult.get();
        System.out.println("Post com ID " + id + " deletado.");
    }
}
