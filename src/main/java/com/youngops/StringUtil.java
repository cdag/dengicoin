package com.youngops;

import java.security.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class StringUtil {
    private static final String ALGORITHM = "Ed25519";

    // Private constructor to prevent instantiation
    private StringUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Applies Sha256 to a string and returns the result.
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Applies sha256 to our input,
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(); // This will contain hash as hexadecimal
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Sign data using Ed25519
    public static String signData(String data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance(ALGORITHM);
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Verify a signature using Ed25519
    public static boolean verifySignature(String data, String signatureStr, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance(ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
            return signature.verify(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
