package com.example.betme.controller;


import com.example.betme.dtos.request.AddBetRequest;
import com.example.betme.dtos.response.BetResponse;
import com.example.betme.services.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Bet-Me")
public class BetController {
    @Autowired
    private BetService betService;


    @PostMapping("/PlaceBet")
    public String placeBet(@RequestBody AddBetRequest addBetRequest){
        try{
            betService.placeBet(addBetRequest);

            return "Bet Successful";
        }catch (Exception e){
            return e.getMessage();
        }


    }
}
