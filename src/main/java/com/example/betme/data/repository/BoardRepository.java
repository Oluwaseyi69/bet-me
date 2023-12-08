package com.example.betme.data.repository;

import com.example.betme.data.model.BoardMan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardRepository extends MongoRepository<BoardMan, String> {

}
