package com.excel.excel_handling.service;

import com.excel.excel_handling.dto.ExerciseDTO;
import com.excel.excel_handling.model.Exercise;
import com.excel.excel_handling.model.ExerciseModel;
import com.excel.excel_handling.repository.ExerciseRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseService {
    @Autowired
    private ExerciseRepository exerciseRepository;

    public List<ExerciseDTO> getAllExercises() {
        List<Exercise> exercises = exerciseRepository.findAll();

        return exercises.stream().map(exercise ->
                new ExerciseModel(exercise).toDto())
                .collect(Collectors.toList());
    }

    public void exportToExcel(HttpServletResponse response) throws IOException {
        // return application/octet-stream type
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=exercise-list.xlsx";
        response.setHeader(headerKey, headerValue);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Exercise");

        //Header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        headerRow.createCell(1).setCellValue("Image");
        headerRow.createCell(2).setCellValue("Affected Muscle Groups");
        headerRow.createCell(3).setCellValue("Steps");
        headerRow.createCell(4).setCellValue("Actions");

        //Add data
        List<Exercise> exercises = exerciseRepository.findAll();
        int rowNum = 1;
        for (Exercise exercise: exercises) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(exercise.getName());
            row.createCell(1).setCellValue(exercise.getImage_url());
            row.createCell(2).setCellValue(exercise.getAffected_muscle_groups());
            row.createCell(3).setCellValue(exercise.getSteps());
            row.createCell(4).setCellValue(exercise.getAction());
        }

        //Write to the response output stream
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                return cell.getCellFormula(); // hoặc evaluate nếu cần kết quả

            case BLANK:
            case _NONE:
            case ERROR:
            default:
                return "";
        }
    }

    public String importExcel(MultipartFile file) {
        try(InputStream is = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(is)
        ) {
            XSSFSheet sheet = workbook.getSheetAt(1);
            Iterator<Row> rows = sheet.iterator();
            rows.next(); //Skip header row

            while (rows.hasNext()) {
                Row row = rows.next();
                String name = getCellValueAsString(row.getCell(0));
                String image_url = getCellValueAsString(row.getCell(1));
                String affected_muscle_groups = getCellValueAsString(row.getCell(2));
                String steps = getCellValueAsString(row.getCell(3));
                String actions = getCellValueAsString(row.getCell(4));

                Exercise exercise = new Exercise();
                exercise.setName(name);
                exercise.setImage_url(image_url);
                exercise.setAffected_muscle_groups(affected_muscle_groups);
                exercise.setSteps(steps);
                exercise.setAction(actions);

                exerciseRepository.save(exercise);
            }

            return "Import successful";
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}
