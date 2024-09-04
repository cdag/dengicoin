package com.youngops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class Transaction {
    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

    public static final String SIGNING_ALGORITHM = "Ed25519";

    private String sender;
    private String recipient;
    private int amount;
    private String senderPublicKey;
    private String signature;

    public Transaction(String sender, String recipient, int amount, PublicKey senderPublicKey) {
        if (sender.equals("Genesis") && recipient.equals("System")) {
            this.sender = sender;
            this.recipient = recipient;
            this.amount = amount;
            this.senderPublicKey = null; // Or some other way to indicate it's a genesis transaction
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
            throw new RuntimeException("Failed to sign transaction for sender " + sender
                    + " to recipient " + recipient, e);
        }
    }

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
            throw new RuntimeException("Failed to verify signature for transaction from " + sender
                    + " to " + recipient, e);
        }
    }

    // Getters for accessing the transaction details
    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public int getAmount() {
        return amount;
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public String getSignature() {
        return signature;
    }

}
