package nomad.user;

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
			UserHost userHost = gson.fromJson(payload, UserHost.class);
			hostDAO.update(userHost);
			System.out.println(userHost.getName());
		}
		else if(adminDAO.get(loggedInUsername) != null)
		{
			adminDAO.update(gson.fromJson(payload, UserAdmin.class));
		} 
		else 
		{
			response.status(404); // pitati!!!
			return response;
		}
		
		response.status(200);
		return response;
	};
	
	public static Route getUser = (Request request, Response response) ->
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
				loggedInUsername = claims.getBody().getSubject();
				
				if(guestDAO.get(claims.getBody().getSubject()) != null)
				{
					response.status(200);
					return gson.toJson(guestDAO.get(claims.getBody().getSubject()));
				}
				else if (hostDAO.get(claims.getBody().getSubject()) != null) 
				{
					response.status(200);
					return gson.toJson(hostDAO.get(claims.getBody().getSubject()));
				}
				else if(adminDAO.get(claims.getBody().getSubject()) != null)
				{
					response.status(200);
					return gson.toJson(adminDAO.get(claims.getBody().getSubject()));
				} 
				else 
				{
					response.status(404); // pitati!!!
					return response;
				}
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
