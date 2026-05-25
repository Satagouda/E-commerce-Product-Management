package com.ecommerce.config;


import com.ecommerce.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchInitializer implements CommandLineRunner {

    private final SearchService searchService;

    @Override
    public void run(String... args) {

        log.info("Initializing Meilisearch");

        searchService.configureSearchSettings();

        searchService.configureSynonyms();

        log.info("Meilisearch initialized successfully");
    }
}
