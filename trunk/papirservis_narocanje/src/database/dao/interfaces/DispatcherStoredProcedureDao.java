package database.dao.interfaces;

import org.classes.objects.Document;
import org.json.simple.JSONObject;

public interface DispatcherStoredProcedureDao {

	public abstract Object LogIn(String username, String password);
	public abstract Object GetDocuments(String documentType, String sortOrder, String sortType, String sortString);
	public abstract Object GetSubjects(String userId, String enotaId);
	public abstract Object GetMaterials();
	public abstract Object GetUsers();
	public abstract int AddOrder(JSONObject jdata);
	

}