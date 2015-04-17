package com.shangbao.remotemodel;

public class ResponseModel {
	private int ResultCode;
	private String ResultMsg;
	private UserInfo Data;
	
	public ResponseModel(){
		
	}
	
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
	public UserInfo getData() {
		return Data;
	}
	public void setData(UserInfo data) {
		Data = data;
	}
	
}
