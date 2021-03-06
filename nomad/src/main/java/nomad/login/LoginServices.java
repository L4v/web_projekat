package nomad.login;

import static nomad.utils.Responses.notFound;

import java.time.ZonedDateTime;
import java.util.Date;

import static nomad.Application.adminDAO;
import static nomad.Application.gson;
import static nomad.Application.guestDAO;
import static nomad.Application.hostDAO;
import static nomad.Application.key;
import static nomad.Application.parseJws;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.user.UserAdmin;
import nomad.user.UserBase;
import nomad.user.UserGuest;
import nomad.user.UserHost;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginServices
{

	public static UserBase loggedInUser(LoginDTO loginDTO)
	{
		for (UserAdmin u : adminDAO.getAll())
		{
			if (u.getUsername().equals(loginDTO.getUsername()) && u.getPassword().equals(loginDTO.getPassword()))
			{
				return u;
			}
		}

		for (UserGuest u : guestDAO.getAll())
		{
			if (u.getUsername().equals(loginDTO.getUsername()) && u.getPassword().equals(loginDTO.getPassword()))
			{
				return u;
			}
		}

		for (UserHost u : hostDAO.getAll())
		{
			if (u.getUsername().equals(loginDTO.getUsername()) && u.getPassword().equals(loginDTO.getPassword()))
			{
				return u;
			}
		}

		return null;
	}

	public static Route verifyLogin = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if (jws == null)
		{
			return notFound("User not found", response);
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			response.body(claims.getBody().getSubject());
			response.status(200);
			return response;
		} catch (Exception e)
		{
			return notFound("S<erver error: " + e.getMessage(), response);
		}

	};

	public static Route login = (Request request, Response response) ->
	{
		String payload = request.body();
		LoginDTO user = gson.fromJson(payload, LoginDTO.class);
		UserBase loggedInUser = LoginServices.loggedInUser(user);
		if (loggedInUser == null)
		{
			return notFound("User not found", response);
		}
		response.type("application/json");
		response.status(200);
		String jws = Jwts.builder().setSubject(loggedInUser.getUsername()).setExpiration(Date.from(ZonedDateTime.now().plusMinutes(30).toInstant())).signWith(key).compact();

		return jws;
	};
}
