package com.example.nenadjovanovikjemployees.services;

import com.example.nenadjovanovikjemployees.model.EmployeePair;
import com.example.nenadjovanovikjemployees.model.EmployeeProjectData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Service
public interface FileLoadingService {
    ArrayList<EmployeeProjectData> loadData(MultipartFile file);
    EmployeePair getPairThatWorkedTogetherTheLongest(ArrayList<EmployeeProjectData> employeeProjectDataList);
}
