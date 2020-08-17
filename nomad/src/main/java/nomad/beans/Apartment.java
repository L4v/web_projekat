package nomad.beans;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import nomad.beans.enums.ApartmentStatus;
import nomad.beans.enums.ApartmentType;

public class Apartment implements Serializable
{
	// TODO(Jovan): Use ID's instead of objects
	private static final long serialVersionUID = -8563884059267108747L;
	private String id;
	private ApartmentType type;
	private int noRooms;
	private int noGuests;
	private Location location;
	private ArrayList<Date> rentDates;
	private HashMap<Date, Boolean> availableDates;
	private UserHost host;
	// NOTE(Jovan): {username: comment}
	private HashMap<String, Comment> comments;
	// NOTE(Jovan): Image urls
	private ArrayList<String> images;
	private double price;
	private LocalTime checkinTime;
	private LocalTime checkoutTime;
	private ApartmentStatus status;
	private ArrayList<Amenity> amenities;
	private ArrayList<Reservation> reservations;

	public Apartment(String id, ApartmentType type, int noRooms, int noGuests, Location location, UserHost host,
			double price, LocalTime checkinTime, LocalTime checkoutTime)
	{
		this.id = id;
		this.type = type;
		this.noRooms = noRooms;
		this.noGuests = noGuests;
		this.location = location;
		this.host = host;
		this.price = price;
		this.checkinTime = checkinTime;
		this.checkoutTime = checkoutTime;
	}

	public Apartment(String id, ApartmentType type, int noRooms, int noGuests, Location location, UserHost host,
			double price)
	{
		this.id = id;
		this.type = type;
		this.noRooms = noRooms;
		this.noGuests = noGuests;
		this.location = location;
		this.host = host;
		this.price = price;
		this.checkinTime = LocalTime.of(14, 0);
		this.checkoutTime = LocalTime.of(22, 0);
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public ApartmentType getType()
	{
		return type;
	}

	public void setType(ApartmentType type)
	{
		this.type = type;
	}

	public int getNoRooms()
	{
		return noRooms;
	}

	public void setNoRooms(int noRooms)
	{
		this.noRooms = noRooms;
	}

	public int getNoGuests()
	{
		return noGuests;
	}

	public void setNoGuests(int noGuests)
	{
		this.noGuests = noGuests;
	}

	public Location getLocation()
	{
		return location;
	}

	public void setLocation(Location location)
	{
		this.location = location;
	}

	public ArrayList<Date> getRentDates()
	{
		return rentDates;
	}

	public void setRentDates(ArrayList<Date> rentDates)
	{
		this.rentDates = rentDates;
	}

	public HashMap<Date, Boolean> getAvailableDates()
	{
		return availableDates;
	}

	public void setAvailableDates(HashMap<Date, Boolean> availableDates)
	{
		this.availableDates = availableDates;
	}

	public UserHost getHost()
	{
		return host;
	}

	public void setHost(UserHost host)
	{
		this.host = host;
	}

	public HashMap<String, Comment> getComments()
	{
		return comments;
	}

	public void setComments(HashMap<String, Comment> comments)
	{
		this.comments = comments;
	}

	public ArrayList<String> getImages()
	{
		return images;
	}

	public void setImages(ArrayList<String> images)
	{
		this.images = images;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public LocalTime getCheckinTime()
	{
		return checkinTime;
	}

	public void setCheckinTime(LocalTime checkinTime)
	{
		this.checkinTime = checkinTime;
	}

	public LocalTime getCheckoutTime()
	{
		return checkoutTime;
	}

	public void setCheckoutTime(LocalTime checkoutTime)
	{
		this.checkoutTime = checkoutTime;
	}

	public ApartmentStatus getStatus()
	{
		return status;
	}

	public void setStatus(ApartmentStatus status)
	{
		this.status = status;
	}

	public ArrayList<Amenity> getAmenities()
	{
		return amenities;
	}

	public void setAmenities(ArrayList<Amenity> amenities)
	{
		this.amenities = amenities;
	}

	public ArrayList<Reservation> getReservations()
	{
		return reservations;
	}

	public void setReservations(ArrayList<Reservation> reservations)
	{
		this.reservations = reservations;
	}

}
