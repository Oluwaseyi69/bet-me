package com.example.betme.services;

import com.example.betme.data.model.Bet;
import com.example.betme.data.model.Player;
import com.example.betme.data.repository.BetRepository;
import com.example.betme.dtos.request.AddBetRequest;
import com.example.betme.dtos.response.BetResponse;
import com.example.betme.exceptions.LogInFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.random.RandomGenerator;

@Service
public class BetServiceImpl implements BetService{
    @Autowired
    private BetRepository betRepository;

    @Override
    public BetResponse placeBet(AddBetRequest addBetRequest) {
        addBetRequest.setUsername(addBetRequest.getUsername());
        addBetRequest.setEvent(addBetRequest.getEvent());
        addBetRequest.setAmount(addBetRequest.getAmount());

//        checkIfPlayerIsLoggedIn(player);
        System.out.println("now i'm here");
        Bet bet = new Bet();
        bet.setEvent(addBetRequest.getEvent());
        bet.setAmount(addBetRequest.getAmount());
        bet.setId(generateId());
        betRepository.save(bet);

        BetResponse betResponse = new BetResponse();
        betResponse.setId(generateId());
        betResponse.setMessage("Bet Successful");
//        betResponse.setMessage(ticketNumber);

        return betResponse;
    }
    public void checkIfPlayerIsLoggedIn(Player player) {
        if(player.isLogIn()){
            return;
        }
        else {
            System.out.println("i am here");
            throw new LogInFailureException("User not Logged in");
        }
    }

    public String generateId() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder stringBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 8; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

}
