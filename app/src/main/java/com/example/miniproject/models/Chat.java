package com.example.miniproject.models;

public class Chat {
    static String MSG = "msg";
    static String PDF = "pdf";
    static String VIDEO = "video";
    int id;
    String msg;
    int teacherId;
    int studentId;
    String student;
    String teacher;
    int type;
    Boolean isteacher;

    public Chat(int id, String msg, int teacherId, String teacher, int studentId, String student, int type) {
        this.id = id;
        this.msg = msg;
        this.teacherId = teacherId;
        this.teacher = teacher;
        this.type = type;
        this.studentId = studentId;
        this.student = student;
        isteacher = false;
    }

    public Boolean getIsteacher() {
        return isteacher;
    }

    public void setIsteacher(Boolean isteacher) {
        this.isteacher = isteacher;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getStudent() {
        return student;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getType() {
        switch (type) {
            case 0:
                return MSG;
            case 1:
                return PDF;
            case 2:
                return VIDEO;
        }
        return "";
    }

    public void setType(int type) {
        this.type = type;
    }
}
