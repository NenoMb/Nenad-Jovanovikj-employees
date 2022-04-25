package com.example.nenadjovanovikjemployees.controllers;

import com.example.nenadjovanovikjemployees.model.EmployeePair;
import com.example.nenadjovanovikjemployees.model.EmployeeProjectData;
import com.example.nenadjovanovikjemployees.services.FileLoadingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Controller
public class IndexController {
    private final FileLoadingService fileLoadingService;

    public IndexController(FileLoadingService fileLoadingService) {
        this.fileLoadingService = fileLoadingService;
    }


    @RequestMapping({"","/","index","index.html"})
    public String index(){
        return "index";
    }

    @PostMapping("/upload")
    public String uploadCsv(@RequestParam("file") MultipartFile file, Model model){
        ArrayList<EmployeeProjectData> employeeProjectEntries =  fileLoadingService.loadData(file);
        EmployeePair pair = fileLoadingService.getPairThatWorkedTogetherTheLongest(employeeProjectEntries);
        model.addAttribute("firstEmployee",pair.getFirstEmployeeId());
        model.addAttribute("secondEmployee",pair.getSecondEmployeeId());
        model.addAttribute("commonProjects",pair.getDaysTogetherOnProject());
        return "results";
    }
}
