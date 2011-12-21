package database.dao.interfaces;

import org.classes.objects.Document;

public interface DispatcherStoredProcedureDao {

	public abstract Object LogIn(String username, String password);
	public abstract Object GetDocuments(String documentType, String sortOrder, String sortType, String sortString);
	public abstract Object GetSubjects(String userId, String enotaId);
	public abstract Object GetVehicles(String orderColumn, String searchString_Infix, Integer maxRecordCount, int matchesCount);
	

}