package com.gigiozzz.application.services;

import java.util.List;

import com.gigiozzz.application.domain.Customer;


public interface CustomerService {

	public Customer addCustomer(Customer customer);
	public Customer getCustomer(Long id);
	public void deleteCustomer(Long id);
	public List<Customer> litCustomers();
	
}
