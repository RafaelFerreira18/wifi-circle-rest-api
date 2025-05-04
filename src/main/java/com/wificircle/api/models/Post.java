package com.wificircle.api.models;

import java.util.Date;
import java.util.List;

import com.google.cloud.firestore.annotation.DocumentId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {
    @DocumentId
    private String id;
    private String communityId;
    private String authorId;
    private String title;
    private String content;
    private Date createdAt;
    private List<User> upvotedBy;
    private List<User> downvotedBy;
}
