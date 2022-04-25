package com.example.nenadjovanovikjemployees.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProjectData {
    private int empId;
    private int projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public boolean hasWorkedOnProject(int projectId){
        return this.projectId == projectId;
    }
}
