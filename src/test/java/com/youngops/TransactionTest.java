package com.youngops;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.security.PrivateKey;

class TransactionTest {

    @Test
    void testSignAndVerifyTransaction() {
        Wallet senderWallet = new Wallet();

        // Correctly using sender's public key in the transaction
        Transaction tx = new Transaction("Alice", "Bob", 100, senderWallet.getPublicKey());
        PrivateKey senderPrivateKey = senderWallet.getPrivateKey();

        tx.signTransaction(senderPrivateKey);

        // Assert that the signature is valid
        assertTrue(tx.verifySignature(), "Signature should be valid.");
        assertNotNull(tx.getSignature(), "Signature should not be null.");
    }

    @Test
    void testInvalidSignature() {
        Wallet senderWallet = new Wallet();
        Wallet attackerWallet = new Wallet();

        // Correctly using sender's public key in the transaction
        Transaction tx = new Transaction("Alice", "Bob", 100, senderWallet.getPublicKey());
        PrivateKey senderPrivateKey = senderWallet.getPrivateKey();

        tx.signTransaction(senderPrivateKey);

        // Assert that the original signature is valid
        assertTrue(tx.verifySignature(), "Signature should be valid with correct keys.");

        // Attacker attempts to tamper with the signature (re-signing with a different
        // key)
        tx.signTransaction(attackerWallet.getPrivateKey());

        // After tampering, the signature should be invalid
        assertFalse(tx.verifySignature(), "Signature should be invalid after tampering.");
    }
}
