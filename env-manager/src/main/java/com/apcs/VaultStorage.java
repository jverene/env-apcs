package com.apcs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class VaultStorage {
    private final ObjectMapper objectMapper;

    public VaultStorage() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public Map<String, EnvEntry> load(Path vaultFile, String password) throws Exception {
        if (!Files.exists(vaultFile)) {
            return new HashMap<>();   // new vault
        }
        String encryptedJson = Files.readString(vaultFile);
        Decrypter decrypter = new Decrypter();
        String json = decrypter.decrypt(encryptedJson, password);
        // Deserialize JSON to Map<String, EnvEntry>
        return objectMapper.readValue(json, new TypeReference<>() {});
    }

    public void save(Path vaultFile, String password, Map<String, EnvEntry> entries) throws Exception {
        String json = objectMapper.writeValueAsString(entries);
        Encrypter encrypter = new Encrypter();
        String encryptedJson = encrypter.encrypt(json, password);
        // Ensure parent directory exists
        Files.createDirectories(vaultFile.getParent());
        Files.writeString(vaultFile, encryptedJson);
    }
}