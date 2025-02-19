package com.banking1.service;

import java.util.List;

import com.banking1.entity.Employee;

public interface BankingServiceInterface {

	String createProfileService(Employee emp);

	List<Employee> getAllRecordService();

	String editRecordService(Employee emp);

	String deleteRecordService(String email);

	Employee getEmployeeRecordByIdService(String email);

}
