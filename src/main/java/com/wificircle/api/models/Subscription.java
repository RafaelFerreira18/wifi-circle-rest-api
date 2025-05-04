package com.wificircle.api.models;

import com.google.cloud.firestore.annotation.DocumentId;
import com.wificircle.api.enums.SubscriptionPlan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Subscription {
    @DocumentId
    private String id;
    private String userId;
    private SubscriptionPlan plan;
    private Date startDate;
    private Date endDate;
    private boolean active;
}
