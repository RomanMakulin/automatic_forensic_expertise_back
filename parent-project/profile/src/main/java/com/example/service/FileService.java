package com.example.service;

import com.example.model.File;
import com.example.repository.FileRepository;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private final FileRepository fileRepository;


    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public File save(File file) {
        return fileRepository.save(file);
    }

}
