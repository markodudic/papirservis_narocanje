package database.dao.interfaces;

import org.json.simple.JSONObject;

public interface DispatcherStoredProcedureDao {

	public abstract Object LogIn(String username, String password);
	public abstract Object GetSubjects(String userId, String enotaId);
	public abstract Object GetMaterials();
	public abstract Object GetUsers(String userId, String enotaId);
	public abstract int AddOrder(JSONObject jdata);
	public abstract Object GetOrders(String userId, String enotaId, String sortOrder, String sortType, String sortString);
	public abstract Object GetPotnik(String sif_str);
	public abstract Object GetUser(String narocil);
	

}