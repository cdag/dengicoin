package com.youngops;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a transaction in a blockchain system.
 */
public class Transaction {

    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

    /**
     * The algorithm used for signing transactions.
     */
    public static final String SIGNING_ALGORITHM = "Ed25519";

    private final String sender;
    private final String recipient;
    private final int amount;
    private final String senderPublicKey;
    private String signature;

    /**
     * Constructs a new Transaction.
     *
     * @param sender the sender's identifier
     * @param recipient the recipient's identifier
     * @param amount the amount to be transferred
     * @param senderPublicKey the sender's public key
     * @throws IllegalArgumentException if senderPublicKey is null for
     * non-genesis transactions
     */
    public Transaction(String sender, String recipient, int amount, PublicKey senderPublicKey) {
        if (sender.equals("Genesis") && recipient.equals("System")) {
            this.sender = sender;
            this.recipient = recipient;
            this.amount = amount;
            this.senderPublicKey = null;
            return;
        }

        if (senderPublicKey == null) {
            throw new IllegalArgumentException("PublicKey cannot be null for transaction creation");
        }
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.senderPublicKey = Base64.getEncoder().encodeToString(senderPublicKey.getEncoded());
    }

    /**
     * Signs the transaction using the provided private key.
     *
     * @param privateKey the private key used to sign the transaction
     * @throws RuntimeException if signing fails
     */
    public void signTransaction(PrivateKey privateKey) {
        String data = sender + recipient + amount;
        try {
            Signature sig = Signature.getInstance(SIGNING_ALGORITHM, "BC");
            sig.initSign(privateKey);
            sig.update(data.getBytes());
            byte[] signatureBytes = sig.sign();
            this.signature = Base64.getEncoder().encodeToString(signatureBytes);
            logger.info("Transaction signed successfully.");
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException
                | SignatureException e) {
            throw new RuntimeException(
                    "Failed to sign transaction for sender " + sender + " to recipient " + recipient, e);
        }
    }

    /**
     * Verifies the signature of the transaction.
     *
     * @return true if the signature is valid, false otherwise
     * @throws RuntimeException if verification fails
     */
    public boolean verifySignature() {
        String data = sender + recipient + amount;
        try {
            Signature sig = Signature.getInstance(SIGNING_ALGORITHM, "BC");
            byte[] publicKeyBytes = Base64.getDecoder().decode(senderPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(SIGNING_ALGORITHM, "BC");
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            sig.initVerify(pubKey);
            sig.update(data.getBytes());
            boolean isValid = sig.verify(Base64.getDecoder().decode(signature));
            if (isValid) {
                logger.info("Signature verified successfully.");
            } else {
                logger.warn("Signature verification failed.");
            }
            return isValid;
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException
                | SignatureException | InvalidKeySpecException e) {
            throw new RuntimeException(
                    "Failed to verify signature for transaction from " + sender + " to " + recipient, e);
        }
    }

    /**
     * Gets the sender's identifier.
     *
     * @return the sender's identifier
     */
    public String getSender() {
        return sender;
    }

    /**
     * Gets the recipient's identifier.
     *
     * @return the recipient's identifier
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Gets the amount to be transferred.
     *
     * @return the amount to be transferred
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the sender's public key.
     *
     * @return the sender's public key
     */
    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    /**
     * Gets the transaction's signature.
     *
     * @return the transaction's signature
     */
    public String getSignature() {
        return signature;
    }
}
