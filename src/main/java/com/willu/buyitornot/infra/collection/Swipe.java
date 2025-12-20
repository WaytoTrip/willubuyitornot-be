package com.willu.buyitornot.infra.collection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "swipes")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Swipe {

    @Id
    ObjectId id;

    List<ObjectId> gameIdList;

    LocalDateTime createdAt;

    public Swipe(List<ObjectId> gameIdList) {
        this.gameIdList = gameIdList;
        this.createdAt = LocalDateTime.now();
    }
}
