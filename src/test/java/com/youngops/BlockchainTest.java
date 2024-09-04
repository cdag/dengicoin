package com.youngops;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class BlockchainTest {

    @BeforeEach
    void setUp() {
        try {
            // Reset the blockchain instance before each test
            Field instanceField = Blockchain.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to reset Blockchain instance", e);
        }
    }

    @Test
    void testBlockchainInitialization() {
        Blockchain blockchain = Blockchain.getInstance();
        assertNotNull(blockchain.getChain(), "Blockchain chain should not be null.");
        assertEquals(1, blockchain.getChain().size(),
                "Blockchain should contain only the genesis block initially.");
    }

    @Test
    void testAddBlock() {
        Blockchain blockchain = Blockchain.getInstance();
        int initialSize = blockchain.getChain().size();

        List<Transaction> transactions = new ArrayList<>();
        Wallet sender = new Wallet();
        Wallet recipient = new Wallet();

        // Debugging statement to ensure keys are not null
        assertNotNull(sender.getPublicKey(), "Sender's public key should not be null.");
        assertNotNull(recipient.getPublicKey(), "Recipient's public key should not be null.");
        System.out.println("Sender Public Key: " + sender.getPublicKey());
        System.out.println("Recipient Public Key: " + recipient.getPublicKey());

        // Using sender's public key in the transaction
        Transaction tx = new Transaction("Alice", "Bob", 50, sender.getPublicKey());
        tx.signTransaction(sender.getPrivateKey());
        transactions.add(tx);

        Block newBlock = new Block(transactions,
                blockchain.getChain().get(blockchain.getChain().size() - 1).getHash());
        blockchain.addBlock(newBlock);

        assertEquals(initialSize + 1, blockchain.getChain().size(),
                "Blockchain size should increase by one.");
        assertTrue(Blockchain.isChainValid(),
                "Blockchain should be valid after adding a new block.");
    }

    @Test
    void testInvalidBlockchain() throws Exception {
        Blockchain blockchain = Blockchain.getInstance();

        // Ensure there's more than one block to tamper with
        if (blockchain.getChain().size() > 1) {
            Block tamperedBlock = blockchain.getChain().get(1);
            Transaction tamperedTransaction = tamperedBlock.getTransactions().get(0);
            Field amountField = tamperedTransaction.getClass().getDeclaredField("amount");
            amountField.setAccessible(true);
            amountField.set(tamperedTransaction, 100); // Tampering the amount

            assertFalse(Blockchain.isChainValid(), "Blockchain should be invalid after tampering.");
        }
    }
}
