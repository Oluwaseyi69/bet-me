package com.example.betme.services;

import com.example.betme.data.model.Notification;
import com.example.betme.data.model.Player;
import com.example.betme.data.model.Role;
import com.example.betme.data.repository.PlayerRepository;
import com.example.betme.dtos.request.*;
import com.example.betme.dtos.response.*;
import com.example.betme.exceptions.*;
import com.example.betme.security.services.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
public class PlayerServiceImpl implements PlayerService{

    private PlayerRepository playerRepository;
    private BetService betService;
    private NotificationService notificationService;
    private PaymentService paymentService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;


    @Override
    public RegisterUserResponse register(RegisterUserRequest registerUserRequest) {
        Player registeredPlayer = Player.builder()
                .username(registerUserRequest.getUsername())
                .email(registerUserRequest.getEmail())
                .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                .role(Role.USER)
                .build();

        playerRepository.save(registeredPlayer);
        String jwtToken = jwtService.generateToken(registeredPlayer);
        return RegisterUserResponse.builder()
                .email(registeredPlayer.getEmail())
                .token(jwtToken)
                .message("Successfully created user")
                .build();
    }

    public LoginPlayerResponse login(LoginRequest request) {

        try {
            authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            Player player = playerRepository.findPlayerByUsername(request.getUsername()).orElse(null);
            if (player == null) {
                return null;
            }

            String jwtToken = jwtService.generateToken( player);

            return LoginPlayerResponse.builder()
                    .token(jwtToken)
                    .message("successfully logged in")
                    .build();
        } catch (AuthenticationException e) {
            return LoginPlayerResponse.builder().message( e.getMessage()).build();
        }
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
            log.info("payRequest--->{}", buildPaymentRequest(player, deposit));
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

    private Player getPlayer(String email) {
        Optional<Player> player = playerRepository.findPlayerByUsername(email);
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

