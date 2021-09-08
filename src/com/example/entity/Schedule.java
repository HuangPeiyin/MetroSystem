package com.example.entity;

/**
 * 时间信息
 * @author Administrator
 *
 */
public class Schedule {
	private int SID;// 时间编号
	private String Sstart;// 发车时间
	private String Send;// 停车时间
	private int Mid;// 地铁编号
	public int getSid() {
		return SID;
	}
	public void setSid(int sid) {
		SID = sid;
	}
	public String getSstart() {
		return Sstart;
	}
	public void setSstart(String sstart) {
		Sstart = sstart;
	}
	public String getSend() {
		return Send;
	}
	public void setSend(String send) {
		Send = send;
	}
	public int getMid() {
		return Mid;
	}
	public void setMid(int mid) {
		Mid = mid;
	}
	@Override
	public String toString() {
		return "时间 ID:" + SID + ", 发车时间:" + Sstart + ", 停车时间:" + Send
				+ ", 地铁编号:" + Mid ;
	}
}
