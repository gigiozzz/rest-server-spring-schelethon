package com.gigiozzz.application.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gigiozzz.application.domain.Customer;
import com.gigiozzz.application.repositories.CustomerRepository;
import com.gigiozzz.application.services.CustomerService;
import com.gigiozzz.framework.exception.DataServiceException;
import com.gigiozzz.framework.rest.RestList;
import com.gigiozzz.framework.rest.RestListMetadata;
import com.google.common.collect.Lists;

@Repository
@Transactional
public class CustomerServiceImpl implements CustomerService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final Integer DEFAULT_LIMIT_SIZE = 10;
	private static final Integer DEFAULT_OFFSET_SIZE = 0;
	
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

	public void deleteAllCustomer(){
		try {
			customerRepository.deleteAll();
		}
		catch(EmptyResultDataAccessException ex){
			logger.debug("errore cancellazione valore nn esiste",ex);
			throw ex;
		}
	}

	
	public RestList<Customer> litCustomers(Integer limit,Integer offset){
		RestList<Customer> result= new RestList<Customer>();

		// valuto se mettere default
		limit = (limit!=null && limit>0)?limit:DEFAULT_LIMIT_SIZE;
		offset = (offset!=null && offset>0)?offset:DEFAULT_OFFSET_SIZE;
		
		Integer pageNumber = offset!=0?offset/limit:0;
		Integer pageSize = limit;
		
		PageRequest pr = new PageRequest(pageNumber,pageSize);
		
		Page<Customer> pc = customerRepository.findAll(pr);

		result.setElements(Lists.newArrayList(pc.getContent()));
		result.setMetadata(new RestListMetadata(pc.getSize(),pc.getNumber()*pc.getSize(),pc.getTotalElements()));
		
		return result;
	}


}
