package com.youngops;

import java.security.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class StringUtil {
  private static final String ALGORITHM = "Ed25519";

  /**
   * Private constructor to prevent instantiation. Throws UnsupportedOperationException if called.
   */
  private StringUtil() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Applies SHA-256 hashing algorithm to the input string and returns the result as a hexadecimal
   * string.
   *
   * @param input the input string to be hashed
   * @return the SHA-256 hash of the input string in hexadecimal format
   */
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

  /**
   * Signs the input data using the Ed25519 algorithm and the provided private key.
   *
   * @param data the data to be signed
   * @param privateKey the private key used for signing
   * @return the Base64 encoded signature of the data
   */
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

  /**
   * Verifies the signature of the input data using the Ed25519 algorithm and the provided public
   * key.
   *
   * @param data the data whose signature is to be verified
   * @param signatureStr the Base64 encoded signature to be verified
   * @param publicKey the public key used for verification
   * @return true if the signature is valid, false otherwise
   */
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
