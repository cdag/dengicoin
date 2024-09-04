package com.youngops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;

/**
 * The DengiCoinChain class represents the main entry point for the DengiCoin blockchain
 * application. It initializes wallets, creates and signs transactions, groups transactions into
 * blocks, adds blocks to the blockchain, and validates the blockchain.
 */
public class DengiCoinChain {
  private static final Logger logger = LoggerFactory.getLogger(DengiCoinChain.class);

  /**
   * The main method is the entry point of the application. It performs the following steps: 1.
   * Initializes wallets. 2. Creates transactions. 3. Signs transactions. 4. Groups transactions
   * into lists. 5. Adds blocks to the blockchain. 6. Validates the blockchain. 7. Prints the
   * blockchain.
   *
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    Security.addProvider(new BouncyCastleProvider());
    try {
      Wallet wallet1 = new Wallet();
      Wallet wallet2 = new Wallet();
      Wallet wallet3 = new Wallet();
      Wallet wallet4 = new Wallet();
      logger.info("Initialized wallets.");
      Transaction transaction1 = new Transaction("Alice", "Bob", 50, wallet1.getPublicKey());
      Transaction transaction2 = new Transaction("Bob", "Charlie", 30, wallet2.getPublicKey());
      Transaction transaction3 = new Transaction("Charlie", "Dave", 20, wallet3.getPublicKey());
      Transaction transaction4 = new Transaction("Dave", "Alice", 10, wallet4.getPublicKey());
      logger.info("Created transactions.");
      transaction1.signTransaction(wallet1.getPrivateKey());
      transaction2.signTransaction(wallet2.getPrivateKey());
      transaction3.signTransaction(wallet3.getPrivateKey());
      transaction4.signTransaction(wallet4.getPrivateKey());
      logger.info("Signed transactions.");
      List<Transaction> transactions1 = new ArrayList<>();
      transactions1.add(transaction1);
      List<Transaction> transactions2 = new ArrayList<>();
      transactions2.add(transaction2);
      List<Transaction> transactions3 = new ArrayList<>();
      transactions3.add(transaction3);
      List<Transaction> transactions4 = new ArrayList<>();
      transactions4.add(transaction4);
      logger.info("Grouped transactions into lists.");
      Blockchain blockchainInstance = Blockchain.getInstance();
      Block block1 = new Block(transactions1,
          blockchainInstance.getChain().get(blockchainInstance.getChain().size() - 1).getHash());
      blockchainInstance.addBlock(block1);
      logger.info("Added Block 1.");
      Block block2 = new Block(transactions2,
          blockchainInstance.getChain().get(blockchainInstance.getChain().size() - 1).getHash());
      blockchainInstance.addBlock(block2);
      logger.info("Added Block 2.");
      Block block3 = new Block(transactions3,
          blockchainInstance.getChain().get(blockchainInstance.getChain().size() - 1).getHash());
      blockchainInstance.addBlock(block3);
      logger.info("Added Block 3.");
      Block block4 = new Block(transactions4,
          blockchainInstance.getChain().get(blockchainInstance.getChain().size() - 1).getHash());
      blockchainInstance.addBlock(block4);
      logger.info("Added Block 4.");
      boolean isValid = Blockchain.isChainValid();
      logger.info("Blockchain is Valid: {}", isValid);
      System.out.println("\nBlockchain is Valid: " + isValid);
      String blockchainJson = blockchainInstance.toJson();
      System.out.println("\nThe block chain: ");
      System.out.println(blockchainJson);
      logger.info("Printed blockchain JSON.");

    } catch (Exception e) {
      logger.error("An error occurred in DengiCoinChain: {}", e.getMessage());
      e.printStackTrace();
    }
  }
}
