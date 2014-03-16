package kms7530.noticer.data;

public class ContectValue {
	private String name, phoneNum;
	
	public ContectValue(String name, String phoneNum) {
		this.name = name;
		this.phoneNum = phoneNum;
	}
	
	public String getPhoneNum() {
		return phoneNum;
	}
	
	public String getName() {
		return name;
	}
}
