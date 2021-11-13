package com.linkerbell.portradebackend.global.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.linkerbell.portradebackend.global.common.File;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.FileHandlingException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@NoArgsConstructor
public class S3Util {

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public File upload(MultipartFile file) {
        String fileName = extractFileName(file);
        String name = extractName(fileName);
        String extension = extractExtension(fileName);
        String newFileName = generateNewFileName(name, extension);

        try {
            s3Client.putObject(new PutObjectRequest(bucket, newFileName, file.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            String url = s3Client.getUrl(bucket, newFileName).toString();

            return File.builder()
                    .url(url)
                    .fileName(newFileName)
                    .extension(extension)
                    .build();
        } catch (IOException e) {
            throw new FileHandlingException(ErrorCode.FILE_UPLOAD_FAILURE);
        }
    }

    // TODO delete uploaded file

    /**
     * 파일명 관련
     */
    public String extractFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (!StringUtils.hasText(fileName) || !fileName.contains(".")) {
            throw new FileHandlingException(ErrorCode.INVALID_FILE_NAME);
        }
        return fileName;
    }

    public String extractName(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == 0) {
            throw new FileHandlingException(ErrorCode.INVALID_FILE_NAME);
        }
        return fileName.substring(0, lastIndexOfDot);
    }

    public String extractExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == fileName.length() - 1) {
            throw new FileHandlingException(ErrorCode.INVALID_FILE_NAME);
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public String generateNewFileName(String fileName, String extension) {
        return fileName + "_" + System.currentTimeMillis() + "." + extension;
    }
}