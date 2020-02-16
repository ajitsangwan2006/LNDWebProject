package com.model.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * WalletStore: Manages the wallet data source get and put operations
 */
public class WalletStore {

    private static WalletStore store;
    private Map<String, Wallet> walletDataSource;

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
        walletDataSource.put(walletId, wallet);
    }

    protected Wallet get(String walletId) {
        return walletDataSource.get(walletId);
    }
}
