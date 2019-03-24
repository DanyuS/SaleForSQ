package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import entity.Commodity;
import entity.Customer;
import service.LoginService;
import serviceImpl.LoginServiceImpl;

/**
 * Servlet implementation class ShowShoppingCartServlet
 */
@WebServlet("/ShoppingListServlet")
public class ShoppingListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource datasource = null;

	//private LoginService loginService=new LoginServiceImpl();
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShoppingListServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		InitialContext jndiContext = null;

		Properties properties = new Properties();
		properties.put(javax.naming.Context.PROVIDER_URL, "jnp:///");
		properties.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		try {
			jndiContext = new InitialContext(properties);
			datasource = (DataSource) jndiContext.lookup("java:comp/env/jdbc/j2ee");
			System.out.println("got context");
			System.out.println("About to get ds---ShowMyj2eeShopingList");
			System.out.println("---------------------shoppinglist");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("UTF-8");
		
		HttpSession session = req.getSession(false);
		boolean cookieFound = false;
		System.out.println("1cookie "+req.getParameter("login") + " req");
		
		String name=req.getParameter("login");
		String psw=req.getParameter("password");
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		
		int a=0;
		try {
			connection = datasource.getConnection();
			System.out.println("----------------------has connected");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			stmt = connection.prepareStatement("select * from customer");
			result = stmt.executeQuery();
			while (result.next()) {
				String username=result.getString("name");
				String password=result.getString("password");
				if(username.equals(name)&&password.equals(psw)) {
					a=1;
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*Customer customer = null;
		try {
			customer = loginService.login(name, psw);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//if(customer!=null) {
		if(a==1) {
			Cookie cookie = null;
			Cookie[] cookies = req.getCookies();
			if (null != cookies) {
				// Look through all the cookies and see if the
				// cookie with the login info is there.
				for (int i = 0; i < cookies.length; i++) {
					cookie = cookies[i];
					if (cookie.getName().equals("LoginCookie")) {
						cookieFound = true;
						break;
					}
				}
			}

			if (session == null) {
				String loginValue = req.getParameter("login");
				boolean isLoginAction = (null == loginValue) ? false : true;

				System.out.println("2seesion "+loginValue + " session null");
				if (isLoginAction) { // User is logging in
					if (cookieFound) { // If the cookie exists update the value only
						// if changed
						if (!loginValue.equals(cookie.getValue())) {
							cookie.setValue(loginValue);
							resp.addCookie(cookie);
						}
					} else {
						// If the cookie does not exist, create it and set value
						cookie = new Cookie("LoginCookie", loginValue);
						cookie.setMaxAge(Integer.MAX_VALUE);
						System.out.println("Add cookie");
						resp.addCookie(cookie);
					}

					// create a session to show that we are logged in
					session = req.getSession(true);
					session.setAttribute("login", loginValue);

					req.setAttribute("login", loginValue);
					getShoppingList(req, resp);
					displayShoppingListPage(req, resp);
					displayLogoutPage(req, resp);

				} else {
					System.out.println("3seesion "+loginValue + " session null");
					// Display the login page. If the cookie exists, set login
					resp.sendRedirect(req.getContextPath() + "/CustomerLoginServlet");
				}
			} else {
				// 或未注销，重新加载该页面，session不为空
				String loginValue = (String) session.getAttribute("login");
				System.out.println("4session"+loginValue + " session");

				req.setAttribute("login", loginValue);
				getShoppingList(req, resp);
				displayShoppingListPage(req, resp);
				displayLogoutPage(req, resp);

			}
		}else {
			displayWrongUserPage(req, resp);
		}
		
		

	}

	public void getShoppingList(HttpServletRequest req, HttpServletResponse res) {

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		ArrayList list = new ArrayList();
		Statement sm = null;
		Customer customer=new Customer();
		try {
			connection = datasource.getConnection();
			System.out.println("----------------------has connected");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("下为商品列表 ： "+req.getAttribute("login"));
			stmt = connection.prepareStatement("select cid from customer where name = ?");
			stmt.setString(1, (String) req.getAttribute("login"));
			result = stmt.executeQuery();
			while (result.next()) {
				
				customer.setId(result.getString("cid"));
				//list.add(commodity);
				System.out.println("customer id: "+result.getString("cid"));
			}//得到用户id添加到购物车中
			PreparedStatement stmt1=connection.prepareStatement("select * from commodity");
			//stmt1.setString(1, customer.getId());
			ResultSet rs = stmt1.executeQuery();
			while(rs.next()) {
				Commodity commodity=new Commodity();
				commodity.setId(rs.getInt("coid"));
				commodity.setCommodityName(rs.getString("commodityName"));
				commodity.setType(rs.getString("type"));
				commodity.setPrice(rs.getDouble("price"));
				commodity.setStock(rs.getInt("stock"));
				list.add(commodity);
			}
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		req.setAttribute("list", list);
		

	}
	
	
	public void addToShoppingCart(HttpServletRequest req, HttpServletResponse res) {

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		//ArrayList list1 = new ArrayList();
		Statement sm = null;
		Customer customer=new Customer();
		try {
			connection = datasource.getConnection();
			System.out.println("----------------------add to cart");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt = connection.prepareStatement("select cid from customer where name = ?");
			stmt.setString(1, (String) req.getAttribute("login"));
			result = stmt.executeQuery();
			while (result.next()) {
				
				customer.setId(result.getString("cid"));
				//list.add(commodity);
				System.out.println("customer id: "+result.getString("cid"));
			}//得到用户id添加到购物车中
			
			//不用判断客户，主要判断商品是否存在
			
			//判断商品在不在购物车中，有则加一，无则添加进去再置为1
			PreparedStatement stmt1=connection.prepareStatement("select * from cart where cid=?");
			stmt1.setString(1, customer.getId());
			ResultSet rs = stmt1.executeQuery();
			
			while(rs.next()) {
				
			}
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// req.setAttribute("list", list);
		

	}
	

	public void displayLogoutPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		// 注销Logout
		out.println("<form method='GET' action='" + res.encodeURL(req.getContextPath() + "/CustomerLoginServlet") + "'>");
		out.println("</p>");
		out.println("<input type='submit' name='Logout' value='Logout'>");
		out.println("</form>");
		//out.println("<p>Servlet is version @version@</p>");
		out.println("</body></html>");

	}

	public void displayWrongUserPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		out.println("<html><body>");
		out.println("<p>Your username or password may be wrong!</p>");
		out.println("Click <a href='" + res.encodeURL(req.getContextPath() + "/CustomerLoginServlet") + "'>here</a> to back.<br>");
		out.println("</body></html>");

	}
	
	public void displayShoppingListPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		ArrayList list = (ArrayList) req.getAttribute("list"); // resp.sendRedirect(req.getContextPath()+"/MyStockList");

		PrintWriter out = res.getWriter();
		out.println("<html><body>");
		out.println("<table width='650' border='0' >");
		out.println("<tr>");
		out.println("<td width='650' height='80' background='" + req.getContextPath() + "/assets/pics/gintama.jpg'>&nbsp;</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<p>Welcome " + req.getAttribute("login") + "</p>");

		//out.println("Shoppinglist:  ");
		out.println("<p></p>");
		
		out.println("<table>");
		out.println("<thead>");
		out.println("<tr>");
		out.println("<caption>");
		out.println("Shoppinglist:  ");
		out.println("</caption>");
		out.println("<th>");
		out.println("编号");
		out.println("</th>");
		out.println("<th>");
		out.println("商品名");
		out.println("</th>");
		out.println("<th>");
		out.println("种类");
		out.println("</th>");
		out.println("<th>");
		out.println("单价");
		out.println("</th>");
		out.println("<th>");
		out.println("库存");
		out.println("</th>");
		out.println("<th>");
		out.println("操作");
		out.println("</th>");
		out.println("</tr>");
		out.println("</thead>");
		out.println("<tbody>");
		for (int i = 0; i < list.size(); i++) {
			Commodity commodity = (Commodity) list.get(i);
			out.println("<form method='POST' action='"
					+ res.encodeURL(req.getContextPath() + "/ShowShoppingCartServlet") + "'>");
			out.println("<tr>");
			out.println("<td>");
			out.println(commodity.getId());
			out.println("</td>");
			out.println("<td>");
			out.println(commodity.getCommodityName());
			out.println("</td>");
			out.println("<td>");
			out.println(commodity.getType());
			out.println("</td>");
			out.println("<td>");
			out.println(commodity.getPrice());
			out.println("</td>");
			out.println("<td>");
			out.println(commodity.getStock());
			out.println("</td>");
			out.println("<td>");
			out.println("<input type='submit' name='commodity' value='Add'");
			out.println("</td>");
			out.println("</tr>");
			out.println("</form>");
		}
		out.println("</tbody>");
		out.println("</table>");
		out.println("</p>");
		//前往购物车
		out.println("Click <a href='" + res.encodeURL(req.getContextPath() + "/ShowShoppingCartServlet") + "'>here</a> to go to your shopping cart.<br>");
	}

}
