package com.app.controller;

import com.app.dto.RequestResponseMapper;
import com.app.dto.WalletRequest;
import com.app.dto.WalletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dao.Wallet;
import com.model.dao.WalletService;
import com.model.exception.DuplicateTransactionException;
import com.model.exception.InsufficientBalanceException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

/**
 * WalletController:
 * REST controller for get, credit and debit the wallet
 * methods: getWallet(), credit(), debit()
 */
public class WalletController {
    private WalletService walletService;
    private ObjectMapper mapper;

    public WalletController(){
        walletService = new WalletService();
        mapper = new ObjectMapper();
    }

    /*
     * Credits to a wallet will return the updated balance and a 201/Created HTTP response code.
     * Requests for an known wallet's coin balance return the balance with a 200 HTTP response code.
     */
    public void getWallet(HttpExchange exchange, String walletId) throws IOException {
        Wallet wallet = walletService.getWallet(walletId);
        if(wallet != null){
            produceSuccessResponse(exchange, RequestResponseMapper.walletToWalletResponse(wallet), 200);
        }else{
            produceBadResponse(exchange, 404);
        }
    }

    /*
     * Credits to a wallet will return the updated balance and a 201/Created HTTP response code.
     * If the same Credit is issued immediately after each other, the request is acknowledged as successful, but is not actually processed.
     * The balance is returned with a 202/Accepted HTTP response code.
     */
    public void credit(HttpExchange exchange, String walletId) throws IOException {
        WalletRequest walletRequest = mapper.readValue(exchange.getRequestBody(), WalletRequest.class);
        Wallet wallet = RequestResponseMapper.walletRequestToWalletConverter(walletRequest);
        try {
            wallet = walletService.credit(wallet, walletId);
            produceSuccessResponse(exchange, RequestResponseMapper.walletToWalletResponse(wallet), 201);
        }catch (DuplicateTransactionException e) {
            produceSuccessResponse(exchange, RequestResponseMapper.walletToWalletResponse(wallet), 202);
        }
    }

    /*
     * Debits to a wallet will return the updated balance and a 201/Created HTTP response code.
     * If the same debit is issued immediately after each other, the request is acknowledged as successful, but is not actually processed.
     * But the balance is returned with a 202/Accepted HTTP response code.
     * Debit for more than the balance will be rejected with a 400/BadRequest HTTP response code.
     */
    public void debit(HttpExchange exchange, String walletId) throws IOException {
        WalletRequest walletRequest = mapper.readValue(exchange.getRequestBody(), WalletRequest.class);
        Wallet wallet = RequestResponseMapper.walletRequestToWalletConverter(walletRequest);
        try {
            wallet = walletService.debit(wallet, walletId);
            produceSuccessResponse(exchange, RequestResponseMapper.walletToWalletResponse(wallet), 201);
        }catch (DuplicateTransactionException e) {
            produceSuccessResponse(exchange, RequestResponseMapper.walletToWalletResponse(wallet), 202);
        } catch (InsufficientBalanceException e) {
            produceBadResponse(exchange, 400);
        }
    }

    /*
     * Produce the success response by passing the wallet and codes (200, 201 & 202)
     */
    private void produceSuccessResponse(HttpExchange exchange, WalletResponse wallet, int code) throws IOException {
        byte[] response = mapper.writeValueAsBytes(wallet);
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseHeaders().set("Content-Type", "appication/json");
        OutputStream output = exchange.getResponseBody();
        output.write(response);
        output.flush();
        exchange.close();
    }

    /*
     * Produce the bad response by passing the code (400 & 404)
     */
    private void produceBadResponse(HttpExchange exchange, int code) throws IOException {
        exchange.sendResponseHeaders(code, -1);
    }


}
