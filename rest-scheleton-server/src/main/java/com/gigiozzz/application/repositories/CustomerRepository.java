package com.gigiozzz.application.repositories;

import org.springframework.data.repository.CrudRepository;

import com.gigiozzz.application.domain.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
