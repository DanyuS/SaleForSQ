package entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class Customer implements Serializable{
	private String id;
	private String name;
	private String password;
	private String identity;//也许未来有vip??现在就叫customer
	private double account;//金额
	private Date registTime;
	private String phoneNumber;
	private List<String> shoplist;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public double getAccount() {
		return account;
	}
	public void setAccount(double account) {
		this.account = account;
	}
	public Date getRegistTime() {
		return registTime;
	}
	public void setRegistTime(Date registTime) {
		this.registTime = registTime;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public List<String> getShoplist() {
		return shoplist;
	}
	public void setShoplist(List<String> shoplist) {
		this.shoplist = shoplist;
	}
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\",\"name\":\"" + name + "\",\"password\":\"" + password + "\",\"identity\":\""
				+ identity + "\",\"account\":\"" + account + "\",\"registTime\":\"" + registTime
				+ "\",\"phoneNumber\":\"" + phoneNumber + "\",\"shoplist\":\"" + shoplist + "\"}";
	}
	
	
}
