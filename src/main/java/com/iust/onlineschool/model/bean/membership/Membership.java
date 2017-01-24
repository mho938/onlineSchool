package com.iust.onlineschool.model.bean.membership;

import com.fasterxml.jackson.annotation.JsonProperty.Access;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iust.onlineschool.enumaration.RoleType;
import com.iust.onlineschool.model.bean.authentication.Authentication;
import com.iust.onlineschool.model.bean.course.Course;
import com.iust.onlineschool.model.bean.request.Request;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
    private transient      Set<Course>     courses = new HashSet<Course>();

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
    @JsonIgnore
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Column(name = "role", nullable = false, insertable = true, updatable = true)
    public RoleType getRole() { return role; }
    public void setRole(RoleType role) { this.role = role; }



    //@ManyToMany(fetch = FetchType.EAGER, mappedBy = "students")
    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(value = { CascadeType.SAVE_UPDATE, CascadeType.DELETE })
    @JoinTable(name = "tbl_course_students", joinColumns = {
            @JoinColumn(name = "course_id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "student_id",
                    nullable = false, updatable = false) })

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}
