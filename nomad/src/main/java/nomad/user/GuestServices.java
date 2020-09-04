package nomad.user;

import static nomad.Application.apartmentDAO;
import static nomad.Application.gson;
import static nomad.Application.guestDAO;
import static nomad.Application.invalidResponse;
import static nomad.Application.key;
import static nomad.Application.parseJws;

import java.util.ArrayList;
import java.util.Collection;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.apartment.Apartment;
import nomad.apartment.ApartmentStatus;
import spark.Request;
import spark.Response;
import spark.Route;

public class GuestServices
{
	
	private static Collection<Apartment> getActiveApartments()
	{
		ArrayList<Apartment> activeApartments = new ArrayList<Apartment>();
		
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) apartmentDAO.getAll();
		for (Apartment apartment : apartments) 
		{
			if(apartment.getStatus() == ApartmentStatus.ACTIVE)
			{
				activeApartments.add(apartment);
			}
		}
		return activeApartments;
	}
	
	public static Route allApartments = (Request request, Response response) ->
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
			
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if(guest == null)
			{
				return invalidResponse("Invalid guest", response);
			}
			response.type("application/json");
			response.status(200);
			ArrayList<Apartment> apartments = (ArrayList<Apartment>) getActiveApartments();
			response.status(200);
			return gson.toJson(apartments);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return invalidResponse("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route getUsername = (Request request, Response response) ->
	{
		String username = request.params("username");
		if(username == null)
		{
			return invalidResponse("Invalid username", response);
		}
		UserGuest guest = guestDAO.get(username);
		if(guest == null)
		{
			return invalidResponse("Invalid username", response);
		}
		
		response.status(200);
		response.body("Username exists");
		return response;
	};

}
