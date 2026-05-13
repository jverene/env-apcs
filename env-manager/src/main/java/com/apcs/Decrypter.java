package com.apcs;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Decrypter {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final String PBKDF2_ALGO = "PBKDF2WithHmacSHA256";
    private static final int KEY_LEN = 256;
    private static final int ITERATIONS = 100_000;
    private static final int TAG_LEN = 128;

    public String decrypt(String encryptedData, String password) throws Exception {
        // Split the combined string
        String[] parts = encryptedData.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid encrypted data format");
        }
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] iv = Base64.getDecoder().decode(parts[1]);
        byte[] ciphertext = Base64.getDecoder().decode(parts[2]);

        SecretKey key = deriveKey(password, salt);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LEN, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] plaintext = cipher.doFinal(ciphertext);
        return new String(plaintext, java.nio.charset.StandardCharsets.UTF_8);
    }

    private SecretKey deriveKey(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LEN);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGO);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }
}