package com.iust.onlineschool.model.bean.membership;


import com.iust.onlineschool.model.bean.root._RootDAO;

import java.util.List;

/**
 * Created by mohsen.oloumi on 28/02/2016.
 */
public interface MembershipDAO extends _RootDAO<Membership, Long> {

    List<Membership> getAll();

    boolean isUsernameExist(String username);

    Membership findByUserName(String username, String password);
    Membership findByUserName(String username);
    List<Membership> getAllTeachers();
}
