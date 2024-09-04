package com.youngops;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.*;
import java.util.Base64;

public class Wallet {
    private static final Logger logger = LoggerFactory.getLogger(Wallet.class);

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        generateKeyPair();
        System.out.println(
                "Wallet created with PublicKey: " + (publicKey != null ? publicKey : "NULL"));
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("Ed25519", "BC");
            KeyPair keyPair = keyGen.generateKeyPair();
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
            logger.info("Generated new key pair.");
            if (logger.isInfoEnabled()) {
                logger.info("Public Key: {}",
                        Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to generate key pair.", e);
        }
    }

    public PublicKey getPublicKey() {
        if (publicKey == null) {
            logger.error("PublicKey is null in Wallet");
        }
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public String getPublicKeyString() {
        if (publicKey == null) {
            logger.error("Attempted to get PublicKeyString but publicKey is null");
            return "NULL_PUBLIC_KEY";
        }
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
}
