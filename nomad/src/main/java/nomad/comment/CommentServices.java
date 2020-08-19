package nomad.comment;


import static nomad.Application.parseJws;
import static nomad.Application.invalidResponse;
import static nomad.Application.key;
import static nomad.Application.gson;
import static nomad.Application.hostDAO;

import java.util.ArrayList;
import java.util.Collection;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.apartment.Apartment;
import nomad.user.UserHost;
import spark.Route;
import spark.Request;
import spark.Response;

public class CommentServices
{
	
	public static Collection<Comment> allHostComments(UserHost host)
	{
		ArrayList<Comment> comments = new ArrayList<Comment>();
		
		for(Apartment apartment : host.getApartments())
		{
			if(apartment.getHost().getUsername().equals(host.getUsername()))
			{
				comments.addAll((ArrayList<Comment>)apartment.getComments());
			}
		}
		
		return comments;
	}
	public static Route hostViewComments = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if(jws == null)
		{
			return invalidResponse("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if(host == null)
			{
				return invalidResponse("Invalid host", response);
			}
			response.type("application/json");
			response.status(200);
			ArrayList<Comment> comments = (ArrayList<Comment>) allHostComments(host);
			response.body(gson.toJson(comments));
			return response;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return invalidResponse("Server error: " + e.getMessage(), response);
		}
	};
}
