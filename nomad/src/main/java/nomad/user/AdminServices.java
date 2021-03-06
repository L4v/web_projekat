package nomad.user;

import static nomad.Application.parseJws;
import static nomad.utils.Responses.forbidden;
import static nomad.utils.Responses.ok;
import static nomad.utils.Responses.serverError;
import static nomad.Application.gson;
import static nomad.Application.key;
import static nomad.Application.guestDAO;
import static nomad.Application.hostDAO;
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
	
	public static Route checkIfAdmin = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if(jws == null)
		{
			return forbidden("User not authorized as admin", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if(admin == null)
			{
				return forbidden("User not authorized as admin", response);
			}
			return ok("Access granted, user is admin", response);
		}
		catch(Exception e)
		{
			return serverError(e.getMessage(), response);
		}
	};

	public static Route getAllUsers = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if (jws == null)
		{
			return forbidden("User not authorized as admin", response);
		} else
		{
			try
			{
				Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
				UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
				if (admin == null)
				{
					return forbidden("User not authorized as admin", response);
				}
				ArrayList<UserBase> users = (ArrayList<UserBase>) getAll();
				String usersJson = gson.toJson(users);
				return ok(usersJson, response);
			} catch (Exception e)
			{
				e.printStackTrace();
				return serverError(e.getMessage(), response);
			}
		}
	};
	
	public static Route allApartments = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if (jws == null)
		{
			return forbidden("User not authorized as admin", response);
		} 
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if (admin == null)
			{
				return forbidden("User not authorized as admin", response);
			}
			ArrayList<Apartment> apartments = (ArrayList<Apartment>) apartmentDAO.getAll();
			String json = gson.toJson(apartments);
			return ok(json, response);
		} catch (Exception e)
		{
			e.printStackTrace();
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route allAmenities = (Request request, Response response) ->
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
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if (admin == null)
			{
				return forbidden("Not admin", response);
			}

			ArrayList<Amenity> amenities = (ArrayList<Amenity>) amenityDAO.getAll();
			String json = gson.toJson(amenities);
			return ok(json, response);
		} catch (Exception e)
		{
			e.printStackTrace();
			return serverError("Server error: " + e.getMessage(), response);
		}
	};

}
