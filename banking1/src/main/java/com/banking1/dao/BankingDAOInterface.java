package com.banking1.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking1.entity.Employee;

@Repository
public interface BankingDAOInterface extends JpaRepository<Employee, String>{

}
