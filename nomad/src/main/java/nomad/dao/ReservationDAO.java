package nomad.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nomad.beans.Reservation;

public class ReservationDAO {

	private Map<String, Reservation> reservations = new HashMap<>();
	
	public ReservationDAO() {
		
	}
	
	public ReservationDAO(String contextPath) {
		loadReservations(contextPath);
	}
	
	private void loadReservations(String contextPath) {
		//TODO
	}
	
	public Reservation find(String id) {
		return reservations.containsKey(id) ? reservations.get(id) : null;
	}
	
	public Collection<Reservation> findAll(){
		return reservations.values();
	}
}
