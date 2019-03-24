package serviceImpl;

import dao.CustomerDao;
import daoImpl.CustomerDaoImpl;
import entity.Customer;
import service.LoginService;

public class LoginServiceImpl implements LoginService {

	CustomerDao customerDao=new CustomerDaoImpl();
	
	@Override
	public Customer login(String name, String password) throws Exception {
		// TODO Auto-generated method stub
		return customerDao.login(name, password);
	}

}
