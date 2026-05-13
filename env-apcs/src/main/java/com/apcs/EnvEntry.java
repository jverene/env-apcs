package com.apcs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class EnvEntry {
    private final String key;
    private final String value;
    private final String description;
    private final Instant createdAt;

    @JsonCreator
    public EnvEntry(@JsonProperty("key") String key,
                    @JsonProperty("value") String value,
                    @JsonProperty("description") String description,
                    @JsonProperty("createdAt") Instant createdAt) {
        this.key = key;
        this.value = value;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Convenience constructor without description/createdAt (auto-set)
    public EnvEntry(String key, String value) {
        this(key, value, "", Instant.now());
    }

    public EnvEntry(String key, String value, String description) {
        this(key, value, description, Instant.now());
    }

    public String getKey() { return key; }
    public String getValue() { return value; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return String.format("%s=%s (%s) [%s]", key, value, description, createdAt);
    }
}