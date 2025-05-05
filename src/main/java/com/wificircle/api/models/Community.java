package com.wificircle.api.models;

import java.util.Date;
import java.util.List;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Community {
    @DocumentId
    private String id;
    private String name;
    private String title;
    private String description;
    private String ownerId;
    private Timestamp createdAt = Timestamp.now();
    private List<String> moderatorIds;
}