package com.iust.onlineschool.model.bean.request;


import com.iust.onlineschool.model.bean.root._RootDAO;

import java.util.List;

/**
 * Created by mohsen.oloumi on 28/02/2016.
 */
public interface RequestDAO extends _RootDAO<Request, Long> {

    List<Request> getAll();
}
