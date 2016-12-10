package com.iust.onlineschool.model.bean.course;

import com.iust.onlineschool.model.bean.root._RootImpl;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mohsen.oloumi on 28/02/2016.
 */
@Repository
@Transactional
public class CourseImpl extends _RootImpl<Course, Long> implements CourseDAO {

    @Override
    public Class<Course> getClassType() {
        return Course.class;
    }

    @Override
    public List<Course> getAll() { return createCriteria().list(); }

    @Override
    public List<Course> getAllBySearch(String key) {
        Criteria c2 = getSession().createCriteria(Course.class);
        return c2.add(Restrictions.ilike("name",key, MatchMode.ANYWHERE)).list();
    }


}
