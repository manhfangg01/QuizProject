package com.quiz.learning.Demo.service.azure;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.UUID; // Để tạo tên file duy nhất

@Service
public class AzureBlobService {

    private final BlobServiceClient blobServiceClient;
    private final String containerName;

    public AzureBlobService(BlobServiceClient blobServiceClient,
            @Value("${AZURE_STORAGE_QUIZ_BLOB_CONNECTION_NAME}") String containerName) {
        this.blobServiceClient = blobServiceClient;
        this.containerName = containerName;
    }

    private BlobContainerClient getBlobContainerClient() {
        return blobServiceClient.getBlobContainerClient(containerName);
    }

    /**
     * Uploads a file to Azure Blob Storage.
     * 
     * @param file The MultipartFile to upload.
     * @return The URL of the uploaded blob.
     * @throws IOException If an I/O error occurs.
     */
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        BlobContainerClient containerClient = getBlobContainerClient();
        // Kiểm tra xem container có tồn tại không, nếu không thì tạo mới (chỉ chạy lần
        // đầu)
        if (!containerClient.exists()) {
            containerClient.create();
        }

        // Tạo tên file duy nhất để tránh trùng lặp
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            fileExtension = originalFilename.substring(dotIndex);
        }
        String fileName = UUID.randomUUID().toString() + fileExtension;

        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(file.getInputStream(), file.getSize(), true); // true để ghi đè nếu tồn tại

        return blobClient.getBlobUrl(); // Trả về URL của blob
    }

    /**
     * Downloads a file from Azure Blob Storage.
     * 
     * @param fileName The name of the blob to download.
     * @return The byte array of the file.
     * @throws IOException If an I/O error occurs.
     */
    public byte[] downloadFile(String fileName) throws IOException {
        BlobClient blobClient = getBlobContainerClient().getBlobClient(fileName);
        if (!blobClient.exists()) {
            return null; // Hoặc ném ngoại lệ
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.downloadStream(outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Deletes a file from Azure Blob Storage.
     * 
     * @param fileName The name of the blob to delete.
     * @return True if deletion was successful, false otherwise.
     */
    public boolean deleteFile(String fileName) {
        BlobClient blobClient = getBlobContainerClient().getBlobClient(fileName);
        return blobClient.deleteIfExists(); // Xóa nếu tồn tại
    }

    /**
     * Extracts blob name from full blob URL.
     * 
     * @param blobUrl The full URL of the blob.
     * @return The name of the blob.
     */
    public String getBlobNameFromUrl(String blobUrl) {
        if (blobUrl == null || blobUrl.isEmpty()) {
            return null;
        }
        // Example URL:
        // https://mylibraryblobs.blob.core.windows.net/librarian-photos/some-uuid.jpg
        int lastSlash = blobUrl.lastIndexOf('/');
        if (lastSlash != -1 && lastSlash < blobUrl.length() - 1) {
            return blobUrl.substring(lastSlash + 1);
        }
        return null;
    }
}