package com.iust.onlineschool.model.bean.authentication;

import com.iust.onlineschool.model.bean.membership.Membership;

import javax.persistence.*;

/**
 * Created by Mohsen on 12/8/2016.
 */
@Entity
@Table(name = "tbl_authentication")
public class Authentication {
    private         long            id;
    private         String          sessionId;
    private         Membership      membership;


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
    @Column(name = "sessionid", nullable = false, insertable = true, updatable = true)
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @ManyToOne
    @JoinColumn(name="membership_id")
    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }
}
