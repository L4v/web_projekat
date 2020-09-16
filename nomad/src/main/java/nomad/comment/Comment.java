package nomad.comment;

import java.io.Serializable;


public class Comment implements Serializable
{
	private static final long serialVersionUID = 2251140122076809587L;
	private String id;
	private String guestId;
	private String apartmentId;
	private String text;
	private int rating;
	private boolean deleted;

	public Comment(String id, String guestId, String apartmentId, String text, int rating)
	{
		this.id = id;
		this.guestId = guestId;
		this.apartmentId = apartmentId;
		this.text = text;
		this.rating = rating;
		this.deleted = false;
	}
	
	public boolean getDeleted()
	{
		return this.deleted;
	}
	
	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
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
