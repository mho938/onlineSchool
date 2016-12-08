package com.iust.onlineschool.database;

import com.iust.onlineschool.enumaration.RoleType;
import com.iust.onlineschool.model.bean.membership.Membership;
import com.iust.onlineschool.model.bean.membership.MembershipDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;

/**
 * Created by mohsen.oloumi on 10/03/2016.
 */
@Configuration
public class InitialData {

    @Autowired
    private MembershipDAO memberships;

    @PostConstruct
    public void initialUser() {
        if (false) {
            Membership user = new Membership();
            user.setUsername("admin");
            user.setPassword("admin");
            user.setRole(RoleType.superuser);
            try {
                memberships.saveOrUpdate(user);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
