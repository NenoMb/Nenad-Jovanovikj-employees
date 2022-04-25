package com.example.nenadjovanovikjemployees.model;


import java.util.*;


public class EmployeePair {
    private int firstEmployeeId;
    private int secondEmployeeId;
    private long totalDaysTogetherOnSameProjects;
    private HashMap<Integer,Long> daysTogetherOnProject;

    public EmployeePair(int firstEmployeeId, int secondEmployeeId) {
        this.firstEmployeeId = firstEmployeeId;
        this.secondEmployeeId = secondEmployeeId;
        this.totalDaysTogetherOnSameProjects = 0;
        this.daysTogetherOnProject = new HashMap<>();
    }

    public void addProject(Integer projectId,Long days){
        this.daysTogetherOnProject.put(projectId,days);
    }


    public HashMap<Integer,Long> getDaysTogetherOnProject() {
        return daysTogetherOnProject;
    }

    public int getFirstEmployeeId() {
        return firstEmployeeId;
    }

    public int getSecondEmployeeId() {
        return secondEmployeeId;
    }

    public long getTotalDaysTogetherOnSameProjects() {
        return totalDaysTogetherOnSameProjects;
    }

    public void setTotalDaysTogetherOnSameProject(long daysToBeAdded) {
        this.totalDaysTogetherOnSameProjects += daysToBeAdded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePair that = (EmployeePair) o;
        return (firstEmployeeId == that.firstEmployeeId && secondEmployeeId == that.secondEmployeeId)
                || (firstEmployeeId == that.secondEmployeeId && secondEmployeeId == that.firstEmployeeId);
    }

    @Override
    public int hashCode() {
        if (firstEmployeeId < secondEmployeeId) {
           int tmp = secondEmployeeId;
           secondEmployeeId = firstEmployeeId;
           firstEmployeeId = tmp;
        }
        return Objects.hash(firstEmployeeId, secondEmployeeId);
    }
}
