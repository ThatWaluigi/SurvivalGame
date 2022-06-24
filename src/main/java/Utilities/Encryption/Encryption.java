package Utilities.Encryption;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class Encryption {
    public static SecretKey getKey(String salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
        SecretKeyFactory Factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec Spec = new PBEKeySpec(salt.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(Factory.generateSecret(Spec).getEncoded(), "AES");
    }

    public static Key ReturnKey(String salt){
        try {
            return getKey(salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String Encrypt(String text, Key key){
        try {
            return Seed128Cipher.encrypt(text, key.getEncoded(), null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String Decrypt(String text, Key key){
        try {
            return Seed128Cipher.decrypt(text, key.getEncoded(), null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}