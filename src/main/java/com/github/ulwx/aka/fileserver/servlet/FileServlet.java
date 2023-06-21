package com.github.ulwx.aka.fileserver.servlet;

import com.github.ulwx.aka.fileserver.utils.AkaFileUploadAppConfig;
import com.ulwx.tool.IOUtils;
import com.ulwx.tool.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet(name = "FileServlet", value = "/file/*")
public class FileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path=StringUtils.trimLeadingString(request.getRequestURI().toString(),
                request.getContextPath()+"/file/");
        String uploadDir=AkaFileUploadAppConfig.getUploadDir();
        String filePath=uploadDir+"/"+path;
        String contentType = this.getServletContext()
                .getMimeType(filePath);//通过文件名称获取MIME类型
        //failIfDirectoryTraversal(filePath);
        response.setContentLengthLong(new File(filePath).length());
        response.setContentType(contentType);
        FileInputStream fileInputStream = new FileInputStream(filePath);
        IOUtils.copy(fileInputStream,response.getOutputStream(),true);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }


    public void failIfDirectoryTraversal(String relativePath)
    {
        File file = new File(relativePath);

        if (file.isAbsolute())
        {
            throw new RuntimeException("Directory traversal attempt - absolute path not allowed");
        }

        String pathUsingCanonical;
        String pathUsingAbsolute;
        try
        {
            pathUsingCanonical = file.getCanonicalPath();
            pathUsingAbsolute = file.getAbsolutePath();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Directory traversal attempt?", e);
        }


        // Require the absolute path and canonicalized path match.
        // This is done to avoid directory traversal
        // attacks, e.g. "1/../2/"
        if (! pathUsingCanonical.equals(pathUsingAbsolute))
        {
            throw new RuntimeException("Directory traversal attempt?");
        }
    }
}
