package com.home.eschool.models.payload;

import java.util.UUID;

public class TeachersSubjectsAndClassesPayload {

    private UUID id;
    private ClassesPayload classes;
    private SubjectsPayload subject;
    private TeachersPayload teacher;

    public TeachersSubjectsAndClassesPayload(UUID id,
                                             ClassesPayload classes,
                                             SubjectsPayload subject,
                                             TeachersPayload teacher) {
        this.id = id;
        this.classes = classes;
        this.subject = subject;
        this.teacher = teacher;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ClassesPayload getClasses() {
        return classes;
    }

    public void setClasses(ClassesPayload classes) {
        this.classes = classes;
    }

    public SubjectsPayload getSubject() {
        return subject;
    }

    public void setSubject(SubjectsPayload subject) {
        this.subject = subject;
    }

    public TeachersPayload getTeacher() {
        return teacher;
    }

    public void setTeacher(TeachersPayload teacher) {
        this.teacher = teacher;
    }
}
