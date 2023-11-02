package org.example.controller;





import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;

import org.springframework.http.HttpStatusCode;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;



import org.example.model.UploadResponseMessage;

import org.example.service.FileService;



//import com.example.model.UploadResponseMessage;

//import com.example.service.*;



@RestController

@RequestMapping("/api")

public class FilesController {





    @Value("${upload.path.jd}")

    private String jdUploadPath;





    @Value("${upload.path.cv}")

    private String cvUploadPath;



    @Autowired
    private FileService fileService;



    @CrossOrigin(origins = "http://localhost:4200")

    @PostMapping("/Files/JobDescription")

    public ResponseEntity<UploadResponseMessage> uploadFileJD(@RequestParam("file") MultipartFile file) {

        fileService.save(file,jdUploadPath);



        return ResponseEntity.status(HttpStatus.OK)

                .body(new UploadResponseMessage("Uploaded the file successfully: " + file.getOriginalFilename()));

    }



    @CrossOrigin(origins = "http://localhost:4200")

    @PostMapping("/Files/Resume")

    public ResponseEntity<UploadResponseMessage> uploadFileCV(@RequestParam("file") List<MultipartFile> file) {

        //System.out.println(file.);



        System.out.println(file);

        for(MultipartFile fileRecived:file) {

            fileService.save(fileRecived,cvUploadPath);



        }



        return ResponseEntity.status(HttpStatus.OK)

                .body(new UploadResponseMessage("Uploaded the file successfully: " ));

    }



}