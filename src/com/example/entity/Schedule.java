package com.example.entity;

/**
 * ʱ����Ϣ
 * @author Administrator
 *
 */
public class Schedule {
	private int SID;// ʱ����
	private String Sstart;// ����ʱ��
	private String Send;// ͣ��ʱ��
	private int Mid;// �������
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
		return "ʱ�� ID:" + SID + ", ����ʱ��:" + Sstart + ", ͣ��ʱ��:" + Send
				+ ", �������:" + Mid ;
	}
}
