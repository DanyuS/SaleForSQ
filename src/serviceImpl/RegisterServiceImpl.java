package serviceImpl;

import dao.CustomerDao;
import daoImpl.CustomerDaoImpl;
import service.RegisterService;

public class RegisterServiceImpl implements RegisterService {
	CustomerDao customerDao=new CustomerDaoImpl();

	@Override
	public String registerByCustomer(String username, String password, String phone) throws Exception {
		// TODO Auto-generated method stub
		return customerDao.registerByCustomer(username, password,phone);
	}

}
