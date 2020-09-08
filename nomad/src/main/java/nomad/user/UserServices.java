package nomad.user;

import static nomad.utils.Responses.notFound;
import static nomad.Application.guestDAO;
import static nomad.Application.adminDAO;
import static nomad.Application.hostDAO;
import static nomad.Application.key;
import static nomad.Application.parseJws;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import static nomad.Application.gson;

import spark.Route;
import spark.Request;
import spark.Response;

public class UserServices
{
	private static String loggedInUsername;

	public static Route personalData = (Request request, Response response) ->
	{
		response.type("application/json");
		String payload = request.body();
		
		if(guestDAO.get(loggedInUsername) != null)
		{
			guestDAO.update(gson.fromJson(payload, UserGuest.class));
		}
		else if (hostDAO.get(loggedInUsername) != null) 
		{
			hostDAO.update(gson.fromJson(payload, UserHost.class));
		}
		else if(adminDAO.get(loggedInUsername) != null)
		{
			adminDAO.update(gson.fromJson(payload, UserAdmin.class));
		} 
		else 
		{
			return notFound("Invalid login", response);
		}
		
		response.status(200);
		return response;
	};
	
	public static Route getUser = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if(jws == null)
		{
			return notFound("Invalid login", response);
		}
		else
		{
			try
			{
				Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
				loggedInUsername = claims.getBody().getSubject();
				
				if(guestDAO.get(claims.getBody().getSubject()) != null)
				{
					response.status(200);
					response.body(gson.toJson(guestDAO.get(claims.getBody().getSubject())));
					return response;
				}
				else if (hostDAO.get(claims.getBody().getSubject()) != null) 
				{
					response.status(200);
					response.body(gson.toJson(hostDAO.get(claims.getBody().getSubject())));
					return response;
				}
				else if(adminDAO.get(claims.getBody().getSubject()) != null)
				{
					response.status(200);
					
					return gson.toJson(adminDAO.get(claims.getBody().getSubject()));
				} 
				else 
				{
					return notFound("Invalid login", response);
				}
			}
			catch(Exception e)
			{
				return notFound("Server error: " + e.getMessage(), response);
			}
		}
	};
}
