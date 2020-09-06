package nomad.amenity;

import static nomad.utils.Responses.notFound;
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
			return response;
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
		/*if (jws == null)
		{
			return invalidResponse("Invalid login", response);
		}*/
		try
		{
			/*Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if (admin == null)
			{
				return invalidResponse("Invalid admin", response);
			}*/

			String json = request.body();
			Amenity amenity = gson.fromJson(json, Amenity.class);
			if(amenityDAO.add(amenity)) 
			{
				response.status(200);
				response.body("Amenity updated!");
				return response;
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
	
	public static Route removeAmenity = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		/*if (jws == null)
		{
			return invalidResponse("Invalid login", response);
		}*/
		try
		{
			/*Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if (admin == null)
			{
				return invalidResponse("Invalid admin", response);
			}
			 */
			String json = request.body();
			Amenity amenity = gson.fromJson(json, Amenity.class);
			if(amenityDAO.remove(amenity.getId())) 
			{
				for(Apartment apartment : apartmentDAO.getAll())
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
				response.body("Amenity removed!");
				return response;
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
