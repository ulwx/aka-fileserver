package com.github.ulwx.aka.fileserver.protocol.upload.services.domain;

import com.ulwx.tool.ObjectUtils;


/*********************************************

***********************************************/
public class JOss  implements java.io.Serializable {

	private Integer id;/*id;len:10*/
	private String branchName;/*机构名称;len:50*/
	private String code;/*代号;len:50*/
	private String outServerUrl;/*sdk上传外网地址;len:200*/
	private String serverUrl;/*sdk上传内网地址;len:200*/
	private String pubUrl;/*对外提供的HTTP地址;len:200*/
	private String accessKeyId;/*accessKeyId;len:200*/
	private String accessKeySecret;/*accessKeySecret;len:200*/
	private Integer status;/*状态;len:10*/
	private Integer useUploadType;/*1：使用内部url上传 2：使用外部url上传;len:3*/
	private String memo;/*;len:50*/

	public void setId(Integer id){
		this.id = id;
	}
	public Integer getId(){
		return id;
	}
	public void setBranchName(String branchName){
		this.branchName = branchName;
	}
	public String getBranchName(){
		return branchName;
	}
	public void setCode(String code){
		this.code = code;
	}
	public String getCode(){
		return code;
	}
	public void setOutServerUrl(String outServerUrl){
		this.outServerUrl = outServerUrl;
	}
	public String getOutServerUrl(){
		return outServerUrl;
	}
	public void setServerUrl(String serverUrl){
		this.serverUrl = serverUrl;
	}
	public String getServerUrl(){
		return serverUrl;
	}
	public void setPubUrl(String pubUrl){
		this.pubUrl = pubUrl;
	}
	public String getPubUrl(){
		return pubUrl;
	}
	public void setAccessKeyId(String accessKeyId){
		this.accessKeyId = accessKeyId;
	}
	public String getAccessKeyId(){
		return accessKeyId;
	}
	public void setAccessKeySecret(String accessKeySecret){
		this.accessKeySecret = accessKeySecret;
	}
	public String getAccessKeySecret(){
		return accessKeySecret;
	}
	public void setStatus(Integer status){
		this.status = status;
	}
	public Integer getStatus(){
		return status;
	}
	public void setUseUploadType(Integer useUploadType){
		this.useUploadType = useUploadType;
	}
	public Integer getUseUploadType(){
		return useUploadType;
	}
	public void setMemo(String memo){
		this.memo = memo;
	}
	public String getMemo(){
		return memo;
	}

	public String toString(){
		return  ObjectUtils.toString(this);
	}

	private static final long serialVersionUID =2001039508L;

}