package com.model.dao;

import com.model.exception.DuplicateTransactionException;
import com.model.exception.InsufficientBalanceException;

/**
 * WalletService perform the get, credit and debit operations on wallet store.
 * Unusual behaviour is handled by throwing custom exceptions
 */
public class WalletService {
    private WalletStore walletStore = WalletStore.getWalletStore();

    //Returns null if not found
    public Wallet getWallet(String walletId){
        return walletStore.get(walletId);
    }

    /**
     * Method: credit
     * Creates wallet with requested balance and version 1 if wallet dose not exist.
     * Validates for duplicate request on basis of transactionId and throws DuplicateTransactionException if found duplicate.
     * Otherwise credit the coins into wallet and increase the version by 1, also update with new transactionId
     */
    public Wallet credit(Wallet walletRequest, String walletId) throws DuplicateTransactionException {
        Wallet wallet = walletStore.get(walletId);
        if(wallet == null){
            wallet = walletRequest;
            wallet.setVersion(1);
            walletStore.put(walletId, wallet);
        }else{
            if(wallet.getTransactionId().equals(walletRequest.getTransactionId())){
                throw new DuplicateTransactionException();
            }else{
                wallet.setTransactionId(walletRequest.getTransactionId());
                wallet.setCoins(walletRequest.getCoins() + wallet.getCoins());
                wallet.setVersion(wallet.getVersion() + 1);
                walletStore.put(walletId, wallet);
            }
        }
        return wallet;
    }

    /**
     * Method: debit
     * Validates for duplicate request on basis of transactionId and throws DuplicateTransactionException if found duplicate.
     * Debit for more than the balance will be rejected with InsufficientBalanceException
     * Otherwise debit the coins into wallet and increase the version by 1, also update with new transactionId
     */
    public Wallet debit(Wallet walletRequest, String walletId) throws DuplicateTransactionException, InsufficientBalanceException {
        Wallet wallet = walletStore.get(walletId);
        if(wallet != null){
            if(wallet.getTransactionId().equals(walletRequest.getTransactionId())){
                throw new DuplicateTransactionException();
            }else {
                long coinsBalance = wallet.getCoins() - walletRequest.getCoins();
                if(coinsBalance < 0){
                    throw new InsufficientBalanceException();
                }else {
                    wallet.setCoins(coinsBalance);
                    wallet.setTransactionId(walletRequest.getTransactionId());
                    wallet.setVersion(wallet.getVersion() + 1);
                    walletStore.put(walletId, wallet);
                }
            }
        }
        return wallet;
    }
}
