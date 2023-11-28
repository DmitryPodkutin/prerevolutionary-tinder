package ru.liga.integration.imagegenerator;

import org.springframework.http.ResponseEntity;

public interface ImageGeneratingService {
    ResponseEntity<byte[]> fetchImageFromRemoteService(String resource);
}
