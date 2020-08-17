package nomad.services;

import static nomad.Application.apartmentDAO;
import static nomad.Application.hostDAO;
import static nomad.Application.key;
import static nomad.Application.parseJws;
import static nomad.Application.gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.beans.Apartment;
import nomad.beans.UserHost;
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
}
