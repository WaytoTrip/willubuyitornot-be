package com.willu.buyitornot.infra.collection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "user_swipes")
@CompoundIndex(name = "user_swipe_unique", def = "{'userId': 1, 'swipeId': 1}", unique = true)
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSwipe {

    @Id
    ObjectId id;

    @Indexed
    ObjectId userId;

    @Indexed
    ObjectId swipeId;

    List<ObjectId> buy = new ArrayList<>();

    List<ObjectId> skip = new ArrayList<>();

    List<ObjectId> maybe = new ArrayList<>();

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    public UserSwipe(ObjectId userId, ObjectId swipeId) {
        this.userId = userId;
        this.swipeId = swipeId;
        this.buy = new ArrayList<>();
        this.skip = new ArrayList<>();
        this.maybe = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
