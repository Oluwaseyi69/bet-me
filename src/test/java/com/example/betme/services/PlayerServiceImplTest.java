package com.example.betme.services;

import com.example.betme.data.model.Bet;
import com.example.betme.data.model.Player;
import com.example.betme.data.repository.BetRepository;
import com.example.betme.data.repository.PlayerRepository;
import com.example.betme.dtos.request.*;
import com.example.betme.dtos.response.BetResponse;
import com.example.betme.dtos.response.DepositResponse;
import com.example.betme.dtos.response.LoginUserResponse;
import com.example.betme.dtos.response.WithdrawalResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PlayerServiceImplTest {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private BetService betService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private BetRepository betRepository;

    @BeforeEach
    public void startWith(){
        playerRepository.deleteAll();
        betRepository.deleteAll();
        playerRepository.deleteAll();

    }

    @Test
    public void testToRegisterUser(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Tomide");
        registerUserRequest.setPassword("password");

        playerService.register(registerUserRequest);
        assertThat(playerRepository.count(), is(1L));

        RegisterUserRequest registerUserRequest1 = new RegisterUserRequest();
        registerUserRequest1.setUsername("Seyi");
        registerUserRequest1.setPassword("password");

        playerService.register(registerUserRequest1);
        assertThat(playerRepository.count(), is(2L));
    }

    @Test
    public void testToLogInRegisteredUser(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Tomide");
        registerUserRequest.setPassword("password");

        playerService.register(registerUserRequest);
        assertThat(playerRepository.count(), is(1L));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Tomide");
        loginRequest.setPassword("password");
        LoginUserResponse loginUserResponse = playerService.login(loginRequest);
        assertThat( loginUserResponse.isLoggedIn(), is(true));
    }

    @Test
    public void testThatPlayerCanDeposit(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Tomide");
        registerUserRequest.setPassword("password");

        playerService.register(registerUserRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Tomide");
        loginRequest.setPassword("password");

        LoginUserResponse loginUserResponse = playerService.login(loginRequest);
        assertThat(loginUserResponse.isLoggedIn(), is(true));

        AddDepositRequest addDepositRequest = new AddDepositRequest();
        addDepositRequest.setAmount("2000");
        addDepositRequest.setId(loginUserResponse.getId());
        System.out.println(loginUserResponse.getId());

        DepositResponse depositResponse = playerService.depositFund(addDepositRequest);

        assertNotNull(depositResponse);
    }
    @Test
    public void testThatPlayerCanConfirmBalance(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Tomide");
        registerUserRequest.setPassword("password");

        playerService.register(registerUserRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Tomide");
        loginRequest.setPassword("password");
        LoginUserResponse loginUserResponse = playerService.login(loginRequest);
        assertThat(loginUserResponse.isLoggedIn(), is(true));

        AddDepositRequest addDepositRequest = new AddDepositRequest();
        addDepositRequest.setAmount("2000");
        addDepositRequest.setId(loginUserResponse.getId());

        DepositResponse depositResponse = playerService.depositFund(addDepositRequest);
        assertNotNull(depositResponse);
        String balance = playerService.checkBalance(loginUserResponse.getId());



        AddDepositRequest addDepositRequest1 = new AddDepositRequest();
        addDepositRequest1.setAmount("3000");
        addDepositRequest1.setId(loginUserResponse.getId());

        DepositResponse depositResponse1 = playerService.depositFund(addDepositRequest1);
        assertNotNull(depositResponse1);

        String balance1 = playerService.checkBalance(loginUserResponse.getId());
        assertThat(balance1, is("5000"));

    }
    @Test
    public void testThatPlayerCanWithdraw(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Tomide");
        registerUserRequest.setPassword("password");

        playerService.register(registerUserRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Tomide");
        loginRequest.setPassword("password");
        LoginUserResponse loginUserResponse = playerService.login(loginRequest);
        assertThat(loginUserResponse.isLoggedIn(), is(true));

        AddDepositRequest addDepositRequest = new AddDepositRequest();
        addDepositRequest.setAmount("2000");
        addDepositRequest.setId(loginUserResponse.getId());

        DepositResponse depositResponse = playerService.depositFund(addDepositRequest);
        assertNotNull(depositResponse);



        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setAmount("1000");
        withdrawRequest.setId(loginUserResponse.getId());

        WithdrawalResponse withdrawalResponse = playerService.withdrawFund(withdrawRequest);

        assertNotNull(withdrawalResponse);

        assertEquals(withdrawalResponse.getBalance(), BigDecimal.valueOf(1000));
        String balance1 = playerService.checkBalance(loginUserResponse.getId());
        System.out.println(balance1);
        assertThat(balance1, is("1000"));


    }
    @Test
    public void testThat_A_RegisteredPlayerCanPlaceBet(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Seyi");
        registerUserRequest.setPassword("password");

        playerService.register(registerUserRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Seyi");
        loginRequest.setPassword("password");

        LoginUserResponse loginUserResponse = playerService.login(loginRequest);
        assertThat(loginUserResponse.isLoggedIn(), is(true));



        Player player = new Player();
        player.setLogIn(true);


        AddBetRequest betRequest = new AddBetRequest();
        betRequest.setAmount(BigDecimal.valueOf(10000));
        betRequest.setEvent("Football");
        betRequest.setUsername("Seyi");


        BetResponse betResponse = betService.placeBet(betRequest);
        assertNotNull(betResponse);
        assertThat(betResponse.getId(),betResponse.getMessage(), is("Bet Successful"));


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

        AddDepositRequest addDepositRequest = new AddDepositRequest();
        addDepositRequest.setAmount("200000");
        addDepositRequest.setId(loginUserResponse.getId());
        DepositResponse depositResponse = playerService.depositFund(addDepositRequest);


        AddBetRequest betRequest = new AddBetRequest();
        betRequest.setId(loginUserResponse.getId());
        betRequest.setAmount(BigDecimal.valueOf(10000));
        betRequest.setEvent("Football");
        betRequest.setUsername("Seyi");

        BetResponse betResponse = playerService.placeBet(betRequest);
        assertNotNull(betResponse);
        assertThat(betResponse.getId(),betResponse.getMessage(), is("Bet Successful"));


    }

}