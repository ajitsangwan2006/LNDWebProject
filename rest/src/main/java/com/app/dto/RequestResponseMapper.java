package com.app.dto;

import com.model.dao.Wallet;

public class RequestResponseMapper {
    public static Wallet walletRequestToWalletConverter(WalletRequest request){
        Wallet wallet = new Wallet();
        wallet.setCoins(request.getCoins());
        wallet.setTransactionId(request.getTransactionId());
        return wallet;
    }

    public static WalletResponse walletToWalletResponse(Wallet wallet){
        WalletResponse response = new WalletResponse();
        response.setCoins(wallet.getCoins());
        response.setTransactionId(wallet.getTransactionId());
        response.setVersion(wallet.getVersion());
        return response;
    }
}
