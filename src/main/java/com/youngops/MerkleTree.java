package com.youngops;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Merkle Tree, which is a binary tree used to efficiently and securely verify the
 * integrity of data.
 */
public class MerkleTree {
    private List<String> transactions;

    /**
     * Constructs a MerkleTree with the given list of transactions.
     *
     * @param transactions the list of transactions to be included in the Merkle Tree
     */
    public MerkleTree(List<String> transactions) {
        this.transactions = transactions;
    }

    /**
     * Returns the Merkle Root of the transactions.
     *
     * @return the Merkle Root as a hexadecimal string
     */
    public String getMerkleRoot() {
        return constructMerkleTree(this.transactions);
    }

    /**
     * Recursively constructs the Merkle Tree and returns the Merkle Root.
     *
     * @param transactions the list of transactions to construct the Merkle Tree from
     * @return the Merkle Root as a hexadecimal string
     */
    private String constructMerkleTree(List<String> transactions) {
        if (transactions.size() == 1) {
            return transactions.get(0);
        }

        List<String> updatedList = new ArrayList<>();
        for (int i = 0; i < transactions.size(); i += 2) {
            String left = transactions.get(i);
            String right = (i + 1 < transactions.size()) ? transactions.get(i + 1) : left;
            updatedList.add(applySha256(left + right));
        }

        return constructMerkleTree(updatedList);
    }

    /**
     * Applies the SHA-256 hash function to the given input string.
     *
     * @param input the input string to be hashed
     * @return the SHA-256 hash as a hexadecimal string
     */
    private String applySha256(String input) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
