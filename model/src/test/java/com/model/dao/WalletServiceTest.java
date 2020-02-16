package com.model.dao;

import com.model.exception.DuplicateTransactionException;
import com.model.exception.InsufficientBalanceException;
import org.junit.Test;

import static org.junit.Assert.*;

public class WalletServiceTest {

    private WalletService walletService = new WalletService();

    @Test
    public void test_get_unknown_wallet() {
        Wallet wallet = walletService.getWallet("unknownWalletId");
        assertNull(wallet);
    }

    @Test
    public void test_get_known_wallet() throws DuplicateTransactionException {
        Wallet request1 = new Wallet();
        request1.setTransactionId("trx1");
        request1.setCoins(100l);
        walletService.credit(request1, "testWalletID_1");
        Wallet request2 = walletService.getWallet("testWalletID_1");
        assertNotNull(request2);
        assertEquals(request1.getTransactionId(), request2.getTransactionId());
    }

    @Test(expected = DuplicateTransactionException.class)
    public void test_valid_duplicate_credit() throws DuplicateTransactionException {
        Wallet request = new Wallet();
        request.setTransactionId("trx1");
        request.setCoins(100l);
        walletService.credit(request, "testWalletID_2");
        walletService.credit(request, "testWalletID_2");
    }

    @Test
    public void test_multiple_credits() throws DuplicateTransactionException {
        Wallet request1 = new Wallet();
        request1.setTransactionId("trx1");
        request1.setCoins(100l);
        walletService.credit(request1, "testWalletID_3");
        Wallet request2 = new Wallet();
        request2.setTransactionId("trx2");
        request2.setCoins(100l);
        Wallet wallet = walletService.credit(request2, "testWalletID_3");
        assertNotNull(wallet);
        assertEquals(200l, (long)wallet.getCoins());
    }

    @Test
    public void test_valid_debit() throws DuplicateTransactionException, InsufficientBalanceException {
        Wallet request1 = new Wallet();
        request1.setTransactionId("trx2");
        request1.setCoins(100l);
        walletService.credit(request1, "testWalletID_4");
        Wallet request2 = new Wallet();
        request2.setTransactionId("trx3");
        request2.setCoins(50l);
        Wallet result = walletService.debit(request2, "testWalletID_4");
        assertEquals(50l, (long) result.getCoins());
    }

    @Test(expected = DuplicateTransactionException.class)
    public void test_duplicate_debit() throws DuplicateTransactionException, InsufficientBalanceException {
        Wallet request1 = new Wallet();
        request1.setTransactionId("trx1");
        request1.setCoins(100l);
        walletService.credit(request1, "testWalletID_5");
        Wallet request2 = new Wallet();
        request2.setTransactionId("trx2");
        request2.setCoins(50l);
        walletService.debit(request2, "testWalletID_5");
        walletService.debit(request2, "testWalletID_5");
    }

    @Test(expected = InsufficientBalanceException.class)
    public void test_invalid_debit() throws DuplicateTransactionException, InsufficientBalanceException {
        Wallet request1 = new Wallet();
        request1.setTransactionId("trx1");
        request1.setCoins(100l);
        walletService.credit(request1, "testWalletID_6");
        Wallet request2 = new Wallet();
        request2.setTransactionId("trx2");
        request2.setCoins(200l);
        walletService.debit(request2, "testWalletID_6");
    }
}