package org.classes.controllers;

import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.classes.login.Scrambler;
import org.classes.objects.Document;
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

import database.dao.interfaces.DispatcherStoredProcedureDao;



@Controller
@RequestMapping(value="")
public class DispatcherController {
	private static Log log = LogFactory.getLog(DispatcherController.class); 
	
	
	//private Validator customPageValidator;
	private String contextPath = "";
	private String documentType = "";
	private String receiver = "";
	private String sender = "";
	private String jobID = "";
	private String refreshItems = "";
	private String refreshSubjects = "";
	private String refreshAllDocuments = "";
	private String refreshDocument = "";

	HashMap hm = new HashMap();

	@Autowired
	DispatcherStoredProcedureDao dispatcherStoredProcedure;

	@Autowired
	public void initialize() {
	}	
	

	@Autowired
	public DispatcherController(Validator validator) {
	    hm.put("dobavitelj", "Partner02_Name");
	    hm.put("naslov", "Partner02_Address2");
	    hm.put("narocilo", "GenericText01, GenericText02");
	    hm.put("evl", "DocumentID");
	    hm.put("datum_narocila", "Date01"); 
	    hm.put("datum_izvedbe", "Date02"); 
	    hm.put("vrsta_odpadka", "TradeUnitName"); 
	    hm.put("klasifikacijska_stevilka", "GenericData01"); 
	    hm.put("kolicina", "Quantity"); 
	    hm.put("placnik", "Buyer01_Name"); 
	    hm.put("kontakt", "Buyer01_ReferenceNumber01, PhoneNumber, FaxNumber, Email"); 
	    hm.put("opomba", "Partner03_GenericText02"); 
	} 



	
	@RequestMapping(value="/dispatcher", method=RequestMethod.GET)
	public ModelAndView GetDocuments(HttpServletResponse response) throws Exception {
		log.debug("**** GetDocuments");
		setChacheHeaders(response);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("dispatcher");
		//mav.addObject("page", customPageStoredProcedure.GetCustomPages());
		return mav;
	}

	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView Index(HttpServletResponse response) throws Exception {
		log.debug("**** index");
		setChacheHeaders(response);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		//mav.addObject("page", customPageStoredProcedure.GetCustomPages());
		return mav;
	}
	
	private static final Pattern p = Pattern.compile("\\\\u([0-9A-F]{4})");
	public static String U2U(String s) {
		String res = s;
		Matcher m = p.matcher(res);
		while(m.find()) {
			res = res.replaceAll("\\" + m.group(0),	Character.toString((char)Integer.parseInt(m.group( 1), 16)));
		}
	return res;
	}
	
	List documentsOld = new ArrayList();
	String dataArrayOld = "";
	@RequestMapping(value="/dispatcher-table", method=RequestMethod.GET)
	public ModelAndView GetDocumentsTable(
			@RequestParam String page, 
			@RequestParam String perpage, 
			@RequestParam String sorton, 
			@RequestParam String sortby, 
			@RequestParam String sortorder, 
			@RequestParam String seltext, 
			@RequestParam String selvozilo, 
			@RequestParam String selstatus, 
			@RequestParam String datumod, 
			@RequestParam String datumdo, 
			HttpServletResponse response) throws Exception {
		log.debug("**** GetDocuments="+page+" "+perpage+" "+sorton+" "+sortby+" "+sortorder);
		log.debug("**** GetDocuments="+seltext + " " + selvozilo+" "+selstatus+" "+datumod+" "+datumdo);
		
		seltext = U2U(seltext.replace("%u", "\\u"));
		seltext = URLDecoder.decode(seltext, "UTF-8");
		setChacheHeaders(response);
		
		//premapiram nazive kolon ce je sortOrder=0, za sortiranje ce kliknes na header vrstice
		if (Integer.parseInt(sortorder) == 0) {
			if (hm.containsKey(sorton))
				sorton = (String) hm.get(sorton);
		}
		log.debug("**** sorton="+sorton);
		
		List documents = (List) dispatcherStoredProcedure.GetDocuments(documentType, sortorder, sortby, sorton);
		
		int start = (Integer.parseInt(page)-1) * Integer.parseInt(perpage);
		int end = start + Integer.parseInt(perpage);
		//if (end > documents.size()) end = documents.size();
		
	    JSONArray dataArray = new JSONArray();
	    int cur = 0;
	    int curSet = 0;
	    for (int i=0; i<documents.size(); i++) {
	    	Document document = (Document) documents.get(i);
			Calendar calOd = Calendar.getInstance();
			Calendar calDo = Calendar.getInstance();
			Calendar cal = Calendar.getInstance();
			
			if (!datumod.equals("")) {
				int d  = Integer.parseInt(datumod.substring(0,2));
				int m  = Integer.parseInt(datumod.substring(3,5));
				int y  = Integer.parseInt(datumod.substring(6,10));
			    calOd.set(y, m, d);
			}
			if (!datumdo.equals("")) {
				int d  = Integer.parseInt(datumdo.substring(0,2));
				int m  = Integer.parseInt(datumdo.substring(3,5));
				int y  = Integer.parseInt(datumdo.substring(6,10));
			    calDo.set(y, m, d);
			}
			if (document.getDatum_narocila()!=null) {
				int d  = Integer.parseInt(document.getDatum_narocila().substring(0,2));
				int m  = Integer.parseInt(document.getDatum_narocila().substring(3,5));
				int y  = Integer.parseInt(document.getDatum_narocila().substring(6,10));
			    cal.set(y, m, d);
			}
			
			if ((seltext.equals("") || 
				 document.getDobavitelj().toUpperCase().indexOf(seltext.toUpperCase()) > -1 ||
				 document.getNaslov().toUpperCase().indexOf(seltext.toUpperCase()) > -1 ||
				 document.getNarocilo().toUpperCase().indexOf(seltext.toUpperCase()) > -1 ||
				 document.getEvl().toUpperCase().indexOf(seltext.toUpperCase()) > -1 ||
				 document.getVrsta_odpadka().toUpperCase().indexOf(seltext.toUpperCase()) > -1 ||
				 document.getKlasifikacijska_stevilka().toUpperCase().indexOf(seltext.toUpperCase()) > -1 ||
				 document.getPlacnik().toUpperCase().indexOf(seltext.toUpperCase()) > -1 ||
				 document.getKontakt().toUpperCase().indexOf(seltext.toUpperCase()) > -1 ||
				 document.getOpomba().toUpperCase().indexOf(seltext.toUpperCase()) > -1) &&
				(selstatus.equals("-1") || 
				 document.getStatus().equals(selstatus)) &&
				(selvozilo.equals("-1") || 
				 document.getVozilo().equals(selvozilo)) &&
				(datumod.equals("") || 
				 cal.after(calOd)) &&
				(datumdo.equals("") || 
				 cal.before(calDo))
				) {
					if ((cur >= start) && (cur <= end) && (curSet <= Integer.parseInt(perpage))) {
						dataArray.add(curSet, documentJSON(document));
						curSet++;
					}
					cur++;
			}
			//if (curSet >= Integer.parseInt(perpage)) break;
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

	
	public JSONObject documentJSON(Document document) {
		JSONObject data = new JSONObject();
		try {
			data.put("dobavitelj", document.getDobavitelj()==null?"":document.getDobavitelj());
			data.put("naslov", document.getNaslov()==null?"":document.getNaslov());
			data.put("narocilo", document.getNarocilo()==null?"":document.getNarocilo());
			data.put("evl", document.getEvl()==null?"":document.getEvl());
			data.put("datum_narocila", document.getDatum_narocila()==null?"":document.getDatum_narocila());
			data.put("datum_izvedbe", document.getDatum_izvedbe()==null?"":document.getDatum_izvedbe());
			data.put("vrsta_odpadka", document.getVrsta_odpadka()==null?"":document.getVrsta_odpadka());
			data.put("klasifikacijska_stevilka", document.getKlasifikacijska_stevilka()==null?"":document.getKlasifikacijska_stevilka());
			data.put("kolicina", document.getKolicina());
			data.put("placnik", document.getPlacnik()==null?"":document.getPlacnik());
			data.put("kontakt", document.getKontakt()==null?"":document.getKontakt());
			data.put("opomba", document.getOpomba()==null?"":document.getOpomba());
			data.put("status", document.getStatus()==null?"":document.getStatus());
			data.put("vozilo", document.getVozilo()==null?"":document.getVozilo());
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
		String userId = (String) session.getAttribute("id");
		String sifEnote = (String) session.getAttribute("enota");

		List subjects = (List) dispatcherStoredProcedure.GetSubjects(userId, sifEnote);
		
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
	
	@RequestMapping(value="/vehicles", method=RequestMethod.GET)
	public ModelAndView vehicles(HttpServletResponse response) throws Exception {
		log.debug("**** vehicles");
		setChacheHeaders(response);
		
		List subjects = (List) dispatcherStoredProcedure.GetVehicles(null, null, null, 0);
		
		JSONArray dataArray = new JSONArray();
		for (int i=0; i<subjects.size(); i++) {
			Subject subject = (Subject) subjects.get(i);
			dataArray.add(i, subjectsJSON(subject));
		}
		//log.debug("*** POST /JSONArray:" + dataArray.toString());
		
		return ajaxResult(dataArray.toString(),null);
	}	
	
	public JSONObject subjectsJSON(Subject subject) {
		JSONObject data = new JSONObject();
		data.put("sifra", subject.getSifra());
		data.put("naziv", subject.getNaziv());
		data.put("kol_os", subject.getKol_osnovna());
		data.put("kont_os", subject.getKontOseba());
		data.put("kraj", subject.getKraj());
		data.put("kupec", subject.getKupec());
		data.put("naslov", subject.getNaslov());
		data.put("osnovna", subject.getOsnovna());
		data.put("posta", subject.getPosta());
		data.put("skupina", subject.getSkupina());
		data.put("telefon", subject.getTelefon());
        
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
	@RequestMapping(value="/confirm", method=RequestMethod.POST)
	public RedirectView confirm(@RequestParam String confirmData, 
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		log.debug("**** confirm="+confirmData);
		setChacheHeaders(response);
		
		JSONObject jdata = (JSONObject)new JSONParser().parse(new StringReader(confirmData));
		for (Iterator iterator = jdata.keySet().iterator(); iterator.hasNext(); ) {
			Object key = iterator.next();
			Object val = jdata.get(key);
			int state = 0;
			
			if (val.toString().equals("-1")) {
				state = 2;
			}
			
		}
		
		RedirectView rv = new RedirectView();
		rv.setUrl(contextPath+"/dispatcher");
		return rv;	
	}	
	

	
	@RequestMapping(value="/pdf", method=RequestMethod.POST)
	public void pdf(@RequestParam("form_html") String html,
			@RequestParam("form_title") String title,
			HttpServletRequest request,
			HttpServletResponse response
		) {
		log.debug("**** Pdf/");
		String realPath = request.getSession().getServletContext().getRealPath("/").replaceAll("\\\\", "/");
		PdfCreator.createPdf(html,title,realPath,request,response);
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
	
	public String getDocumentType() {
		return documentType;
	}


	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	

	public String getReceiver() {
		return receiver;
	}


	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}


	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}


	public String getJobID() {
		return jobID;
	}


	public void setJobID(String jobID) {
		this.jobID = jobID;
	}


	public String getRefreshItems() {
		return refreshItems;
	}


	public void setRefreshItems(String refreshItems) {
		this.refreshItems = refreshItems;
	}


	public String getRefreshSubjects() {
		return refreshSubjects;
	}


	public void setRefreshSubjects(String refreshSubjects) {
		this.refreshSubjects = refreshSubjects;
	}


	public String getRefreshAllDocuments() {
		return refreshAllDocuments;
	}


	public void setRefreshAllDocuments(String refreshAllDocuments) {
		this.refreshAllDocuments = refreshAllDocuments;
	}


	public String getRefreshDocument() {
		return refreshDocument;
	}


	public void setRefreshDocument(String refreshDocument) {
		this.refreshDocument = refreshDocument;
	}


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
