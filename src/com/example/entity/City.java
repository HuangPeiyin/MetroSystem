package com.example.entity;

/**
 * 城市信息
 * @author Administrator
 *
 */
public class City {
	private int CID;// 城市编号
	private String Clocation;// 地点
	private Integer Croute_num;// 总路线数
	public int getCID() {
		return CID;
	}
	public void setCID(int cID) {
		CID = cID;
	}
	public String getClocation() {
		return Clocation;
	}
	public void setClocation(String clocation) {
		Clocation = clocation;
	}
	public Integer getCroute_num() {
		return Croute_num;
	}
	public void setCroute_num(Integer croute_num) {
		Croute_num = croute_num;
	}
	@Override
	public String toString() {
		return "城市ID:" + CID + ", 地点:" + Clocation
				+ ", 总路线数:" + Croute_num ;
	}
}
