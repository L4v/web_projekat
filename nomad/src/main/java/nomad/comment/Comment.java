package nomad.comment;

import java.io.Serializable;

import nomad.apartment.Apartment;
import nomad.user.UserGuest;

public class Comment implements Serializable
{
	private static final long serialVersionUID = 2251140122076809587L;
	private String id;
	private UserGuest guest;
	private Apartment apartment;
	private String text;
	private int rating;

	public Comment(String id, UserGuest guest, Apartment apartment, String text, int rating)
	{
		this.id = id;
		this.guest = guest;
		this.apartment = apartment;
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

	public UserGuest getGuest()
	{
		return guest;
	}

	public void setGuest(UserGuest guest)
	{
		this.guest = guest;
	}

	public Apartment getApartment()
	{
		return apartment;
	}

	public void setApartment(Apartment apartment)
	{
		this.apartment = apartment;
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
