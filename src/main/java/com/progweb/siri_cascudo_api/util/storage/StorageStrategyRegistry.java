package com.progweb.siri_cascudo_api.util.storage;

import com.progweb.siri_cascudo_api.exception.CustomException;
import com.progweb.siri_cascudo_api.util.storage.services.LocalStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class StorageStrategyRegistry {

    @Value("${app.storage-engine}")
    private String storageEngine;

    private final Map<String, Storage> strategies = new HashMap<>();

    @Autowired
    private LocalStorageService localStorage;

    @PostConstruct
    public void init() {
        strategies.put("local", localStorage);
        // Adicione outras estratégias aqui
    }

    public Storage getSaveStrategy() {
        if (!isValidStorage(storageEngine)) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Storage Engine selected is not valid.", "Verify your environment settings or add a new storage engine.");
        }

        return strategies.get(storageEngine);
    }

    private boolean isValidStorage(String environment) {
        System.out.println("Storage engine running: " + environment);
        return strategies.containsKey(environment);
    }
}
