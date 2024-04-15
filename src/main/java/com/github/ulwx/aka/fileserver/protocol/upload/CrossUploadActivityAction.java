package com.github.ulwx.aka.fileserver.protocol.upload;

import com.aliyun.oss.OSSClient;
import com.github.ulwx.aka.fileserver.protocol.upload.services.dao.ossdb.JOssDao;
import com.github.ulwx.aka.fileserver.protocol.upload.services.domain.JOss;
import com.github.ulwx.aka.fileserver.utils.FileTypeUtils;
import com.github.ulwx.aka.fileserver.utils.SnowflakeIdWorker;
import com.github.ulwx.aka.fileserver.utils.*;
import com.github.ulwx.aka.webmvc.BeanGet;
import com.github.ulwx.aka.webmvc.annotation.AkaMvcActionMethod;
import com.github.ulwx.aka.webmvc.web.action.ActionSupport;
import com.github.ulwx.aka.webmvc.web.action.CbResult;
import com.github.ulwx.aka.webmvc.web.action.Status;
import com.ulwx.tool.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.Collection;

@Tag(name = "跨域zip文件上传",description = "跨域zip文件上传" )
public class CrossUploadActivityAction  extends ActionSupport {
	protected static Logger logger = Logger.getLogger(CrossUploadAction.class);
	@Schema( name ="CrossUploadActivityAction.RequestModel")
	public static class RequestModel {
		@Schema(description = "上传的zip文件",type = "object")
		public File file;
		@Schema(description= "回调url地址")
		public String callbackUrl;

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public String getCallbackUrl() {
			return callbackUrl;
		}

		public void setCallbackUrl(String callbackUrl) {
			this.callbackUrl = callbackUrl;
		}
	}

	@Operation(summary  = "跨域zip文件上传", description  ="跨域zip文件上传")
	@ApiResponse(description = "响应", useReturnTypeSchema=true)
	@RequestBody(description = "",
			content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
					schema = @Schema(type = "object",implementation= RequestModel.class )
			))
	@AkaMvcActionMethod(httpMethod = "post")
	public String genBean(RequestModel requestModel) {
		//RequestModel requestModel=request.getData();
		try {
			String str = "activitys/" + CTime.formatShortDate();
			logger.debug("str=" + str);

			String fileUploadRoot = AkaFileUploadAppConfig.getUploadDir() + "/";
			FileUtils.makeDirectory(fileUploadRoot + str);
			String name = SnowflakeIdWorker.instance.nextId() + "";
			String fileType = FileTypeUtils.getTypeByFile(requestModel.file);

			String pathName = "";
			if (fileType != null && fileType.equals("zip")) {
				pathName = str + "/" + name + "." + fileType;
			} else {

				throw new ProtocolException("不是zip文件格式，请确定！");
			}
			File desFile = new File(fileUploadRoot + pathName);
			FileUtils.copyFile(requestModel.file, desFile);

			String zipToRelaDir = str + "/" + name;
			String zipToDir = fileUploadRoot + zipToRelaDir;

			FileUtils.makeDirectory(zipToDir);
			ZipUtils.unZip(zipToDir, desFile, "gbk");

			String relaFilePath = zipToRelaDir;
			String httpPath = AkaFileUploadAppConfig.getHttpPrefix() + "/" + zipToRelaDir;

			String ossPath = zipToRelaDir;
			File zipToDirFile = new File(zipToDir);
			String ossHttpPath = ossUpload(zipToDirFile, zipToRelaDir,desFile);

			ResCrossUploadActivity ru = new ResCrossUploadActivity();
			//ru.setRedirectURL(requestModel.callbackUrl);
			ru.httpPath=httpPath;
			ru.ossHttpPath=ossHttpPath;
			ru.ossPath=ossPath;
			ru.relaFilePath=relaFilePath;
			logger.debug("ru=" + ObjectUtils.toJsonString(ru));
			CbResult<ResCrossUploadActivity> ret=new CbResult<>();
			ret.setData(ru);
			return this.RedirectView(requestModel.callbackUrl,ret);

		} catch (Exception e) {
			logger.error("", e);
			CbResult<ResCrossUploadActivity> ret=new CbResult<>();
			ret.setError(ERRS.BG_ERROR);
			ret.setMessage(e.getMessage());
			ret.setStatus(Status.ERR);
			String redirectUrl=requestModel.callbackUrl;
			return this.RedirectView(redirectUrl,ret);
		}
	}

	public static String ossUpload(File dirfile, String zipToRelaDir, File zipFile) throws Exception {
		zipToRelaDir = FileUtils.toUNIXpath(zipToRelaDir);
		JOss branch = BeanGet.getBean(JOssDao.class).get();
		OSSClient ossClient = null;
		try {
			String url=branch.getServerUrl();
			if(branch.getUseUploadType()==1) {
				///
			}else {
				url=branch.getOutServerUrl();
			}
			ossClient = new OSSClient(url, branch.getAccessKeyId(), branch.getAccessKeySecret());
			Collection<File> fsList = FileUtils.listFilesAndDirs(dirfile, true);
			String fileUpRoot = FileUtils.toUNIXpath(AkaFileUploadAppConfig.getUploadDir());
			for (File f : fsList) {
				if (f.isFile()) {
					String filePath = FileUtils.toUNIXpath(f.getAbsolutePath());
					String s = StringUtils.trimLeadingString(filePath, fileUpRoot);
					s = StringUtils.trimLeadingString(s, "/");
					ossClient.putObject(String.valueOf(branch.getCode()) + "", s, f);
				}
			}
			ossClient.putObject(String.valueOf(branch.getCode()) + "", zipToRelaDir+".zip", zipFile);
		} finally {
			if(ossClient!=null) {
				ossClient.shutdown();
			}
		}
		String url = AkaFileUploadAppConfig.getOssHttpPrefix() + "/" + zipToRelaDir;
		return url;
	}



	public static class ResCrossUploadActivity {
		@Schema(description = "文件服务器相对路径")
		public String relaFilePath;
		@Schema(description  = "文件服务器http绝对地址")
		public String httpPath;
		@Schema(description  = "阿里云oss相对路径")
		public String ossPath;
		@Schema(description =  "阿里云oss相对路径的http绝对地址")
		public String ossHttpPath;

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
