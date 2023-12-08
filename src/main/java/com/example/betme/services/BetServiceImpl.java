package com.example.betme.services;

import com.example.betme.data.model.Bet;
import com.example.betme.data.model.Player;
import com.example.betme.data.repository.BetRepository;
import com.example.betme.dtos.request.AddBetRequest;
import com.example.betme.dtos.response.BetResponse;
import com.example.betme.exceptions.LogInFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.random.RandomGenerator;

@Service
public class BetServiceImpl implements BetService{
    @Autowired
    private BetRepository betRepository;

    @Override
    public BetResponse placeBet(AddBetRequest addBetRequest) {
        addBetRequest.setId(addBetRequest.getId());
        addBetRequest.setEvent(addBetRequest.getEvent());
        addBetRequest.setAmount(addBetRequest.getAmount());

//        checkIfPlayerIsLoggedIn();
        System.out.println("now i'm here");
        Bet bet = new Bet();
        bet.setEvent(addBetRequest.getEvent());
        bet.setAmount(addBetRequest.getAmount());
        generateId();
        betRepository.save(bet);

        BetResponse betResponse = new BetResponse();
        betResponse.setId(addBetRequest.getId());
//        String betMessage = betResponse.getMessage();
        betResponse.setMessage("Bet Successful");
//        betResponse.setMessage(ticketNumber);

        return betResponse;
    }
    public void checkIfPlayerIsLoggedIn(Player player) {
        if(!player.isLogIn()){
            System.out.println("i am here");
            throw new LogInFailureException("User not Logged in");
        }
    }

    public String generateId(){
        String alphnumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//        String alphnumeric = "[Aa-Zz0-9]";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = RandomGenerator.getDefault().nextInt(alphnumeric.length());
            builder.append(alphnumeric.charAt(index));
        }
        System.out.println(builder);
        return alphnumeric.toString();
    }

}
