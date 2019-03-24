package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dao.CustomerDao;
import entity.Customer;

public class CustomerDaoImpl implements CustomerDao {

	JDBConnect j = new JDBConnect();
	Connection connection=j.connectSql();
	
	@Override
	public Customer login(String name, String password) throws Exception {
		// TODO Auto-generated method stub
		Customer customer = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("select * from customer");
			rs = stmt.executeQuery();
			while (rs.next()) {
				String username=rs.getString("name");
				String psw=rs.getString("password");
				if(username.equals(name)&&psw.equals(password)) {
					customer.setName(rs.getString("name"));
					
					//a=1;
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return customer;
	}

	@Override
	public String registerByCustomer(String name, String password, String phone) throws Exception {
		return phone;
		/*String result = "true";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from customer");
		String errorinfo="";
		if (isSpecialChar(name)) {
			result = "invalid";// ���������ַ�
			errorinfo="�û����к��зǷ��ַ�!";
			System.out.println("�û����к��зǷ��ַ���");
			return errorinfo;
		}
		// user Ϊ��������
		while (rs.next()) {
			String username = rs.getString("name");
			if (username.equals(name)) {// �û����ظ�
				result = "false";
				errorinfo="�û����ѱ�ע��";
				break;
			}
			String number=rs.getString("phone");
			if(number.equals(phone)) {//�ֻ����Ѿ���ע��
				result="false";
				errorinfo="�ֻ����ѱ�ע��";
				break;
			}

		}
		if (result.equals("false")) {
			System.out.println(errorinfo);
			return errorinfo;
		} else {
			// ��params�õ�����worker����
			rs = stmt.executeQuery("select * from customer");
			int currentCustomerNum = 0;
			while (rs.next()) {
				currentCustomerNum++;
			}
			currentCustomerNum++;
			// ����worker
			String ID = "c" + currentCustomerNum;//
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String present = dateFormat.format(now);
			
			
			
			String sql2 = "insert into customer values(?,?,?,'customer',0,?,?,?)";
			PreparedStatement pst = con.prepareStatement(sql2);// ����һ��Statement����
			pst.setString(1, ID);
			pst.setString(2, name);
			pst.setString(3, password);
			pst.setString(4, present);
			pst.setString(5, phone);
			pst.setString(6, "");
			pst.executeUpdate();
			System.out.println("ע��ɹ���");
			return ID;
		}*/
	}
	public static boolean isSpecialChar(String str) {
		String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~��@#��%����&*��������+|{}������������������������]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

}
