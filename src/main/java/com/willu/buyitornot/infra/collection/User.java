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

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    ObjectId id;

    String nickname;

    LocalDateTime createdAt;

    public User(String nickname) {
        this.nickname = nickname;
        this.createdAt = LocalDateTime.now();
    }
}
