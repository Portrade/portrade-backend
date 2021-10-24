package com.linkerbell.portradebackend.domain.file.service;

import com.linkerbell.portradebackend.domain.file.domain.File;
import com.linkerbell.portradebackend.domain.file.domain.PortfolioContentFile;
import com.linkerbell.portradebackend.domain.file.domain.PortfolioMainImage;
import com.linkerbell.portradebackend.domain.file.domain.ProfileImage;
import com.linkerbell.portradebackend.domain.file.repository.FileRepository;
import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.dto.UploadResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.FileHandlingException;
import com.linkerbell.portradebackend.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final S3Util s3Util;

    @Transactional
    public <T extends File> File createFile(Class<T> fileClass, Object entity, MultipartFile multipartFile) {
        UploadResponseDto uploadResponseDto = s3Util.upload(multipartFile);

        switch (fileClass.getSimpleName()) {
            case "PortfolioMainImage":
                PortfolioMainImage portfolioMainImage = PortfolioMainImage.of(uploadResponseDto, (Portfolio) entity);
                fileRepository.save(portfolioMainImage);
                return portfolioMainImage;
            case "PortfolioContentFile":
                PortfolioContentFile portfolioContentFile = PortfolioContentFile.of(uploadResponseDto, (Portfolio) entity);
                fileRepository.save(portfolioContentFile);
                return portfolioContentFile;
            case "ProfileImage":
                ProfileImage profileImage = ProfileImage.of(uploadResponseDto, (User) entity);
                fileRepository.save(profileImage);
                return profileImage;
            default:
                throw new FileHandlingException(ErrorCode.FILE_UPLOAD_FAILURE);
        }
    }

/*  TODO
    @Transactional
    public void updateFile(Long fileId) {
    }

    @Transactional
    public void deleteFile(Long fileId) {
    }
*/
}
