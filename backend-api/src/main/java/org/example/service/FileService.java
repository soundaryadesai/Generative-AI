package org.example.service;



import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.Paths;



import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;



import org.example.exception.FileUploadException;



@Service
public class FileService {







    public void save(MultipartFile file,String uploadPath) throws FileUploadException {

        try {

            Path root = Paths.get(uploadPath);

            Path resolve = root.resolve(file.getOriginalFilename());

            if (resolve.toFile()

                    .exists()) {

                throw new FileUploadException("File already exists: " + file.getOriginalFilename());

            }

            Files.copy(file.getInputStream(), resolve);

        } catch (Exception e) {

            throw new FileUploadException("Could not store the file. Error: " + e.getMessage());

        }

    }





}