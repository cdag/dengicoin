package com.youngops;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.security.PrivateKey;

/**
 * Test class for Transaction.
 */
class TransactionTest {

  /**
   * Tests the signing and verification of a transaction.
   */
  @Test
  void testSignAndVerifyTransaction() {
    Wallet senderWallet = new Wallet();
    Transaction tx = new Transaction("Alice", "Bob", 100, senderWallet.getPublicKey());
    PrivateKey senderPrivateKey = senderWallet.getPrivateKey();
    tx.signTransaction(senderPrivateKey);
    assertTrue(tx.verifySignature(), "Signature should be valid.");
    assertNotNull(tx.getSignature(), "Signature should not be null.");
  }

  /**
   * Tests the verification of a transaction with an invalid signature.
   */
  @Test
  void testInvalidSignature() {
    Wallet senderWallet = new Wallet();
    Wallet attackerWallet = new Wallet();
    Transaction tx = new Transaction("Alice", "Bob", 100, senderWallet.getPublicKey());
    PrivateKey senderPrivateKey = senderWallet.getPrivateKey();
    tx.signTransaction(senderPrivateKey);
    assertTrue(tx.verifySignature(), "Signature should be valid with correct keys.");
    tx.signTransaction(attackerWallet.getPrivateKey());
    assertFalse(tx.verifySignature(), "Signature should be invalid after tampering.");
  }
}
