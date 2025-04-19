package com.excel.excel_handling.controller;
import com.excel.excel_handling.model.ApiResponse;
import com.excel.excel_handling.service.ExerciseService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/excel")
public class ExcelHandlingController {
    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        exerciseService.exportToExcel(response);
    }

    @PostMapping("/import")
    public ApiResponse<String> importFromExcel(@RequestParam("file") MultipartFile file) {
        String message = exerciseService.importExcel(file);

        return new ApiResponse<>(200, "Import thành công", message, null);
    }
}
