package com.example.demo.dao;

import com.example.demo.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by koreyoshi on 2017/12/13.
 */
public interface ConversationRepository extends MongoRepository<User,String> {
}
