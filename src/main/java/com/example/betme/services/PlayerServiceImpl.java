package com.example.betme.services;

import com.example.betme.data.model.Bet;
import com.example.betme.data.model.Player;
import com.example.betme.data.repository.BetRepository;
import com.example.betme.data.repository.PlayerRepository;
import com.example.betme.dtos.request.*;
import com.example.betme.dtos.response.*;
import com.example.betme.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.example.betme.utils.Mapper.map;

@Service
public class PlayerServiceImpl implements PlayerService{
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerService playerService;
//    @Autowired
//    private BetService betService;
    @Autowired
    private BetRepository betRepository;


    @Override
    public RegisterUserResponse register(RegisterUserRequest registerUserRequest) {
        findPlayer(registerUserRequest);
        return map(playerRepository.save(map(registerUserRequest)));
    }

    @Override
    public LoginUserResponse login(LoginRequest loginRequest) {
        Optional<Player> player = getPlayer(loginRequest.getUsername());
        if(player.isEmpty()) throw new PlayerNotFound("Player not found");
        if(!player.get().getPassword().equals(loginRequest.getPassword()))
            throw new IncorrectDetails("Incorrect Username or Password");

        Player newPlayer = player.get();
        newPlayer.setLogIn(true);
        Player player1 = playerRepository.save(newPlayer);
        LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setId(player1.getId());
        loginUserResponse.setMessage("Login Successful");
        loginUserResponse.setLoggedIn(player1.isLogIn());
        return loginUserResponse;
    }

    @Override
    public String checkBalance(String id) {
        Player player = findPlayerBy(id);
        return String.valueOf(player.getBalance());
    }

    @Override
    public DepositResponse depositFund(AddDepositRequest addDepositRequest) {
        Player player = findPlayerBy(addDepositRequest.getId());
        checkIfPlayerIsLoggedIn(player);
        Player newPlayer = deposit(player,BigDecimal.valueOf(Long.parseLong(addDepositRequest.getAmount())));
        playerRepository.save(newPlayer);

        DepositResponse depositResponse = new DepositResponse();
//        depositResponse.setId(player.getId());
        depositResponse.setMessage("Deposit Successfully");
        depositResponse.setBalance(newPlayer.getBalance());
        return depositResponse;
    }

    @Override
    public WithdrawalResponse withdrawFund(WithdrawRequest withdrawRequest) {
        Player player = findPlayerBy(withdrawRequest.getId());
        System.out.print(player);
        checkIfPlayerIsLoggedIn(player);
        Player player1 = withdrawal(player,BigDecimal.valueOf(Long.parseLong(withdrawRequest.getAmount())));
        playerRepository.save(player1);

        WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
        withdrawalResponse.setMessage("Withdrawal Completed");
        withdrawalResponse.setBalance(player1.getBalance());

        return withdrawalResponse;
    }



    public Player deposit(Player player, BigDecimal deposit) {
        if (deposit.compareTo(BigDecimal.ZERO) > 0) {
            player.setBalance(player.getBalance().add(deposit));
            return player;
        }
        throw new AmountCanNotNegativeException("Amount cannot be negative");

    }



    public Player withdrawal(Player player,BigDecimal withdrawal) {
        if (withdrawal.compareTo(BigDecimal.ZERO) > 0){
            BigDecimal balance = player.getBalance().subtract(withdrawal);
            player.setBalance(balance);
            return player;
        } else {
            return player;
        }

    }
    private void checkIfPlayerIsLoggedIn(Player player) {
        if(!player.isLogIn()){
            throw new LogInFailureException("User not Logged in");
        }
    }

    private Player findPlayerBy(String id) {
        Player foundPlayer =  getPlayerById(id);
        return foundPlayer;
    }

    private Player getPlayerById(String id) {
        Optional<Player> foundPlayer = playerRepository.findById(id);
        //        if(foundPlayer.isEmpty()) throw new PlayerNotFound("Not found"){
        //        };
        return foundPlayer.orElse(null);


    }

    private Optional<Player> getPlayer(String username) {
        Optional<Player> player = playerRepository.findPlayerByUsername(username);
        return player;
    }

    private void findPlayer(RegisterUserRequest registerUserRequest) {
            Optional<Player> player = getPlayer(registerUserRequest.getUsername());
            if (player.isPresent())
                throw new PlayerAlreadyExist("Player already Exist");
    }

//    @Override
//    public BetResponse placeBet(AddBetRequest addBetRequest) {
//        Player player = findPlayerBy(addBetRequest.getId());
//        checkIfPlayerIsLoggedIn(player);
//        if (player.getBalance().compareTo(addBetRequest.getAmount()) < 0) {
//            BetResponse betResponse = new BetResponse();
//            betResponse.setMessage("Insufficient funds");
//            return betResponse;
//        }
//        addBetRequest.setUsername(addBetRequest.getUsername());
//        addBetRequest.setEvent(addBetRequest.getEvent());
//        addBetRequest.setAmount(addBetRequest.getAmount());
//
//        System.out.println("now i'm here");
//        Bet bet = new Bet();
//
//        bet.setEvent(addBetRequest.getEvent());
//        bet.setAmount(addBetRequest.getAmount());
//        bet.setId(generateId());
//        betRepository.save(bet);
//
//        BetResponse betResponse = new BetResponse();
//        betResponse.setId(generateId());
//        betResponse.setMessage("Bet Successful");
////        betResponse.setMessage(ticketNumber);
//
//        return betResponse;
//    }

    @Override
    public BetResponse placeBet(AddBetRequest addBetRequest) {
        Player player = findPlayerBy(addBetRequest.getId());

        if (player == null) {
//            BetResponse betResponse = new BetResponse();
//            betResponse.setMessage("Player not found");
//            return betResponse;

            throw new PlayerNotFound("Player not found");
        }

        checkIfPlayerIsLoggedIn(player);

        BigDecimal betAmount = addBetRequest.getAmount();
        BigDecimal playerBalance = player.getBalance();

        if (playerBalance.compareTo(betAmount) < 0) {
//            BetResponse betResponse = new BetResponse();
//            betResponse.setMessage("Insufficient funds");
//            return betResponse;
            throw new InsufficientFundException("Insufficient Fund");
        }

        // Rest of your code remains unchanged...

        BetResponse betResponse = new BetResponse();
        betResponse.setId(generateId());
        betResponse.setMessage("Bet Successful");
        return betResponse;
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

