package com.example.entity;

/**
 * ������Ϣ
 * @author Administrator
 *
 */
public class City {
	private int CID;// ���б��
	private String Clocation;// �ص�
	private Integer Croute_num;// ��·����
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
		return "����ID:" + CID + ", �ص�:" + Clocation
				+ ", ��·����:" + Croute_num ;
	}
}
