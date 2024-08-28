package com.github.ulwx.aka.fileserver.protocol.upload;

import com.github.ulwx.aka.fileserver.utils.ERRS;
import com.github.ulwx.aka.webmvc.annotation.AkaMvcActionMethod;
import com.github.ulwx.aka.webmvc.web.action.ActionSupport;
import com.github.ulwx.aka.webmvc.web.action.CbResult;
import com.github.ulwx.aka.webmvc.web.action.Status;
import com.ulwx.tool.ObjectUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;

import java.io.File;
@Tag(name = "跨域上传",description = "跨域上传" )
public class CrossUploadAction extends ActionSupport {
	protected static Logger logger = Logger.getLogger(CrossUploadAction.class);
	@Schema( name ="CrossUploadAction.RequestModel")
	public static class RequestModel {
		@Schema(description  = "文件",type = "object")
		public File file;
		@Schema(description  = "文件名称")
		public String fileFileName;
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

		@Schema(description="回调url地址")
		private String callbackUrl;

		public String getMemo() {
			return memo;
		}

		public String getFileFileName() {
			return fileFileName;
		}

		public void setFileFileName(String fileFileName) {
			this.fileFileName = fileFileName;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

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

		public String getCallbackUrl() {
			return callbackUrl;
		}

		public void setCallbackUrl(String callbackUrl) {
			this.callbackUrl = callbackUrl;
		}
	}
	@Operation(summary  = "跨域上传", description  ="跨域上传")
	@ApiResponse(description = "响应", content = @Content(examples = {
			@ExampleObject(name="",
			   value="通过callbackUrl调转返回，会把值放入ret参数里。\n http://xxx.yy.zz/abc?ret=<返回的json结果>")
	},schema = @Schema(implementation =CrossUploadResp.class )))
	@RequestBody(description = "",
			content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
					schema = @Schema(type = "object",implementation=RequestModel.class )
			))
	@AkaMvcActionMethod(httpMethod = "post")
	public String genBean(RequestModel requestModel)  {
		//RequestModel requestModel=request.getData();
		try {
			UploadAction upload = new UploadAction();
			UploadAction.RequestModel uploadReq=new UploadAction.RequestModel();
			uploadReq.file = requestModel.file;
			uploadReq.ftype = requestModel.ftype;
			uploadReq.type = requestModel.type;
			uploadReq.id =requestModel.id;
			uploadReq.userid=requestModel.userid;
			uploadReq.dir=requestModel.dir;
			uploadReq.memo =requestModel.memo;
			uploadReq.fileFileName=requestModel.fileFileName;
			CbResult<UploadAction.ResUpload> br = upload.genBean(uploadReq);
			if (br.getStatus() == 1) {
				UploadAction.ResUpload rul=br.getData();
				CrossUploadResp ru = new CrossUploadResp();
				ru.httpPath=rul.httpPath;
				ru.ossHttpPath=rul.ossHttpPath;
				ru.ossPath=rul.ossPath;
				ru.relaFilePath=rul.relaFilePath;
				ru.memo =rul.memo;
				ru.fileName=rul.fileName;
				CbResult<CrossUploadResp> ret=new CbResult<>();
				ret.setData(ru);
				logger.debug("ru=" + ObjectUtils.toJsonString(ru));
				String redirectUrl=requestModel.callbackUrl;
				return this.RedirectView(redirectUrl,ret);

			} else {
				CbResult<CrossUploadResp> ret=new CbResult<>();
				ret.setError(br.getError());
				ret.setMessage(br.getMessage());
				ret.setStatus(br.getStatus());
				String redirectUrl=requestModel.callbackUrl;
				return this.RedirectView(redirectUrl,ret);

			}
		} catch (Exception e) {
			logger.error("", e);
			CbResult<CrossUploadResp> ret=new CbResult<>();
			ret.setError(ERRS.BG_ERROR);
			ret.setMessage(e.getMessage());
			ret.setStatus(Status.ERR);
			String redirectUrl=requestModel.callbackUrl;
			return this.RedirectView(redirectUrl,ret);
		}
	}


	public static class CrossUploadResp  {
		@Schema(description = "文件服务器相对路径")
		public String relaFilePath;
		@Schema(description = "文件服务器http绝对地址")
		public String httpPath;
		@Schema(description = "阿里云oss相对路径")
		public String ossPath;
		@Schema(description  = "阿里云oss相对路径的http绝对地址")
		public String ossHttpPath;
		@Schema(description = "备注")
		public String memo;
		@Schema(description = "文件名称")
		public String fileName;
		public String getMemo() {
			return memo;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public void setMemo(String memo) {
			this.memo = memo;
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
}
