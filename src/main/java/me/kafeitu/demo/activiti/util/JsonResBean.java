package me.kafeitu.demo.activiti.util;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class JsonResBean<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1460533461836322506L;
	private int status = 0;
	private String message = "";
	private T data;

	public JsonResBean() {
	}

	public static <T> JsonResBean<T> buildSuccRet(T data) {
		JsonResBean<T> ret = new JsonResBean<T>();
		ret.setStatus(0);
		ret.setMessage("success");
		ret.setData(data);
		return ret;
	}

	public static <T> JsonResBean<T> buildFailRet(String errmsg) {
		JsonResBean<T> ret = new JsonResBean<T>();
		ret.setStatus(-1);
		ret.setMessage(errmsg);
		ret.setData(null);
		return ret;
	}

    public static <T> JsonResBean<T> buildFailRet(String errmsg, T data){
        JsonResBean<T> ret = new JsonResBean<T>();
        ret.setStatus(-1);
        ret.setMessage(errmsg);
        ret.setData(data);
        return ret;
    }

    public int getStatus() {
        return status;
    }

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "{" + "\"status\":" + status + ", \"message\":\"" + message + "\"" + ", \"data\":"
				+ JSON.toJSONString(data) + '}';
	}
}
