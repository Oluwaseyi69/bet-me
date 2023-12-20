package com.example.betme.services;

import com.example.betme.data.model.Bet;
import com.example.betme.data.repository.BetRepository;
import com.example.betme.dtos.request.AddBetRequest;
import com.example.betme.dtos.response.BetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class BetServiceImpl implements BetService{
    @Autowired
    private BetRepository betRepository;

    @Override
    public BetResponse placeBet(AddBetRequest addBetRequest) {

        Bet bet = new Bet();
        bet.setEvent(addBetRequest.getEvent());
        bet.setAmount(addBetRequest.getAmount());
        bet.setFirstPlayer(addBetRequest.getStarterPlayer());
        bet.setSecondPlayer(addBetRequest.getOpponentPlayer());
        bet.setReferenceId(generateReferenceId());
        betRepository.save(bet);

        BetResponse betResponse = new BetResponse();
        betResponse.setReferenceId(bet.getReferenceId());
        betResponse.setMessage("Bet Successful");



        return betResponse;
    }

    public String generateReferenceId() {
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
