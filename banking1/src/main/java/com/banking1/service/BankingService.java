package com.banking1.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking1.dao.BankingDAOInterface;
import com.banking1.entity.Employee;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;


@Service
@Transactional
public class BankingService implements BankingServiceInterface{
	
	@Autowired
	private BankingDAOInterface bDao;
	
	private static final String CIRCUIT_BREAKER_NAME = "myCircuitBreaker";

	@Override
	@CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackResponse")
	public String createProfileService(Employee emp) {
		bDao.save(emp);
		return "user registered successfully";
	}
	
	public String fallbackResponse(Exception e) {
        return "Payment Service is down. Please try later!";
    }
	
	private static final String RETRY_NAME = "myRetry";

   
   

	@Override
	 @Retry(name = RETRY_NAME, fallbackMethod = "fallbackPayment")
	public List<Employee> getAllRecordService() {
		// TODO Auto-generated method stub
		return bDao.findAll();
	}
	 public String fallbackPayment(Exception e) {
	        return "Payment failed after retries. Please try again later!";
	    }
	 
	 
	@Override
	@RateLimiter(name = "myRateLimiter", fallbackMethod = "rateLimitFallback")
	public String editRecordService(Employee emp) {
		bDao.saveAndFlush(emp);
		return "record edited";
	}
	public String rateLimitFallback(Exception e) {
        return "Too many requests! Please try again later.";
    }
	 
	@Override
	public String deleteRecordService(String email) {
		bDao.deleteById(email);
		return "record deleted";
	}
	
	
	@Override
	@Bulkhead(name = "myBulkhead", fallbackMethod = "bulkheadFallback")
	public Employee getEmployeeRecordByIdService(String email) {
	   Optional<Employee> ee=	  bDao.findById(email);
		return ee.get();
	}
	public String bulkheadFallback(Exception e) {
        return "Too many users requesting! Try later.";
    }

}