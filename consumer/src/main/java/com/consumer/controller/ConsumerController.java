package com.consumer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.consumer.dto.Employee;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;



import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
@RequestMapping("/api/v1/consumers")
public class ConsumerController {
	
	@Autowired
	private DiscoveryClient ds; 
	
	private static final String CIRCUIT_BREAKER_NAME = "myCircuitBreaker";
	private static final String RETRY_NAME = "myRetry";
	
	@GetMapping
	@Retry(name = RETRY_NAME, fallbackMethod = "fallbackPayment")
	public List<Employee> consumeAllEmployee(){
		
		List<ServiceInstance> serviceList = ds.getInstances("GATEWAY");
		ServiceInstance ss = serviceList.get(0);
		String url = ss.getUri().toString();
				
		RestTemplate rtemp = new RestTemplate();
		List<Employee> ee = rtemp.getForObject(url+"/api/v1/employees", List.class);
		return ee;
	}
	 public String fallbackPayment(Exception e) {
	        return "Payment failed after retries. Please try again later!";
	    }
	
	
	@PostMapping
	@CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackResponse")
	public String createConsumerProfile(@RequestBody Employee emp) {
		
		List<ServiceInstance> serviceList = ds.getInstances("GATEWAY");
		ServiceInstance ss = serviceList.get(0);
		String url = ss.getUri().toString();
		
		RestTemplate rtemp = new RestTemplate();
		String result  = rtemp.postForObject(url+"/api/v1/employees", emp,String.class);
		return result;
	}
	public String fallbackResponse(Exception e) {
        return "Payment Service is down. Please try later!";
    }
	
	
	@PutMapping("/{uid}")
	@RateLimiter(name = "myRateLimiter", fallbackMethod = "rateLimitFallback")
	public String updateConsumerProfile(@PathVariable("uid") String email,@RequestBody Employee emp) {
		
		List<ServiceInstance> serviceList = ds.getInstances("GATEWAY");
		ServiceInstance ss = serviceList.get(0);
		String url = ss.getUri().toString();
		
		RestTemplate rtemp = new RestTemplate();
		rtemp.put(url+"/api/v1/employees/" + email, emp);
		return "record updated"; 
	}
	
	public String rateLimitFallback(Exception e) {
        return "Too many requests! Please try again later.";
    }

	@DeleteMapping("/{uid}")
	@TimeLimiter(name = "myTimeLimiter", fallbackMethod = "timeLimitFallback")
	public String deleteConsumerProfile(@PathVariable("uid") String email) {
		
		List<ServiceInstance> serviceList = ds.getInstances("GATEWAY");
		ServiceInstance ss = serviceList.get(0);
		String url = ss.getUri().toString();
		
		RestTemplate rtemp = new RestTemplate();
		rtemp.delete(url+"/api/v1/employees/" + email);
		return "record deleted" ;	
	}
	public CompletableFuture<String> timeLimitFallback(Exception e) {
        return CompletableFuture.supplyAsync(() -> "Request timed out! Try again later.");
    }
	
}
