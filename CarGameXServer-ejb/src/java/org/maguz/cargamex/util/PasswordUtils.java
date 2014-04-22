package org.maguz.cargamex.util;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * Password handling functions.
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
public class PasswordUtils {
    // The higher the number of iterations the more 
    // expensive computing the hash is for us
    // and also for a brute force attack.
    private static final int iterations = 10*1024;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;
    private static final Logger logger = Logger.getLogger(PasswordUtils.class.getSimpleName());
    /** Computes a salted PBKDF2 hash of given plaintext password
        suitable for storing in a database. 
        Empty passwords are not supported.
     * @param password plaintext password
     * @return salted has of password.
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.NoSuchProviderException
     */
    public static String getSaltedHash(String password) throws NoSuchAlgorithmException, NoSuchProviderException {
        logger.log(Level.INFO, "getSaltedHash enter");
        byte[] salt;
        try {
            SecureRandom sr = SecureRandom.getInstance("NativePRNG", "SUN");
            salt = sr.generateSeed(saltLen);
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, null, ex);
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
        to a stored salted hash of the password.
     * @param password
     * @param stored
     * @return 
     * @throws java.lang.Exception */
    public static boolean check(String password, String stored) throws Exception{
        logger.log(Level.INFO, "check() enter");
        String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2)
            return false;
        String hashOfInput = hash(password, DatatypeConverter.parseBase64Binary(saltAndPass[0]));
        logger.log(Level.INFO, "check() exit");
        return hashOfInput.equals(saltAndPass[1]);
    }

    // using PBKDF2 from Sun
    private static String hash(String password, byte[] salt) throws Exception {
        logger.log(Level.INFO, "Enter hash()");
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, desiredKeyLen);
        SecretKey key = f.generateSecret(spec);
        String ret = DatatypeConverter.printBase64Binary(key.getEncoded());
        logger.log(Level.INFO, "Exit hash(): {0}", ret);
        return ret;
    }
}
