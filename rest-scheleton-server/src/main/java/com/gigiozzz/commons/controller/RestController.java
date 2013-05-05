package com.gigiozzz.commons.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


public abstract class RestController<T> {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected static final String METADATA="_metadata";

	@Autowired 
	protected MessageSource messageSource;
	
	@Autowired 
	protected Validator validator;
		
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

	// retrieve single element
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public @ResponseBody HashMap<String,T> getJSON(@PathVariable Long id) {
		HashMap<String,T> response = null;
		response = getGenericObject(id);
		return response;
	}		

	public abstract HashMap<String,T> getGenericObject(Long id);

	// retrieve bulk
	@RequestMapping(value="/", method = RequestMethod.GET)
	public @ResponseBody HashMap<String,Object> listCustomersJSON(@RequestParam(required=false,value="limit")Integer limit,
			@RequestParam(required=false,value="offset")Integer offset) {
		HashMap<String,Object> response = null;
		response = listGenericObject(limit,offset);
		return response;	
	}		

	public abstract HashMap<String,Object> listGenericObject(Integer limit, Integer offset);

	//delete single element
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public void deleteCustomerJSON(@PathVariable Long id) {

		deleteGenericObject(id);

	}

	public abstract void deleteGenericObject(Long id);

	//delete bulk
	@RequestMapping(value="/", method = RequestMethod.DELETE)
	public void deleteAllJSON() {

		deleteAllGenericObject();

	}

	public abstract void deleteAllGenericObject();

	// create single element
	@RequestMapping(value="/", method = RequestMethod.POST)
	public @ResponseBody HashMap<String,T> saveJSON(@RequestBody @Valid T object) {
			
		return createGenericObject(object);
	}

	public abstract HashMap<String,T> createGenericObject(T object);

	// update single element
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	public @ResponseBody HashMap<String,T> updateCustomerJSON(@PathVariable Long id,@RequestBody @Valid T object) {

		return updateGenericObject(object);
	}

	public abstract HashMap<String,T> updateGenericObject(T object);

	// update bulk
	@RequestMapping(value="/", method = RequestMethod.PUT)
	public @ResponseBody HashMap<String,List<T>> updateAllCustomerJSON(@PathVariable Long id,@RequestBody @Valid List<T> objects) {

		return updateAllGenericObject(objects);
	}
	
	public abstract HashMap<String,List<T>> updateAllGenericObject(List<T> customers);


}