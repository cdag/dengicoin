package com.youngops;

import java.security.MessageDigest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private int nonce;

    /**
     * Constructs a new Block.
     *
     * @param transactions the list of transactions
     * @param previousHash the hash of the previous block
     */
    public Block(List<Transaction> transactions, String previousHash) {
        this.index = 0; // Set if needed
        this.timestamp = System.currentTimeMillis();
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.hash = calculateHash();
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
            logger.error("Error calculating hash: {}", e.getMessage());
            throw new RuntimeException("Failed to calculate hash.", e);
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
            nonce++;
            hash = calculateHash();
        }
        logger.info("Block mined: {}", hash);
    }

    // Getters and Setters
    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public int getNonce() {
        return nonce;
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
            sb.append("    ").append(tx.getSender()).append(" -> ").append(tx.getRecipient())
                    .append(": ").append(tx.getAmount()).append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

}
