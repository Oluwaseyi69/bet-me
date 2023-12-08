package com.example.betme.services;

import com.example.betme.data.model.Player;
import com.example.betme.data.repository.PlayerRepository;
import com.example.betme.dtos.request.AddDepositRequest;
import com.example.betme.dtos.request.LoginRequest;
import com.example.betme.dtos.request.RegisterUserRequest;
import com.example.betme.dtos.request.WithdrawRequest;
import com.example.betme.dtos.response.DepositResponse;
import com.example.betme.dtos.response.LoginUserResponse;
import com.example.betme.dtos.response.RegisterUserResponse;
import com.example.betme.dtos.response.WithdrawalResponse;
import com.example.betme.exceptions.IncorrectDetails;
import com.example.betme.exceptions.LogInFailureException;
import com.example.betme.exceptions.PlayerAlreadyExist;
import com.example.betme.exceptions.PlayerNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService{
    @Autowired
    private PlayerRepository playerRepository;

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

        player.get().setLogIn(true);
        Player player1 = playerRepository.save(player.get());
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
        deposit(player,BigDecimal.valueOf(Long.parseLong(addDepositRequest.getAmount())));
        playerRepository.save(player);

        DepositResponse depositResponse = new DepositResponse();
        depositResponse.setId(player.getId());
        depositResponse.setMessage("Deposit Successfully");
        return depositResponse;
    }

    @Override
    public WithdrawalResponse withdrawFund(WithdrawRequest withdrawRequest) {
        Player player = findPlayerBy(withdrawRequest.getAccountId());
        checkIfPlayerIsLoggedIn(player);
        Player player1 = withdrawal(player,BigDecimal.valueOf(Long.parseLong(withdrawRequest.getAmount())));
        playerRepository.save(player1);

        WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
        withdrawalResponse.setId(player1.getId());
        withdrawalResponse.setBalance(player1.getBalance());
        withdrawalResponse.setMessage("Withdrawal Completed");
        return withdrawalResponse;
    }

    public void deposit(Player player, BigDecimal deposit) {
        if (deposit.compareTo(BigDecimal.ZERO) > 0)
            player.setBalance(player.getBalance().add(deposit));
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
        System.out.println("i got here");
        Player foundPlayer = getPlayerById(id);
        return foundPlayer;
    }

    private Player getPlayerById(String id) {
        Optional<Player> foundPlayer = playerRepository.findById(id);
        if(foundPlayer.isEmpty()) throw new PlayerNotFound("Not found");
        return foundPlayer.get();
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
    private static Player map(RegisterUserRequest registerUserRequest){
        Player user = new Player();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(registerUserRequest.getPassword());
        user.setBalance(BigDecimal.ZERO);
        user.setLogIn(false);
        user.setId(registerUserRequest.getId());

        return user;
    }

    private static RegisterUserResponse map(Player player){
        RegisterUserResponse registerUserResponse = new RegisterUserResponse();
        registerUserResponse.setUsername(player.getUsername());
        registerUserResponse.setRegisterDate(DateTimeFormatter
                .ofPattern("EEE dd/MMM/yyyy HH:mm:ss a")
                .format(LocalDateTime.now()));
        registerUserResponse.setMessage("Successful");
        registerUserResponse.setId(player.getId());
        return registerUserResponse;
    }
}

