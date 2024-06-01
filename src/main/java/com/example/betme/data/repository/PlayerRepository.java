package com.example.betme.data.repository;

import com.example.betme.data.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface   PlayerRepository extends MongoRepository<Player, String> {
    Optional<Player> findPlayerByEmail(String email);

    Optional<Player> findPlayerByUsername(String username);
}
