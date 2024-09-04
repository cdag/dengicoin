package com.youngops;

import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * The Blockchain class represents a simple blockchain implementation. It uses a singleton pattern
 * to ensure only one instance of the blockchain exists.
 */
public class Blockchain {
  private static final Logger logger = LoggerFactory.getLogger(Blockchain.class);
  public static final int DIFFICULTY = 4;

  private List<Block> chain;

  // Singleton pattern
  private static Blockchain instance = null;

  /**
   * Private constructor to initialize the blockchain with a genesis block.
   */
  private Blockchain() {
    this.chain = new ArrayList<>();
    List<Transaction> genesisTransactions = new ArrayList<>();
    genesisTransactions.add(new Transaction("Genesis", "System", 0, null));
    Block genesisBlock = new Block(0, genesisTransactions, "0"); // Pass the index as 0 for the genesis block
    genesisBlock.mineBlock(DIFFICULTY);
    this.chain.add(genesisBlock);
    logger.info("Genesis block created.");
}


  /**
   * Returns the singleton instance of the Blockchain.
   * 
   * @return the singleton instance of the Blockchain.
   */
  public static Blockchain getInstance() {
    if (instance == null) {
      instance = new Blockchain();
    }
    return instance;
  }

  /**
   * Returns the list of blocks in the blockchain.
   * 
   * @return the list of blocks in the blockchain.
   */
  public List<Block> getChain() {
    return chain;
  }

  /**
   * Sets the list of blocks in the blockchain.
   * 
   * @param chain the new list of blocks to set.
   */
  public void setChain(List<Block> chain) {
    this.chain = chain;
  }

  /**
   * Adds a new block to the blockchain after mining it.
   * 
   * @param newBlock the new block to add.
   */
  public void addBlock(Block newBlock) {
    newBlock.mineBlock(DIFFICULTY);
    chain.add(newBlock);
    logger.info("New block added to the blockchain.");
  }

  /**
   * Validates the entire blockchain by checking the hashes and signatures of all blocks and
   * transactions.
   * 
   * @return true if the blockchain is valid, false otherwise.
   */
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

  /**
   * Converts the blockchain to a JSON string representation.
   * 
   * @return the JSON string representation of the blockchain.
   */
  public String toJson() {
    return new GsonBuilder().setPrettyPrinting().create().toJson(this.chain);
  }
}
