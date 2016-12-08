package com.iust.onlineschool.model.bean.authentication;


import com.iust.onlineschool.model.bean.course.Course;
import com.iust.onlineschool.model.bean.membership.Membership;
import com.iust.onlineschool.model.bean.root._RootDAO;

import java.util.List;

/**
 * Created by mohsen.oloumi on 28/02/2016.
 */
public interface AuthenticationDAO extends _RootDAO<Authentication, Long> {

    List<Authentication> getAll();
    Authentication findBySession(Membership membership, String sessionId);
    Authentication findBySession(String sessionId);

}
