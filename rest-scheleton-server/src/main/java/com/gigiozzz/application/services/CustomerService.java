package com.gigiozzz.application.services;

import com.gigiozzz.application.domain.Customer;
import com.gigiozzz.framework.rest.RestList;


public interface CustomerService {

	public Customer addCustomer(Customer customer);
	public Customer getCustomer(Long id);
	public void deleteCustomer(Long id);
	public void deleteAllCustomer();
	public RestList<Customer> litCustomers(Integer limit,Integer offset);
	
}
