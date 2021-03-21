package Model.Database.Support;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class UserAccessHelper {

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
        return "";
    }
}
