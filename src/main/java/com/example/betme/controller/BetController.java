//package com.example.betme.controller;
//
//
//import com.example.betme.data.model.Player;
//import com.example.betme.dtos.request.AddBetRequest;
//import com.example.betme.dtos.response.BetResponse;
//import com.example.betme.services.BetService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("Bet-Me")
//public class BetController {
//    @Autowired
//    private BetService betService;
//
//
//    @PostMapping("/PlaceBet")
//    public BetResponse placeBet(@RequestBody AddBetRequest addBetRequest, Player player){
//        try{
//           BetResponse betResponse = betService.placeBet(addBetRequest, player);
//            return betResponse;
//        }catch (Exception e){
//            BetResponse betResponse = new BetResponse();
//            betResponse.setMessage(e.getMessage());
//            return betResponse;
//        }
//
//
//    }
//}
