package database.dao.implementations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.classes.objects.Material;
import org.classes.objects.Order;
import org.classes.objects.Subject;
import org.classes.objects.User;
import org.json.simple.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import database.dao.interfaces.DispatcherStoredProcedureDao;

public class DispatcherStoredProcedureDaoImpl implements DispatcherStoredProcedureDao {
	private static Log log = LogFactory.getLog(DispatcherStoredProcedureDaoImpl.class); 

	private JdbcTemplate jdbcTemplate;


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public User LogIn(String username, String password) {
    	User user = new User();
		try {
			user = (User) this.jdbcTemplate.queryForObject(
				    "SELECT * FROM uporabniki WHERE aktiven=1 and narocila>0 and uporabnisko_ime = ? and geslo = ?",
				    new Object[]{username,password},
				    new RowMapper() {
				        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				        	User user = new User();
				        	user.setId(rs.getString("sif_upor"));
				        	user.setName(rs.getString("ime_in_priimek"));
				        	user.setSurname("");
				        	user.setEnota(rs.getString("sif_enote"));
				        	user.setStatus("0");
				        	user.setSif_kupca(rs.getString("sif_kupca"));
				        	user.setNarocila(rs.getString("narocila"));
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
	public Collection GetSubjects(String sif_kupca, String narocila) {	
		return this.jdbcTemplate.query( 
	    		"SELECT sif_str, s.naziv, s.naslov, s.posta, s.kraj, telefon, kont_os, s.opomba, osnovna, kol_os, k.naziv kupec, potnik.ime_in_priimek as potnik, sk.tekst " +
				"FROM (SELECT stranke.*  " +
				"	FROM stranke, (SELECT sif_str, max(zacetek) datum FROM stranke group by sif_str ) zadnji " + 
				"	WHERE stranke.sif_str = zadnji.sif_str and  " +
				"     	stranke.zacetek = zadnji.datum) s,   " +
				"	(SELECT osnovna.*  " +
				"		FROM osnovna, (SELECT sif_os, max(zacetek) datum FROM osnovna group by sif_os ) zadnji1 " + 
				"		WHERE osnovna.sif_os = zadnji1.sif_os and  " +
				"		      osnovna.zacetek = zadnji1.datum) o,  " +
				"	skup sk, kupci k   " +
				"left join uporabniki as potnik on (k.potnik = potnik.sif_upor) " +			      
				"where s.sif_os = o.sif_os and k.sif_kupca = s.sif_kupca and " +  
				"	k.skupina = sk.skupina  and k.blokada = 0  and " +
				"	(((k.sif_kupca = ?) and (?=1)) OR (?=2)) " +
				"ORDER BY s.naziv",
	    		new Object[]{sif_kupca,narocila,narocila},
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
	        subject.setPotnik(rs.getString("potnik"));
	        subject.setNaslov(rs.getString("naslov"));
	        subject.setOsnovna(rs.getString("osnovna"));
	        subject.setPosta(rs.getString("posta"));
	        subject.setSkupina(rs.getString("tekst"));
	        subject.setTelefon(rs.getString("telefon"));
	        subject.setOpomba(rs.getString("opomba"));
	        
	        return subject;
	    }
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection GetMaterials() {
	    return this.jdbcTemplate.query( 
	    		"SELECT materiali.*  " +
				"FROM materiali, (SELECT koda, max(zacetek) datum FROM materiali group by koda ) zadnji1 " + 
				"WHERE materiali.koda = zadnji1.koda and  " +
				"	      materiali.zacetek = zadnji1.datum " +
				"ORDER BY koda",
	    		new Object[]{},
	    		new MaterialsMapper());
	}

	@SuppressWarnings("rawtypes")
	private static final class MaterialsMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	Material material = new Material();
	    	material.setKoda(rs.getString("koda"));
	    	material.setMaterial(rs.getString("material"));
	        
	        return material;
	    }
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection GetUsers(String sif_kupca, String narocila) {
	    return this.jdbcTemplate.query( 
	    		"SELECT * " +
	    		"FROM uporabniki " +
				"WHERE (((sif_kupca = ?) and (?=1)) OR (?=2)) " +
	    		"ORDER BY ime_in_priimek",
	    		new Object[]{sif_kupca,narocila,narocila},
	    		new UsersMapper());
	}

	@SuppressWarnings("rawtypes")
	private static final class UsersMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	        User user = new User();
	        user.setId(rs.getString("sif_upor"));
	        user.setName(rs.getString("ime_in_priimek"));
	        
	        return user;
	    }
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int AddOrder(JSONObject jdata) {
		String stranka = (String) jdata.get("stranka");
		String material = (String) jdata.get("material");
		String narocil = (String) jdata.get("narocil");
		String datum = (String) jdata.get("datum");
		String kolicina = ((String) jdata.get("kolicina")).equals("")?"0":(String) jdata.get("kolicina");
		String opomba = (String) jdata.get("opomba");
		String [] tokens = datum.split("\\.");
		log.debug(datum+"-"+tokens+"-"+tokens.length);
		String dobLeto = tokens[2];
		String datumUTC = tokens[2]+"-"+tokens[1]+"-"+tokens[0];
		int nextId = GetNextId(dobLeto);
		Map kupec = GetKupec(stranka);
		
		return this.jdbcTemplate.update( 
	    		"insert into dob" + dobLeto + " (st_dob, pozicija, datum, sif_str, sif_kupca, koda, kolicina, opomba, skupina, uporabnik) " +
	    		"values (?,?,?,?,?,?,?,?,?,?)",
	    		new Object[]{nextId, 1, datumUTC, stranka, kupec.get("sif_kupca"), material, kolicina, opomba, kupec.get("skupina"), narocil});
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int GetNextId(String dobLeto) {
			this.jdbcTemplate.update("update dob_bianco set st_dob = st_dob + 1 where id = 'dob" + dobLeto + "'");
			return this.jdbcTemplate.queryForInt("SELECT max(st_dob) cnt FROM `dob_bianco` where id = 'dob" + dobLeto + "'");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map GetKupec(String stranka) {
			return this.jdbcTemplate.queryForMap(
					"SELECT distinct kupci.sif_kupca, skupina "+
					"FROM kupci left join (SELECT stranke.*  " +
					"						FROM stranke, (SELECT sif_str, max(zacetek) datum FROM stranke group by sif_str ) zadnji " + 
					"						WHERE stranke.sif_str = " + stranka + " and " +
					"							stranke.sif_str = zadnji.sif_str and  " +
					"     						stranke.zacetek = zadnji.datum) s " +
					"ON (kupci.sif_kupca = s.sif_kupca) "+
					"WHERE s.sif_str = " + stranka);
	}	


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection GetOrders(String sif_kupca, String narocila, String sortOrder, String sortType, String sortString) {
		Calendar toDay = Calendar.getInstance();
		int year = toDay.get(Calendar.YEAR);
		
	    return this.jdbcTemplate.query( 
	    		"select st_dob, DATE_FORMAT(dob" + year + ".datum, '%d.%m.%Y') as datum, stranka, kupci.naziv as kupec, potnik.ime_in_priimek as potnik, material, kolicina, dob" + year + ".opomba as opomba, upor.ime_in_priimek as narocil " +
				"from dob" + year + " " +
				"left join kupci on (dob" + year + ".sif_kupca = kupci.sif_kupca) " +
				"left join (SELECT materiali.*   " +
				"				FROM materiali, (SELECT koda, max(zacetek) datum FROM materiali group by koda ) zadnji1 " + 
				"					WHERE materiali.koda = zadnji1.koda and   " +
				"					      materiali.zacetek = zadnji1.datum) as m on (dob" + year + ".koda = m.koda) " +
				"left join uporabniki as upor on (dob" + year + ".uporabnik = upor.sif_upor) " +			      
				"left join uporabniki as potnik on (kupci.potnik = potnik.sif_upor) " +			      
				"where obdelana = 0 and " +
				"    (((kupci.sif_kupca = ?) and (?=1)) OR (?=2)) " +
				"order by " + sortString + " " + sortType,
	    		new Object[]{sif_kupca,narocila,narocila},
	    		new OrdersMapper());
	}
	
	
	protected static final class OrdersMapper implements RowMapper {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Order order = new Order();
					
			order.setStDob(rs.getString("st_dob"));
			order.setDatum(rs.getString("datum"));
			order.setStranka(rs.getString("stranka"));
			order.setKupec(rs.getString("kupec"));
			order.setMaterial(rs.getString("material"));
			order.setKolicina(rs.getString("kolicina"));
			order.setOpomba(rs.getString("opomba"));
			order.setNarocil(rs.getString("narocil"));
			order.setPotnik(rs.getString("potnik"));
			return order;
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
