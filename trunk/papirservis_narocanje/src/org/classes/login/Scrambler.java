package org.classes.login;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Scrambler {
	private static Log log = LogFactory.getLog(Scrambler.class); 
	
	static Cipher ecipher;
    static Cipher dcipher;

    // 8-byte Salt
    /*static byte[] salt = {
        (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
        (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
    };*/
    static byte[] salt = {
        (byte)0xA9, (byte)0x9B, (byte)0xC9, (byte)0x32,
        (byte)0x56, (byte)0x33, (byte)0xE4, (byte)0xDD
    };

    // Iteration count
    static int iterationCount = 12;
    static String passphrase = "EfsaRules";
    
    static {
        try {
            // Create the key
            KeySpec keySpec = new PBEKeySpec(passphrase.toCharArray());
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            // Create the ciphers
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (java.security.InvalidAlgorithmParameterException e) {
        } catch (java.security.spec.InvalidKeySpecException e) {
        } catch (javax.crypto.NoSuchPaddingException e) {
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (java.security.InvalidKeyException e) {
        }
    }

    public static void main(String[] args) {

        // Here is an example that uses the class
         try {
        	if (args.length == 1) {
        		log.debug("kriptirano geslo je:"+encrypt(args[0]));
        	} else {
        
            // Encrypt
            String encrypted = encrypt("177|bpovhfdfdfd");
        
            log.debug("enkriptirano geslo: "+encrypted);
            log.debug(encrypt("178|bostjanpovh"));
            log.debug(encrypt("179|bpovh"));
            /*encrypted = encrypt(encrypted.substring(encrypted.length()-10, encrypted.length()));
            // Decrypt
            log.debug(encrypted);*/
            String d = decrypt("bjFFSN9ycy7X/p49Yt67CQ==");
            log.debug(d);

           /* encrypted = encrypt("eplacilassl");
            encrypted = encrypt(encrypted.substring(encrypted.length()-10, encrypted.length()));
            
            
            log.debug(encrypted);
            // Decrypt
            decrypted = decrypt(encrypted);
            log.debug(decrypted);*/
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
    

    
    public static String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return new sun.misc.BASE64Encoder().encode(enc);
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    public static String decrypt(String str) {
        try {
            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
            // Decrypt
            //byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(dec, "UTF8");
/*        } catch (javax.crypto.BadPaddingException e) {
        	e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
        	e.printStackTrace();*/
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }
    

    public static String encryptUrl(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            String encd = new sun.misc.BASE64Encoder().encode(enc).replaceAll("/", "_").replaceAll("\\+", "\\*").replaceAll("=", "-")+"u";
            return encd;
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
    
    public static String decryptUrl(String str) {
        try {
            // Decode base64 to get bytes
        	str=str.substring(0,str.length()-1); // zbrišem zadnjo črko
        	String decd = str.replaceAll("_", "/").replaceAll("\\*", "\\+").replaceAll("-", "=");
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(decd);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }

}
