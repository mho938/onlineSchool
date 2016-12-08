package com.iust.onlineschool.model.bean.person;



import com.iust.onlineschool.model.bean.root._RootDAO;

import java.util.List;

/**
 * Created by Mohsen on 11/27/2015.
 */
public interface PersonDAO  extends _RootDAO<Person, Long> {
    public List<Person> getAll();
    public Person findById(long id);
}
