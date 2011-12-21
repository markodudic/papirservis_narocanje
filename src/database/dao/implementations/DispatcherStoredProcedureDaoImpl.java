package database.dao.implementations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.classes.objects.Document;
import org.classes.objects.Subject;
import org.classes.objects.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;

import database.dao.interfaces.DispatcherStoredProcedureDao;

public class DispatcherStoredProcedureDaoImpl implements DispatcherStoredProcedureDao {
	private static Log log = LogFactory.getLog(DispatcherStoredProcedureDaoImpl.class); 

	private JdbcTemplate jdbcTemplate;

	class GetDocuments extends StoredProcedure {
		private static final String getDocuments = "HYp_LDE_CustomDataView_1";
		private static final String DOCUMENTTYPE = "@DocumentType_FullID";
		private static final String SORTORDER = "@SortOrder";
		private static final String SORTTYPE = "@SortType";
		private static final String SORTSTRING = "@SortString";

		public GetDocuments(DataSource dataSource) {
			super(dataSource, getDocuments);
			declareParameter(new SqlReturnResultSet("rs", new DocumentsMapper()));
			declareParameter(new SqlParameter(DOCUMENTTYPE, Types.VARCHAR));
			declareParameter(new SqlParameter(SORTORDER, Types.NUMERIC));
			declareParameter(new SqlParameter(SORTTYPE, Types.VARCHAR));
			declareParameter(new SqlParameter(SORTSTRING, Types.VARCHAR));
			compile();
		}
	}

	
	class GetVehicles extends StoredProcedure {
		private static final String getVehicles = "dbo.HYpc_SubjectVehicle_ListGet";
		private static final String ORDERCOLUMN = "@OrderColumn";
		private static final String SEARCHSTRING = "@SearchString_Infix";
		private static final String MAXRECORDCOUNT = "@MaxRecordCount";
		private static final String MATCHESCOUNT = "@MatchesCount";
         
		public GetVehicles(DataSource dataSource) {
			super(dataSource, getVehicles);
			declareParameter(new SqlReturnResultSet("rs", new SubjectsMapper()));
			declareParameter(new SqlParameter(ORDERCOLUMN, Types.NULL));
			declareParameter(new SqlParameter(SEARCHSTRING, Types.NULL));
			declareParameter(new SqlParameter(MAXRECORDCOUNT, Types.NULL));
			declareParameter(new SqlParameter(MATCHESCOUNT, Types.INTEGER));
			compile();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public User LogIn(String username, String password) {
    	User user = new User();
		try {
			user = (User) this.jdbcTemplate.queryForObject(
				    "SELECT * FROM uporabniki WHERE aktiven=1 and uporabnisko_ime = ? and geslo = ?",
				    new Object[]{username,password},
				    new RowMapper() {
				        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				        	User user = new User();
				        	user.setId(rs.getString("sif_upor"));
				        	user.setName(rs.getString("ime_in_priimek"));
				        	user.setSurname("");
				        	user.setEnota(rs.getString("sif_enote"));
				        	user.setStatus("0");
				            return user;
				        }
				    });
			
		} catch (DataAccessException ex) {
			System.out.println("Exception:" + ex);
			ex.printStackTrace();

			user.setStatus("1");
		}
		
		return user;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection GetSubjects(String userId, String enotaId) {
	    return this.jdbcTemplate.query( 
	    		"SELECT sif_str, s.naziv, s.naslov, s.posta, s.kraj, telefon, kont_os, osnovna, kol_os, k.naziv kupec, sk.tekst " +
				"FROM (SELECT stranke.*  " +
				"	FROM stranke, (SELECT sif_str, max(zacetek) datum FROM stranke group by sif_str ) zadnji " + 
				"	WHERE stranke.sif_str = zadnji.sif_str and  " +
				"     	stranke.zacetek = zadnji.datum) s,   " +
				"	(SELECT osnovna.*  " +
				"		FROM osnovna, (SELECT sif_os, max(zacetek) datum FROM osnovna group by sif_os ) zadnji1 " + 
				"		WHERE osnovna.sif_os = zadnji1.sif_os and  " +
				"		      osnovna.zacetek = zadnji1.datum) o,  " +
				"	kupci k, skup sk   " +
				"where s.sif_os = o.sif_os and k.sif_kupca = s.sif_kupca and " +  
				"	k.skupina = sk.skupina  and k.blokada = 0  and " +
				"	k.potnik = ? and " +
				"	k.sif_enote = ?  " +
				"ORDER BY s.naziv",
	    		new Object[]{userId,enotaId},
	    		new SubjectsMapper());
	}

	@SuppressWarnings("rawtypes")
	private static final class SubjectsMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	        Subject subject = new Subject();
	        subject.setSifra(rs.getString("sif_str"));
	        subject.setNaziv(rs.getString("naziv"));
	        subject.setKol_osnovna(rs.getString("kol_os"));
	        subject.setKontOseba(rs.getString("kont_os"));
	        subject.setKraj(rs.getString("kraj"));
	        subject.setKupec(rs.getString("kupec"));
	        subject.setNaslov(rs.getString("naslov"));
	        subject.setOsnovna(rs.getString("osnovna"));
	        subject.setPosta(rs.getString("posta"));
	        subject.setSkupina(rs.getString("tekst"));
	        subject.setTelefon(rs.getString("telefon"));
	        
	        return subject;
	    }
	}
	
	
	
	public Object GetDocuments(String documentType, String sortOrder, String sortType, String sortString) {
		try {
			GetDocuments proc = new GetDocuments(getJdbcTemplate().getDataSource());
			Map results = proc.execute(documentType, sortOrder, sortType, sortString);
			List pagesRaw = (List) results.get("rs");
			return pagesRaw;
		} catch (DataAccessException ex) {
			System.out.println("Exception:" + ex);
			ex.printStackTrace();
		}
		
		return null;
	}

	

	public Object GetVehicles(String orderColumn, String searchString_Infix, Integer maxRecordCount, int matchesCount) {
		try {
			GetVehicles proc = new GetVehicles(getJdbcTemplate().getDataSource());
			Map results = proc.execute(orderColumn, searchString_Infix, maxRecordCount, matchesCount);
			List pagesRaw = (List) results.get("rs");
			return pagesRaw;
		} catch (DataAccessException ex) {
			System.out.println("Exception:" + ex);
			ex.printStackTrace();
		}
		
		return null;
	}
	
	
	protected static final class DocumentsMapper implements RowMapper {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Document document = new Document();
			document.setDobavitelj(rs.getString("Partner02_Name").trim());
			document.setNaslov(rs.getString("Partner02_Address2"));
			document.setNarocilo(rs.getString("GenericText01")+"/"+rs.getString("GenericText02"));
			document.setEvl(rs.getString("DocumentID"));
			document.setDatum_narocila(rs.getString("Date01"));
			document.setDatum_izvedbe(rs.getString("Date02"));
			document.setVrsta_odpadka(rs.getString("TradeUnitName"));
			document.setKlasifikacijska_stevilka(rs.getString("GenericData01"));
			document.setKolicina(rs.getInt("Quantity"));
			document.setPlacnik(rs.getString("Buyer01_Name").trim());
			document.setKontakt(rs.getString("Buyer01_ReferenceNumber01")+" "+rs.getString("PhoneNumber")+" "+rs.getString("FaxNumber")+" "+rs.getString("Email"));
			document.setOpomba(rs.getString("Partner03_GenericText02"));
			document.setStatus(rs.getString("Status"));
			document.setVozilo(rs.getString("Partner03_GenericText04")==null?"":rs.getString("Partner03_GenericText04").trim());
			return document;
		}
	}

 
    protected static final class LogInMapper implements RowMapper {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("UserID"));
			user.setName(rs.getString("Name"));
			user.setSurname(rs.getString("Surname"));
			user.setStatus(rs.getString("ExitStatus"));
			log.debug("**** UserID="+rs.getString("UserID"));
			return user;
		}
	}    
    
	protected static final class AssignResponseMapper implements RowMapper {

		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("ExitStatus");
		}
	}
	
	/**
	 * Spring DI for the datasource. instantiates the JdbcTemplate
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

}
