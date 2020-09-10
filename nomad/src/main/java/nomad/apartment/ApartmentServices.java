package nomad.apartment;

import static nomad.utils.Responses.badRequest;
import static nomad.utils.Responses.notFound;
import static nomad.utils.Responses.forbidden;
import static nomad.utils.Responses.ok;
import static nomad.utils.Responses.serverError;
import static nomad.Application.apartmentDAO;
import static nomad.Application.hostDAO;
import static nomad.Application.adminDAO;
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
			return forbidden("Not host", response);
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if (host == null)
			{
				return forbidden("Not host", response);
			}

			String json = request.body();
			Apartment apartment = gson.fromJson(json, Apartment.class);
			if (apartment == null)
			{
				return badRequest("Invalid apartment object", response);
			}
			apartment.setHostId(host.getUsername());
			// TODO(Jovan): Set id
			if (apartmentDAO.add(apartment) == true)
			{
				return ok("Apartment added", response);
			}
			return badRequest("Something failed while adding apartment. Not added", response);
		} catch (Exception e)
		{
			return serverError("Error: " + e.getMessage(), response);
		}
	};
	
	public static Route adminDeleteApartments = (Request request, Response response) ->
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
				return notFound("Not admin", response);
			}

			if(apartmentDAO.removeAll()) 
			{
				response.status(200);
				response.body("Removed all apartments");
				return response;
			}
			return notFound("Failed removing all apartments", response);
		} catch (Exception e)
		{
			e.printStackTrace();
			return notFound("Server error: " + e.getMessage(), response);
		}
	};
	
	
	
	
}
