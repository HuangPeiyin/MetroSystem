package com.example.entity;

public class Visitor {
	private int Vid; // �οͱ��
	private String phone; // �˺ţ����ֻ���
	private String password; // ����
	private String nickname; // �ǳ�
	public int getVid() {
		return Vid;
	}
	public void setVid(int vid) {
		Vid = vid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
