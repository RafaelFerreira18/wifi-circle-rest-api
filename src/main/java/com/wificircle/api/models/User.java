package com.wificircle.api.models;

import com.google.cloud.firestore.annotation.DocumentId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @DocumentId
    private String uid;
    private String username;
    private String email;
    private String avatarUrl;
}
