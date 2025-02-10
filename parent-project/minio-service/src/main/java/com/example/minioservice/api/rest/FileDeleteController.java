package com.example.minioservice.api.rest;

import com.example.minioservice.service.FileDeleteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер работы с файлами пользователя через minIO - удаление файлов
 */
@RestController
@RequestMapping("/api/files")
public class FileDeleteController {

    private final FileDeleteService fileDeleteService;

    public FileDeleteController(FileDeleteService fileDeleteService) {
        this.fileDeleteService = fileDeleteService;
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
            fileDeleteService.deletePhoto(profileId);
            return ResponseEntity.ok("Successfully deleted photo");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для удаления файла пользователя.
     *
     * @param profileId ID пользователя
     * @return статус операции
     */
    @PostMapping("/delete-diplom")
    public ResponseEntity<String> deleteDiplom(@RequestParam("profileId") UUID profileId) {
        try {
            fileDeleteService.deleteDiplom(profileId);
            return ResponseEntity.ok("Successfully deleted diplom");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для удаления паспорта пользователя.
     *
     * @param profileId ID пользователя
     * @return статус операции
     */
    @PostMapping("/delete-passport")
    public ResponseEntity<String> deletePassport(@RequestParam("profileId") UUID profileId) {
        try {
            fileDeleteService.deletePassport(profileId);
            return ResponseEntity.ok("Successfully deleted passport");
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
            fileDeleteService.deleteTemplate(profileId);
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
            fileDeleteService.deleteFile(path);
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
            fileDeleteService.deleteFiles(pathList);
            return ResponseEntity.ok("Successfully deleted files");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
