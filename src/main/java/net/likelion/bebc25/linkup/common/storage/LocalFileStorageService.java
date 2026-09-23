package net.likelion.bebc25.linkup.common.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Profile("local")
@RequiredArgsConstructor
public class LocalFileStorageService implements FileStorageService {

    private final Path uploadPath = Paths.get("uploads");

    @Override
    public String upload(MultipartFile file, String directory) {
        try {
            Files.createDirectories(uploadPath);

            String originalFilename = file.getOriginalFilename();
            String storedFilename = UUID.randomUUID() + "_" + originalFilename;

            Path targetPath = uploadPath.resolve(storedFilename);

            file.transferTo(targetPath);

            return "/uploads/";

        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }
    }
}