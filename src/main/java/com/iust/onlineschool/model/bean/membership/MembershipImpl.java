package com.iust.onlineschool.model.bean.membership;

import com.iust.onlineschool.model.bean.root._RootImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mohsen.oloumi on 28/02/2016.
 */
@Repository
@Transactional
public class MembershipImpl extends _RootImpl<Membership, Long> implements MembershipDAO {

    @Override
    public Class<Membership> getClassType() {
        return Membership.class;
    }

    @Override
    public List<Membership> getAll() { return createCriteria().list(); }

    @Override
    public boolean isUsernameExist(String username) {
        Session session = getSession();

        return ((long)session.createQuery(
                    "SELECT             COUNT(*) " +
                    "FROM               Membership m " +
                    "WHERE              m.username = :un")
                .setParameter("un", username)
                .uniqueResult()) > 0;
    }

    public Membership findByUserName(String username,String password) {
        Session session = getSession();

        return ((Membership)session.createQuery(
                        "SELECT             m " +
                        "FROM               Membership m " +
                        "WHERE              m.username = :un and m.password = :ps")
                .setParameter("un", username).setParameter("ps",password)
                .uniqueResult());
    }
    public Membership findByUserName(String username) {
        Session session = getSession();
        return ((Membership)session.createQuery(
                        "SELECT             m " +
                        "FROM               Membership m " +
                        "WHERE              m.username = :un")
                .setParameter("un", username)
                .uniqueResult());
    }
}
