package com.linkerbell.portradebackend.global.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.linkerbell.portradebackend.global.common.dto.UploadResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.FileUploadException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;

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

    public UploadResponseDto upload(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String[] fileNames = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String newFileName = fileNames[0] + "_" + System.currentTimeMillis() + "." + fileNames[1];

        try {
            s3Client.putObject(new PutObjectRequest(bucket, newFileName, file.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            String url = s3Client.getUrl(bucket, newFileName).toString();

            return UploadResponseDto.builder()
                    .originalFileName(originalFileName)
                    .newFileName(newFileName)
                    .url(url)
                    .build();
        }catch (IOException e) {
            throw new FileUploadException(ErrorCode.FAILURE_FILE_UPLOAD);
        }
    }
}