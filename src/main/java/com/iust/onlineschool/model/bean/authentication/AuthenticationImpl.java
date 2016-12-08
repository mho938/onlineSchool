package com.iust.onlineschool.model.bean.authentication;

import com.iust.onlineschool.model.bean.course.Course;
import com.iust.onlineschool.model.bean.membership.Membership;
import com.iust.onlineschool.model.bean.root._RootImpl;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mohsen.oloumi on 28/02/2016.
 */
@Repository
@Transactional
public class AuthenticationImpl extends _RootImpl<Authentication, Long> implements AuthenticationDAO {

    @Override
    public Class<Authentication> getClassType() {
        return Authentication.class;
    }

    @Override
    public List<Authentication> getAll() { return createCriteria().list(); }

    @Override
    public Authentication findBySession(Membership membership, String sessionId) {
        Session session = getSession();

        try {
            return ((Authentication) session.createQuery(
                    "SELECT             m " +
                            "FROM               Authentication m " +
                            "WHERE              m.membership = :un and m.sessionId = :si")
                    .setParameter("un", membership).setParameter("si", sessionId)
                    .uniqueResult());
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Authentication findBySession(String sessionId) {
        Session session = getSession();

        try {
            return ((Authentication) session.createQuery(
                    "SELECT             m " +
                            "FROM               Authentication m " +
                            "WHERE              m.sessionId = :si")
                    .setParameter("si", sessionId)
                    .uniqueResult());
        }catch (Exception e){
            return null;
        }
    }


}
