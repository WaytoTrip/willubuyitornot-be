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

@Document(collection = "games")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Game {

    @Id
    ObjectId id;

    String imageUrl;

    String genre;

    Integer releaseYear;

    String title;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    public Game(String imageUrl, String genre, Integer releaseYear, String title) {
        this.imageUrl = imageUrl;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.title = title;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
