package com.example.entity;

/**
 * ������Ϣ
 * @author Administrator
 *
 */
public class Metro {
	private int MID; // �������
	private int CID; // ���б��
	private String Mroute; // ·��
	private String Mdirection; // ����
	private String Mstart; // ���
	private String Mdestination; // �յ�
	private int Mnumber; // ;��վ��
	private float Mduration; // ����ʱ��
	private float Mprice; // Ʊ��
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
		return "����ID:" + MID + ", ����ID:" + CID + ", ·��:" + Mroute
				+ ", ����:" + Mdirection + ", ���:" + Mstart
				+ ", �յ�:" + Mdestination + ", ;��վ��:" + Mnumber
				+ ", ����ʱ��:" + Mduration + ", Ʊ��:" + Mprice ;
	}
}
