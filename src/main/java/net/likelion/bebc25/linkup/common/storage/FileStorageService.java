package net.likelion.bebc25.linkup.common.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    // 파일, 이미지 업로드
    String upload(MultipartFile file, String directory);

    // 파일, 이미지 삭제
    void delete(String storedFileReference);
}
