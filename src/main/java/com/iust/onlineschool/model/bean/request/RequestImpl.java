package com.iust.onlineschool.model.bean.request;

import com.iust.onlineschool.model.bean.root._RootImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mohsen.oloumi on 28/02/2016.
 */
@Repository
@Transactional
public class RequestImpl extends _RootImpl<Request, Long> implements RequestDAO {

    @Override
    public Class<Request> getClassType() {
        return Request.class;
    }

    @Override
    public List<Request> getAll() {
        return createCriteria().list();
    }


}
