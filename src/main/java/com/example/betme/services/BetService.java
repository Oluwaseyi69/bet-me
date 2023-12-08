package com.example.betme.services;

import com.example.betme.dtos.request.AddBetRequest;
import com.example.betme.dtos.response.BetResponse;

public interface BetService {
    BetResponse placeBet (AddBetRequest addBetRequest);

}
