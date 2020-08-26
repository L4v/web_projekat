package nomad.apartment;

import static nomad.Application.apartmentDAO;
import static nomad.Application.hostDAO;
import static nomad.Application.adminDAO;
import static nomad.Application.invalidResponse;
import static nomad.Application.key;
import static nomad.Application.parseJws;
import static nomad.Application.gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.user.UserAdmin;
import nomad.user.UserHost;
import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentServices
{

	public static Route hostAddApartment = (Request request, Response response) ->
	{
		response.type("text/html");
		String jws = parseJws(request);
		if (jws == null)
		{
			response.status(404);
			return response;
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if (host == null)
			{
				response.status(404);
				response.body("Not host!");
				return response;
			}

			String json = request.body();
			Apartment apartment = gson.fromJson(json, Apartment.class);
			if (apartment == null)
			{
				response.status(404);
				response.body("Invalid apartment object!");
				return response;
			}
			if (apartmentDAO.add(apartment) == true)
			{
				response.status(200);
				response.body("Apartment added!");
				return response;
			}
			response.status(404);
			response.body("Failed adding apartment!");
			return response;
		} catch (Exception e)
		{
			e.printStackTrace();
			response.status(404);
			return response;
		}
	};
	
	public static Route adminDeleteApartments = (Request request, Response response) ->
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

			if(apartmentDAO.removeAll()) 
			{
				response.status(200);
				response.body("Removed all apartments");
				return response;
			}
			return invalidResponse("Failed removing all apartments", response);
		} catch (Exception e)
		{
			e.printStackTrace();
			return invalidResponse("Server error: " + e.getMessage(), response);
		}
	};
	
	
	
	
}
