package com.banking1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking1.entity.Employee;
import com.banking1.service.BankingServiceInterface;

@RestController
@RequestMapping("/api/v1/employees")
public class BankingController {
	
	
	@Autowired
	private BankingServiceInterface bService;
	
	
	@GetMapping
	public List<Employee> displayAll() {
		return bService.getAllRecordService(); 
	}
	
	
	@GetMapping("/{uid}")
	public Employee getEmployeeById(@PathVariable("uid") String email) {
		return bService.getEmployeeRecordByIdService(email); 
	}
	



	@PostMapping
	public String registerEmployee(@RequestBody Employee emp) {
		
		return bService.createProfileService(emp);
	}
	
	@PutMapping("/{uid}")
	public String edit(@PathVariable("uid") String email,@RequestBody Employee emp) {
		return bService.editRecordService(emp);
		
	}
	
	@DeleteMapping("/{uid}")
	public String remove(@PathVariable("uid") String email) {
		return bService.deleteRecordService(email);
	} 
}















