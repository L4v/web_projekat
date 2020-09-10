package nomad.user;

import static nomad.utils.Responses.notFound;
import static nomad.utils.Responses.forbidden;
import static nomad.utils.Responses.ok;
import static nomad.utils.Responses.serverError;
import static nomad.Application.guestDAO;
import static nomad.Application.apartmentDAO;
import static nomad.Application.reservationDAO;
import static nomad.Application.hostDAO;
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
				for (Reservation reservation : reservationDAO.getForGuest(userGuest.getUsername()))
				{
					Apartment apartment = apartmentDAO.get(reservation.getApartmentId());
					if(apartment.getHostId().equals(username))
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
			return notFound("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if(host == null)
			{
				return notFound("Invalid host", response);
			}
			response.status(200);
			ArrayList<UserGuest> guests = (ArrayList<UserGuest>) getGuests(host.getUsername());
			//ArrayList<UserGuest> guests = (ArrayList<UserGuest>) guestDAO.getAll();
			return gson.toJson(guests);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return serverError("Server error: " + e.getMessage(), response);
		}
	};

	private static Collection<Apartment> getApartments(String username)
	{
		UserHost host = hostDAO.get(username);
		if (host == null)
		{
			return new ArrayList<Apartment>();
		}
		return new ArrayList<Apartment>(apartmentDAO.getByIds(host.getApartments()));
	}

	public static Route allApartments = (Request request, Response response) ->
	{
		String jws = parseJws(request);

		if (jws == null)
		{
			forbidden("Not host", response);
		}
		Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
		UserHost host = hostDAO.get(claims.getBody().getSubject());
		if (host == null)
		{
			forbidden("Not host", response);
		}
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) getApartments(host.getUsername());
		String json = gson.toJson(apartments);
		response.type("application/json");
		return ok(json, response);
	};
	
	public static Route hostSearchGuests = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		
		if (jws == null)
		{
			return notFound("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if(host == null)
			{
				return notFound("Invalid host", response);
			}
			
			ArrayList<UserGuest> guests = (ArrayList<UserGuest>) getGuests(host.getUsername());
			//ArrayList<UserGuest> guests = (ArrayList<UserGuest>) guestDAO.getAll();
			ArrayList<UserGuest> retVal = new ArrayList<UserGuest>();

			String json = request.body();
			ParameterDTO parameter = gson.fromJson(json, ParameterDTO.class);
			
			if(parameter.getUsername().equals(""))
			{
				for(UserGuest userGuest : guests)
				{
					if(userGuest.getSex().equals(parameter.getSex()))
					{
						retVal.add(userGuest);
					}
				}
			} 
			else if(parameter.getSex() == null)
			{
				for(UserGuest userGuest : guests)
				{
					if(userGuest.getUsername().equalsIgnoreCase(parameter.getUsername()))
					{
						retVal.add(userGuest);
					}
				}
			}
			else 
			{
				for(UserGuest userGuest : guests)
				{
					if(userGuest.getUsername().equalsIgnoreCase(parameter.getUsername()) && userGuest.getSex() == parameter.getSex())
					{
						retVal.add(userGuest);
					}
				}
			}
			
			response.status(200);
			return gson.toJson(retVal);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return serverError("Server error: " + e.getMessage(), response);
		}
	};

}
