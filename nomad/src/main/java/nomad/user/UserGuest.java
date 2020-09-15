package nomad.user;

import java.io.Serializable;
import java.util.ArrayList;

public class UserGuest extends UserBase implements Serializable
{
	private static final long serialVersionUID = -168036936931749782L;
	private ArrayList<String> rented;
	private ArrayList<String> reservations;

	public UserGuest(String username, String password, String name, String surname, Sex sex)
	{
		super(username, password, name, surname, sex, UserType.GUEST);
	}

	public ArrayList<String> getRented()
	{
		return rented;
	}

	public void setRented(ArrayList<String> rented)
	{
		this.rented = rented;
	}

	public ArrayList<String> getReservations()
	{
		return reservations;
	}

	public void setReservations(ArrayList<String> reservations)
	{
		this.reservations = reservations;
	}
	
	public void addReservation(String reservationId)
	{
		this.reservations.add(reservationId);
	}
	
	public void removeReservation(String reservationId)
	{
		this.reservations.remove(reservationId);
	}

	// TODO(Jovan): Getter and setters for lists?
}
