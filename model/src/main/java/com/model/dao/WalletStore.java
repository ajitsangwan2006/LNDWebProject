package com.model.dao;

import java.util.*;

/**
 * WalletStore: Manages the wallet data source get and put operations
 */
public class WalletStore {

    private static WalletStore store;
    private Map<String, LinkedList<Wallet>> walletDataSource;

    private WalletStore(){
        walletDataSource = Collections.synchronizedMap(new HashMap<>());
    }

    protected static WalletStore getWalletStore(){
        if(store == null){
            store = new WalletStore();
        }
        return store;
    }

    protected void put(String walletId, Wallet wallet) {
        LinkedList<Wallet> existingData = walletDataSource.get(walletId);
        if(existingData == null){
            existingData = new LinkedList<>();
            existingData.push(wallet);
        }else {
            existingData.addLast(wallet);
        }
        walletDataSource.put(walletId, existingData);
    }

    protected Wallet get(String walletId) {
        LinkedList<Wallet> existingData = walletDataSource.get(walletId);
        return existingData != null ? existingData.getLast() : null;
    }
}
