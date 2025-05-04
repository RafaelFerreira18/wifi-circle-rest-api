package com.wificircle.api.models;

import com.google.cloud.firestore.annotation.DocumentId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @DocumentId
    private String id;
    private String postId;
    private String authorId;
    private String content;
}
