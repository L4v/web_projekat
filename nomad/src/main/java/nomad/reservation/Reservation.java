package nomad.reservation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import nomad.apartment.Apartment;
import nomad.user.UserGuest;

public class Reservation implements Serializable
{
	// TODO(Jovan): Keep ID's instead of objects
	private static final long serialVersionUID = 1318727009852237993L;
	private String apartmentId;
	private Date startDate;
	private int noDays;
	private double totalPrice;
	private String reservationMessage;
	private String guestId;
	private ReservationStatus status;
	private String id;

	private Reservation(String id, String apartmentId, Date startDate, int noDays, double totalPrice,
			String reservationMessage, String guestId, ReservationStatus status)
	{
		this.apartmentId = apartmentId;
		this.startDate = startDate;
		this.noDays = noDays;
		this.totalPrice = totalPrice;
		this.reservationMessage = reservationMessage;
		this.guestId = guestId;
		this.status = status;
	}

	private Reservation(String id, String apartmentId, Date startDate, double totalPrice, String reservationMessage,
			String guestId, ReservationStatus status)
	{
		this.apartmentId = apartmentId;
		this.startDate = startDate;
		this.noDays = 1;
		this.totalPrice = totalPrice;
		this.reservationMessage = reservationMessage;
		this.guestId = guestId;
		this.status = status;
	}

	public String getApartmentId()
	{
		return apartmentId;
	}

	public void setApartmentId(String apartmentId)
	{
		this.apartmentId = apartmentId;
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

	public String getGuestId()
	{
		return guestId;
	}

	public void setGuestId(String guestId)
	{
		this.guestId = guestId;
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
	
	public Date getEndDate()
	{
		LocalDateTime.from(this.startDate.toInstant()).plusDays(this.noDays);
		return null;
	}

}
