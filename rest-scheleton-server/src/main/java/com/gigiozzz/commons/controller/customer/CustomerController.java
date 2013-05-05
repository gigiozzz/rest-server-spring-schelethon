package com.gigiozzz.commons.controller.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gigiozzz.application.domain.Customer;
import com.gigiozzz.application.services.CustomerService;
import com.gigiozzz.commons.controller.RestController;
import com.gigiozzz.framework.rest.RestList;


@Controller
@RequestMapping("/"+ApiV1Controller.API_V1+"/customer")
public class CustomerController extends RestController<Customer> {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String ELEMENT="customer";
	private static final String ELEMENTS="customers";
	
	@Autowired
	private CustomerService customerService;
	
	// get
	public HashMap<String,Customer> getGenericObject(Long id){
		HashMap<String,Customer> response = new HashMap<String,Customer>();
		Customer customer = null;
		
		customer = customerService.getCustomer(id);

		response.put(ELEMENT,customer);		

		return response;
	}

	//list
	public HashMap<String,Object> listGenericObject(Integer limit, Integer offset){
		HashMap<String,Object> response = new HashMap<String,Object>();
	
		RestList<Customer> customers = null;
		
		customers = customerService.litCustomers(limit,offset);
		logger.debug("test msgSrc: {}",messageSource.getMessage("titolo.lenght", null, Locale.ITALY));
			
		response.put(ELEMENTS, customers.getElements());
		response.put(METADATA, customers.getMetadata());
	
		return response;
	}
	

	//create
	public HashMap<String,Customer> createGenericObject(Customer customer){
		HashMap<String,Customer> response = new HashMap<String,Customer>();

		Customer c = customerService.addCustomer(customer);
		response.put(ELEMENT,c);
		
		return response;
	}
	

	//delete
	public void deleteGenericObject(Long id){
		customerService.deleteCustomer(id);
	}

	//delete bulk
	public void deleteAllGenericObject(){
		customerService.deleteAllCustomer();
	}
	
	//update
	public HashMap<String,Customer> updateGenericObject(Customer customer){
		HashMap<String,Customer> response = new HashMap<String,Customer>();

		Customer c = customerService.addCustomer(customer);
		response.put(ELEMENT,c);
		
		return response;
	}

	//update bulk
	public HashMap<String,List<Customer>> updateAllGenericObject(List<Customer> customers){
		HashMap<String,List<Customer>> response = new HashMap<String,List<Customer>>();

		List<Customer> result = new ArrayList<Customer>();
		for(Customer c:customers){
			Customer r = customerService.addCustomer(c);
			result.add(r);
		}
		response.put(ELEMENTS,result);
		
		return response;
	}
	
	// only for compatibility (POST ONLY)
	// mhhh i shouldn't pass argument Customer for simple delete 
	// BUT @RequestBody don't have required false option before spring 3.2M1
	@RequestMapping(value="/{id}", method = RequestMethod.POST)
	public @ResponseBody Object genericCustomerJSON(@PathVariable Long id,
			@RequestParam("reqMethod") String method,@RequestBody @Valid Customer customer) {
		
		if(method.equalsIgnoreCase("PUT")){
			return updateCustomerJSON(id, customer);
		}
		if(method.equalsIgnoreCase("DELETE")){
			deleteCustomerJSON(id);
			return new HashMap<String,Object>();
		}
			
		return true;

	}

	

	
	@RequestMapping(value="/errortest/", method = RequestMethod.GET)
	public @ResponseBody Customer testErrorJSON() throws Exception {

		throw new Exception("errore json test");
		
	}	
	

}