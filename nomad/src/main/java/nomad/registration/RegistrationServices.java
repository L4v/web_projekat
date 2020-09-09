package nomad.registration;

import static nomad.Application.key;
import static nomad.Application.parseJws;
import static nomad.Application.adminDAO;
import static nomad.Application.guestDAO;
import static nomad.Application.hostDAO;

import nomad.user.UserAdmin;
import nomad.user.UserGuest;
import nomad.user.UserHost;

import static nomad.Application.gson;
import static nomad.utils.Responses.ok;
import static nomad.utils.Responses.serverError;
import static nomad.utils.Responses.badRequest;
import static nomad.utils.Responses.forbidden;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import spark.Request;
import spark.Response;
import spark.Route;

public class RegistrationServices
{

	private static boolean registerGuest(UserGuest guest)
	{
		return guestDAO.add(guest);
	}
	
	private static boolean registerHost(UserHost host)
	{
		return hostDAO.add(host);
	}

	public static Route registerHost = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if(jws == null)
		{
			return forbidden("User not admin", response);
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if(admin == null)
			{
				return forbidden("User not admin", response);
			}
			
			String payload = request.body();
			UserHost host = gson.fromJson(payload, UserHost.class);
			return registerHost(host) ? ok("Host registered", response) : badRequest("Host could not be registered", response);	
		}
		catch(Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route registerGuest = (Request request, Response response) ->
	{
		response.type("application/json");
		String payload = request.body();
		UserGuest guest = gson.fromJson(payload, UserGuest.class);
		response.status(registerGuest(guest) ? 200 : 404);
		return response;
	};

}
