package com.shangbao.app.model;

public class AppResponseModel {
	private int ResultCode;//0表示失败；1表示成功
	private String ResultMsg;
	public int getResultCode() {
		return ResultCode;
	}
	public void setResultCode(int resultCode) {
		ResultCode = resultCode;
	}
	public String getResultMsg() {
		return ResultMsg;
	}
	public void setResultMsg(String resultMsg) {
		ResultMsg = resultMsg;
	}
	
	
}
