package org.openmrs.module.icchange.pharmacy.api.db.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.DrugOrder;
import org.openmrs.module.icchange.pharmacy.DrugOrderStatus;
import org.openmrs.module.icchange.pharmacy.api.db.DrugOrderStatusDao;

public class HibernateDrugOrderStatusDao implements DrugOrderStatusDao{

	protected final Log log = LogFactory.getLog(this.getClass());
	private SessionFactory sessionFactory;
	
	public HibernateDrugOrderStatusDao () {}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public DrugOrderStatus saveDrugOrderStatus(DrugOrderStatus status) {
		sessionFactory.getCurrentSession().saveOrUpdate(status);
		return status;
	}

	@Override
	public DrugOrderStatus getDrugOrderStatusById(Integer id) {
		return (DrugOrderStatus)sessionFactory.getCurrentSession().get(DrugOrderStatus.class, id);
	}

	@Override
	public DrugOrderStatus getDrugOrderStatusByUuid(String uuid) {
		if (uuid == null)
			return null;
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(DrugOrderStatus.class);
		crit.add(Restrictions.eq("uuid", uuid));
		
		return (DrugOrderStatus)crit.uniqueResult();
	}

	@Override
	public DrugOrderStatus getDrugOrderStatusByDrugOrder(DrugOrder order) {
		if (order == null)
			return null;
		
		return getDrugOrderStatusById(order.getId());
	}

 }
