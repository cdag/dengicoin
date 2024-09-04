package com.youngops;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    void testKeyPairGeneration() {
        Wallet wallet = new Wallet();
        assertNotNull(wallet.getPrivateKey(), "Private key should not be null.");
        assertNotNull(wallet.getPublicKey(), "Public key should not be null.");
    }

    @Test
    void testPublicKeyString() {
        Wallet wallet = new Wallet();
        String pubKeyStr = wallet.getPublicKeyString();
        assertNotNull(pubKeyStr, "Public key string should not be null.");
        assertFalse(pubKeyStr.isEmpty(), "Public key string should not be empty.");
    }
}
