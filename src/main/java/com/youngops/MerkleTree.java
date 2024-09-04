package com.youngops;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {
    private List<String> transactions;

    public MerkleTree(List<String> transactions) {
        this.transactions = transactions;
    }

    public String getMerkleRoot() {
        return constructMerkleTree(this.transactions);
    }

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
