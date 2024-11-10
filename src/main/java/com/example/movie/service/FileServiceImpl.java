package com.example.movie.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        // get file name and file path
        String fileName = file.getOriginalFilename();
        String filePath = path + File.pathSeparator + fileName;

        // get the directory(e.g: ../../posters/)
        File f = new File(path);
        if (!f.exists()){
              f.mkdir();
        }

        // copy the file into the directory
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        // get file path
        String filePath = path + File.pathSeparator + fileName;

        // load the file
        File f = new File(filePath);
        if (!f.exists()){
            throw new FileNotFoundException(filePath);
        }

        return new FileInputStream(f);
    }
}
