package seedu.address.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.StringUtil;

/**
 * A class containing utility methods to encrypt and decrypt files for Storage
 */
public class SecurityUtil {
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    private static SecretKey secretKey;
    private static final String AES_KEY_FILEPATH = "data/aeskey.key";

    /**
     * Encrypts file using {@code secretKey}
     * @param file the file given to encrypt
     * @throws IOException if an input or output exception occurred
     */
    public static void encrypt(File file) throws IOException {
        loadKey();
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            processFile(cipher, file);
        } catch (GeneralSecurityException e) {
            logger.severe("Failed to encrypt message " + StringUtil.getDetails(e));
            System.exit(1);
        }
    }

    /**
     * Decrypts file using {@code secretKey} located at {@code AES_KEY_FILEPATH}
     *
     * @param file the file given to decrypt
     * @throws IOException if an input or output exception occurred
     */
    public static void decrypt(File file) throws IOException {
        loadKey();
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            processFile(cipher, file);
        } catch (GeneralSecurityException e) {
            logger.severe("Failed to decrypt message " + StringUtil.getDetails(e));
            System.exit(1);
        }
    }

    /**
     * Encrypts or decrypts the given file using the given cipher
     * @param cipher mode determines whether to encrypt or decrypt
     * @param file the file given to encrypt or decrypt
     */
    private static void processFile(Cipher cipher, File file) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] inputBytes = new byte[(int) file.length()];
            fis.read(inputBytes);

            FileOutputStream fos = new FileOutputStream(file);
            byte[] outputBytes = cipher.doFinal(inputBytes);
            fos.write(outputBytes);

            fis.close();
            fos.close();

        } catch (IllegalBlockSizeException e) {
            logger.warning("File data is not a multiple of the block-size");
            encrypt(file);
        } catch (BadPaddingException e) {
            logger.warning("Key or File data is invalid " + StringUtil.getDetails(e));
        }
    }

    /**
     * Reads the key from the path given by {@code AES_KEY_FILEPATH}
     */
    private static void loadKey() {
        try {
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(AES_KEY_FILEPATH));
            secretKey = (SecretKey) ois.readObject();
        } catch (IOException oie) {
            logger.severe("Failed to read key from file");
            createKey();
        } catch (ClassNotFoundException cnfe) {
            logger.severe("Failed to typecast to class SecretKey");
            createKey();
        }
        logger.fine("Key successfully read from file");
    }

    /**
     * Initializes and creates a key
     */
    private static void createKey() {
        initKey();
        saveKey();
    }

    /**
     * Creates a new key with the filepath {@code AES_KEY_FILEPATH}
     */
    private static void initKey() {
        KeyGenerator keyGen = null;

        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            logger.severe("Failed to generate AES key");
            System.exit(1);
        }

        keyGen.init(128);
        secretKey = keyGen.generateKey();
        logger.fine("Key generated");
    }

    /**
     * Saves the key at the filepath {@code AES_KEY_FILEPATH}
     */
    private static void saveKey() {
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(new FileOutputStream(AES_KEY_FILEPATH));
            oos.writeObject(secretKey);
            oos.close();
        } catch (IOException e) {
            logger.severe("Failed to save key to file");
            System.exit(1);
        }
        logger.fine("Key saved to file");
    }
}
