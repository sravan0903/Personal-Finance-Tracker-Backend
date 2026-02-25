package com.example.ExpenseTracker.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ExpenseTracker.entity.Income;
import com.example.ExpenseTracker.entity.User;
import com.example.ExpenseTracker.repository.IncomeRepository;
import com.example.ExpenseTracker.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IncomeDownloadServiceImpl implements IncomeDownloadService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    @Override
    public byte[] generateIncomeExcel(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        List<Income> incomes =
                incomeRepository.findByUser(user);

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Incomes");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Source");
            header.createCell(1).setCellValue("Amount");
            header.createCell(2).setCellValue("Date");

            int rowIdx = 1;
            for (Income i : incomes) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(i.getSource());
                row.createCell(1).setCellValue(i.getAmount());
                row.createCell(2).setCellValue(i.getDate().toString());
            }

            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate income Excel", e);
        }
    }
}
