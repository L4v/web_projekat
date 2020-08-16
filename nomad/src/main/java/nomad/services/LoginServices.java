package nomad.services;

import static nomad.Application.adminDAO;
import static nomad.Application.guestDAO;
import static nomad.Application.hostDAO;
import static nomad.Application.parseJws;
import static nomad.Application.key;
import static nomad.Application.gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.beans.UserAdmin;
import nomad.beans.UserBase;
import nomad.beans.UserGuest;
import nomad.beans.UserHost;
import nomad.dto.LoginDTO;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginServices {

	
	public static UserBase loggedInUser(LoginDTO loginDTO)
	{
		for(UserAdmin u : adminDAO.getAll())
		{
			if(u.getUsername().equals(loginDTO.getUsername())
					&& u.getPassword().equals(loginDTO.getPassword()))
			{
				return u;
			}
		}
	
		for(UserGuest u : guestDAO.getAll())
		{
			if(u.getUsername().equals(loginDTO.getUsername())
					&& u.getPassword().equals(loginDTO.getPassword()))
			{
				return u;
			}
		}
		
		for(UserHost u : hostDAO.getAll())
		{
			if(u.getUsername().equals(loginDTO.getUsername())
					&& u.getPassword().equals(loginDTO.getPassword()))
			{
				return u;
			}
		}
		
		return null;
	}
	
	public static Route test = (Request request, Response response) ->
	{
		response.type("application/json");
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
				response.body(claims.getBody().getSubject());
				response.status(200);
				return response;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				response.status(404);
				return response;
			}
		}
	};
	
	public static Route login = (Request request, Response response) ->
	{
		response.type("application/json");
		String payload = request.body();
		LoginDTO user = gson.fromJson(payload, LoginDTO.class);
		UserBase loggedInUser = LoginServices.loggedInUser(user);
		if(loggedInUser == null)
		{
			response.status(404);
			return null;
		}
		response.status(200);
		String jws = Jwts.builder().setSubject(loggedInUser.getUsername()).signWith(key).compact();
		
		return jws;
	};
}
