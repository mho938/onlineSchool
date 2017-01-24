package com.iust.onlineschool.model.bean.course;


import com.iust.onlineschool.model.bean.root._RootDAO;

import java.util.List;

/**
 * Created by mohsen.oloumi on 28/02/2016.
 */
public interface CourseDAO extends _RootDAO<Course, Long> {

    List<Course> getAll();
    List<Course> getAllBySearch(String key);
    void deleteCourse(Course course);
}
