package com.iust.onlineschool.model.bean.request;

import com.iust.onlineschool.model.bean.course.Course;
import com.iust.onlineschool.model.bean.membership.Membership;

import javax.persistence.*;

/**
 * Created by Mohsen on 12/24/2015.
 */
@Entity
@Table(name = "tbl_request")
public class Request {
    private     long                id;
    private     Membership          applicant;
    private     Course              course;
    private     long                date;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="membership_id")
    public Membership getApplicant() {
        return applicant;
    }

    public void setApplicant(Membership applicant) {
        this.applicant = applicant;
    }

    @ManyToOne
    @JoinColumn(name="course_id")
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Basic
    @Column(name = "weekcount", nullable = false, insertable = true, updatable = true)
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
