package dev.hamasakis.etl.services;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {
    private final S3Template s3Template;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket:etl-raw-data}")
    private String bucketName;
    
    // Função para upload de arquivos para o S3, utilizando o S3Template do Spring Cloud AWS, que gerencia o stream dos dados, evitando problemas de memória.
    public String uploadFile(MultipartFile file) {

        // Verificando se existe ou não o arquivo ou se o nome for vazio.
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("File is empty or has no name");
        }

        // Pegar o nome original do arquivo
        String originalFilename = file.getOriginalFilename();

        // Gerando um nome unico para o arquivo, para evitar reescrever o mesmo arquivo... 
        String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;

        log.info("Starting upload for file: {} to bucket: {}", uniqueFilename, bucketName);

        try {
            // Chamando o upload do S3 (Gerencia sozinho o stream dos dodos) - Muito daora isso aki.
            s3Template.upload(bucketName, uniqueFilename, file.getInputStream());

            // Criando URL para arquivo na S3
            String uri = "s3://" + bucketName + "/" + uniqueFilename;

            log.info("File successfully uploaded to Storage: {}", uri);
            return uri;
        } catch (IOException e) {
            log.error("Failed do read InputStream from file: {}", originalFilename, e);
            throw new RuntimeException("Failed to upload file: " + originalFilename, e);
        }
    }        
    
    public List<String> listFiles() {
       log.info("Listing all files from bucket: {}", bucketName); 
       
       try {
           ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                   .bucket(bucketName)
                   .build();

           ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);
           
           List<String> fileUris = listObjectsV2Response.contents().stream()
                   .map(S3Object::key)
                   .map(key -> "s3://" + bucketName + "/" + key)
                   .toList();
           log.info("Successfully listed {} files from bucket: {}", fileUris.size(), bucketName);
           
           return fileUris;
       } catch (Exception e) {
           log.error("Failed to list files from bucket: {}", bucketName, e);
           throw new RuntimeException("Failed to list files from bucket: " + bucketName, e);
       }
    }
    
    public InputStream getFile(String fileName) {
        log.info("Retrieving file stream for: {} from bucket: {}", fileName, bucketName);
        
        try {
            // S3template busca o arquivo e retorna como o Resource do Spring
            Resource resource = s3Template.download(bucketName, fileName);
            
            return resource.getInputStream();
        } catch (Exception e) {
            log.error("Failed to retrieve file stream for: {} from bucket: {}", fileName, bucketName, e);
            throw new RuntimeException("Failed to retrieve file stream for: " + fileName, e);
        }
    }
}
