package edu.eltex.forms.controller;

import edu.eltex.forms.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = imageService.saveImage(file);
            return ResponseEntity.ok("/api/images/" + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File could not be uploaded: " + e.getMessage());
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> getImage(@PathVariable String fileName) {
        try {
            Resource image = imageService.getImage(fileName);
            String contentType = Files.probeContentType(image.getFile().toPath());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000");

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(contentType))
                    .headers(headers)
                    .body(image);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
