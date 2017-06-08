package com.hmkcode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64.InputStream;
import com.itextpdf.text.pdf.codec.Base64.OutputStream;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class App {
	public static void main(String[] args) throws DocumentException, IOException {

		if (args.length < 2) {
			System.out.println("./pdf [htmlPath] [pdfPath]");
			return;
		}
		String htmlPath = args[0];
		String pdfPath = args[1];
		
		System.out.println(htmlPath + "\t" + pdfPath);
		String pdfPath2 = pdfPath + "_2.pdf"; 
				
		FileInputStream htmlFileStream = new FileInputStream(htmlPath);
		InputStreamReader isr = new InputStreamReader(htmlFileStream, "UTF-8");

		//通过JTidy进行HTML标签的修正
		Tidy tidy = new Tidy();
		ByteArrayOutputStream xhtmlos = new ByteArrayOutputStream();
		tidy.setXHTML(true);
		tidy.setOutputEncoding("UTF-8");
		tidy.setTidyMark(false); 
		tidy.setXmlPi(true); 
		tidy.setIndentContent(true);
		tidy.parse(isr, xhtmlos);
		
		//isr.close();
		String temp = "/tmp/temp.xhtml";
		FileOutputStream os = new FileOutputStream(temp);
		xhtmlos.writeTo(os);
		os.close();
		
		String url = new File(temp).toURI().toURL().toString();
		//String url = "http://cn.bing.com/";
		FileOutputStream pdfos = new FileOutputStream(pdfPath);
		org.xhtmlrenderer.pdf.ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(url);
		org.xhtmlrenderer.pdf.ITextFontResolver fontResolver = renderer.getFontResolver();
		try {
			//指定对应的字体来支持中文
			fontResolver.addFont("/Users/qpzhang/Downloads/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			renderer.layout();
			renderer.createPDF(pdfos);
		} catch (com.lowagie.text.DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		os.close();

		//以下调用不支持中文,没有html标签的修正
		 // step 1
		 Document document = new Document(); 
		 // step 2 
		 PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath2)); 
		 // step 3 
		 document.open();
		 
		 FileInputStream htmlFileStream1 = new FileInputStream(htmlPath);
		 InputStreamReader isr1 = new InputStreamReader(htmlFileStream1, "UTF-8");
		 
		 // step 4
		 XMLWorkerHelper.getInstance().parseXHtml(writer, document, isr1);
		 
		 System.out.println("new chinese v1"); 
		 // step 5
		 document.close();
		  
		 System.out.println("PDF Created!");
	}
}
