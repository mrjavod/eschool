package com.home.eschool.services;

import com.home.eschool.entity.AppSettings;
import com.home.eschool.entity.Teachers;
import com.home.eschool.entity.enums.SetsEnum;
import com.home.eschool.models.dto.ExportDto;
import com.home.eschool.models.payload.ExportPayload;
import com.home.eschool.models.payload.StudyYearsPayload;
import com.home.eschool.repository.AppSettingsRepo;
import com.home.eschool.utils.Utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppSettingsService {

    private final AppSettingsRepo appSettingsRepo;
    private final TeachersService teachersService;

    public AppSettingsService(AppSettingsRepo appSettingsRepo,
                              TeachersService teachersService) {
        this.appSettingsRepo = appSettingsRepo;
        this.teachersService = teachersService;
    }

    private List<StudyYearsPayload> getStudyYears() {
        List<StudyYearsPayload> list = new ArrayList<>();
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2022-2023"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2023-2024"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2024-2025"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2025-2026"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2026-2027"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2027-2028"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2028-2029"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2029-2030"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2030-2031"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2031-2032"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2032-2033"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2033-2034"));
        list.add(new StudyYearsPayload(UUID.randomUUID(), "2034-2035"));
        return list;
    }

    public StudyYearsPayload getStudyYearsByName(String name) {
        return getStudyYears().stream().filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public StudyYearsPayload getStudyYearsById(UUID id) {
        Optional<AppSettings> optional = appSettingsRepo.findByKey(id);
        if (optional.isPresent()) {
            AppSettings settings = optional.get();
            return new StudyYearsPayload(settings.getKey(), settings.getValue());
        }
        return null;
    }

    public UUID getKeyByLabel(SetsEnum label) {
        return appSettingsRepo.findByLabel(label)
                .map(AppSettings::getKey).orElse(null);
    }

    public void createDefaultSettings() {

        AppSettings appSettings = new AppSettings();
        appSettings.setId(UUID.randomUUID());
        appSettings.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        appSettings.setLabel(SetsEnum.STUDY_YEAR);
        appSettings.setName("O'quv yili");

        StudyYearsPayload studyYearsPayload = getStudyYearsByName("2022-2023");
        if (studyYearsPayload != null) {
            appSettings.setKey(studyYearsPayload.getId());
            appSettings.setValue(studyYearsPayload.getName());
        }

        appSettingsRepo.save(appSettings);
    }

    public ExportPayload export(ExportDto dto) {

        try {
            String object = dto.getObject();
            String fileName = String.format("%s.xlsx", object);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(new ClassPathResource(String.format("templates/%s", fileName))
                    .getInputStream());
            CellStyle style = getStyle(workbook);

            switch (dto.getObject()) {
                case "teachers": {

                    fillTeachersRow(workbook, style, teachersService.getAllTeachers());
                    break;
                }
            }

            workbook.write(outputStream);
            return new ExportPayload(fileName, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ExportPayload("error.xlsx", null);
    }

    private CellStyle getStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private void setCellValue(XSSFRow row,
                              CellStyle style,
                              int cellPosition,
                              String value) {
        XSSFCell cell = row.createCell(cellPosition);
        cell.setCellStyle(style);
        cell.setCellType(CellType.STRING);
        cell.setCellValue(!Utils.isEmpty(value) ? value : "");
    }

    private void fillTeachersRow(XSSFWorkbook workbook,
                                 CellStyle style,
                                 List<Teachers> allTeachers) {

        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowNumber = 0;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (Teachers teacher : allTeachers) {

            XSSFRow row = sheet.createRow(++rowNumber);

            setCellValue(row, style, 0, teacher.getLastName());
            setCellValue(row, style, 1, teacher.getFirstName());
            setCellValue(row, style, 2, teacher.getSureName());
            setCellValue(row, style, 3, teacher.getInn());
            setCellValue(row, style, 4, teacher.getInps());
            setCellValue(row, style, 5, teacher.getPnfl());
            setCellValue(row, style, 6, teacher.getPassportSeries());
            setCellValue(row, style, 7, teacher.getPassportNumber());
            setCellValue(row, style, 8, simpleDateFormat.format(teacher.getDateOfBirth()));
            setCellValue(row, style, 9, teacher.getPhoneNumber());
            setCellValue(row, style, 10, teacher.getEmail());
        }
    }

    public ResponseEntity importFile(MultipartFile file, String object) {

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            sheet.protectSheet(null);
            for (Row row : sheet) {
                if (row != null && row.getRowNum() > 0) {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
