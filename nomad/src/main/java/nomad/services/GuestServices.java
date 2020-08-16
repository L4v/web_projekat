package nomad.services;

import static nomad.Application.parseJws;
import static nomad.Application.gson;
import static nomad.Application.guestDAO;
import static nomad.Application.key;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.beans.UserGuest;
import spark.Request;
import spark.Response;
import spark.Route;

public class GuestServices {

	// TODO(Jovan -> Kris): UserServices?
	public static Route getGuest = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if(jws == null)
		{
			response.status(404);
			return response;
		}
		else
		{
			try
			{
				Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
				UserGuest guest = guestDAO.get(claims.getBody().getSubject());
				response.status(200);
				return gson.toJson(guest);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				response.status(404);
				return response;
			}
		}
	};
}
