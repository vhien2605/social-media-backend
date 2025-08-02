package com.hien.back_end_app.services;


import com.cloudinary.Cloudinary;
import com.hien.back_end_app.dto.request.SocketMessageMediaDTO;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final AsyncFileService asyncFileService;
    private final Cloudinary cloudinary;

    public String uploadFile(byte[] fileData, String extension, String type, String folderName) {
        String cloudName = cloudinary.config.cloudName;
        String uuid = UUID.randomUUID().toString();
        String subPath = "";
        if (type.contains("image")) {
            subPath = "image";
        } else if (type.contains("video")) {
            subPath = "video";
        } else if (type.contains("audio")) {
            subPath = "audio";
        }
        // build url storage for access
        String url = String.format(
                "https://res.cloudinary.com/%s/%s/upload/%s/%s.%s",
                cloudName, subPath, folderName, uuid, extension
        );
        // handle upload to cloud in sub thread
        try {
            asyncFileService.handleUploadFile(fileData, folderName, uuid, cloudinary);
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
        return url;
    }


    public void deleteFileByUrl(String imageUrl) throws IOException {
        // extract file id from url
        String publicId = extractPublicIdFromUrl(imageUrl);
        if (publicId == null) {
            return;
        }
        // delete by id
        asyncFileService.handleDeleteFile(publicId, cloudinary);
    }


    public MultipartFile convertToMultipartFile(String name, String type, String base64Data) {
        byte[] fileBytes = Base64.getDecoder().decode(base64Data);
        return new MockMultipartFile(
                name,
                name,
                type,
                fileBytes
        );
    }

    public String getFileExtension(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            return "jpg";
        }
        return originalName.substring(originalName.lastIndexOf('.') + 1);
    }


    private String extractPublicIdFromUrl(String imageUrl) {
        try {
            String baseUrl = "https://res.cloudinary.com/";
            if (!imageUrl.startsWith(baseUrl)) return null;
            // Cắt từ phần sau 'upload/' tới trước đuôi .jpg/.png...
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2) return null;
            String path = parts[1];
            int dotIndex = path.lastIndexOf('.');
            if (dotIndex != -1) {
                path = path.substring(0, dotIndex);
            }
            return path;
        } catch (Exception e) {
            return null;
        }
    }
}
