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
        return null;
    }

    public static String generateVerification(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static Integer getAccessPrivilegeType(String privilege){
        int privilegeType = 0;
        switch(privilege) {
            case "admin":
                privilegeType = 1;
                break;
            case "user":
                privilegeType = 2;
                break;
        }
        return privilegeType;
    }
}
