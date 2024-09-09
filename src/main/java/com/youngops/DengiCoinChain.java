package com.youngops;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DengiCoinChain {

    private static final Logger logger = LoggerFactory.getLogger(DengiCoinChain.class);

    /**
     * The main method to initialize and run the DengiCoinChain application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        try {
            List<Wallet> wallets = initializeWallets();
            List<Transaction> transactions = createTransactions(wallets);
            signTransactions(transactions, wallets);
            List<List<Transaction>> groupedTransactions = groupTransactions(transactions);
            Blockchain blockchainInstance = Blockchain.getInstance();
            addBlocksToBlockchain(blockchainInstance, groupedTransactions);
            validateAndPrintBlockchain(blockchainInstance);
        } catch (Exception e) {
            logger.error("An error occurred in DengiCoinChain: {}", e.getMessage(), e);
        }
    }

    /**
     * Initializes a list of wallets.
     *
     * This method creates a list of Wallet objects, adds four new Wallet
     * instances to the list, logs the initialization process, and then returns
     * the list of wallets.
     *
     * @return a list containing four initialized Wallet objects
     */
    private static List<Wallet> initializeWallets() {
        List<Wallet> wallets = new ArrayList<>();

        wallets.add(new Wallet());
        wallets.add(new Wallet());
        wallets.add(new Wallet());
        wallets.add(new Wallet());

        logger.info("Initialized wallets.");

        return wallets;
    }

    /**
     * Creates a list of transactions between predefined parties using the
     * public keys from the provided wallets.
     *
     * @param wallets the list of wallets providing the public keys for the
     * transactions
     * @return a list of transactions created between predefined parties
     */
    private static List<Transaction> createTransactions(List<Wallet> wallets) {

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction("Alice", "Bob", 50, wallets.get(0).getPublicKey()));
        transactions.add(new Transaction("Bob", "Charlie", 30, wallets.get(1).getPublicKey()));
        transactions.add(new Transaction("Charlie", "Dave", 20, wallets.get(2).getPublicKey()));
        transactions.add(new Transaction("Dave", "Alice", 10, wallets.get(3).getPublicKey()));
        logger.info("Created transactions.");

        return transactions;
    }

    /**
     * Signs the first four transactions in the provided list using the private
     * keys from the corresponding wallets.
     *
     * @param transactions the list of transactions to be signed
     * @param wallets the list of wallets providing the private keys for signing
     * the transactions
     */
    private static void signTransactions(List<Transaction> transactions, List<Wallet> wallets) {

        transactions.get(0).signTransaction(wallets.get(0).getPrivateKey());
        transactions.get(1).signTransaction(wallets.get(1).getPrivateKey());
        transactions.get(2).signTransaction(wallets.get(2).getPrivateKey());
        transactions.get(3).signTransaction(wallets.get(3).getPrivateKey());

        logger.info("Signed transactions.");
    }

    /**
     * Groups each transaction into its own list and returns a list of these
     * lists.
     *
     * @param transactions the list of transactions to be grouped
     * @return a list of lists, where each inner list contains a single
     * transaction
     */
    private static List<List<Transaction>> groupTransactions(List<Transaction> transactions) {
        List<List<Transaction>> groupedTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            List<Transaction> transactionList = new ArrayList<>();
            transactionList.add(transaction);
            groupedTransactions.add(transactionList);
        }

        logger.info("Grouped transactions into lists.");
        return groupedTransactions;
    }

    /**
     * Adds a list of grouped transactions as blocks to the blockchain.
     *
     * @param blockchainInstance the instance of the Blockchain to which the
     * blocks will be added
     * @param groupedTransactions a list of lists, where each inner list
     * contains transactions to be added as a block
     */
    private static void addBlocksToBlockchain(Blockchain blockchainInstance,
            List<List<Transaction>> groupedTransactions) {
        int previousIndex
                = blockchainInstance.getChain().get(blockchainInstance.getChain().size() - 1).getIndex();
        for (List<Transaction> transactionList : groupedTransactions) {
            Block block = new Block(previousIndex + 1, transactionList,
                    blockchainInstance.getChain().get(blockchainInstance.getChain().size() - 1).getHash());
            blockchainInstance.addBlock(block);
            logger.info("Added Block {}.", block.getIndex());
            previousIndex = block.getIndex();
        }
    }

    /**
     * Validates the blockchain and prints its JSON representation.
     *
     * @param blockchainInstance the instance of the Blockchain to be validated
     * and printed
     */
    private static void validateAndPrintBlockchain(Blockchain blockchainInstance) {
        boolean isValid = Blockchain.isChainValid();
        logger.info("Blockchain is Valid: {}", isValid);
        System.out.println("\nBlockchain is Valid: " + isValid);
        String blockchainJson = blockchainInstance.toJson();
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
        logger.info("Printed blockchain JSON.");
    }

}
