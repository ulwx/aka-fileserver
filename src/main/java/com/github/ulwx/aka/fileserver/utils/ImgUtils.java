package com.github.ulwx.aka.fileserver.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImgUtils {

	public static int[] getPicWH(String path) {

		File file = new File(path);
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(file);
			BufferedImage bufferedImg = ImageIO.read(fis);
			int imgWidth = bufferedImg.getWidth();
			int imgHeight = bufferedImg.getHeight();
			
			return new int[] { imgWidth, imgHeight };
		} catch (Exception e) {
			return null;
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
