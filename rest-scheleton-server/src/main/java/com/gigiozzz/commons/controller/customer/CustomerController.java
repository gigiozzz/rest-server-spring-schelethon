package com.gigiozzz.commons.controller.customer;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gigiozzz.application.domain.Customer;
import com.gigiozzz.application.services.CustomerService;


@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired 
	private MessageSource messageSource;
	
	@Autowired 
	private Validator validator;
	
	@Autowired
	private CustomerService customerService;
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

	@RequestMapping(value="/get/{id}", method = RequestMethod.GET)
	public @ResponseBody Customer getCustomerJSON(@PathVariable Long id) {
		
		Customer customer = null;
		
		customer = customerService.getCustomer(id);

		return customer;
	}		

	@RequestMapping(value="/list/", method = RequestMethod.GET)
	public @ResponseBody List<Customer> listCustomerJSON() {
		List<Customer> customers = null;
		
		customers = customerService.litCustomers();
		logger.debug("test msgSrc: {}",messageSource.getMessage("titolo.lenght", null, Locale.ITALY));
			
		return customers;
	}	
	
	
	@RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
	public @ResponseBody boolean deleteCustomerJSON(@PathVariable Long id) {

		customerService.deleteCustomer(id);
		return true;

	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST)
	public @ResponseBody Long saveCustomerJSON(@RequestBody @Valid Customer customer) {
		Customer result = customerService.addCustomer(customer);
		return result.getId();
	}

	
	@RequestMapping(value="/errortest/", method = RequestMethod.GET)
	public @ResponseBody Customer testErrorJSON() throws Exception {

		throw new Exception("errore json test");
		
	}	
	

}