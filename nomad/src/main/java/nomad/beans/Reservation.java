package nomad.beans;

import java.io.Serializable;
import java.util.Date;

import nomad.beans.enums.ReservationStatus;

public class Reservation implements Serializable
{
	// TODO(Jovan): Keep ID's instead of objects
	private static final long serialVersionUID = 1318727009852237993L;
	private Apartment apartment;
	private Date startDate;
	private int noDays;
	private double totalPrice;
	private String reservationMessage;
	private UserGuest guest;
	private ReservationStatus status;
	private String id;

	private Reservation(String id, Apartment apartment, Date startDate, int noDays, double totalPrice,
			String reservationMessage, UserGuest guest, ReservationStatus status)
	{
		this.apartment = apartment;
		this.startDate = startDate;
		this.noDays = noDays;
		this.totalPrice = totalPrice;
		this.reservationMessage = reservationMessage;
		this.guest = guest;
		this.status = status;
	}

	private Reservation(String id, Apartment apartment, Date startDate, double totalPrice, String reservationMessage,
			UserGuest guest, ReservationStatus status)
	{
		this.apartment = apartment;
		this.startDate = startDate;
		this.noDays = 1;
		this.totalPrice = totalPrice;
		this.reservationMessage = reservationMessage;
		this.guest = guest;
		this.status = status;
	}

	public Apartment getApartment()
	{
		return apartment;
	}

	public void setApartment(Apartment apartment)
	{
		this.apartment = apartment;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public int getNoDays()
	{
		return noDays;
	}

	public void setNoDays(int noDays)
	{
		this.noDays = noDays;
	}

	public double getTotalPrice()
	{
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice)
	{
		this.totalPrice = totalPrice;
	}

	public String getReservationMessage()
	{
		return reservationMessage;
	}

	public void setReservationMessage(String reservationMessage)
	{
		this.reservationMessage = reservationMessage;
	}

	public UserGuest getGuest()
	{
		return guest;
	}

	public void setGuest(UserGuest guest)
	{
		this.guest = guest;
	}

	public ReservationStatus getStatus()
	{
		return status;
	}

	public void setStatus(ReservationStatus status)
	{
		this.status = status;
	}

	public String getId()
	{
		return this.id;
	}

}
