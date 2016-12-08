package com.iust.onlineschool.model.bean.person;


import com.iust.onlineschool.enumaration.Field;
import com.iust.onlineschool.enumaration.Grade;
import com.iust.onlineschool.enumaration.RoleType;
import com.iust.onlineschool.model.bean.membership.Membership;

import javax.persistence.*;

/**
 * Created by Mohsen on 11/27/2015.
 */
@Entity
@Table(name = "tbl_person")
@PrimaryKeyJoinColumn(name="membership_id")
public class Person extends Membership{
    private     String          name;
    private     String          family;
    private     long            balance;
    private     String          email;
    private     long            phoneNumber;
    private     long            nationalNumber;
    private     long            birthDate;
    private     Field           field;
    private     Grade           grade;

    public Person(String username, String password, RoleType role) {
        super(username, password, role);
    }

    public Person(String name, String family, long balance, String email, long phoneNumber, long nationalNumber, long birthDate, Field field, Grade grade) {
        this.name = name;
        this.family = family;
        this.balance = balance;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nationalNumber = nationalNumber;
        this.birthDate = birthDate;
        this.field = field;
        this.grade = grade;
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
    @Column(name = "family", nullable = false, insertable = true, updatable = true)
    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    @Basic
    @Column(name = "balance", nullable = false, insertable = true, updatable = true)
    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    @Basic
    @Column(name = "email", nullable = false, insertable = true, updatable = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phoneNumber", nullable = false, insertable = true, updatable = true)
    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "nationalNumber", nullable = false, insertable = true, updatable = true)
    public long getNationalNumber() {
        return nationalNumber;
    }

    public void setNationalNumber(long nationalNumber) {
        this.nationalNumber = nationalNumber;
    }

    @Basic
    @Column(name = "birthDate", nullable = false, insertable = true, updatable = true)
    public long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
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
}