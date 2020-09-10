package nomad.amenity;

import static nomad.utils.Responses.notFound;
import static nomad.utils.Responses.ok;

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
			return notFound("Invalid login", response);
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if (admin == null)
			{
				return notFound("Invalid admin", response);
			}

			String json = request.body();
			Amenity amenity = gson.fromJson(json, Amenity.class);
			amenityDAO.update(amenity);
			
			response.status(200);
			/*response.body("Amenity updated!");
			return response;*/
			ArrayList<Amenity> retVal = (ArrayList<Amenity>) amenityDAO.getAll();
			return gson.toJson(retVal);
			
		} catch (Exception e)
		{
			e.printStackTrace();
			return notFound("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route addAmenity = (Request request, Response response) ->
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
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if (admin == null)
			{
				return notFound("Invalid admin", response);
			}

			String json = request.body();
			Amenity amenity = gson.fromJson(json, Amenity.class);
			
			ArrayList<Amenity> amenities = (ArrayList<Amenity>) amenityDAO.getAll();
			for(Amenity a : amenities)
			{
				if(amenity.getName().equalsIgnoreCase(a.getName()))
				{
					response.status(404);
					response.body("Amenity already exists!");
					return response;
				}
			}

			if(amenityDAO.add(amenity)) 
			{
				response.status(200);
				/*response.body("Amenity successfully added!");
				return response;*/
				ArrayList<Amenity> retVal = (ArrayList<Amenity>) amenityDAO.getAll();
				return gson.toJson(retVal);
			}
			else
			{
				response.status(404);
				response.body("Failed adding amenity!");
				return response;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return notFound("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route removeAmenity = (Request request, Response response) ->
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
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if (admin == null)
			{
				return notFound("Invalid admin", response);
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
				response.status(200);
				/*response.body("Amenity removed!");
				return response;*/
				ArrayList<Amenity> retVal = (ArrayList<Amenity>) amenityDAO.getAll();
				return gson.toJson(retVal);
			}
			else
			{
				response.status(404);
				return response;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return notFound("Server error: " + e.getMessage(), response);
		}
	};
}
