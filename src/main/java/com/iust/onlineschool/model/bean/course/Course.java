package com.iust.onlineschool.model.bean.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iust.onlineschool.enumaration.Field;
import com.iust.onlineschool.enumaration.Grade;
import com.iust.onlineschool.model.bean.membership.Membership;
import com.iust.onlineschool.model.bean.request.Request;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mohsen on 12/24/2015.
 */
@Entity
@Table(name = "tbl_course")
public class Course {
    private     long                id;
    private     Membership          teacher;
    private     Set<Request>        requests = new HashSet<Request>();
    private     long                createDate;
    private     boolean             validate;
    private     Field               field;
    private     Grade               grade;
    private     int                 weekCount;
    private     String              name;
    private     String              imagePath;
    private     String              description;
    private     long                balance;


    public Course() {
    }

    public Course(Membership teacher, long createDate, boolean validate, Field field, Grade grade, int weekCount, String name) {
        this.teacher = teacher;
        this.createDate = createDate;
        this.validate = validate;
        this.field = field;
        this.grade = grade;
        this.weekCount = weekCount;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description", nullable = false, insertable = true, updatable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "imagePath", nullable = false, insertable = true, updatable = true)
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @ManyToOne
    @JoinColumn(name="membership_id")
    @JsonIgnore
    public Membership getTeacher() {
        return teacher;
    }

    public void setTeacher(Membership teacher) {
        this.teacher = teacher;
    }

    @Basic
    @Column(name = "createDate", nullable = false, insertable = true, updatable = true)
    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "isvalidate", nullable = false, insertable = true, updatable = true)
    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    @Column(name = "field", nullable = false, insertable = true, updatable = true)
    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @Column(name = "grade", nullable = false, insertable = true, updatable = true)
    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    @Basic
    @Column(name = "weekcount", nullable = false, insertable = true, updatable = true)
    public int getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(int weekCount) {
        this.weekCount = weekCount;
    }

    @OneToMany(mappedBy="course",fetch = FetchType.EAGER)
    @JsonIgnore
    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    @Basic
    @Column(name = "balance", nullable = false, insertable = true, updatable = true)
    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

}
