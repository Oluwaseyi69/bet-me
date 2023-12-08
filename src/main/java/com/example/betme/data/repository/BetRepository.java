package com.example.betme.data.repository;

import com.example.betme.data.model.Bet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BetRepository extends MongoRepository<Bet, String> {
    Optional<Bet> findBetByIdMatches(String id);
}
