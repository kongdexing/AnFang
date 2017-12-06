package com.shuhai.anfang.bean;

import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2016/10/31.
 */

public class BeanScoreTeacher {

    private String studentName;
    private LinkedHashMap<String,String> scoreMap = new LinkedHashMap<>();

    private String courseName;
    private String courseScore;

    public BeanScoreTeacher(){}

    public BeanScoreTeacher(String courseName, String courseScore) {
        this.courseName = courseName;
        this.courseScore = courseScore;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LinkedHashMap<String, String> getScoreMap() {
        return scoreMap;
    }

    public void setScoreMap(LinkedHashMap<String, String> scoreMap) {
        this.scoreMap = scoreMap;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseScore() {
        return courseScore;
    }

    public void setCourseScore(String courseScore) {
        this.courseScore = courseScore;
    }
}
