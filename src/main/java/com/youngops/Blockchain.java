package com.youngops;

import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private static final Logger logger = LoggerFactory.getLogger(Blockchain.class);
    public static final int DIFFICULTY = 4;

    private List<Block> chain;

    // Singleton pattern
    private static Blockchain instance = null;

    private Blockchain() {
        this.chain = new ArrayList<>();
        // Add genesis block
        List<Transaction> genesisTransactions = new ArrayList<>();
        genesisTransactions.add(new Transaction("Genesis", "System", 0, null));
        Block genesisBlock = new Block(genesisTransactions, "0");
        genesisBlock.mineBlock(DIFFICULTY);
        this.chain.add(genesisBlock);
        logger.info("Genesis block created.");
    }

    public static Blockchain getInstance() {
        if (instance == null) {
            instance = new Blockchain();
        }
        return instance;
    }

    public List<Block> getChain() {
        return chain;
    }

    public void setChain(List<Block> chain) {
        this.chain = chain;
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(DIFFICULTY);
        chain.add(newBlock);
        logger.info("New block added to the blockchain.");
    }

    public static boolean isChainValid() {
        Blockchain blockchain = getInstance();
        List<Block> chain = blockchain.getChain();

        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                logger.warn("Current block hash is invalid.");
                return false;
            }

            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                logger.warn("Previous block hash does not match.");
                return false;
            }

            // Verify all transactions
            for (Transaction tx : currentBlock.getTransactions()) {
                if (!tx.verifySignature()) {
                    logger.warn("Invalid transaction signature detected.");
                    return false;
                }
            }
        }

        logger.info("Blockchain is valid.");
        return true;
    }

    public String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this.chain);
    }
}
