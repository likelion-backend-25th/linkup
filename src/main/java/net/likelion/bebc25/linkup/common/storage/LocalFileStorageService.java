package net.likelion.bebc25.linkup.common.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Profile("local")
public class LocalFileStorageService implements FileStorageService {

    private final Path uploadPath =
            Paths.get("uploads").toAbsolutePath().normalize();

    @Override
    public String upload(MultipartFile file, String directory) {
        try {
            Path directoryPath = uploadPath
                    .resolve(directory)
                    .normalize();

            Files.createDirectories(directoryPath);

            String extension = StringUtils.getFilenameExtension(
                    file.getOriginalFilename()
            );

            String storedFilename = UUID.randomUUID()
                    + (extension == null ? "" : "." + extension);

            Path targetPath = directoryPath.resolve(storedFilename);

            file.transferTo(targetPath);

            return "/uploads/"
                    + directory
                    + "/"
                    + storedFilename;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "파일 저장에 실패했습니다.", e
            );
        }
    }
}