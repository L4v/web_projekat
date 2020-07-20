package nomad.beans;

import java.io.Serializable;
import java.util.HashMap;

import nomad.beans.enums.Sex;
import nomad.beans.enums.UserType;

public class UserHost extends UserBase implements Serializable {
	private static final long serialVersionUID = 5032688984515531286L;
	private HashMap<String, Apartment> forRent;

	public UserHost(String username, String password, String name, String surname, Sex sex, UserType userType) {
		super(username, password, name, surname, sex, UserType.HOST);
	}

	public HashMap<String, Apartment> getForRent() {
		return forRent;
	}

	public void setForRent(HashMap<String, Apartment> forRent) {
		this.forRent = forRent;
	}

	// TODO(Jovan): Get i set za forRent, povratna vrednost lista ili mapa?

}
