package com.youngops;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Wallet class.
 */
class WalletTest {

    /**
     * Tests the key pair generation in the Wallet class. Ensures that both
     * private and public keys are generated and are not null.
     */
    @Test
    void testKeyPairGeneration() {
        Wallet wallet = new Wallet();
        assertNotNull(wallet.getPrivateKey(), "Private key should not be null.");
        assertNotNull(wallet.getPublicKey(), "Public key should not be null.");
    }

    /**
     * Tests the public key string generation in the Wallet class. Ensures that
     * the public key string is not null and not empty.
     */
    @Test
    void testPublicKeyString() {
        Wallet wallet = new Wallet();
        String pubKeyStr = wallet.getPublicKeyString();
        assertNotNull(pubKeyStr, "Public key string should not be null.");
        assertFalse(pubKeyStr.isEmpty(), "Public key string should not be empty.");
    }
}
