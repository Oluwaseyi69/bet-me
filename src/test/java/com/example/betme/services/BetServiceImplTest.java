package com.example.betme.services;

import com.example.betme.data.model.Bet;
import com.example.betme.data.model.Player;
import com.example.betme.data.repository.BetRepository;
import com.example.betme.data.repository.PlayerRepository;
import com.example.betme.dtos.request.AddBetRequest;
import com.example.betme.dtos.request.LoginRequest;
import com.example.betme.dtos.request.RegisterUserRequest;
import com.example.betme.dtos.response.BetResponse;
import com.example.betme.dtos.response.LoginUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BetServiceImplTest {
    @Autowired
    private BetRepository betRepository;
    @Autowired
    private BetService betService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerRepository playerRepository;

    @BeforeEach
    public void startwith(){
        betRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Test
    public void testThatPlayerCanPlaceBet(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Seyi");
        registerUserRequest.setPassword("password");

        playerService.register(registerUserRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Seyi");
        loginRequest.setPassword("password");

        LoginUserResponse loginUserResponse = playerService.login(loginRequest);
        assertThat(loginUserResponse.isLoggedIn(), is(true));

        Bet bet = new Bet();
        bet.setAmount(BigDecimal.valueOf(10000));
        bet.setEvent("Football");
        bet.setDate(bet.getDate());
//        System.out.println(bet.getDate());


        AddBetRequest betRequest = new AddBetRequest();
        betRequest.setAmount(BigDecimal.valueOf(10000));
        betRequest.setEvent("Football");
        betRequest.setDate(betRequest.getDate());

        BetResponse betResponse = betService.placeBet(betRequest);
        assertNotNull(betResponse);
        assertThat(betResponse.getId(),betResponse.getMessage(), is("Bet Successful"));


    }

}