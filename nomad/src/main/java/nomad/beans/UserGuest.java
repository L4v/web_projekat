package nomad.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import nomad.beans.enums.Sex;
import nomad.beans.enums.UserType;

public class UserGuest extends UserBase implements Serializable {
	private static final long serialVersionUID = -168036936931749782L;
	private HashMap<String, Apartment> rented;
	private ArrayList<Reservation> reservations;

	public UserGuest(String username, String password, String name, String surname, Sex sex) {
		super(username, password, name, surname, sex, UserType.GUEST);
	}

	public HashMap<String, Apartment> getRented() {
		return rented;
	}

	public void setRented(HashMap<String, Apartment> rented) {
		this.rented = rented;
	}

	public ArrayList<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}

	// TODO(Jovan): Get i set za rented mapa ili lista?
	// TODO(Jovan): Get i set sa reservations
}
