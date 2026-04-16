package com.example.neet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.neet.Service.NeetPdfService;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "http://localhost:5173")
public class PdfUploadController {

    @Autowired
    private NeetPdfService service;

    @PostMapping("/upload")
    public String upload(
            @RequestParam MultipartFile file,
            @RequestParam int year,
            @RequestParam String subject) {

        service.processPdf(file, year, subject);
        return "PDF uploaded successfully";
    }
}
