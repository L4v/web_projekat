package nomad.user;

import static nomad.Application.guestDAO;
import static nomad.Application.hostDAO;
import static nomad.Application.invalidResponse;
import static nomad.Application.parseJws;
import static nomad.Application.gson;
import static nomad.Application.key;

import java.util.ArrayList;
import java.util.Collection;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.apartment.Apartment;
import nomad.reservation.Reservation;
import spark.Request;
import spark.Response;
import spark.Route;

public class HostServices
{

	private static Collection<UserGuest> getGuests(String username)
	{
		Collection<UserGuest> users = new ArrayList<UserGuest>();

		for (UserGuest userGuest : guestDAO.getAll())
		{
			if(userGuest.getReservations() != null) 
			{
				for (Reservation reservation : userGuest.getReservations())
				{
					if(reservation.getApartment().getHost().getUsername().equals(username))
					{
						users.add(userGuest);
						break;
					}
				}
			}
		}
		return users;
	}
	
	public static Route getMyGuests = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		
		if (jws == null)
		{
			return invalidResponse("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if(host == null)
			{
				return invalidResponse("Invalid host", response);
			}
			response.status(200);
			ArrayList<UserGuest> guests = (ArrayList<UserGuest>) getGuests(host.getUsername());
			response.body(gson.toJson(guests));
			return response;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return invalidResponse("Server error: " + e.getMessage(), response);
		}
	};

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