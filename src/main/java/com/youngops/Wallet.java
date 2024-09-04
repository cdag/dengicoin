package com.youngops;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.*;
import java.util.Base64;

/**
 * The Wallet class represents a digital wallet that holds a private and public key pair. It
 * provides functionality for managing and accessing these keys.
 */
public class Wallet {
    private static final Logger logger = LoggerFactory.getLogger(Wallet.class);

    private PrivateKey privateKey;
    private PublicKey publicKey;

    /**
     * Constructor for Wallet. Initializes the BouncyCastle security provider if not already present
     * and generates a key pair.
     */
    public Wallet() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        generateKeyPair();
        System.out.println(
                "Wallet created with PublicKey: " + (publicKey != null ? publicKey : "NULL"));
    }

    /**
     * Generates a new key pair using the Ed25519 algorithm and BouncyCastle provider. Sets the
     * privateKey and publicKey fields with the generated key pair.
     */
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

    /**
     * Returns the public key of the wallet. Logs an error if the public key is null.
     *
     * @return the public key
     */
    public PublicKey getPublicKey() {
        if (publicKey == null) {
            logger.error("PublicKey is null in Wallet");
        }
        return publicKey;
    }

    /**
     * Returns the private key of the wallet.
     *
     * @return the private key
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * Returns the public key as a Base64 encoded string. Logs an error and returns
     * "NULL_PUBLIC_KEY" if the public key is null.
     *
     * @return the public key as a Base64 encoded string
     */
    public String getPublicKeyString() {
        if (publicKey == null) {
            logger.error("Attempted to get PublicKeyString but publicKey is null");
            return "NULL_PUBLIC_KEY";
        }
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
}
