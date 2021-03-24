package Model.Database.Support;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Contains all supporting methods related to the user accessing the system
 */
public class UserAccessHelper {

    /**
     * Hashing a password, uses PBKDF2 algorithm
     */
    public static String hashPassword(String password){
        SecureRandom random = new SecureRandom();
        random.setSeed(2021);
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            byte[] hash = factory.generateSecret(spec).getEncoded();
            String hexaString = new BigInteger(1, hash).toString(16);
            return hexaString;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Used by admin for generating verification codes for user to be able to register
     */
    public static String generateVerification(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; // Array of possible characters in the verification code
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length()))); // Randomly append one of the possible characters to the result string
        return sb.toString();
    }
}
