package nomad.comment;


import static nomad.Application.parseJws;
import static nomad.Application.invalidResponse;
import static nomad.Application.key;
import static nomad.Application.gson;
import static nomad.Application.hostDAO;
import static nomad.Application.commentDAO;
import static nomad.Application.adminDAO;
import static nomad.Application.guestDAO;

import java.util.ArrayList;
import java.util.Collection;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.apartment.Apartment;
import nomad.reservation.Reservation;
import nomad.reservation.ReservationStatus;
import nomad.user.UserAdmin;
import nomad.user.UserGuest;
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
	
	public static Route adminViewComments = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if(jws == null)
		{
			return invalidResponse("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if(admin == null)
			{
				response.status(404);
				return response;
			}
			response.type("application/json");
			response.status(200);
			ArrayList<Comment> comments = (ArrayList<Comment>) commentDAO.getAll();
			response.body(gson.toJson(comments));
			return response;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return invalidResponse("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route addComment = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if(jws == null)
		{
			return invalidResponse("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if(guest == null)
			{
				return invalidResponse("Invalid guest", response);
			}
			
			//here comes adding comment to the specific apartment
			
			/*dobaviti jos komentar i apartman
			Apartment apartment;
			Comment comment;
			if(!addCommentToApartment(guest.getUsername(), comment, apartment))
			{
				return invalidResponse("Don't have a rejected or cancelled reservation!", response);
			}*/
			
			response.status(200);
			return response;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return invalidResponse("Server error: " + e.getMessage(), response);
		}
	};
	
	private static boolean addCommentToApartment(String username, Comment comment, Apartment apartment) 
	{
		if(apartment.getReservations() != null)
		{
			for(Reservation reservation : apartment.getReservations())
			{
				if(reservation.getStatus() == ReservationStatus.CANCELLED || reservation.getStatus() == ReservationStatus.REJECTED)
				{
					apartment.addComment(username, comment);
					return true;
				}
			}
		}
		return false;
	}
}
