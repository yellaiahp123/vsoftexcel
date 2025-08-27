package com.vsoftexcel.vsoftexcel.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vsoftexcel.vsoftexcel.entity.Project;
import com.vsoftexcel.vsoftexcel.repository.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Map<String, Object> saveFileData(InputStream file) throws IOException {
        List<Project> projectsToSave = new LinkedList<>();


        List<String> duplicateTitles = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file)) {  // Auto-close workbook
            Sheet sheet = workbook.getSheetAt(0);

            sheet.forEach(row -> {
                if (row.getRowNum() == 0) return; // Skip header

                if (row.getPhysicalNumberOfCells() < 6) {
                    throw new IllegalArgumentException(
                            "Invalid file format at row " + row.getRowNum() + " (less columns)"
                    );
                }

                String title = row.getCell(4).getStringCellValue();

                if (projectRepository.existsByTitle(title)) {
                    duplicateTitles.add("(row " + row.getRowNum() + ")");
                } else {
                    Project p = new Project();
                    p.setDegree(row.getCell(0).getStringCellValue());
                    p.setBranch(row.getCell(1).getStringCellValue());
                    p.setType(row.getCell(2).getStringCellValue());
                    p.setDomain(row.getCell(3).getStringCellValue());
                    p.setTitle(title);
                    p.setDescription(row.getCell(5).getStringCellValue());
                    projectsToSave.add(p);
                }
            });

            // Save only new projects
            List<Project> savedProjects = projectRepository.saveAll(projectsToSave);

            // Prepare response
            Map<String, Object> data = new HashMap<>();

            data.put("savedProjects", savedProjects);
            data.put("duplicates", duplicateTitles);
            return data;
        }
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }
}