package com.gigiozzz.application.repositories;

import org.springframework.data.repository.CrudRepository;

import com.gigiozzz.application.domain.RestUser;

public interface RestUserRepository extends CrudRepository<RestUser, Long> {

	RestUser findByUsername(String username);

}
