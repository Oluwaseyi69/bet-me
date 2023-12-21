package com.example.betme.services;

import com.example.betme.data.model.Notification;
import com.example.betme.data.model.Player;
import com.example.betme.data.repository.PlayerRepository;
import com.example.betme.dtos.request.*;
import com.example.betme.dtos.response.*;
import com.example.betme.exceptions.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.example.betme.utils.Mapper.map;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService{

    private PlayerRepository playerRepository;
    private BetService betService;
    private NotificationService notificationService;
    private PaymentService paymentService;


    @Override
    public RegisterUserResponse register(RegisterUserRequest registerUserRequest) {
        checkIfPlayerAlreadyExist(registerUserRequest);
        return map(playerRepository.save(map(registerUserRequest)));
    }

    @Override
    public LoginUserResponse login(LoginRequest loginRequest) {
        Player player = getPlayer(loginRequest.getUsername());
        validatePassword(loginRequest, player);

        player.setLogIn(true);
        Player player1 = playerRepository.save(player);

        LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setPlayerId(player1.getId());
        loginUserResponse.setMessage("Login Successful");
        loginUserResponse.setLoggedIn(player1.isLogIn());

        return loginUserResponse;
    }

    private static void validatePassword(LoginRequest loginRequest, Player player) {
        if(!player.getPassword().equals(loginRequest.getPassword()))
            throw new IncorrectDetails("Incorrect Username or Password");
    }

    @Override
    public String checkBalance(String id) {
        Player player = getPlayerById(id);
        return String.valueOf(player.getBalance());
    }

    @Override
    public DepositResponse<?> depositFund(AddDepositRequest addDepositRequest) {
        Player player = getPlayerById(addDepositRequest.getPlayerId());
        checkIfPlayerIsLoggedIn(player);
        CreatePaymentResponse<?> createPaymentResponse = deposit(player,BigDecimal.valueOf(Long.parseLong(addDepositRequest.getAmount())));

        DepositResponse<CreatePaymentResponse<?>> depositResponse = new DepositResponse<>();
        depositResponse.setMessage("Deposit request created Successfully");
        depositResponse.setCreatedPaymentRequest(createPaymentResponse);
        return depositResponse;
    }

    public UpdateBalanceResponse updateBalace(UpdateBalanceRequest updateBalanceRequest){
        Player player = getPlayerById(updateBalanceRequest.getPlayerId());

        PaystackTransactionVerificationResponse<?> response = paymentService.verifyPayment(updateBalanceRequest.getReference());
        if (response.getMessage().contains("Verification successful")){
            player.setBalance(player.getBalance().add(updateBalanceRequest.getAmount()));
            playerRepository.save(player);

            return buildUpdateBalanceResponse(player);

        }
        throw new InvalidPaymentException("Deposit was not Successful");
    }

    private UpdateBalanceResponse buildUpdateBalanceResponse(Player player) {
        UpdateBalanceResponse updateBalanceResponse = new UpdateBalanceResponse();
        updateBalanceResponse.setNewBalance(player.getBalance());
        updateBalanceResponse.setMessage("Balance Updated Successfully");
        return updateBalanceResponse;
    }

    @Override
    public WithdrawalResponse withdrawFund(WithdrawRequest withdrawRequest) {
        Player player = getPlayerById(withdrawRequest.getPlayerId());
        checkIfPlayerIsLoggedIn(player);
        Player player1 = withdrawal(player,BigDecimal.valueOf(Long.parseLong(withdrawRequest.getAmount())));
        playerRepository.save(player1);

        WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
        withdrawalResponse.setMessage("Withdrawal Completed");
        withdrawalResponse.setBalance(player1.getBalance());

        return withdrawalResponse;
    }


    public CreatePaymentResponse<?> deposit(Player player, BigDecimal deposit) {
        if (deposit.compareTo(BigDecimal.ZERO) > 0) {
            return paymentService.createPayment(buildPaymentRequest(player, deposit));
        }
        throw new AmountCanNotNegativeException("Amount cannot be negative");

    }

    private CreatePaymentRequest buildPaymentRequest(Player player, BigDecimal deposit) {
        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setEmail(player.getEmail());
        createPaymentRequest.setAmount(deposit);
        return createPaymentRequest;
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

    private Player getPlayerById(String id) {
        Optional<Player> foundPlayer = playerRepository.findById(id);
        if(foundPlayer.isEmpty()) throw new PlayerNotFound("User Not Found");
        return foundPlayer.get();

    }

    private Player getPlayer(String username) {
        Optional<Player> player = playerRepository.findPlayerByUsername(username);
        if(player.isEmpty()) throw new PlayerNotFound("User Not Found");
        return player.get();
    }

    private void checkIfPlayerAlreadyExist(RegisterUserRequest registerUserRequest) {
            Optional<Player> player = playerRepository.findPlayerByUsername(registerUserRequest.getUsername());
            if (player.isPresent())
                throw new PlayerAlreadyExist("Player already Exist");
    }

    @Override
    public BetResponse placeBet(AddBetRequest addBetRequest) {
        Player player = getPlayerById(addBetRequest.getFirstPlayerId());
        checkIfPlayerIsLoggedIn(player);
        Player opponent = getPlayer(addBetRequest.getOpponentUsername());

        addBetRequest.setStarterPlayer(player);
        addBetRequest.setOpponentPlayer(opponent);
        checkPlayersBalance(addBetRequest);

        BetResponse response = betService.placeBet(addBetRequest);
        Notification notification = notificationService.notifyPlayer(buildNotificationRequest(addBetRequest));

        opponent.getNotifications().add(notification);
        playerRepository.save(opponent);
        return response;

    }

    private NotificationRequest buildNotificationRequest(AddBetRequest addBetRequest) {
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setCaller(addBetRequest.getStarterPlayer());
        notificationRequest.setAmount(addBetRequest.getAmount());
        notificationRequest.setEvent(addBetRequest.getEvent());
        notificationRequest.setReceivingPlayer(addBetRequest.getOpponentPlayer());
        return new NotificationRequest();
    }

    private void checkPlayersBalance(AddBetRequest addBetRequest) {
        if(addBetRequest.getStarterPlayer().getBalance().compareTo(addBetRequest.getAmount()) < 0){
            throw new InsufficientFundException("Insufficient Balance");
        }

        if(addBetRequest.getOpponentPlayer().getBalance().compareTo(addBetRequest.getAmount()) < 0){
            throw new InsufficientFundException("Opponent Balance is Insufficient");
        }
    }


}

