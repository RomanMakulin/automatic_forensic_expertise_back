package com.example.minioservice.api.rest;


import com.example.minioservice.service.FileGetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер работы с файлами пользователя через minIO - получение файлов
 */
@RestController
@RequestMapping("/api/files")
public class FileGetController {

    private final FileGetService fileGetService;

    public FileGetController(FileGetService fileGetService) {
        this.fileGetService = fileGetService;
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
            return ResponseEntity.ok(fileGetService.getPhoto(profileId));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для получения диплома пользователя.
     *
     * @param profileId ID пользователя
     * @return диплом пользователя
     */
    @GetMapping("/get-diplom")
    public ResponseEntity<String> getDiplom(@RequestParam("profileId") UUID profileId) {
        try {
            return ResponseEntity.ok(fileGetService.getDiplom(profileId));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для получения паспорта пользователя.
     *
     * @param profileId ID пользователя
     * @return паспорт пользователя
     */
    @GetMapping("/get-passport")
    public ResponseEntity<String> getPassport(@RequestParam("profileId") UUID profileId) {
        try {
            return ResponseEntity.ok(fileGetService.getPassport(profileId));
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
            return ResponseEntity.ok(fileGetService.getTemplate(profileId));
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
            return ResponseEntity.ok(fileGetService.getFiles(profileId));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API для получения файла пользователя.
     *
     * @param path путь к файлу
     * @return файла пользователя
     */
    @GetMapping("/get-file")
    public ResponseEntity<String> getFile(@RequestParam("path") String path) {
        try {
            return ResponseEntity.ok(fileGetService.getFile(path));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
