package com.example.betme.services;

import com.example.betme.config.PaymentConfiguration;
import com.example.betme.data.repository.BetRepository;
import com.example.betme.data.repository.PlayerRepository;
import com.example.betme.dtos.request.*;
import com.example.betme.dtos.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;


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
        addDepositRequest.setPlayerId(loginUserResponse.getPlayerId());
        System.out.println(loginUserResponse.getPlayerId());

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
        addDepositRequest.setPlayerId(loginUserResponse.getPlayerId());

        DepositResponse depositResponse = playerService.depositFund(addDepositRequest);
        assertNotNull(depositResponse);
        String balance = playerService.checkBalance(loginUserResponse.getPlayerId());



        AddDepositRequest addDepositRequest1 = new AddDepositRequest();
        addDepositRequest1.setAmount("3000");
        addDepositRequest1.setPlayerId(loginUserResponse.getPlayerId());

        DepositResponse depositResponse1 = playerService.depositFund(addDepositRequest1);
        assertNotNull(depositResponse1);

        String balance1 = playerService.checkBalance(loginUserResponse.getPlayerId());
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
        addDepositRequest.setPlayerId(loginUserResponse.getPlayerId());

        DepositResponse depositResponse = playerService.depositFund(addDepositRequest);
        assertNotNull(depositResponse);


        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setAmount("1000");
        withdrawRequest.setPlayerId(loginUserResponse.getPlayerId());

        WithdrawalResponse withdrawalResponse = playerService.withdrawFund(withdrawRequest);

        assertNotNull(withdrawalResponse);

        assertEquals(withdrawalResponse.getBalance(), BigDecimal.valueOf(1000));
        String balance1 = playerService.checkBalance(loginUserResponse.getPlayerId());
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


        AddBetRequest betRequest = new AddBetRequest();
        betRequest.setAmount(BigDecimal.valueOf(10000));
        betRequest.setEvent("Football");
        betRequest.setFirstPlayerId(loginUserResponse.getPlayerId());


        BetResponse betResponse = betService.placeBet(betRequest);
        assertNotNull(betResponse);
        assertThat(betResponse.getReferenceId(),betResponse.getMessage(), is("Bet Successful"));


    }
    @Test
    public void testThatPlayerCanPlaceBet(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Seyi");
        registerUserRequest.setPassword("password");

        RegisterUserRequest registerNewUserRequest = new RegisterUserRequest();
        registerNewUserRequest.setUsername("Tomide");
        registerNewUserRequest.setPassword("password");

        playerService.register(registerUserRequest);
        RegisterUserResponse userResponse = playerService.register(registerNewUserRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Seyi");
        loginRequest.setPassword("password");
        LoginUserResponse loginUserResponse = playerService.login(loginRequest);
        assertThat(loginUserResponse.isLoggedIn(), is(true));

        LoginRequest request = new LoginRequest();
        request.setUsername("Tomide");
        request.setPassword("password");
        LoginUserResponse loggedResponse = playerService.login(request);

        AddDepositRequest addDepositRequest = new AddDepositRequest();
        addDepositRequest.setAmount("200000");
        addDepositRequest.setPlayerId(loginUserResponse.getPlayerId());
        DepositResponse depositResponse = playerService.depositFund(addDepositRequest);

        AddDepositRequest depositRequest = new AddDepositRequest();
        depositRequest.setAmount("100000");
        depositRequest.setPlayerId(loggedResponse.getPlayerId());
        DepositResponse newDepositResponse = playerService.depositFund(depositRequest);


        AddBetRequest betRequest = new AddBetRequest();
        betRequest.setFirstPlayerId(loginUserResponse.getPlayerId());
        betRequest.setOpponentUsername("Tomide");
        betRequest.setAmount(BigDecimal.valueOf(1000));
        betRequest.setEvent("Football");

        BetResponse betResponse = playerService.placeBet(betRequest);
        assertNotNull(betResponse);
        assertThat(betResponse.getReferenceId(),betResponse.getMessage(), is("Bet Successful"));


    }



}