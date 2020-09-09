package nomad.comment;


import static nomad.utils.Responses.notFound;
import static nomad.Application.parseJws;
import static nomad.Application.key;
import static nomad.Application.gson;
import static nomad.Application.hostDAO;
import static nomad.Application.commentDAO;
import static nomad.Application.adminDAO;
import static nomad.Application.guestDAO;
import static nomad.Application.apartmentDAO;

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
			return notFound("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if(host == null)
			{
				return notFound("Invalid host", response);
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
			return notFound("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route adminViewComments = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if(jws == null)
		{
			return notFound("Invalid login", response);
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
			return notFound("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route guestViewApartmentsComments = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if(jws == null)
		{
			return notFound("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if(guest == null)
			{
				response.status(404);
				return response;
			}
			
			String json = request.body();
			Apartment apartment = gson.fromJson(json, Apartment.class);
			
			response.type("application/json");
			response.status(200);
			ArrayList<Comment> comments = (ArrayList<Comment>) apartment.getComments();
			response.body(gson.toJson(comments));
			return response;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return notFound("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route addComment = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if(jws == null)
		{
			return notFound("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if(guest == null)
			{
				return notFound("Invalid guest", response);
			}
			
			String json = request.body();
			Comment comment = gson.fromJson(json, Comment.class);

			if(!addCommentToApartment(comment))
			{
				return notFound("Don't have a rejected or cancelled reservation!", response);
			}
			
			response.status(200);
			response.body("Comment added");
			return response;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return notFound("Server error: " + e.getMessage(), response);
		}
	};
	
	private static boolean addCommentToApartment(Comment comment) 
	{
		Apartment apartment = comment.getApartment();
		UserGuest guest = comment.getGuest();
		if(apartment.getReservations() != null)
		{
			for(Reservation reservation : apartment.getReservations())
			{
				if(reservation.getStatus() == ReservationStatus.CANCELLED || reservation.getStatus() == ReservationStatus.REJECTED)
				{
					apartment.addComment(guest.getUsername(), comment);
					apartmentDAO.update(apartment);
					return true;
				}
			}
		}
		return false;
	}
}
