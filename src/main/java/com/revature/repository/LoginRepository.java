package com.revature.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.revature.models.AppUser;

@Repository
public interface LoginRepository extends CrudRepository<AppUser, Integer>{

}