package com.example.nenadjovanovikjemployees.services;

import com.example.nenadjovanovikjemployees.model.EmployeePair;
import com.example.nenadjovanovikjemployees.model.EmployeeProjectData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
@Slf4j
public class FileLoadingServiceImpl implements FileLoadingService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(""
            + "[yyyy-MM-dd]"
            + "[yyyy.MM.dd]"
            + "[yyyy/MM/dd]"
            + "[yyyy MM dd]"
            + "[dd-MM-yyyy]"
            + "[dd.MM.yyyy]"
            + "[dd/MM/yyyy]"
            + "[dd MM yyyy]"
    );


    @Override
    public ArrayList<EmployeeProjectData> loadData(MultipartFile file) {
        BufferedReader fileReader = null;
        ArrayList<EmployeeProjectData> list =  new ArrayList<>();
        try {
            fileReader = new BufferedReader(new
                    InputStreamReader(file.getInputStream(), "UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                if(csvRecord.get(0).equals("EmpID")){
                    // skip the header
                    continue;
                }
                else {
                    EmployeeProjectData employeeProjectData = deserializeFromCsv(csvRecord);
                    if (employeeProjectData != null){
                        list.add(employeeProjectData);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public EmployeePair getPairThatWorkedTogetherTheLongest(ArrayList<EmployeeProjectData>
                                                                             employeeProjectDataList) {
        HashMap<Integer,EmployeePair> map = new HashMap<>();
        long max = 0;
        EmployeePair maxEmployeePair = null;
        for(EmployeeProjectData epdFirst : employeeProjectDataList) {
            for(EmployeeProjectData epdSecond : employeeProjectDataList) {
                if(epdFirst.getEmpId() == epdSecond.getEmpId()) {
                    continue;
                }
                if(epdSecond.hasWorkedOnProject(epdFirst.getProjectId())
                        && hasEmployeesWorkedOnAProjectInTheSameTime(epdFirst,epdSecond)) {
                    int keyId = epdFirst.getEmpId() + epdSecond.getEmpId();
                    long daysTogether = calculateDaysTogetherOnProject(epdFirst,epdSecond);
                    EmployeePair pair;
                    if(map.containsKey(keyId)) {
                        pair = map.get(keyId);
                     } else {
                        pair = new EmployeePair(epdFirst.getEmpId(),epdSecond.getEmpId());
                    }
                    pair.setTotalDaysTogetherOnSameProject(daysTogether);
                    pair.addProject(epdFirst.getProjectId(),daysTogether);
                    if (max < pair.getTotalDaysTogetherOnSameProjects()) {
                       max = pair.getTotalDaysTogetherOnSameProjects();
                       maxEmployeePair = pair;
                    }
                    map.put(keyId,pair);
                }
            }

        }
        return maxEmployeePair;
    }


    private boolean hasEmployeesWorkedOnAProjectInTheSameTime(EmployeeProjectData epdFirst, EmployeeProjectData epdSecond){
        return  (epdFirst.getDateFrom().isBefore(epdSecond.getDateTo())) &&
                (epdFirst.getDateTo().isAfter(epdSecond.getDateFrom()));
    }

    private long calculateDaysTogetherOnProject(EmployeeProjectData epdFirst, EmployeeProjectData epdSecond){
        LocalDate start = null;
        LocalDate end = null;
        if(epdFirst.getDateFrom().isAfter(epdSecond.getDateFrom())){
            start = epdFirst.getDateFrom();
        } else {
            start = epdSecond.getDateFrom();
        }

        if(epdFirst.getDateTo().isBefore(epdSecond.getDateTo())){
            end = epdFirst.getDateTo();
        } else {
            end = epdSecond.getDateTo();
        }

        return DAYS.between(start,end);
    }

    private EmployeeProjectData deserializeFromCsv (CSVRecord csvRecord){
        try {
            int empId = Integer.parseInt(csvRecord.get(0).trim());
            int projectId = Integer.parseInt(csvRecord.get(1).trim());
            LocalDate dateFrom = null;
            LocalDate dateTo = null;
            if (!csvRecord.get(2).trim().equals("NULL")) {
                dateFrom = LocalDate.parse(csvRecord.get(2).trim(), formatter);
            } else {
                dateFrom = LocalDate.now();
            }
            if (!csvRecord.get(3).trim().equals("NULL")) {
                dateTo = LocalDate.parse(csvRecord.get(3).trim(), formatter);
            } else {
                dateTo = LocalDate.now();
            }
            return new EmployeeProjectData(empId,projectId,dateFrom,dateTo);
        } catch (NumberFormatException numberFormatException) {
            log.warn("Error while trying to parse empId/projectId, the line {} will be skipped", csvRecord.toString());
            return null;
        }
        catch (DateTimeParseException dateTimeParseException) {
            log.warn("Bad date format, the line {} will be skipped", csvRecord.toString());
            return null;
        }
    }
}
