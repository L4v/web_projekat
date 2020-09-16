package nomad.amenity;

import static nomad.utils.Responses.ok;
import static nomad.utils.Responses.forbidden;
import static nomad.utils.Responses.serverError;
import static nomad.utils.Responses.badRequest;

import java.util.ArrayList;

import static nomad.Application.adminDAO;
import static nomad.Application.amenityDAO;
import static nomad.Application.apartmentDAO;
import static nomad.Application.gson;
import static nomad.Application.key;
import static nomad.Application.parseJws;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.apartment.Apartment;
import nomad.user.UserAdmin;
import spark.Request;
import spark.Response;
import spark.Route;

public class AmenityServices {

	public static Route getAmenities = (Request request, Response response) ->
	{
		// TODO(Jovan): Validation not required?
		ArrayList<Amenity> amenities = (ArrayList<Amenity>) amenityDAO.getAll();
		String json = gson.toJson(amenities);
		response.type("application/json");
		return ok(json, response);
	};

	public static Route updateAmenity = (Request request, Response response) ->
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
				return forbidden("Invalid admin", response);
			}

			String json = request.body();
			Amenity amenity = gson.fromJson(json, Amenity.class);
			amenityDAO.update(amenity);
			
			ArrayList<Amenity> retVal = (ArrayList<Amenity>) amenityDAO.getAll();
			String jsonRet = gson.toJson(retVal);
			return ok(jsonRet, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route addAmenity = (Request request, Response response) ->
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
				return forbidden("Invalid admin", response);
			}

			String json = request.body();
			Amenity amenity = gson.fromJson(json, Amenity.class);
			
			ArrayList<Amenity> amenities = (ArrayList<Amenity>) amenityDAO.getAll();
			for(Amenity a : amenities)
			{
				if(amenity.getName().equalsIgnoreCase(a.getName()))
				{
					return badRequest("Amenity already exists!", response);
				}
			}

			if(amenityDAO.add(amenity)) 
			{
				ArrayList<Amenity> retVal = (ArrayList<Amenity>) amenityDAO.getAll();
				String jsonRet = gson.toJson(retVal);
				return ok(jsonRet, response);
			}
			else
			{
				return badRequest("Failed adding amenity!", response);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route removeAmenity = (Request request, Response response) ->
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
				return forbidden("Invalid admin", response);
			}
			
			String json = request.body();
			Amenity amenity = gson.fromJson(json, Amenity.class);
			
			ArrayList<Apartment> apartments = (ArrayList<Apartment>) apartmentDAO.getAll();
			if(amenityDAO.remove(amenity.getId())) 
			{
				for(Apartment apartment : apartments)
				{
					for(Amenity apartmentAmenity : apartment.getAmenities())
					{
						if(amenity.getId() == apartmentAmenity.getId())
						{
							apartment.getAmenities().remove(apartmentAmenity);
							apartmentDAO.update(apartment);
							break;
						}
					}
				}
				ArrayList<Amenity> retVal = (ArrayList<Amenity>) amenityDAO.getAll();
				String jsonRet = gson.toJson(retVal);
				return ok(jsonRet, response);
			}
			else
			{
				return badRequest("Failed removing amenity!", response);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
}
