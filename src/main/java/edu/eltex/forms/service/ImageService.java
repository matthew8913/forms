package edu.eltex.forms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${image.storage.path}")
    private String imageStoragePath;

    public String saveImage(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (!isValidImageMimeType(file)) {
            throw new IllegalArgumentException("Invalid type of file: " + filename);
        }

        Path path = Paths.get(imageStoragePath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String uniqueFileName = UUID.randomUUID() + "_" + filename;
        Path filePath = path.resolve(uniqueFileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return uniqueFileName;
    }

    @Cacheable("images")
    public Resource getImage(String fileName) throws IOException {
        System.out.println("Fetching image from disk: " + fileName);

        Path path = Paths.get(imageStoragePath, fileName);
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + fileName);
        }

        return new UrlResource(path.toUri());
    }

    private boolean isValidImageMimeType(MultipartFile file) {
        String mimeType = file.getContentType();
        return mimeType.matches("image/(jpeg|png|gif)");
    }
}
