package com.example.minioservice.controller;


import com.example.minioservice.service.MinioServiceImpl;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final MinioServiceImpl minioServiceImpl;

    public FileController(MinioServiceImpl minioServiceImpl) {
        this.minioServiceImpl = minioServiceImpl;
    }

    /**
     * API для загрузки фото и файлов пользователя.
     */
    @PostMapping("/full-upload")
    public ResponseEntity<List<UUID>> uploadPhoto(@RequestParam("profileId") UUID profileId,
                                                  @RequestParam("avatar") MultipartFile avatar,
                                                  @RequestParam("template") MultipartFile template,
                                                  @RequestParam("files") List<MultipartFile> files
    ) {
        //
        return ResponseEntity.ok(photoUrl);
    }

    /**
     * API для удаления фото пользователя.
     */
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<String> deletePhoto(@PathVariable UUID userId) {
//        minioServiceImpl.deleteFile(userId);
//        return ResponseEntity.ok("Фото удалено");
//    }
//
//    /**
//     * Возвращает фото пользователя по его ID в формате Resource.
//     */
//    @GetMapping("/{userId}")
//    public ResponseEntity<Resource> getPhoto(@PathVariable UUID userId) {
//        try {
//            // Получаем InputStream из MinIO
//            InputStream inputStream = minioServiceImpl.getFile(userId);
//
//            // Заворачиваем InputStream в Resource
//            InputStreamResource resource = new InputStreamResource(inputStream);
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userId + ".jpg\"")
//                    .contentType(MediaType.IMAGE_JPEG)
//                    .body(resource);
//
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

}
