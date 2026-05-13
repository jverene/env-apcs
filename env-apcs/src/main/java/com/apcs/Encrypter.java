package com.apcs;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class Encrypter {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final String PBKDF2_ALGO = "PBKDF2WithHmacSHA256";
    private static final int KEY_LEN = 256;
    private static final int ITERATIONS = 100_000;
    private static final int SALT_LEN = 16;      // bytes
    private static final int IV_LEN = 12;         // GCM recommended
    private static final int TAG_LEN = 128;       // bits

    public String encrypt(String plaintext, String password) throws Exception {
        // Generate random salt and IV
        byte[] salt = new byte[SALT_LEN];
        byte[] iv = new byte[IV_LEN];
        new SecureRandom().nextBytes(salt);
        new SecureRandom().nextBytes(iv);

        // Derive key from password and salt
        SecretKey key = deriveKey(password, salt);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LEN, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        // Encode and combine salt:iv:ciphertext
        String saltB64 = Base64.getEncoder().encodeToString(salt);
        String ivB64 = Base64.getEncoder().encodeToString(iv);
        String ctB64 = Base64.getEncoder().encodeToString(ciphertext);
        return saltB64 + ":" + ivB64 + ":" + ctB64;
    }

    private SecretKey deriveKey(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LEN);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGO);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }
}