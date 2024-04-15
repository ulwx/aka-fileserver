package com.github.ulwx.aka.fileserver.protocol.upload;

import com.github.ulwx.aka.fileserver.utils.*;
import com.github.ulwx.aka.webmvc.annotation.AkaMvcActionMethod;
import com.github.ulwx.aka.webmvc.web.action.ActionSupport;
import com.github.ulwx.aka.webmvc.web.action.CbResult;
import com.ulwx.tool.CTime;
import com.ulwx.tool.FileUtils;
import com.ulwx.tool.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;

import java.io.File;

public class UploadAction extends ActionSupport {
	protected static Logger logger = Logger.getLogger(UploadAction.class);
	@Schema( name ="UploadAction.RequestModel")
	public static class RequestModel {
		@Schema(description  = "文件",type = "object")
		public File file;
		@Schema(description  = "1：项目相关文件   2：活动(zip)  3：其他 ")
		public Integer type;
		@Schema(description  = "1：图片  2：文件")
		public Integer ftype;
		@Schema(description = "项目id")
		public String id;
		@Schema(description = "用户id")
		public String userid;
		@Schema(description = "文件上传到的目录名称")
		public String dir;

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public Integer getFtype() {
			return ftype;
		}

		public void setFtype(Integer ftype) {
			this.ftype = ftype;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getDir() {
			return dir;
		}

		public void setDir(String dir) {
			this.dir = dir;
		}
	}
	@Operation(summary  = "直连文件上传", description  ="直连文件上传")
	@ApiResponse(description = "响应", useReturnTypeSchema=true)
	@RequestBody(description = "", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
	   schema = @Schema(type = "object",implementation =RequestModel.class ))
	)
	@AkaMvcActionMethod(httpMethod = "post")
	public CbResult<ResUpload> genBean(RequestModel requestModel) {
		try {
			String dir= StringUtils.trim(requestModel.getDir());
			if(StringUtils.hasText(dir) ){
				if(!dir.endsWith("/")) {
					dir = dir + "/";
				}
			}else{
				dir="dir/";
			}
			String projectId=StringUtils.trim(requestModel.getId());
			if(StringUtils.hasText(projectId) ){
				if(!projectId.endsWith("/")) {
					projectId = projectId + "/";
				}
			}else{
				projectId="pjid/";
			}
			String userId=StringUtils.trim(requestModel.getUserid());
			if(StringUtils.hasText(userId) ){
				if(!userId.endsWith("/")) {
					userId = userId + "/";
				}
			}else{
				userId="usrid/";
			}
			String subDir=dir+projectId+userId;

			//RequestModel requestModel=request.getData();
			String str="";
			if(requestModel.type==1) {//项目相关文件
				str="projects/"+subDir;
			}else if(requestModel.type==3) {
				str="others/"+subDir;
			}else if(requestModel.type==2) {
				str="activitys/"+subDir;
			}
			logger.debug("str="+str);
			String pre= AkaFileUploadAppConfig.getUploadDir()+"/";
			FileUtils.makeDirectory(pre+str);
			String name=SnowflakeIdWorker.instance.nextId()+"";
			String fileType=StringUtils.trim(FileTypeUtils.getTypeByFile(requestModel.file));
			boolean bImage=false;
			if(requestModel.ftype==1) {//如果是上传图片
				bImage=FileTypeUtils.isImage(fileType);
				if(!bImage) {
					throw new ProtocolException("只能为jpg,png,tif,bmp,gif类型图片！");
				}
			}else{//确定是否是文件

			}
			String pathName="";
			String fType=FileUtils.getFileType(requestModel.file);
			if(!fType.equals(fileType)){
				fileType=fType;
			}
			if(StringUtils.isEmpty(fileType)) {
				throw new ProtocolException("无法确定文件类型！");
			}
			//bImage=FileTypeUtils.isImage(fileType);
			if(str.endsWith("/")){
				pathName=str+name+"."+fileType;
			}else {
				pathName = str + "/" + name + "." + fileType;
			}
			
			File desFile=new File(pre+pathName);
			FileUtils.copyFile(requestModel.file, desFile);

			ResUpload res=new ResUpload();
			
			res.relaFilePath=pathName;
			res.httpPath= AkaFileUploadAppConfig.getHttpPrefix()+"/"+pathName;
			logger.debug("pathName2="+pathName);
			String url=this.ossUpload(desFile, pathName);
			res.ossPath=pathName;
			res.ossHttpPath=url;
			return this.result(this.JsonViewSuc(res));
		}  catch (Exception e) {
			logger.error("", e);
			return this.result(this.JsonViewError(ERRS.BG_ERROR,e.getMessage()));
		}
	}

	public String ossUpload(File file,String fileName) throws Exception{

        String url= AkaFileUploadAppConfig.getOssHttpPrefix()+"/" + fileName;
        return url;

	}

	public static class ResUpload {
		@Schema(description  = "文件服务器相对路径")
		public String relaFilePath;
		@Schema(description  = "文件服务器http绝对地址")
		public String httpPath;
		@Schema(description  = "阿里云oss相对路径")
		public String ossPath;
		@Schema(description = "阿里云oss相对路径的http绝对地址")
		public String ossHttpPath;
		@Schema(description = "是否是图片")
		public Boolean isImage;
		public Boolean getImage() {
			return isImage;
		}

		public void setImage(Boolean image) {
			isImage = image;
		}

		public String getRelaFilePath() {
			return relaFilePath;
		}

		public void setRelaFilePath(String relaFilePath) {
			this.relaFilePath = relaFilePath;
		}

		public String getHttpPath() {
			return httpPath;
		}

		public void setHttpPath(String httpPath) {
			this.httpPath = httpPath;
		}

		public String getOssPath() {
			return ossPath;
		}

		public void setOssPath(String ossPath) {
			this.ossPath = ossPath;
		}

		public String getOssHttpPath() {
			return ossHttpPath;
		}

		public void setOssHttpPath(String ossHttpPath) {
			this.ossHttpPath = ossHttpPath;
		}
	}
	public static void main(String[] args) throws Exception{
		
		String file="111/111/../../../../../";
		File f=new File(file);
		System.out.println(f.getPath());
		System.out.println(f.getAbsolutePath());
		System.out.println(f.getCanonicalPath());
		System.out.println(FileUtils.getParentPath(""));
	}
}
