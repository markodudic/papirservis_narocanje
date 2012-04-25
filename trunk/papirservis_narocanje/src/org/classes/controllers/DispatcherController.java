package org.classes.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.classes.login.Scrambler;
import org.classes.objects.Material;
import org.classes.objects.Order;
import org.classes.objects.Subject;
import org.classes.objects.User;
import org.classes.report.PdfCreator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;

import database.dao.interfaces.DispatcherStoredProcedureDao;



@Controller
@RequestMapping(value="")
public class DispatcherController {
	private static Log log = LogFactory.getLog(DispatcherController.class); 
	
	
	//private Validator customPageValidator;
	private String contextPath = "";


	@Autowired
	DispatcherStoredProcedureDao dispatcherStoredProcedure;

	@Autowired
	public void initialize() {
	}	
	

	@Autowired
	public DispatcherController(Validator validator) {

	} 



	
	@RequestMapping(value="/orders", method=RequestMethod.GET)
	public ModelAndView GetDocuments(HttpServletResponse response) throws Exception {
		setChacheHeaders(response);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("dispatcher");
		return mav;
	}

	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView Index(HttpServletResponse response) throws Exception {
		log.debug("**** index");
		setChacheHeaders(response);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		return mav;
	}
	
	String dataArrayOld = "";
	@RequestMapping(value="/orders_table", method=RequestMethod.GET)
	public ModelAndView GetOrdersTable(
					@RequestParam String page, 
					@RequestParam String perpage, 
					@RequestParam String sorton, 
					@RequestParam String sortby, 
					@RequestParam String sortorder, 
					HttpServletRequest request, 
					HttpServletResponse response) throws Exception {
		log.debug("**** GetOrders="+sortorder+"-"+sortby+"-"+sorton);
		
		HttpSession session = request.getSession();
		String sif_kupca = (String) session.getAttribute("sif_kupca");
		String narocila = (String) session.getAttribute("narocila");

		List orders = (List) dispatcherStoredProcedure.GetOrders(sif_kupca, narocila, sortorder, sortby, sorton);
		
		int start = (Integer.parseInt(page)-1) * Integer.parseInt(perpage);
		int end = start + Integer.parseInt(perpage);
		
	    JSONArray dataArray = new JSONArray();
	    int cur = 0;
	    int curSet = 0;
	    for (int i=0; i<orders.size(); i++) {
	    	Order order = (Order) orders.get(i);
			if ((cur >= start) && (cur <= end) && (curSet <= Integer.parseInt(perpage))) {
				dataArray.add(curSet, orderJSON(order));
				curSet++;
			}
			cur++;
		}
	         
		String returnData = "";
		if (dataArrayOld.equals(dataArray.toString())) {
			JSONArray dataArrayEmpty = new JSONArray();
			returnData = dataArrayEmpty.toString();
		} else {
			returnData = "{\"page\":\""+page+"\",\"total\":\""+cur+"\",\"data\":" + dataArray.toString() +"}";
			dataArrayOld = returnData;
		}
		
		log.debug("*** POST /JSONArray:" + dataArray.toString());
		return ajaxResult(returnData,null);
	}

	
	public JSONObject orderJSON(Order order) {
		JSONObject data = new JSONObject();
		try {
			data.put("st_dob", order.getStDob()==null?"":order.getStDob());
			data.put("datum", order.getDatum()==null?"":order.getDatum());
			data.put("stranka", order.getStranka()==null?"":order.getStranka());
			data.put("kupec", order.getKupec()==null?"":order.getKupec());
			data.put("potnik", order.getPotnik()==null?"":order.getPotnik());
			data.put("material", order.getMaterial()==null?"":order.getMaterial());
			data.put("kolicina", order.getKolicina()==null?"":order.getKolicina());
			data.put("opomba", order.getOpomba()==null?"":order.getOpomba());
			data.put("narocil", order.getNarocil()==null?"":order.getNarocil());
		} catch (Exception e) {
			log.debug(e);
		}
		return data;
	}

	
	@RequestMapping(value="/subjects", method=RequestMethod.GET)
	public ModelAndView subjects(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("**** subjects");
		setChacheHeaders(response);
		
		HttpSession session = request.getSession();
		String sif_kupca = (String) session.getAttribute("sif_kupca");
		String narocila = (String) session.getAttribute("narocila");

		List subjects = (List) dispatcherStoredProcedure.GetSubjects(sif_kupca, narocila);
		
		JSONArray dataArray = new JSONArray();
		for (int i=0; i<subjects.size(); i++) {
			Subject subject = (Subject) subjects.get(i);
			dataArray.add(i, subjectsJSON(subject));
		}
		log.debug("*** POST /JSONArray:" + dataArray.toString());
		
		OutputStreamWriter osw = null;
		String acceptEncoding = request.getHeader("Accept-Encoding");
		if (acceptEncoding != null && acceptEncoding.indexOf("gzip") >= 0) {
			response.setHeader("Content-Encoding", "gzip");
			osw = new OutputStreamWriter(new GZIPOutputStream(response.getOutputStream()), "utf-8");
		}
		osw.write(dataArray.toString());
		osw.flush();
		osw.close();
		
		//return ajaxResult(dataArray.toString(),null);
		return null;
	}	
	
	public JSONObject subjectsJSON(Subject subject) {
		JSONObject data = new JSONObject();
		data.put("sifra", subject.getSifra());
		data.put("naziv", subject.getNaziv());
		data.put("kol_os", subject.getKol_osnovna());
		data.put("kont_os", subject.getKontOseba());
		data.put("kraj", subject.getKraj());
		data.put("kupec", subject.getKupec());
		data.put("potnik", subject.getPotnik());
		data.put("naslov", subject.getNaslov());
		data.put("osnovna", subject.getOsnovna());
		data.put("posta", subject.getPosta());
		data.put("skupina", subject.getSkupina());
		data.put("telefon", subject.getTelefon());
		data.put("opomba", subject.getOpomba());
        
        return data;
	}

	
	@RequestMapping(value="/materials", method=RequestMethod.GET)
	public ModelAndView materials(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("**** materials");
		setChacheHeaders(response);
		
		List materials = (List) dispatcherStoredProcedure.GetMaterials();
		
		JSONArray dataArray = new JSONArray();
		for (int i=0; i<materials.size(); i++) {
			Material material = (Material) materials.get(i);
			dataArray.add(i, materialsJSON(material));
		}
		log.debug("*** POST /JSONArray:" + dataArray.toString());
		
		OutputStreamWriter osw = null;
		String acceptEncoding = request.getHeader("Accept-Encoding");
		if (acceptEncoding != null && acceptEncoding.indexOf("gzip") >= 0) {
			response.setHeader("Content-Encoding", "gzip");
			osw = new OutputStreamWriter(new GZIPOutputStream(response.getOutputStream()), "utf-8");
		}
		osw.write(dataArray.toString());
		osw.flush();
		osw.close();
		
		//return ajaxResult(dataArray.toString(),null);
		return null;
	}	
	
	public JSONObject materialsJSON(Material material) {
		JSONObject data = new JSONObject();
		data.put("koda", material.getKoda());
		data.put("material", material.getMaterial());
         
        return data;
	}

	
	@RequestMapping(value="/users", method=RequestMethod.GET)
	public ModelAndView users(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("**** users");
		setChacheHeaders(response);
		
		HttpSession session = request.getSession();
		String sif_kupca = (String) session.getAttribute("sif_kupca");
		String narocila = (String) session.getAttribute("narocila");

		List users = (List) dispatcherStoredProcedure.GetUsers(sif_kupca, narocila);
		
		JSONArray dataArray = new JSONArray();
		for (int i=0; i<users.size(); i++) {
			User user = (User) users.get(i);
			dataArray.add(i, usersJSON(user));
		}
		log.debug("*** POST /JSONArray:" + dataArray.toString());
		
		OutputStreamWriter osw = null;
		String acceptEncoding = request.getHeader("Accept-Encoding");
		if (acceptEncoding != null && acceptEncoding.indexOf("gzip") >= 0) {
			response.setHeader("Content-Encoding", "gzip");
			osw = new OutputStreamWriter(new GZIPOutputStream(response.getOutputStream()), "utf-8");
		}
		osw.write(dataArray.toString());
		osw.flush();
		osw.close();
		
		//return ajaxResult(dataArray.toString(),null);
		return null;
	}	
	
	public JSONObject usersJSON(User user) {
		JSONObject data = new JSONObject();
		data.put("id", user.getId());
		data.put("name", user.getName());
        
        return data;
	}

	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ModelAndView login(@RequestParam String logInData, 
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		log.debug("**** login="+logInData);
		setChacheHeaders(response);
		String j = Scrambler.decrypt(logInData);
		
		JSONObject jdata = (JSONObject)new JSONParser().parse(new StringReader(j));
		String username = (String)jdata.get("username");
		String password = (String)jdata.get("password");
		
		User user = (User) dispatcherStoredProcedure.LogIn(username, password);
		
		HttpSession session = request.getSession();
		session.setAttribute("id", user.getId());
		session.setAttribute("name", user.getName());
		session.setAttribute("surname", user.getSurname());
		session.setAttribute("enota", user.getEnota());
		session.setAttribute("status", user.getStatus());
		session.setAttribute("sif_kupca", user.getSif_kupca());
		session.setAttribute("narocila", user.getNarocila());
		
		JSONArray dataArray = new JSONArray();
		dataArray.add(userJSON(user));
		
		return ajaxResult(dataArray.toString(),null);
	}	
	
	public JSONObject userJSON(User user) {
		JSONObject data = new JSONObject();
		data.put("id", user.getId());
		data.put("name", user.getName());
		data.put("surname", user.getSurname());
		data.put("status", user.getStatus());
		return data;
	}

	@RequestMapping(value="/logout", method=RequestMethod.POST)
	public ModelAndView logout(HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		log.debug("**** logout");
		setChacheHeaders(response);
		
		HttpSession session = request.getSession();
		session.setAttribute("id", null);
		session.setAttribute("name", null);
		session.setAttribute("surname", null);
		session.setAttribute("status", 1);
		return null;
	}
	@RequestMapping(value="/confirm", method=RequestMethod.GET)
	public RedirectView confirm(@RequestParam String confirmData, 
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		log.debug("**** confirm="+confirmData);
		setChacheHeaders(response);
		
		JSONObject jdata = (JSONObject)new JSONParser().parse(new StringReader(confirmData));
		dispatcherStoredProcedure.AddOrder(jdata);
		
		RedirectView rv = new RedirectView();
		rv.setUrl(contextPath+"/orders");
		return rv;	
	}	
	 

	
	@RequestMapping(value="/pdf", method=RequestMethod.POST)
	public void pdf(@RequestParam("form_html") String html,
			@RequestParam("form_title") String title,
			HttpServletRequest request,
			HttpServletResponse response
		) {
		String realPath = request.getSession().getServletContext().getRealPath("/").replaceAll("\\\\", "/");
		log.debug("**** Pdf:"+title+"-"+html);
		PdfCreator.createPdf(html,title,realPath,request,response);
	}
	
	@RequestMapping(value="/xml", method=RequestMethod.POST)
	public void xml(@RequestParam("form_html") String xml,
			HttpServletRequest request,
			HttpServletResponse response
		) {
		log.debug("**** Xml:"+xml);

		response.setContentType("text/xml");
		response.setHeader("Content-disposition", "attachment; filename=form.xml");
		OutputStream out = null;
		try {
			out = response.getOutputStream();			
			out.write(xml.getBytes());
			out.flush();
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
	
	
	public String getContextPath() {
		return contextPath;
	}


	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}


/*	@RequestMapping(value="/**", method=RequestMethod.GET)
	public Object urlerr(HttpServletRequest request) {
		return showErrorMsg(request,"error.url_ne_obstaja");
	}	*/
	

	public ModelAndView ajaxResult(String okmsg,String errorMsg) {
		ModelAndView mav = new ModelAndView();
		if (okmsg!=null) {
			mav.addObject("result", okmsg);
		} else if (errorMsg!=null) {
			mav.addObject("result", errorMsg);
		}
		mav.setViewName("ajax-result");
		return mav;
	}

	@RequestMapping(value="/error-msg", method=RequestMethod.GET)
	public Object getErrorMsg(HttpServletRequest request) {
		String errorMsg = getSessionErrorMsg(request);
		log.debug("*** GET /errorMsg/");

		ModelAndView mav = new ModelAndView();
		log.debug(errorMsg);
		mav.addObject("result", errorMsg);
		mav.setViewName("ajax-result");
		return mav;
	}
	private ModelAndView showErrorMsg(HttpServletRequest request, String errorMsg) {
		setSessionErrorMsg(request, errorMsg);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error");
		return mav;
	}
	
	private void setSessionErrorMsg(HttpServletRequest request, String errorMsg) {
		request.getSession().setAttribute("error_message", errorMsg);
	}
	private String getSessionErrorMsg(HttpServletRequest request) {
		Object em = request.getSession().getAttribute("error_message");
		if (em == null) {
			return "";
		} else {
			return em.toString();
		}
	}
	
	/**
	 * Generira http response headerje, ki prepovejo brskalniku keširanje.
	 * To je še posebej pomembno pri Ajax klicih
	 * @param response
	 */
	private void setChacheHeaders(HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
	    response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
	    response.setDateHeader("Expires", 1);  
	}
	
}
