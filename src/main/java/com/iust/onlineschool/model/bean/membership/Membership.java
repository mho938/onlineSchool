package com.iust.onlineschool.model.bean.membership;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iust.onlineschool.enumaration.RoleType;
import com.iust.onlineschool.model.bean.authentication.Authentication;
import com.iust.onlineschool.model.bean.course.Course;
import com.iust.onlineschool.model.bean.request.Request;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mohsen.oloumi on 28/02/2016.
 */
@Entity
@Table(name = "tbl_membership")
@Inheritance(strategy = InheritanceType.JOINED)
public class Membership {
    private     long            id;
    private     String          username;
    private     String          password;
    private     RoleType        role;
    private     Set<Authentication>  authentication = new HashSet<Authentication>();
    private     Set<Course>     teachers = new HashSet<Course>();
    private     Set<Request>    requests = new HashSet<Request>();
    private     Set<Course>     students = new HashSet<Course>();

    public Membership() {
    }

    public Membership(String username, String password, RoleType role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public long getId() {
        return id;
    }
    public void setId(Long id) { this.id = id; }

    @Basic
    @Column(name = "username", nullable = false, insertable = true, updatable = true)
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Basic
    @Column(name = "password", nullable = false, insertable = true, updatable = true)
    @JsonIgnoreProperties("password")
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Column(name = "role", nullable = false, insertable = true, updatable = true)
    public RoleType getRole() { return role; }
    public void setRole(RoleType role) { this.role = role; }

    @OneToMany(mappedBy="teacher")
    @JsonIgnore
    public Set<Course> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Course> teachers) {
        this.teachers = teachers;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    @JsonIgnore
    public Set<Course> getStudents() {
        return students;
    }

    public void setStudents(Set<Course> students) {
        this.students = students;
    }
    @OneToMany(mappedBy="applicant")
    @JsonIgnore
    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    public void setAuthentication(Set<Authentication> authentication) {
        this.authentication = authentication;
    }
    @OneToMany(mappedBy="membership")
    public Set<Authentication> getAuthentication() {
        return authentication;
    }
}
