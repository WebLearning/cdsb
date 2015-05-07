package com.shangbao.utils;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.springframework.stereotype.Service;

@Service
public class CompressPicUtils {
	
//	public boolean compressPic(File inputFile,// 文件对象
//							   File outputFile,// 输出图路径
//							   int outputWidth,// 默认输出图片宽
//							   int outputHeight,// 默认输出图片高
//							   boolean tag){// 是否等比缩放标记
//		if(!inputFile.exists()){
//			return false;
//		}
//		Image img;
//		try {
//			img = ImageIO.read(inputFile);
//			if(img.getWidth(null) == -1){
//				return false;
//			}else {
//				int newWidth;
//				int newHeight;
//				if(tag){
//					if(outputHeight == 0){
//						double rate = ((double) img.getWidth(null)) / (double) outputWidth + 0.01;
//						newWidth = (int) (img.getWidth(null) / rate);
//						newHeight = (int) (img.getHeight(null) / rate);
//					}else if(outputWidth == 0){
//						double rate = ((double) img.getHeight(null)) / (double) outputHeight + 0.01;
//						newWidth = (int) (img.getWidth(null) / rate);
//						newHeight = (int) (img.getHeight(null) / rate);
//					}else{
//						double rate1 = ((double) img.getWidth(null)) / (double) outputWidth + 0.01;
//						double rate2 = ((double) img.getHeight(null)) / (double) outputHeight + 0.01;
//						double rate = rate1 > rate2 ? rate1 : rate2;
//						newWidth = (int) (img.getWidth(null) / rate);
//						newHeight = (int) (img.getHeight(null) / rate);
//					}
//				}else{
//					newWidth = outputWidth; // 输出的图片宽度
//					newHeight = outputHeight; // 输出的图片高度
//				}
//				BufferedImage buffImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
//				buffImage.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
//				FileOutputStream out = new FileOutputStream(outputFile);
//				// JPEGImageEncoder可适用于其他图片类型的转换
//				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//				encoder.encode(buffImage);
//				out.close();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		return false;
//	}
	
	public boolean compressByThumbnailator(File inputFile,// 文件对象
										   File outputFile,// 输出图路径
										   int outputWidth,// 默认输出图片宽
										   int outputHeight,// 默认输出图片高
										   double outputQutity,
										   boolean tag){
		if(!inputFile.exists()){
			return false;
		}
		Image img;
		try {
			img = ImageIO.read(inputFile);
			if(img.getWidth(null) == -1){
				return false;
			}
			int newWidth = 0;
			int newHeight = 0;
			if(tag){
				if(outputHeight == 0){
//					if(outputWidth < img.getWidth(null)){
						double rate = ((double) img.getWidth(null)) / (double) outputWidth;
						newWidth = (int) (img.getWidth(null) / rate);
						newHeight = (int) (img.getHeight(null) / rate);
//					}else{
//						newWidth = img.getWidth(null);
//						newHeight = img.getHeight(null);
//					}
				}else if(outputWidth == 0){
//					if(outputHeight < img.getHeight(null)){
						double rate = ((double) img.getHeight(null)) / (double) outputHeight;
						newWidth = (int) (img.getWidth(null) / rate);
						newHeight = (int) (img.getHeight(null) / rate);
//					}else{
//						newWidth = img.getWidth(null);
//						newHeight = img.getHeight(null);
//					}
				}else{
					double rate1 = ((double) img.getWidth(null)) / (double) outputWidth;
					double rate2 = ((double) img.getHeight(null)) / (double) outputHeight;
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (img.getWidth(null) / rate);
					newHeight = (int) (img.getHeight(null) / rate);
				}
				Thumbnails.of(inputFile)
						  .size(newWidth, newHeight)
						  .outputQuality(outputQutity)
						  .toFile(outputFile);
				return true;
			}else{
				Thumbnails.of(inputFile)
						  .size(outputWidth, outputHeight)
						  .outputQuality(outputQutity)
						  .toFile(outputFile);
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
//	public boolean compress(File inputFile,// 文件对象
//			   			 File outputFile,// 输出图路径
//			   			 int outputWidth,// 默认输出图片宽
//			   			 int outputHeight,// 默认输出图片高
//			   			 double outputQutity){
//		if(!inputFile.exists() || outputHeight == 0 || outputWidth == 0){
//			return false;
//		}
//		Image img;
//		try {
//			img = ImageIO.read(inputFile);
//			if(img.getWidth(null) == -1){
//				return false;
//			}
//			int newWidth = 0;
//			int newHeight = 0;
//			if(img.getHeight(null) > img.getWidth(null)){//按照宽度缩小
//				newWidth = outputWidth;
//				double rate = ((double) img.getWidth(null)) / (double) outputWidth;
//				newHeight = (int)(img.getHeight(null) / rate);
//			}else{
//				newHeight = outputHeight;
//				double rate = ((double) img.getHeight(null)) / (double) outputHeight;
//				newWidth = (int)(img.getWidth(null) / rate);
//			}
//			Thumbnails.of(inputFile)
//			  .size(newWidth, newHeight)
//			  .outputQuality(outputQutity)
//			  .toFile(outputFile);
//			return true;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return false;
//	}
	
	/**
	 * 压缩图片并添加水印
	 * @param inputFile
	 * @param outputFile
	 * @param waterMark
	 * @param outputWidth
	 * @param outputHeight
	 * @param outputQutity
	 * @param tag
	 * @return
	 */
	public boolean compressWithWatermark(File inputFile,// 文件对象
										 File outputFile,// 输出图路径
										 File waterMark,//水印图片
										 int outputWidth,// 默认输出图片宽
										 int outputHeight,// 默认输出图片高
										 double outputQutity,
										 boolean tag){
		if(!inputFile.exists() || !waterMark.exists()){
			return false;
		}
		Image img;
		try{
				img = ImageIO.read(inputFile);
				if(img.getWidth(null) == -1){
					return false;
				}
				int newWidth = 0;
				int newHeight = 0;
				if(tag){
					if(outputHeight == 0){
						if(outputWidth < img.getWidth(null)){
							double rate = ((double) img.getWidth(null)) / (double) outputWidth;
							newWidth = (int) (img.getWidth(null) / rate);
							newHeight = (int) (img.getHeight(null) / rate);
						}else{
							newWidth = img.getWidth(null);
							newHeight = img.getHeight(null);
						}
					}else if(outputWidth == 0){
						if(outputHeight < img.getHeight(null)){
							double rate = ((double) img.getHeight(null)) / (double) outputHeight;
							newWidth = (int) (img.getWidth(null) / rate);
							newHeight = (int) (img.getHeight(null) / rate);
						}else{
							newWidth = img.getWidth(null);
							newHeight = img.getHeight(null);
						}
					}else{
						double rate1 = ((double) img.getWidth(null)) / (double) outputWidth;
						double rate2 = ((double) img.getHeight(null)) / (double) outputHeight;
						double rate = rate1 > rate2 ? rate1 : rate2;
						newWidth = (int) (img.getWidth(null) / rate);
						newHeight = (int) (img.getHeight(null) / rate);
					}
					Thumbnails.of(inputFile)
					  .size(newWidth, newHeight)
					  .watermark(Positions.TOP_RIGHT, ImageIO.read(waterMark), 0.5f)
					  .outputQuality(outputQutity)
					  .toFile(outputFile);
					return true;
				}else{
					Thumbnails.of(inputFile)
							  .size(newWidth, newHeight)
							  .outputQuality(outputQutity)
							  .toFile(outputFile);
					return true;
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean setWaterMark(File inputFile, File outputFile, File waterMark, Float outputQuality){
		if (!inputFile.exists() || !waterMark.exists()) {
			return false;
		}
		Image img;
		try {
			img = ImageIO.read(inputFile);
			if (img.getWidth(null) == -1) {
				return false;
			}
			Thumbnails.of(inputFile)
					.crop(Positions.CENTER)
					.size(400, 400)
					.watermark(Positions.TOP_LEFT,ImageIO.read(waterMark), 1.0f)
					.outputQuality(outputQuality)
					.toFile(outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
