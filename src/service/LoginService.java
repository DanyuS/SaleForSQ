package service;

import entity.Customer;

public interface LoginService {
	public Customer login(String name, String password) throws Exception;
}
