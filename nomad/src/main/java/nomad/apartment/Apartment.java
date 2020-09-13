package nomad.apartment;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import nomad.amenity.Amenity;
import nomad.comment.Comment;
import nomad.utils.DateRange;

public class Apartment implements Serializable
{
	// TODO(Jovan): Use ID's instead of objects
	private static final long serialVersionUID = -8563884059267108747L;
	private String id;
	private ApartmentType type;
	private int noRooms;
	private int noGuests;
	private Location location;
	private ArrayList<DateRange> rentDates;
	private ArrayList<DateRange> availableDates;
	private String hostId;
	// NOTE(Jovan): {username: comment}
	private ArrayList<Comment> comments;
	// NOTE(Jovan): Image urls
	private ArrayList<String> images;
	private double price;
	private LocalTime checkinTime;
	private LocalTime checkoutTime;
	private ApartmentStatus status;
	private ArrayList<Amenity> amenities;
	private ArrayList<String> reservations;

	public Apartment(String id, ApartmentType type, int noRooms, int noGuests, Location location, String hostId,
			double price, LocalTime checkinTime, LocalTime checkoutTime)
	{
		this.id = id;
		this.type = type;
		this.noRooms = noRooms;
		this.noGuests = noGuests;
		this.location = location;
		this.hostId = hostId;
		this.price = price;
		this.checkinTime = checkinTime;
		this.checkoutTime = checkoutTime;
	}

	public Apartment(String id, ApartmentType type, int noRooms, int noGuests, Location location, String hostId,
			double price)
	{
		this.id = id;
		this.type = type;
		this.noRooms = noRooms;
		this.noGuests = noGuests;
		this.location = location;
		this.hostId = hostId;
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

	public ArrayList<DateRange> getRentDates()
	{
		return rentDates;
	}

	public void setRentDates(ArrayList<DateRange> rentDates)
	{
		this.rentDates = rentDates;
	}

	public ArrayList<DateRange> getAvailableDates()
	{
		return availableDates;
	}

	public void setAvailableDates(ArrayList<DateRange> availableDates)
	{
		this.availableDates = availableDates;
	}

	public String getHostId()
	{
		return hostId;
	}

	public void setHostId(String hostId)
	{
		this.hostId = hostId;
	}

	public Collection<Comment> getComments()
	{
		return comments;
	}

	public void setComments(ArrayList<Comment> comments)
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

}
