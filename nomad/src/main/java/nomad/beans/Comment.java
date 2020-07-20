package nomad.beans;

import java.io.Serializable;

public class Comment implements Serializable {
	private static final long serialVersionUID = 2251140122076809587L;
	private UserGuest guest;
	private Apartment apartment;
	private String text;
	private int rating;

	public Comment(UserGuest guest, Apartment apartment, String text, int rating) {
		this.guest = guest;
		this.apartment = apartment;
		this.text = text;
		this.rating = rating;
	}

	public UserGuest getGuest() {
		return guest;
	}

	public void setGuest(UserGuest guest) {
		this.guest = guest;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

}
