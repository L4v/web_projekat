package nomad.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nomad.beans.Apartment;

public class ApartmentDAO {

	private Map<String, Apartment> apartments = new HashMap<>();
	
	public ApartmentDAO() {
		
	}
	
	public ApartmentDAO(String contextPath) {
		loadApartments(contextPath);
	}
	
	private void loadApartments(String contextPath) {
		//TODO
	}
	
	public Apartment find(String id) {
		return apartments.containsKey(id) ? apartments.get(id) : null;
	}
	
	public Collection<Apartment> findAll(){
		return apartments.values();
	}

	
}
