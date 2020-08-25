package nomad.user;

import static nomad.Application.parseJws;
import static nomad.Application.gson;
import static nomad.Application.key;
import static nomad.Application.guestDAO;
import static nomad.Application.hostDAO;
import static nomad.Application.invalidResponse;
import static nomad.Application.adminDAO;
import static nomad.Application.apartmentDAO;
import static nomad.Application.amenityDAO;
import java.util.ArrayList;
import java.util.Collection;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.amenity.Amenity;
import nomad.apartment.Apartment;
import spark.Request;
import spark.Response;
import spark.Route;

public class AdminServices
{

	private static Collection<UserBase> getAll()
	{
		Collection<UserBase> users = new ArrayList<UserBase>();

		users.addAll(guestDAO.getAll());
		users.addAll(adminDAO.getAll());
		users.addAll(hostDAO.getAll());

		return users;
	}

	public static Route getAllUsers = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if (jws == null)
		{
			response.status(404);
			return response;
		} else
		{
			try
			{
				Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
				UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
				if (admin == null)
				{
					response.status(404);
					return response;
				}
				response.status(200);
				ArrayList<UserBase> users = (ArrayList<UserBase>) getAll();
				String usersJson = gson.toJson(users);
				return usersJson;
			} catch (Exception e)
			{
				e.printStackTrace();
				response.status(404);
				return response;
			}
		}
	};
	
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
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if (admin == null)
			{
				response.status(404);
				return response;
			}
			response.status(200);
			ArrayList<Apartment> apartments = (ArrayList<Apartment>) apartmentDAO.getAll();
			response.status(200);
			return gson.toJson(apartments);
		} catch (Exception e)
		{
			e.printStackTrace();
			return invalidResponse("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route allAmenities = (Request request, Response response) ->
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
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if (admin == null)
			{
				return invalidResponse("Not admin", response);
			}
			response.status(200);
			ArrayList<Amenity> amenities = (ArrayList<Amenity>) amenityDAO.getAll();
			response.status(200);
			return gson.toJson(amenities);
		} catch (Exception e)
		{
			e.printStackTrace();
			return invalidResponse("Server error: " + e.getMessage(), response);
		}
	};

}
