package com.example.minioservice.controller;


import com.example.minioservice.dto.FileDto;
import com.example.minioservice.service.MinioService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер работы с файлами пользователя через minIO
 */
@RestController
@RequestMapping("/api/files")
public class MinioController {

    /**
     * Интерфейс для работы с файлами minIO
     */
    private final MinioService minioService;

    public MinioController(MinioService minioService) {
        this.minioService = minioService;
    }

    /**
     * API для загрузки фото, шаблона и файлов пользователя.
     *
     * @param profileId ID пользователя
     * @param avatar    Аватар пользователя
     * @param template  Шаблон
     * @param files     список файлов
     * @return список сгенерированных UUID для файлов пользователя
     */
    @PostMapping("/upload-all")
    public ResponseEntity<List<FileDto>> uploadAll(@RequestParam("profileId") UUID profileId,
                                                   @RequestParam("avatar") MultipartFile avatar,
//                                                   @RequestParam("template") MultipartFile template,
                                                   @RequestParam("files") List<MultipartFile> files
    ) {
        try {
            return ResponseEntity.ok(minioService.uploadAllFiles(profileId, avatar, files));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для загрузки фото пользователя.
     *
     * @param profileId ID пользователя
     * @param avatar    Аватар пользователя
     * @return UUID файла пользователя
     */
    @PostMapping("/upload-photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("profileId") UUID profileId,
                                              @RequestParam("avatar") MultipartFile avatar) {
        try {
            minioService.uploadPhoto(profileId, avatar);
            return ResponseEntity.ok("Successfully uploaded photo");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для загрузки шаблона пользователя.
     *
     * @param profileId ID пользователя
     * @param template  Шаблон
     * @return UUID файла пользователя
     */
    @PostMapping("/upload-template")
    public ResponseEntity<String> uploadTemplate(@RequestParam("profileId") UUID profileId,
                                                 @RequestParam("template") MultipartFile template) {
        try {
            minioService.uploadTemplate(profileId, template);
            return ResponseEntity.ok("Successfully uploaded template");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для загрузки файла пользователя.
     *
     * @param profileId ID пользователя
     * @param file      Файл
     * @return UUID файла пользователя
     */
    @PostMapping("/upload-file")
    public ResponseEntity<FileDto> uploadFile(@RequestParam("profileId") UUID profileId,
                                              @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(minioService.uploadFile(profileId, file));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для загрузки нескольких файлов пользователя.
     *
     * @param profileId ID пользователя
     * @param files     список файлов
     * @return список сгенерированных UUID для файлов пользователя
     */
    @PostMapping("/upload-files")
    public ResponseEntity<List<FileDto>> uploadFiles(@RequestParam("profileId") UUID profileId,
                                                     @RequestParam("files") List<MultipartFile> files) {
        try {
            return ResponseEntity.ok(minioService.uploadFiles(profileId, files));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для удаления фото пользователя.
     *
     * @param profileId ID пользователя
     * @return статус операции
     */
    @PostMapping("/delete-photo")
    public ResponseEntity<String> deletePhoto(@RequestParam("profileId") UUID profileId) {
        try {
            minioService.deletePhoto(profileId);
            return ResponseEntity.ok("Successfully deleted photo");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для удаления шаблона пользователя.
     *
     * @param profileId ID пользователя
     * @return статус операции
     */
    @PostMapping("/delete-template")
    public ResponseEntity<String> deleteTemplate(@RequestParam("profileId") UUID profileId) {
        try {
            minioService.deleteTemplate(profileId);
            return ResponseEntity.ok("Successfully deleted template");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для удаления файла пользователя.
     *
     * @param path путь к файлу
     * @return статус операции
     */
    @PostMapping("/delete-file")
    public ResponseEntity<String> deleteFile(@RequestParam("path") String path) {
        try {
            minioService.deleteFile(path);
            return ResponseEntity.ok("Successfully deleted file");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для удаления нескольких файлов пользователя.
     *
     * @param pathList список путей к файлам
     * @return статус операции
     */
    @PostMapping("/delete-file-list")
    public ResponseEntity<String> deleteFileList(@RequestBody List<String> pathList) {
        try {
            minioService.deleteFiles(pathList);
            return ResponseEntity.ok("Successfully deleted files");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для получения фото пользователя.
     *
     * @param profileId ID пользователя
     * @return фото пользователя
     */
    @GetMapping("/get-photo")
    public ResponseEntity<String> getPhoto(@RequestParam("profileId") UUID profileId) {
        try {
            return ResponseEntity.ok(minioService.getPhoto(profileId));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для получения шаблона пользователя.
     *
     * @param profileId ID пользователя
     * @return шаблон пользователя
     */
    @GetMapping("/get-template")
    public ResponseEntity<String> getTemplate(@RequestParam("profileId") UUID profileId) {
        try {
            return ResponseEntity.ok(minioService.getTemplate(profileId));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для получения файла пользователя.
     *
     * @param profileId ID пользователя
     * @return файла пользователя
     */
    @GetMapping("/get-files")
    public ResponseEntity<List<String>> getFile(@RequestParam("profileId") UUID profileId) {
        try {
            return ResponseEntity.ok(minioService.getFiles(profileId));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для получения файла пользователя.
     * @param path путь к файлу
     * @return файла пользователя
     */
    @GetMapping("/get-file")
    public ResponseEntity<String> getFile(@RequestParam("path") String path) {
        try {
            return ResponseEntity.ok(minioService.getFile(path));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
