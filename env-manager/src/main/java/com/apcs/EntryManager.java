package com.apcs;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class EntryManager {
    private final VaultStorage storage;
    private final Path vaultPath;
    private String password;          // kept in memory while manager is open
    private Map<String, EnvEntry> entries;

    public EntryManager(Path vaultPath) {
        this.vaultPath = vaultPath;
        this.storage = new VaultStorage();
        this.entries = new HashMap<>();
    }

    // Unlock / load vault with password
    public void unlock(String password) throws Exception {
        this.password = password;
        this.entries = storage.load(vaultPath, password);
    }

    // Save current state back to encrypted file
    public void lockAndSave() throws Exception {
        if (password == null) throw new IllegalStateException("Vault not unlocked");
        storage.save(vaultPath, password, entries);
        password = null;   // clear password from memory
        entries.clear();
    }

    // Add or update an entry
    public void put(String key, String value, String description) {
        checkUnlocked();
        entries.put(key, new EnvEntry(key, value, description));
    }

    public void put(String key, String value) {
        put(key, value, "");
    }

    public EnvEntry get(String key) {
        checkUnlocked();
        return entries.get(key);
    }

    public void delete(String key) {
        checkUnlocked();
        entries.remove(key);
    }

    public Map<String, EnvEntry> listAll() {
        checkUnlocked();
        return new HashMap<>(entries);
    }

    public boolean containsKey(String key) {
        checkUnlocked();
        return entries.containsKey(key);
    }

    private void checkUnlocked() {
        if (password == null || entries == null) {
            throw new IllegalStateException("Vault not unlocked. Call unlock(password) first.");
        }
    }
}