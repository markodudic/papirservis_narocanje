package org.classes.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;

public class PdfCreator {
	private static Log log = LogFactory.getLog(PdfCreator.class); 
	public static void createPdf(String html ,String title, String realPath, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("pdf/application");
		response.setHeader("Content-disposition", "attachment; filename=form.pdf");
		OutputStream out = null;
		try {
			out = response.getOutputStream();

			File jspFile = new File(realPath+"WEB-INF/views/form-pdf.tpl");
			String jsp = new String(getBytesFromFile(jspFile), "utf-8");
			
			String urlPrefix = request.getRequestURL().substring(0,request.getRequestURL().indexOf(request.getRequestURI()));
			String contextPath = urlPrefix+request.getContextPath();
			jsp = jsp.replace("${contextUrl}",contextPath);

			jsp = jsp.replace("${formHtml}",html);
			jsp = jsp.replace("${formTitle}",title);
			InputStream jspis = new ByteArrayInputStream(jsp.getBytes("utf-8"));
			
			DocumentBuilder builder = null;
			Document doc = null;
			
			try {
				builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				doc = builder.parse(jspis);
			} catch (Exception e) {
				log.debug("HTML Potrebno počistit z Jtidy");
				jspis = new ByteArrayInputStream(jsp.getBytes("utf-8")); 
				//** tidy
				Tidy tidy = new Tidy(); // obtain a new Tidy instance
				tidy.setInputEncoding("utf-8");
				tidy.setOutputEncoding("utf-8");
				tidy.setUpperCaseTags(false);
				tidy.setUpperCaseAttrs(false);
				tidy.setDropFontTags(true);
				tidy.setXHTML(true);
				tidy.setDocType("omit");
				tidy.setMakeClean(true);
				tidy.setQuiet(false);
				//tidy.setIndentContent(true);
				//tidy.setSmartIndent(true);
				//tidy.setIndentAttributes(true);
				
				ByteArrayOutputStream xhtml = new ByteArrayOutputStream();
				tidy.parse(jspis, xhtml);
				//byte [] xhtmlb = new String(xhtml.toByteArray(),"utf-8").replaceFirst(".*?<\\?xml", "<?xml").getBytes("utf-8"); 
				//byte [] xhtmlb = new String(xhtml.toByteArray(),"utf-8").substring(110).getBytes("utf-8"); 
				//log.debug(new String(xhtml.toByteArray(),"utf-8"));
				//ByteArrayInputStream xhtmlIn = new ByteArrayInputStream(xhtml.toByteArray());
				ByteArrayInputStream xhtmlIn = new ByteArrayInputStream(xhtml.toByteArray());
				//** konec tidy
				builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				doc = builder.parse(xhtmlIn);
			}

			ITextRenderer renderer = new ITextRenderer();
			try {
				doc.setXmlStandalone(true);
				renderer.setDocument(doc,null);
			} catch (RuntimeException e) {}
			renderer.getFontResolver().addFont(realPath+"WEB-INF/fonts/arial.ttf",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
			renderer.getFontResolver().addFont(realPath+"WEB-INF/fonts/arialbd.ttf",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
			renderer.getFontResolver().addFont(realPath+"WEB-INF/fonts/arialbi.ttf",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
			renderer.getFontResolver().addFont(realPath+"WEB-INF/fonts/ariali.ttf",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);

			renderer.layout();
			renderer.createPDF(out);

			out.close();
		} catch (Exception e) {
			response.setContentType("text/html");
			response.setHeader("Content-disposition", null);
			try {
				e.printStackTrace();
				out.write("PDF error".getBytes());
				out.flush();
				out.close();
			} catch (IOException e1) {}
		} finally {
			if (out!= null) try { out.close(); } catch (IOException e) {}
		}
	}  
	
	public static String tidyHtml(String html) {
		//** tidy
		try {
			
			Tidy tidy = new Tidy(); // obtain a new Tidy instance
			//tidy.setCharEncoding(Configuration.UTF8);
			tidy.setInputEncoding("utf-8");
			tidy.setOutputEncoding("utf-8");
			tidy.setUpperCaseTags(false);
			tidy.setUpperCaseAttrs(false);
			tidy.setDropFontTags(false);
			tidy.setXHTML(false);
			tidy.setDropProprietaryAttributes(false);
			tidy.setForceOutput(true);
			tidy.setPrintBodyOnly(true);
			tidy.setDropEmptyParas(false);
			//tidy.setRawOut(true);
			//tidy.setDocType("strict");
			//tidy.setMakeClean(true);  // ne sme bit, ker merga dive
			tidy.setQuiet(false);
			tidy.setTrimEmptyElements(false);
			//tidy.setJoinClasses(false);
			//tidy.setJoinStyles(false);
			//tidy.setIndentContent(true);
			//tidy.setSmartIndent(true);
			//tidy.setIndentAttributes(true);
			
			/*tidy = new Tidy(); // obtain a new Tidy instance
			tidy.setOutputEncoding("utf-8");
			tidy.setUpperCaseTags(false);
			tidy.setUpperCaseAttrs(false);
			tidy.setDropFontTags(true);
			tidy.setXHTML(true);
			tidy.setDocType("strict");
			tidy.setMakeClean(true);
			tidy.setQuiet(false);
			tidy.setDocType("omit");*/
			//tidy.setIndentContent(true);
			//tidy.setSmartIndent(true);
			//tidy.setIndentAttributes(true);
			
			ByteArrayOutputStream xhtml = new ByteArrayOutputStream();
			ByteArrayInputStream is = new ByteArrayInputStream(html.getBytes("utf-8"));
			tidy.parse(is,xhtml);
			//byte [] xhtmlb = new String(xhtml.toByteArray(),"utf-8").replaceFirst(".*?<\\?xml", "<?xml").getBytes("utf-8"); 
			String cleanhtml = new String(xhtml.toByteArray(),"utf-8");
			
		
			cleanhtml = cleanhtml
				.replaceAll("TEXT-ALIGN", "text-align")
				.replaceAll("FONT-STYLE", "font-style")
				.replaceAll("FONT-WEIGHT", "font-weight")
				.replaceAll("WIDTH", "width")
				.replaceAll("DISPLAY", "display")
				.replaceAll("BACKGROUND-COLOR", "background-color");
			log.debug("CLEAN HTML\n");
			log.debug(cleanhtml+"\n");
			return cleanhtml;
			
		} catch (UnsupportedEncodingException e) {
			return html;
		} 
	}
	
	public static void main(String[] args) {
		String xx="<DIV class=form-row>\n"+
"<H1 style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 243px; DISPLAY: inline-block; FONT-WEIGHT: normal\" name=\"Glavni naslov\" type=\"text\">Glavni naslov</H1></DIV>\n"+
"<DIV class=form-row><LABEL style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; DISPLAY: inline-block; FONT-WEIGHT: normal\">Oznaka</LABEL><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; DISPLAY: inline-block; FONT-WEIGHT: normal\" class=\"required validate-alpha\" type=text name=ime></DIV>\n"+
"<DIV class=form-row><LABEL style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; DISPLAY: inline-block; FONT-WEIGHT: normal\">Oznaka</LABEL><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; DISPLAY: inline-block; FONT-WEIGHT: normal\" class=\"required validate-email\" type=text name=email></DIV>\n"+
"<DIV class=form-row><LABEL style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; DISPLAY: inline-block; FONT-WEIGHT: normal\">Oznaka</LABEL><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; DISPLAY: inline-block; FONT-WEIGHT: normal\" class=\"required validate-url\" type=text name=www></DIV>\n"+
"<DIV class=form-row><LABEL style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; DISPLAY: inline-block; FONT-WEIGHT: normal\">Oznaka</LABEL><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; DISPLAY: inline-block; FONT-WEIGHT: normal\" class=validate-numeric type=text name=broj></DIV>\n"+
"<DIV class=form-row><LABEL style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; DISPLAY: inline-block; FONT-WEIGHT: normal\">Oznaka</LABEL><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; DISPLAY: inline-block; FONT-WEIGHT: normal\" class=\"required chooser-date\" type=text name=\"Ulazno polje\"></DIV>\n"+
"<DIV class=form-row><TEXTAREA style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 300px; DISPLAY: inline-block; HEIGHT: 100px; FONT-WEIGHT: normal\" name=\"Duži tekst\"></TEXTAREA></DIV>\n"+
"<DIV class=form-row>\n"+
"<FIELDSET style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 361px; DISPLAY: inline-block; FONT-WEIGHT: normal\">\n"+
"<DIV class=fieldset-row><INPUT class=checkbox value=\"Opcija 1\" type=checkbox name=Grupa1><LABEL>Opcija 1</LABEL></DIV>\n"+
"<DIV class=fieldset-row><INPUT class=checkbox value=\"Opcija 2\" type=checkbox name=Grupa1><LABEL>Opcija 2</LABEL></DIV></FIELDSET></DIV>\n"+
"<DIV class=form-row>\n"+
"<H3 style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 148px; DISPLAY: inline-block; FONT-WEIGHT: normal\" name=\"Naslov bloka\" type=\"text\">Naslov bloka</H3></DIV>\n"+
"<DIV class=form-row>\n"+
"<DIV style=\"WIDTH: 300px; DISPLAY: inline-block\"><IMG id=\"Unos slike__img\" src=\"/efsa/img/designer/image_big.jpg\" width=300>\n"+
"<FORM class=img_upload_form encType=multipart/form-data onsubmit=\"uploadImage1(this,'Unos slike');\" method=post action=/efsa/upload/img><INPUT value=300 type=hidden name=efs_img_w><INPUT value=200 type=hidden name=efs_img_h><INPUT id=\"Unos slike__imgname\" type=hidden name=efs_img_name><INPUT id=\"Unos slike\" class=imgupload type=file name=\"Unos slike\"><INPUT value=Upload type=submit></FORM></DIV></DIV>\n"+
"<DIV class=form-row><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 240px; DISPLAY: inline-block; FONT-WEIGHT: normal\" id=Dodatki type=file name=Dodatki maxfiles=\"1\"></DIV>\n"+
"<DIV class=form-row>\n"+
"<FIELDSET style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 361px; DISPLAY: inline-block; FONT-WEIGHT: normal\">\n"+
"<DIV class=fieldset-row><INPUT class=radio value=\"Opcija 1\" type=radio name=Grupa><LABEL>Opcija 1</LABEL></DIV>\n"+
"<DIV class=fieldset-row><INPUT class=radio value=\"Opcija 2\" type=radio name=Grupa><LABEL>Opcija 2</LABEL></DIV></FIELDSET></DIV>\n"+
"<DIV class=form-row><SELECT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 150px; DISPLAY: inline-block; FONT-WEIGHT: normal\" name=\"Padajoča lista\"><OPTION selected value=\"Opcija 1\">Opcija 1</OPTION><OPTION value=\"Opcija 2\">Opcija 2</OPTION></SELECT></DIV>\n";
	String yy="<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv><SPAN class=draggable><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 243px; FONT-WEIGHT: normal\" class=\"form-element form-h1\" value=\"Glavni naslov\" elid=\"5422692902471513\"><SPAN class=resizer></SPAN></SPAN></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv><SPAN class=draggable><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; FONT-WEIGHT: normal\" class=\"form-element form-label\" value=Oznaka elid=\"017750517116323583\"><SPAN class=resizer></SPAN></SPAN><SPAN class=draggable><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; FONT-WEIGHT: normal\" class=\"form-element \" value=ime name=ime elid=\"ime\"><SPAN class=resizer></SPAN></SPAN></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv><SPAN class=draggable><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; FONT-WEIGHT: normal\" class=\"form-element form-label \" value=Oznaka elid=\"5942496350642772\"><SPAN class=resizer></SPAN></SPAN><SPAN class=draggable><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; FONT-WEIGHT: normal\" class=\"form-element \" value=email name=email elid=\"email\"><SPAN class=resizer></SPAN></SPAN></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv><SPAN class=draggable><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; FONT-WEIGHT: normal\" class=\"form-element form-label \" value=Oznaka elid=\"594956278605519\"><SPAN class=resizer></SPAN></SPAN><SPAN class=draggable><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; FONT-WEIGHT: normal\" class=\"form-element \" value=www name=www elid=\"www\"><SPAN class=resizer></SPAN></SPAN></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv><SPAN class=draggable><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; FONT-WEIGHT: normal\" class=\"form-element form-label \" value=Oznaka elid=\"0787719982667418\"><SPAN class=resizer></SPAN></SPAN><SPAN class=draggable><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; FONT-WEIGHT: normal\" class=\"form-element \" value=broj name=broj elid=\"broj\"><SPAN class=resizer></SPAN></SPAN></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv><SPAN class=draggable><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; FONT-WEIGHT: normal\" class=\"form-element form-label \" value=Oznaka elid=\"5176997982272992\"><SPAN class=resizer></SPAN></SPAN><SPAN class=draggable><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 127px; FONT-WEIGHT: normal\" class=\"form-element \" value=\"Ulazno polje\" name=\"Ulazno polje\" elid=\"Ulazno polje\"><SPAN class=resizer></SPAN></SPAN></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv><SPAN class=draggable><TEXTAREA style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 300px; HEIGHT: 100px; FONT-WEIGHT: normal\" class=\"form-element \" name=\"Duži tekst\" elid=\"Duži tekst\">Duži tekst</TEXTAREA><SPAN class=resizer></SPAN></SPAN></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv><SPAN class=draggable>\n"+
	"<FIELDSET style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 361px; FONT-WEIGHT: normal\" class=\"form-element \" elid=\"Grupa1\">\n"+
	"<DIV class=field-delete><INPUT class=checkbox value=\"Opcija 1\" type=checkbox name=Grupa1_design_> <INPUT class=form-fieldset-label value=\"Opcija 1\"> <IMG class=field-delete-icon src=\"http://localhost:8080/efsa/img/designer/delete-icon.gif\"> </DIV>\n"+
	"<DIV class=field-add><INPUT class=checkbox value=\"Opcija 2\" type=checkbox name=Grupa1_design_> <INPUT class=form-fieldset-label value=\"Opcija 2\"> <IMG class=field-add-icon src=\"http://localhost:8080/efsa/img/designer/add_icon.gif\"> </DIV></FIELDSET><SPAN class=resizer></SPAN> </SPAN></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv><SPAN class=draggable><INPUT style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 148px; FONT-WEIGHT: normal\" class=\"form-element form-h3 \" value=\"Naslov bloka\" elid=\"548599222673107\"><SPAN class=resizer></SPAN></SPAN></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv><SPAN class=draggable>\n"+
	"<DIV style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 300px; HEIGHT: 200px; FONT-WEIGHT: normal\" class=\"form-element form-imgupload \" elid=\"Unos slike\"><IMG src=\"http://localhost:8080/efsa/img/designer/image.jpg\"> </DIV><SPAN class=resizer></SPAN></SPAN></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv><SPAN class=draggable>\n"+
	"<DIV style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 240px; FONT-WEIGHT: normal\" class=\"form-element form-fileupload form-element-selected\" elid=\"Dodatki\"><INPUT value=Dodatki name=Dodatki><BUTTON type=submit>Pretraži...</BUTTON> </DIV></SPAN></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv><SPAN class=draggable>\n"+
	"<FIELDSET style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 361px; FONT-WEIGHT: normal\" class=\"form-element \" elid=\"Grupa\">\n"+
	"<DIV class=field-delete><INPUT class=radio value=\"Opcija 1\" type=radio name=Grupa_design_> <INPUT class=form-fieldset-label value=\"Opcija 1\"> <IMG class=field-delete-icon src=\"http://localhost:8080/efsa/img/designer/delete-icon.gif\"> </DIV>\n"+
	"<DIV class=field-add><INPUT class=radio value=\"Opcija 2\" type=radio name=Grupa_design_> <INPUT class=form-fieldset-label value=\"Opcija 2\"> <IMG class=field-add-icon src=\"http://localhost:8080/efsa/img/designer/add_icon.gif\"> </DIV></FIELDSET><SPAN class=resizer></SPAN> </SPAN></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv><SPAN class=draggable>\n"+
	"<DIV style=\"TEXT-ALIGN: left; FONT-STYLE: normal; WIDTH: 150px; FONT-WEIGHT: normal\" class=\"form-element form-select \" elid=\"Padajoča lista\"><SELECT name=\"Padajoča lista_design_\"><OPTION selected value=\"Opcija 1\">Opcija 1</OPTION><OPTION value=\"Opcija 2\">Opcija 2</OPTION></SELECT> <IMG class=options-fold-icon src=\"../img/designer/widget_open.gif\"> \n"+
	"<DIV class=option-definer>\n"+
	"<DIV class=field-delete><INPUT class=form-fieldset-label value=\"Opcija 1\"> <IMG class=option-delete-icon src=\"../img/designer/delete-icon.gif\"> </DIV>\n"+
	"<DIV class=field-add><INPUT class=form-fieldset-label value=\"Opcija 2\"> <IMG class=option-add-icon src=\"../img/designer/add_icon.gif\"> </DIV></DIV></DIV><SPAN class=resizer></SPAN></SPAN></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV style=\"BACKGROUND-COLOR: rgb(255,255,255)\" class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV class=dropdiv></DIV>\n"+
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n"+
	"<DIV class=dropdiv-row>\n"+
	"<DIV class=dropdiv></DIV>\n"+ 
	"<DIV class=dropdiv-control><IMG class=dropdiv-plus-icon src=\"http://localhost:8080/efsa/img/designer/help_plus-icon.gif\"> <IMG class=dropdiv-minus-icon src=\"http://localhost:8080/efsa/img/designer/icon_minus.png\"> </DIV></DIV>\n";
		System.out.println(tidyHtml(xx));
	}
	
	private static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
}
