package kms7530.noticer.data;

import kms7530.noticer.defines.PrefDefines.PrefCMD;
import kms7530.noticer.view.helpers.ViewHelper;

public class DrawerData {
	String title;
	int icon;
	public boolean isVisiable;
	
	public DrawerData(String title, int icon, boolean isVisiable) {
		this.title = title;
		this.icon = icon;
		this.isVisiable = isVisiable;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getCMDName() {
		String cmd = "";
		
		if(title.equals("개인")) cmd = PrefCMD.USER;
		if(title.equals("할일")) cmd = PrefCMD.CHECK;
		if(title.equals("가족")) cmd = PrefCMD.HOME;
		if(title.equals("업무")) cmd = PrefCMD.BRIEFCASE;
		if(title.equals("회의")) cmd = PrefCMD.COFFEE;
		if(title.equals("학습")) cmd = PrefCMD.BOOK;
		if(title.equals("여가")) cmd = PrefCMD.GAMEPAD;
		
		return cmd;
	}
	
	public int getIcon() {
		return icon;
	}
	
	public DrawerData setVisiable(int isVisiable, DrawerData old) {
		DrawerData data;
		data = new DrawerData(old.getTitle(), old.icon, !isVisiable(isVisiable));
		return data;
	}
	
	public int isVisiable() {
		int a = 0;
		if(this.isVisiable) a = 0;
		if(!this.isVisiable) a = 4;
		return a;
	}
	
	public int isVisiable(boolean isVisiable) {
		int a = 0;
		if(isVisiable) a = 0;
		if(!isVisiable) a = 4;
		return a;
	}
	
	public boolean isVisiable(int isVisiable) {
		boolean a = false;
		if(isVisiable == 0) a = true;
		if(isVisiable == 4) a = false;
		return a;
	}
}
