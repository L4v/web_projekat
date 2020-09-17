package nomad.user;

import static nomad.Application.apartmentDAO;
import static nomad.Application.gson;
import static nomad.Application.guestDAO;
import static nomad.Application.key;
import static nomad.Application.parseJws;
import static nomad.Application.reservationDAO;
import static nomad.Application.hostDAO;
import static nomad.utils.Responses.badRequest;
import static nomad.utils.Responses.forbidden;
import static nomad.utils.Responses.notFound;
import static nomad.utils.Responses.ok;
import static nomad.utils.Responses.serverError;

import java.util.ArrayList;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.apartment.Apartment;
import nomad.reservation.Reservation;
import spark.Request;
import spark.Response;
import spark.Route;

public class GuestServices
{
	
	public static Route getGuests = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		
		if(jws == null)
		{
			return forbidden("User not authorized as guest", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			
			if(host == null)
			{
				return forbidden("User not authorized as host", response);
			}

			ArrayList<Reservation> hostsReservations = new ArrayList<Reservation>();
			for (Apartment apartment : apartmentDAO.getByIds(host.getApartments()))
			{
				hostsReservations.addAll(reservationDAO.getForApartment(apartment.getId()));
			}

			ArrayList<String> guestIds = new ArrayList<String>();
			for(Reservation reservation : hostsReservations)
			{
				guestIds.add(reservation.getGuestId());
			}
			ArrayList<UserGuest> guests = (ArrayList<UserGuest>) guestDAO.getByIds(guestIds);
			String json = gson.toJson(guests);
			return ok(json, response);
			
		}
		catch(Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route allApartments = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		
		if(jws == null)
		{
			return forbidden("User not authorized as guest", response);
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if(guest == null)
			{
				return forbidden("User not authorized as guest", response);
			}
			ArrayList<Apartment> activeApartments = (ArrayList<Apartment>) apartmentDAO.getActive();
			
			// NOTE(Jovan): Remove already reserved for that guest
			ArrayList<String> guestReservations = guest.getReservations();
			for(String res : guestReservations)
			{
				Reservation reservation = reservationDAO.get(res);
				if(reservation == null)
				{
					return badRequest("Bad reservations", response);
				}
				activeApartments.removeIf(a -> reservation.getApartmentId().equals(a.getId()));
			}
			String json = gson.toJson(activeApartments);
			response.type("application/json");
			return ok(json, response);
		}
		catch(Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route checkIfGuest = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if(jws == null)
		{
			return forbidden("User not authorized as guest", response);
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if(guest == null)
			{
				return forbidden("User not authorized as guest", response);
			}
			return ok("Access granted, user is guest", response);
		}
		catch(Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route reservedApartments = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if (jws == null)
		{
			return forbidden("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if(guest == null)
			{
				return forbidden("Invalid guest", response);
			}
			ArrayList<Apartment> apartments = (ArrayList<Apartment>) apartmentDAO.getActive();
			String json = gson.toJson(apartments);
			return ok(json, response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route getUsername = (Request request, Response response) ->
	{
		String username = request.params("username");
		if(username == null)
		{
			return notFound("Invalid username", response);
		}
		UserGuest guest = guestDAO.get(username);
		if(guest == null)
		{
			return notFound("Invalid username", response);
		}
		
		response.status(200);
		response.body("Username exists");
		return response;
	};

}
