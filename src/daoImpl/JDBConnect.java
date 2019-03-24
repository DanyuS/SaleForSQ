package daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;

public class JDBConnect{  
	
	private static DataSource datasource = null;
	private Connection connection;
	
	public static void init() {
		InitialContext jndiContext = null;

		Properties properties = new Properties();
		properties.put(javax.naming.Context.PROVIDER_URL, "jnp:///");
		properties.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		try {
			jndiContext = new InitialContext(properties);
			datasource = (DataSource) jndiContext.lookup("java:comp/env/jdbc/j2ee");
			System.out.println("got context");
			System.out.println("About to get ds---ShowMyj2eeShopingList");
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    public Connection connectSql(){
    	
    	try {
			
			connection = datasource.getConnection();
			System.out.println("----------------------sql has connected");
		}catch (SQLException e) {
			System.out.print("get data error!"); 
			e.printStackTrace();
		}
    	//Connection connection = null;
      return connection;
      } 
     
 }
