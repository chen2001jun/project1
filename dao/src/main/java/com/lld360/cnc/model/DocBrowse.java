package com.lld360.cnc.model;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * DocBrowse
 */
public class DocBrowse implements Serializable{
	/**
	 * 用户ID
	 */
	@NotBlank(message = "用户ID 不能为空。")
	private Long userId;	
	/**
	 * 文档ID
	 */
	@NotBlank(message = "文档ID 不能为空。")
	private Long docId;	
	/**
	 * updateTime
	 */
	@NotBlank(message = "updateTime 不能为空。")
	private Date updateTime;	
	
	
	public void setUpdateTime(Date value) {
		this.updateTime = value;
	}
	
	public Date getUpdateTime() {
		return this.updateTime;
	}
}