package com.iust.onlineschool.model.bean.root;

import com.kendoui.spring.models.DataSourceRequest;
import com.kendoui.spring.models.DataSourceResult;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Mohsen.oloumi
 *
 * @param <T>
 * @param <ID_TYPE>
 */
@Transactional
public abstract class _RootImpl<T, ID_TYPE> implements _RootDAO<T, ID_TYPE> {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public List<T> getList() {
		return createCriteria().list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getList(Order order) {
		return createCriteria().addOrder(order).list();
	}

	@SuppressWarnings("unchecked")
	public DataSourceResult getList(DataSourceRequest request) {
		return request.toDataSourceResult(getSession(), getClassType());
	}

	@SuppressWarnings("unchecked")
	public DataSourceResult getList(DataSourceRequest request, Order asc) {
		return null;//request.toDataSourceResult(getSession(), getClassType(), createCriteria().addOrder(asc));
	}

	public Criteria createCriteria() {
		return getSession().createCriteria(getClassType());
	}

	public Collection<T> saveOrUpdate(Collection<T> entitys) throws Exception {
		Session session = getSession();
		for (T entity : entitys) {
			saveEntity(session, entity);
		}
		return entitys;
	}

	private void saveEntity(Session session, T entity) throws Exception {
		try {
			session.saveOrUpdate(entity);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public T saveOrUpdate(T entity) throws Exception {
		Session session = getSession();
		saveEntity(session, entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public T regetObject(T app) {
		return (T) getSession().merge(app);
	}

	public void update(T entity) {
		Session session = getSession();
		session.update(entity);
	}

	@SuppressWarnings("unchecked")
	public T findById(ID_TYPE id) {
		return (T) getSession().get(getClassType(), (Serializable) id);
	}

	@Override
	public void persist(T entity) {
		getSession().persist(entity);
	}

	@Override
	public void delete(T entity) {
		getSession().delete(entity);
	}

	@Override
	public Page<T> findAll(Criteria criteria, Pageable pageable) {
		//long totalItemCount = HibernateUtil.count(criteria);
		List<T> items = findAll(criteria, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize(),
				toOrders(pageable.getSort()).toArray(new Order[0]));
		//return new SamimPageImpl<T>(items, pageable, totalItemCount);
		return null;
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		long totalItemCount = count(createCriteria());
		List<T> items = findAll(pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize(),
				toOrders(pageable.getSort()).toArray(new Order[0]));
		//return new SamimPageImpl<T>(items, pageable, totalItemCount);
		return null;

	}

	public long count(Criteria criteria) {
		return (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	public List<T> findAll(int firstResult, int maxResults, Order... orders) {
		return findAll(createCriteria(), firstResult, maxResults, orders);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(Criteria cri, int firstResult, int maxResults, Order... orders) {
		cri = addOrders(cri, orders);
		return (List<T>) setPaging(cri, firstResult, maxResults).list();
	}

	public Criteria setPaging(Criteria criteria, int firstResult, int maxResults) {
		if (firstResult >= 0)
			criteria.setFirstResult(firstResult);
		if (maxResults > 0)
			criteria.setMaxResults(maxResults);

		return criteria;
	}

	public static Criteria addOrders(Criteria dc, Order... orders) {
		for (Order o : orders) {
			dc.addOrder(o);
		}
		return dc;
	}

	public List<Order> toOrders(Sort sort) {
		List<Order> orders = new ArrayList<Order>();

		for (Sort.Order order : sort) {
			if (order.getDirection() == Sort.Direction.ASC)
				orders.add(Order.asc(order.getProperty()));
			else
				orders.add(Order.desc(order.getProperty()));
		}

		return orders;
	}
}