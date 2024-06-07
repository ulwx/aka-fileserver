package com.github.ulwx.aka.fileserver.protocol.upload;

import com.github.ulwx.aka.fileserver.utils.AkaFileUploadAppConfig;
import com.github.ulwx.aka.webmvc.annotation.AkaMvcActionMethod;
import com.github.ulwx.aka.webmvc.web.action.ActionSupport;
import com.github.ulwx.aka.webmvc.web.action.CbResult;
import com.ulwx.tool.ObjectUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "上传多个文件(跨域支持)",description = "上传多个文件(跨域支持)" )
public class CrossUploadManyAction extends ActionSupport {
	protected static Logger logger = Logger.getLogger(CrossUploadManyAction.class);
	@Schema( name ="CrossUploadManyAction.RequestModel")
	public static class RequestModel {
		@Schema(description = "上传的多个文件",type = "object")
		public File[] files;
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

		@Schema(description = "备注")
		public String memo;

		@Schema(description ="回调url地址")
		public String callbackUrl;

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public File[] getFiles() {
			return files;
		}

		public void setFiles(File[] files) {
			this.files = files;
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

		public String getCallbackUrl() {
			return callbackUrl;
		}

		public void setCallbackUrl(String callbackUrl) {
			this.callbackUrl = callbackUrl;
		}
	}

	@Operation(summary  = "上传多个文件(跨域支持)", description  ="上传多个文件(跨域支持)")
	@ApiResponse(description = "响应", useReturnTypeSchema=true)
	@RequestBody(description = "", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
			schema = @Schema(type = "object",implementation= RequestModel.class )
	))
	@AkaMvcActionMethod(httpMethod = "post")
	public String genBean( RequestModel requestModel) {
		//RequestModel requestModel=request.getData();
		CbResult<CrossUploadMany> ret=new CbResult<>();
		try {
			CrossUploadMany ru = new CrossUploadMany();
			ru.httpPathRoot= AkaFileUploadAppConfig.getHttpPrefix();
			ru.ossHttpPathRoot= AkaFileUploadAppConfig.getOssHttpPrefix();
			ru.memo=requestModel.memo;
			for (int i = 0; i < requestModel.files.length; i++) {
				UploadAction upload = new UploadAction();
				UploadAction.RequestModel uploadRequest=new UploadAction.RequestModel();
				uploadRequest.file = requestModel.files[i];
				uploadRequest.ftype = requestModel.ftype;
				uploadRequest.type = requestModel.type;
				uploadRequest.memo= requestModel.memo;
				uploadRequest.id = requestModel.id;
				uploadRequest.dir= requestModel.dir;
				uploadRequest.userid=requestModel.userid;
				CbResult<UploadAction.ResUpload> br = upload.genBean(uploadRequest);
				if (br.getStatus() == 1) {
					UploadAction.ResUpload rul =  br.getData();
					ru.ossPath.add(rul.ossPath);
					ru.relaFilePath.add(rul.relaFilePath);

				} else {
					ret.setStatus(0);
					ret.setMessage(br.getMessage());
					break;
				}
			}
			ret.setData(ru);
		} catch (Exception e) {
			logger.error("", e);
			ret.setStatus(0);
			ret.setMessage(e.getMessage());
		}
		logger.debug("ru=" + ObjectUtils.toJsonString(ret));

		return this.RedirectView(requestModel.callbackUrl,ret);//ForwardBean.SUC(ru);
	}


	public static class CrossUploadMany {

		@Schema(description = "文件服务器http地址的根路径")
		public String httpPathRoot="";
		@Schema(description  = "相对文件服务器的根路径地址，以英文逗号分隔")
		public List<String> relaFilePath=new ArrayList<>();

		@Schema(description  = "阿里云osshttp地址的根路径")
		public String ossHttpPathRoot="";
		@Schema(description = "相对阿里云oss的根路径地址，以英文逗号分隔")
		public List<String>  ossPath=new ArrayList<>();

		@Schema(description = "备注")
		public String memo;

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public String getHttpPathRoot() {
			return httpPathRoot;
		}

		public void setHttpPathRoot(String httpPathRoot) {
			this.httpPathRoot = httpPathRoot;
		}

		public List<String> getRelaFilePath() {
			return relaFilePath;
		}

		public void setRelaFilePath(List<String> relaFilePath) {
			this.relaFilePath = relaFilePath;
		}

		public String getOssHttpPathRoot() {
			return ossHttpPathRoot;
		}

		public void setOssHttpPathRoot(String ossHttpPathRoot) {
			this.ossHttpPathRoot = ossHttpPathRoot;
		}

		public List<String> getOssPath() {
			return ossPath;
		}

		public void setOssPath(List<String> ossPath) {
			this.ossPath = ossPath;
		}
	}

	
}
