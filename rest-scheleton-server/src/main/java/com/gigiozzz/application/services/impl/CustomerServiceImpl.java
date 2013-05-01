package com.gigiozzz.application.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gigiozzz.application.domain.Customer;
import com.gigiozzz.application.repositories.CustomerRepository;
import com.gigiozzz.application.services.CustomerService;
import com.gigiozzz.framework.exception.DataServiceException;
import com.google.common.collect.Lists;

@Repository
@Transactional
public class CustomerServiceImpl implements CustomerService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	CustomerRepository customerRepository;


	@Transactional
	public Customer addCustomer(Customer customer) {
		Customer savedcustomer = customerRepository.save(customer);
		if(savedcustomer==null){
			throw new DataServiceException("data not saved");
		}
		return savedcustomer;
	}

	public Customer getCustomer(Long id){
		Customer customer = customerRepository.findOne(id);
		if(customer==null){
			throw new DataRetrievalFailureException("data not found");
		}
		return customer;
	}
	
	public void deleteCustomer(Long id){
		try {
			customerRepository.delete(id);
		}
		catch(EmptyResultDataAccessException ex){
			logger.debug("errore cancellazione valore nn esiste",ex);
			throw ex;
		}
	}

	
	public List<Customer> litCustomers(){
		return Lists.newArrayList(customerRepository.findAll());
	}


}
