package com.willu.buyitornot.infra.repository;

import com.willu.buyitornot.infra.collection.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends MongoRepository<Game, ObjectId> {

    List<Game> findAllByIdIn(List<ObjectId> ids);
}