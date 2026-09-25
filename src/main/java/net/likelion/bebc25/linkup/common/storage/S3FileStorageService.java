package net.likelion.bebc25.linkup.common.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@Profile("prod")
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile file, String directory) {
        String key = directory + UUID.randomUUID();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        try {
            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );

            return key;
        } catch (IOException e) {
            throw new IllegalStateException("S3 파일 업로드에 실패했습니다.", e);
        }
    }

    @Override
    public void delete(String storedFileReference) {
        if (storedFileReference == null || storedFileReference.isBlank()) {
            throw new IllegalArgumentException("삭제할 파일 key가 필요합니다.");
        }

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(storedFileReference)
                .build();

        s3Client.deleteObject(request);
    }
}
