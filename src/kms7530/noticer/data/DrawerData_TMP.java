package kms7530.noticer.data;

public class DrawerData_TMP {
	String title;
	int icon;
	
	public DrawerData_TMP(String title, int iCoc) {
		this.icon = iCoc;
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	public int getIcon() {
		return icon;
	}
}
