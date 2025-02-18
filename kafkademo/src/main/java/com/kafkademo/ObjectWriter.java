package com.kafkademo;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class ObjectWriter {

	public static void main(String[] args) throws Exception{
		Employee e=new Employee();
		e.setName("Mahesh");
		e.setPassword("abcd");
		
		e.setEmail("abc@yahoo.com");
		e.setAddress("Bangalore");
		
		ObjectOutputStream oo=new ObjectOutputStream(new FileOutputStream("c:/tmp/one.txt"));
		oo.writeObject(e);
		oo.close();
		System.out.println("writing complete");

	}

}
