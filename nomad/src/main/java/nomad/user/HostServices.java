package nomad.user;

import static nomad.Application.apartmentDAO;
import static nomad.Application.gson;
import static nomad.Application.guestDAO;
import static nomad.Application.hostDAO;
import static nomad.Application.key;
import static nomad.Application.parseJws;
import static nomad.Application.reservationDAO;
import static nomad.utils.Responses.badRequest;
import static nomad.utils.Responses.forbidden;
import static nomad.utils.Responses.notFound;
import static nomad.utils.Responses.ok;
import static nomad.utils.Responses.serverError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.apartment.Apartment;
import nomad.apartment.ApartmentStatus;
import nomad.reservation.Reservation;
import nomad.reservation.ReservationStatus;
import spark.Request;
import spark.Response;
import spark.Route;

public class HostServices
{

	private static Collection<UserGuest> getGuests(String username)
	{
		ArrayList<UserGuest> users = new ArrayList<UserGuest>();
		ArrayList<Reservation> reservations = (ArrayList<Reservation>) reservationDAO.getAll();
		
		for(Reservation reservation : reservations)
		{
			if(apartmentDAO.get(reservation.getApartmentId()).getHostId().equals(username))
			{
				if(!containsUserGuest(users, reservation.getGuestId()))
				{
					users.add(guestDAO.get(reservation.getGuestId()));
				}
			}
		}
		return users;
	}
	
	public static Route acceptReservation = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if(jws == null)
		{
			return forbidden("User not authorized as host", response);
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if(host == null)
			{
				return forbidden("User not authorized as host", response);
			}
			String reservationId = request.body();
			Reservation reservation = reservationDAO.get(reservationId);
			if(reservation == null)
			{
				return badRequest("Reservation does not exist", response);
			}
			
			reservation.setStatus(ReservationStatus.ACCEPTED);
			reservationDAO.update(reservation);
			
			return ok("Reservation accepted", response);
		}
		catch(Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route rejectReservation = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if(jws == null)
		{
			return forbidden("User not authorized as host", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if(host == null)
			{
				return forbidden("User not authorized as host", response);
			}
			String reservationId = request.body();
			Reservation reservation = reservationDAO.get(reservationId);
			if(reservation == null)
			{
				return badRequest("Reservation does not exist", response);
			}
			reservation.setStatus(ReservationStatus.REJECTED);
			reservationDAO.update(reservation);
			return ok("Reservation rejected", response);
		}
		catch(Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route finishReservation = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if(jws == null)
		{
			return forbidden("User not authorized as host", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if(host == null)
			{
				return forbidden("User not authorized as host", response);
			}
			String reservationId = request.body();
			Reservation reservation = reservationDAO.get(reservationId);
			if(reservation == null)
			{
				return badRequest("Reservation does not exist", response);
			}
			Date currentDate = new Date();
			if(currentDate.getTime() >= reservation.getEndDate().getTime())
			{
				reservation.setStatus(ReservationStatus.FINISHED);
				return ok("Reservation finished", response);
			}
			
			return badRequest("Reservation could not be finished", response);
		}
		catch(Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route enableApartment = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if(jws == null)
		{
			return forbidden("User not authorized as host", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if(host == null)
			{
				return forbidden("User not authorized as host", response);
			}
			
			String apartmentId = request.body();
			Apartment apartment = apartmentDAO.get(apartmentId);
			if(apartment == null)
			{
				return badRequest("Apartment with id " + apartmentId + " does not exist", response);
			}
			apartment.setStatus(ApartmentStatus.ACTIVE);
			apartmentDAO.update(apartment);
			return ok("Apartment enabled", response);
		}
		catch(Exception e)
		{
			return serverError("Server error " + e.getMessage(), response);
		}
	};
	
	public static Route checkIfHost = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if(jws == null)
		{
			return forbidden("User not authorized as host", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if(host == null)
			{
				return forbidden("User not authorized as host", response);
			}
			return ok("Access granted, user is host", response);
		}
		catch(Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	private static boolean containsUserGuest(Collection<UserGuest> userGuests, String username)
	{
		for(UserGuest userGuest : userGuests)
		{
			if(userGuest.getUsername().equals(username))
			{
				return true;
			}
		}
		return false;
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
