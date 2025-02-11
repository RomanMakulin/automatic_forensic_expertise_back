package com.example.minioservice.api.rest;

import com.example.minioservice.api.dto.FileDto;
import com.example.minioservice.service.FileUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер работы с файлами пользователя через minIO - загрузка файлов
 */
@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    /**
     * API для загрузки фото, шаблона и файлов пользователя.
     *
     * @param profileId ID пользователя
     * @param avatar    Аватар пользователя
     * @param passport  паспорт
     * @param diplom    диплом
//     * @param template  Шаблон
     * @param files     список файлов
     * @return список сгенерированных UUID для файлов пользователя
     */
    @PostMapping("/upload-all")
    public ResponseEntity<List<FileDto>> uploadAll(@RequestParam("profileId") UUID profileId,
                                                   @RequestParam("avatar") MultipartFile avatar,
                                                   @RequestParam("passport") MultipartFile passport,
                                                   @RequestParam("diplom") MultipartFile diplom,
//                                                   @RequestParam("template") MultipartFile template,
                                                   @RequestParam("files") List<MultipartFile> files
    ) {
        try {
            return ResponseEntity.ok(fileUploadService.uploadAllFiles(profileId, avatar, passport, diplom, files));
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
            fileUploadService.uploadPhoto(profileId, avatar);
            return ResponseEntity.ok("Successfully uploaded photo");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для загрузки паспорта пользователя.
     *
     * @param profileId ID пользователя
     * @param passport  Паспорт
     * @return UUID файла пользователя
     */
    @PostMapping("/upload-passport")
    public ResponseEntity<String> uploadPassport(@RequestParam("profileId") UUID profileId,
                                                 @RequestParam("passport") MultipartFile passport) {
        try {
            fileUploadService.uploadPassport(profileId, passport);
            return ResponseEntity.ok("Successfully uploaded passport");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для загрузки диплома пользователя.
     *
     * @param profileId ID пользователя
     * @param diplom    Диплом
     * @return UUID файла пользователя
     */
    @PostMapping("/upload-diplom")
    public ResponseEntity<String> uploadDiplom(@RequestParam("profileId") UUID profileId,
                                               @RequestParam("diplom") MultipartFile diplom) {
        try {
            fileUploadService.uploadDiplom(profileId, diplom);
            return ResponseEntity.ok("Successfully uploaded diplom");
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
            fileUploadService.uploadTemplate(profileId, template);
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
            return ResponseEntity.ok(fileUploadService.uploadFile(profileId, file));
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
            return ResponseEntity.ok(fileUploadService.uploadFiles(profileId, files));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
