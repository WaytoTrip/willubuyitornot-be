package com.willu.buyitornot.infra.repository;

import com.willu.buyitornot.infra.collection.UserSwipe;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSwipeRepository extends MongoRepository<UserSwipe, ObjectId> {

    boolean existsByUserIdAndSwipeId(ObjectId userId, ObjectId swipeId);
}