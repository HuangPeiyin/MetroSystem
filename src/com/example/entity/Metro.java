package com.example.entity;

/**
 * 地铁信息
 * @author Administrator
 *
 */
public class Metro {
	private int MID; // 地铁编号
	private int CID; // 城市编号
	private String Mroute; // 路线
	private String Mdirection; // 方向
	private String Mstart; // 起点
	private String Mdestination; // 终点
	private int Mnumber; // 途径站数
	private float Mduration; // 历经时长
	private float Mprice; // 票价
	public int getMID() {
		return MID;
	}
	public void setMID(int mID) {
		MID = mID;
	}
	public int getCID() {
		return CID;
	}
	public void setCID(int cID) {
		CID = cID;
	}
	public String getMroute() {
		return Mroute;
	}
	public void setMroute(String mroute) {
		Mroute = mroute;
	}
	public String getMdirection() {
		return Mdirection;
	}
	public void setMdirection(String mdirection) {
		Mdirection = mdirection;
	}
	public String getMstart() {
		return Mstart;
	}
	public void setMstart(String mstart) {
		Mstart = mstart;
	}
	public String getMdestination() {
		return Mdestination;
	}
	public void setMdestination(String mdestination) {
		Mdestination = mdestination;
	}
	public int getMnumber() {
		return Mnumber;
	}
	public void setMnumber(int mnumber) {
		Mnumber = mnumber;
	}
	public float getMduration() {
		return Mduration;
	}
	public void setMduration(float mduration) {
		Mduration = mduration;
	}
	public float getMprice() {
		return Mprice;
	}
	public void setMprice(float mprice) {
		Mprice = mprice;
	}
	@Override
	public String toString() {
		return "地铁ID:" + MID + ", 城市ID:" + CID + ", 路线:" + Mroute
				+ ", 方向:" + Mdirection + ", 起点:" + Mstart
				+ ", 终点:" + Mdestination + ", 途径站数:" + Mnumber
				+ ", 历经时长:" + Mduration + ", 票价:" + Mprice ;
	}
}
