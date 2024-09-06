package com.youngops;

import java.security.MessageDigest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.youngops.exception.HashCalculationRuntimeException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

/**
 * Represents a block in the blockchain.
 */
public class Block {
  private static final Logger logger = LoggerFactory.getLogger(Block.class);

  private int index;
  private long timestamp;
  private List<Transaction> transactions;
  private String previousHash;
  private String hash;
  private static final SecureRandom secureRandom = new SecureRandom();
  private long nonce;

  /**
   * Constructs a new Block.
   *
   * @param transactions the list of transactions
   * @param previousHash the hash of the previous block
   */
  public Block(int index, List<Transaction> transactions, String previousHash) {
    this.index = index;
    this.timestamp = System.currentTimeMillis();
    this.transactions = transactions;
    this.previousHash = previousHash;
    this.nonce = generateSecureNonce();
    this.hash = calculateHash();
  }

  /**
   * Generates a secure nonce using a cryptographically strong random number generator. The nonce is
   * an 8-byte long value that is wrapped in a ByteBuffer and converted to a long. The absolute
   * value of the generated long is returned to ensure a positive nonce.
   *
   * @return A positive long value representing the secure nonce.
   */
  private long generateSecureNonce() {
    byte[] nonceBytes = new byte[8];
    secureRandom.nextBytes(nonceBytes);
    return Math.abs(ByteBuffer.wrap(nonceBytes).getLong());
  }

  /**
   * Calculates the hash of the block.
   *
   * @return the calculated hash
   */
  public String calculateHash() {
    try {
      String dataToHash = previousHash + timestamp + transactions.toString() + nonce;
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(dataToHash.getBytes());
      StringBuilder sb = new StringBuilder();
      for (byte b : hashBytes) {
        sb.append(String.format("%02x", b));
      }
      return sb.toString();
    } catch (Exception e) {
      String errorMessage = String.format("Failed to calculate hash for block %d with previous hash %s, timestamp %d, and nonce %d", index, previousHash, timestamp, nonce);
      logger.error(errorMessage, e);
      throw new HashCalculationRuntimeException(errorMessage, e);
    }
  }

  /**
   * Mines the block with the given difficulty.
   *
   * @param difficulty the difficulty level
   */
  public void mineBlock(int difficulty) {
    String target = new String(new char[difficulty]).replace('\0', '0');
    logger.info("Mining block with difficulty: {}", difficulty);
    while (!hash.substring(0, difficulty).equals(target)) {
      if (nonce == Long.MAX_VALUE) {
        // Reset nonce
        nonce = generateSecureNonce();
      } else {
        nonce++;
      }
      hash = calculateHash();
    }
    logger.info("Block mined: {}", hash);
  }

  // Getters

  /**
   * Getter method so the nonce is available outside of this class.
   * 
   * @return
   */
  public long getNonce() {
    return nonce;
  }

  /**
   * Returns the index of the block.
   *
   * @return the index of the block
   */
  public int getIndex() {
    return index;
  }

  /**
   * Returns the timestamp of the block.
   *
   * @return
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Returns the list of transactions in the block.
   *
   * @return
   */
  public List<Transaction> getTransactions() {
    return transactions;
  }

  /**
   * Returns the hash of the previous block.
   *
   * @return
   */
  public String getPreviousHash() {
    return previousHash;
  }

  /**
   * Returns the hash of the current block.
   *
   * @return the hash of the current block
   */
  public String getHash() {
    return hash;
  }

  /**
   * Returns a string representation of the Block object. The string includes the block index,
   * timestamp, previous hash, current hash, nonce, and all transactions.
   *
   * @return A string representation of the Block object.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Block ").append(index).append(" [\n");
    sb.append("  Timestamp: ").append(timestamp).append("\n");
    sb.append("  Previous Hash: ").append(previousHash).append("\n");
    sb.append("  Hash: ").append(hash).append("\n");
    sb.append("  Nonce: ").append(nonce).append("\n");
    sb.append("  Transactions: \n");
    for (Transaction tx : transactions) {
      sb.append("    ").append(tx.getSender()).append(" -> ").append(tx.getRecipient()).append(": ")
          .append(tx.getAmount()).append("\n");
    }
    sb.append("]");
    return sb.toString();
  }

}
