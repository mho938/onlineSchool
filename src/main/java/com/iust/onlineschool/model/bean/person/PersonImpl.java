package com.iust.onlineschool.model.bean.person;

import com.iust.onlineschool.model.bean.root._RootImpl;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Mohsen on 6/28/2016.
 */
@Repository
@Transactional
public class PersonImpl extends _RootImpl<Person, Long> implements PersonDAO {
    @Override
    public List<Person> getAll() {
        return createCriteria().list();
    }

    @Override
    public Person findById(long id) {
        return (Person) createCriteria().add(Restrictions.eq("id", id)).uniqueResult();
    }

    @Override
    public Class<Person> getClassType() {
        return Person.class;
    }
}
