package kms7530.noticer.view.helpers;

import android.graphics.Color;
import kms7530.noticer.defines.IconDefines;

public class ViewHelper {
	public static String getIcon(int what) {
		// Distinguish tag
		String str="";
		
		if(what == IconDefines.GAMEPAD) 			str = "fa-gamepad";
		else if(what == IconDefines.HOME) 			str = "fa-home";
		else if(what == IconDefines.COFFEE) 		str = "fa-coffee";
		else if(what == IconDefines.BRIEFCASE) 		str = "fa-briefcase";
		else if(what == IconDefines.USER) 			str = "fa-user";
		else if(what == IconDefines.BOOK) 			str = "fa-book";
		else if(what == IconDefines.CHEAK) 			str = "fa-check";
		else if(what == IconDefines.SETTING) 		str = "fa-cog";
		else if(what == IconDefines.TAGS) 			str = "fa-tags";
		else if(what == IconDefines.INFO) 			str = "fa-info-circle";
		
		return str;
	}
	
	public static String getDrawerName(int pos) {
		String title = "";
		
		if(pos==0)			title = "개인";
		else if(pos==1) 	title = "할일";
		else if(pos==2) 	title = "가족";
		else if(pos==3) 	title = "업무";
		else if(pos==4) 	title = "회의";
		else if(pos==5)		title = "학습";
		else if(pos==6)		title = "여가";
		else if(pos==7)		title = "병원";
		
		return title;
	}
	
	public static String chageTimeFormat(int hour, int minute) {
		String strH;
		String strM;
		
		if(hour<10) strH="0"+String.valueOf(hour);
		else 		strH=String.valueOf(hour);
		
		if(minute<10) strM="0"+String.valueOf(minute);
		else 		  strM=String.valueOf(minute);
		
		return strH+" : "+strM;
	}
	
	public static int[] getColor(int pos) {
		int colorArr[] = new int[2];
		
		if(pos==0) {
			colorArr[0] = Color.rgb(231, 76, 60);
			colorArr[1] = Color.rgb(192, 57, 43);
		}
		else if(pos==1) {
			colorArr[0] = Color.rgb(230, 126, 34);
			colorArr[1] = Color.rgb(211, 84, 0);
		}
		else if(pos==2) {
			colorArr[0] = Color.rgb(241, 196, 15);
			colorArr[1] = Color.rgb(243, 156, 18);
		}
		else if(pos==3) {
			colorArr[0] = Color.rgb(26, 188, 156);
			colorArr[1] = Color.rgb(22, 160, 133);
		}
		else if(pos==4) {
			colorArr[0] = Color.rgb(46, 204, 113);
			colorArr[1] = Color.rgb(39, 174, 96);
		}
		else if(pos==5) {
			colorArr[0] = Color.rgb(52, 152, 219);
			colorArr[1] = Color.rgb(41, 128, 185);
		}
		else if(pos==6) {
			colorArr[0] = Color.rgb(155, 89, 182);
			colorArr[1] = Color.rgb(142, 68, 173);
		}
		else if(pos==7) {
			colorArr[0] = Color.rgb(149, 165, 166);
			colorArr[1] = Color.rgb(127, 140, 141);
		}
		else if(pos==8) {
			colorArr[0] = Color.rgb(52, 73, 94);
			colorArr[1] = Color.rgb(44, 62, 80);
		}
		
		return colorArr;
	}
}
