package _9oormthonuniv.be.domain.s3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedDirectoryUpload;
import software.amazon.awssdk.transfer.s3.model.CompletedFileUpload;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.s3.bucket}")
    private String bucket;

    private final S3Client s3Client;
    private final S3TransferManager transferManager;



    public String uploadFile(MultipartFile file) throws IOException {
        String originalName = Optional.ofNullable(file.getOriginalFilename())
                .orElse("file");

        String fileName = UUID.randomUUID() + "_" + originalName;
        try{
            // MultipartFile -> File 변환
            File tempFile = File.createTempFile("upload-", originalName);
            file.transferTo(tempFile);
            tempFile.deleteOnExit();

            // UploadFileRequest 생성
            UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
                    .putObjectRequest(b -> b.bucket(bucket).key(
                            fileName
                    ))
                    .source(tempFile.toPath())
                    .build();

            FileUpload fileUpload = transferManager.uploadFile(uploadFileRequest);
            CompletedFileUpload uploadResult = fileUpload.completionFuture().join();


            // ✅ S3 URL 생성해서 반환
            String fileUrl = s3Client.utilities().getUrl(b -> b.bucket(bucket).key(fileName)).toExternalForm();

            return fileUrl;
        }catch (Exception e){
            throw new RuntimeException("파일 업로드 실패", e);
        }

    }
}
