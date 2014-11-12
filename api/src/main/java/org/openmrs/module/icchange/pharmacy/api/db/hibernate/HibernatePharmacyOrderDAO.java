/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.icchange.pharmacy.api.db.hibernate;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.icchange.pharmacy.PharmacyOrder;
import org.openmrs.module.icchange.pharmacy.api.db.PharmacyOrderDAO;

/**
 * It is a default implementation of  {@link PharmacyOrderDAO}.
 */
public class HibernatePharmacyOrderDAO implements PharmacyOrderDAO {

	protected final Log log = LogFactory.getLog(this.getClass());	
	private SessionFactory sessionFactory;
	
	public HibernatePharmacyOrderDAO() {}
	
    public void setSessionFactory(SessionFactory sessionFactory) {
	    this.sessionFactory = sessionFactory;
    }
    
    public SessionFactory getSessionFactory() {
	    return sessionFactory;
    }
    
	@Override
	public PharmacyOrder getPharmacyOrder(Integer pharmacyOrderId)
			throws DAOException {

		return (PharmacyOrder) sessionFactory.getCurrentSession().get(PharmacyOrder.class, pharmacyOrderId);		
	}
    
	@Override
	public PharmacyOrder getPharmacyOrderByUuid(String uuid) throws DAOException {

		if(uuid == null)
			return null;
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PharmacyOrder.class);
		crit.add(Restrictions.eq("uuid", uuid));
		
		return (PharmacyOrder)crit.uniqueResult();
	}
    
/***
	@SuppressWarnings("unchecked")
	@Override
	public List<PharmacyOrder> getPharmacyOrderByPatient(Patient patient) throws DAOException {
		
		if (patient == null)
			return null;

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PharmacyOrder.class);
		crit.add(Restrictions.eq("patient", patient));		
		return crit.list();
	}***/

	@Override
	public List<PharmacyOrder> getPharmacyOrderByDrugOrder(DrugOrder drugOrder) throws DAOException {
		List<DrugOrder> orders = new ArrayList<DrugOrder>();
		orders.add(drugOrder);
		
		return getPharmacyOrdersByDrugOrders(orders);
	}		

	
	@SuppressWarnings("unchecked")
	@Override
	public List<PharmacyOrder> getPharmacyOrdersByDrugOrders(List<DrugOrder> drugOrders) throws DAOException {

		if (drugOrders == null)
			return null;
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PharmacyOrder.class);
		Disjunction dis = Restrictions.disjunction();
		
		for (DrugOrder drugOrder : drugOrders)
			if (drugOrder != null)
				dis.add(Restrictions.eq("drugOrder", drugOrder));
		
		crit.add(dis);
		return crit.list();		
	}
    
	
	@Override
	public PharmacyOrder savePharmacyOrder(PharmacyOrder pharmacyOrder) throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(pharmacyOrder);
		return pharmacyOrder;
	}
	/**
	@Override
	public List<PharmacyOrder> saveAll(List<PharmacyOrder> phamacyOrders) throws DAOException {
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();

		for (PharmacyOrder p: phamacyOrders)
			sessionFactory.getCurrentSession().save(p);
		
		tx.commit();
		return phamacyOrders;
	}**/
}