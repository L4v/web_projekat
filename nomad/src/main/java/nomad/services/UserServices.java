package nomad.services;

import static nomad.Application.guestDAO;
import static nomad.Application.adminDAO;
import static nomad.Application.hostDAO;
import static nomad.Application.key;
import static nomad.Application.parseJws;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import static nomad.Application.gson;

import nomad.beans.UserBase;
import nomad.beans.UserGuest;
import nomad.beans.UserHost;
import spark.Route;
import spark.Request;
import spark.Response;

public class UserServices {

	public static Route personalData = (Request request, Response response) ->
	{
		response.type("application/json");
		String payload = request.body();
		UserBase user = gson.fromJson(payload, UserBase.class);
		
		
		
		//hostDAO.update(user);
		System.out.println(user.getName());
		response.status(200);
		return response;
		
		/*response.type("application/json");
		String payload = request.body();
		UserBase user = gson.fromJson(payload, UserBase.class);
		if(user instanceof UserGuest)
		{
			guestDAO.update((UserGuest)user);
		}
		else if(user instanceof UserAdmin) 
		{
			adminDAO.update((UserAdmin)user);
		}
		else if(user instanceof UserHost)
		{
			hostDAO.update((UserHost)user);
		}
		System.out.println(user.getName());
		response.status(200);
		return response;*/
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
					return null;
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
