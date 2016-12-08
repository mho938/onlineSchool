package com.iust.onlineschool.model.bean.root;

import com.kendoui.spring.models.DataSourceRequest;
import com.kendoui.spring.models.DataSourceResult;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * 
 * @author AhmadReza Nazemi
 *
 * @param <Entity>
 * @param <idType>
 */

public interface _RootDAO<Entity, idType> {

	public void persist(Entity entity);

	public void update(Entity entity);

	public Entity findById(idType id);

	public void delete(Entity entity);

	public Class<?> getClassType();

	public Session getSession();

	public List<Entity> getList();

	List<Entity> getList(Order order);

	public Criteria createCriteria();

	public Collection<Entity> saveOrUpdate(Collection<Entity> entitys) throws Exception;

	public Entity saveOrUpdate(Entity entity) throws Exception;

	public Entity regetObject(Entity app);

	public DataSourceResult getList(DataSourceRequest request);

	Page<Entity> findAll(Criteria criteria, Pageable pageable);

	Page<Entity> findAll(Pageable pageable);

	DataSourceResult getList(DataSourceRequest request, Order asc);

}
