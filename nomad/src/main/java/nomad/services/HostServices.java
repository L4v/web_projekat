package nomad.services;

import static nomad.Application.guestDAO;
import static nomad.Application.hostDAO;
import static nomad.Application.parseJws;
import static nomad.Application.gson;
import static nomad.Application.key;

import java.util.ArrayList;
import java.util.Collection;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.beans.Apartment;
import nomad.beans.Reservation;
import nomad.beans.UserGuest;
import nomad.beans.UserHost;
import spark.Request;
import spark.Response;
import spark.Route;

public class HostServices
{

	private static Collection<UserGuest> getGuests()
	{
		Collection<UserGuest> users = new ArrayList<UserGuest>();

		for (UserGuest userGuest : guestDAO.getAll())
		{
			for (Reservation reservation : userGuest.getReservations())
			{
				// if(reservation.getApartment().getHost().equals())
				{
					users.add(userGuest);
					break;
				}
			}
		}

		return users;
	}

	private static Collection<Apartment> getApartments(String username)
	{
		UserHost host = hostDAO.get(username);
		if (host == null)
		{
			return new ArrayList<Apartment>();
		}
		return new ArrayList<Apartment>(host.getApartments());
	}

	public static Route allApartments = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);

		if (jws == null)
		{
			response.status(404);
			return response;
		}
		Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
		UserHost host = hostDAO.get(claims.getBody().getSubject());
		if (host == null)
		{
			response.status(404);
			return response;
		}
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) getApartments(host.getUsername());
		return gson.toJson(apartments);
	};

}
