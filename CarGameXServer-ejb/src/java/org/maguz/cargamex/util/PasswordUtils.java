package org.maguz.cargamex.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
public class PasswordUtils {
    // The higher the number of iterations the more 
    // expensive computing the hash is for us
    // and also for a brute force attack.
    private static final int iterations = 10*1024;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;
    private static final Logger logger = Logger.getLogger("PasswordUtils");
    /** Computes a salted PBKDF2 hash of given plaintext password
        suitable for storing in a database. 
        Empty passwords are not supported.
     * @param password plaintext password
     * @return salted has of password.
     */
    public static String getSaltedHash(String password) {
        logger.log(Level.INFO, "getSaltedHash enter");
        byte[] salt;
        try {
            salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        try {
            // store the salt with the password
            String result = DatatypeConverter.printBase64Binary(salt) + "$" + hash(password, salt);
            logger.log(Level.INFO, "getSaltedHash: Return {0}", result);
            return result;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }
        logger.log(Level.INFO, "getSaltedHash: Return null!");
        return null;
    }

    /** Checks whether given plaintext password corresponds 
        to a stored salted hash of the password. */
    public static boolean check(String password, String stored) throws Exception{
        String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2)
            return false;
        String hashOfInput = hash(password, DatatypeConverter.parseBase64Binary(saltAndPass[0]));
        return hashOfInput.equals(saltAndPass[1]);
    }

    // using PBKDF2 from Sun
    private static String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
            password.toCharArray(), salt, iterations, desiredKeyLen)
        );
        return DatatypeConverter.printBase64Binary(key.getEncoded());
    }
}
