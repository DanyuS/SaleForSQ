package dao;


import entity.Customer;

public interface CustomerDao {
	public Customer login(String name, String password) throws Exception;
	public String registerByCustomer(String name, String password, String phone) throws Exception;
}
