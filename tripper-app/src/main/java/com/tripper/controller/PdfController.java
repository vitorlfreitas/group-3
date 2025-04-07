package com.tripper.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@RequestMapping("/nlp/trip-pdf")
public class PdfController {

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadPdf(@RequestParam String file) {
        try {
            File pdfFile = new File(file);

            if (!pdfFile.exists()) {
                return ResponseEntity.notFound().build();
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(pdfFile));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (FileNotFoundException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
