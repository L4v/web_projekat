package nomad.comment;

import java.io.Serializable;

import nomad.apartment.Apartment;
import nomad.user.UserGuest;

public class Comment implements Serializable
{
	private static final long serialVersionUID = 2251140122076809587L;
	private String id;
	private String guestId;
	private String apartmentId;
	private String text;
	private int rating;

	public Comment(String id, String guestId, String apartmentId, String text, int rating)
	{
		this.id = id;
		this.guestId = guestId;
		this.apartmentId = apartmentId;
		this.text = text;
		this.rating = rating;
	}
	
	public String getId()
	{
		return this.id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}

	public String getGuestId()
	{
		return guestId;
	}

	public void setGuestId(String guestId)
	{
		this.guestId = guestId;
	}

	public String getApartmentId()
	{
		return apartmentId;
	}

	public void setApartmentId(String apartmentId)
	{
		this.apartmentId = apartmentId;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public int getRating()
	{
		return rating;
	}

	public void setRating(int rating)
	{
		this.rating = rating;
	}

}
