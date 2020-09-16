package nomad.comment;



import static nomad.utils.Responses.forbidden;
import static nomad.utils.Responses.serverError;
import static nomad.utils.Responses.ok;

import static nomad.Application.parseJws;
import static nomad.Application.key;
import static nomad.Application.gson;
import static nomad.Application.hostDAO;
import static nomad.Application.commentDAO;
import static nomad.Application.adminDAO;
import static nomad.Application.guestDAO;
import static nomad.Application.apartmentDAO;
import static nomad.Application.reservationDAO;

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
		
		for(Apartment apartment : apartmentDAO.getByIds(host.getApartments()))
		{
			if(apartment.getHostId().equals(host.getUsername()))
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
			return forbidden("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if(host == null)
			{
				return forbidden("Invalid host", response);
			}
			
			ArrayList<Comment> comments = (ArrayList<Comment>) allHostComments(host);
			String json = gson.toJson(comments);
			response.type("application/json");
			return ok(json, response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route adminViewComments = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if(jws == null)
		{
			return forbidden("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if(admin == null)
			{
				return forbidden("Invalid admin", response);
			}
			ArrayList<Comment> comments = (ArrayList<Comment>) commentDAO.getAll();
			String json = gson.toJson(comments);
			return ok(json, response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route guestViewApartmentsComments = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if(jws == null)
		{
			return forbidden("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if(guest == null)
			{
				return forbidden("Invalid guest", response);
			}
			
			String json = request.body();
			Apartment apartment = gson.fromJson(json, Apartment.class);

			ArrayList<Comment> comments = (ArrayList<Comment>) apartment.getComments();
			String jsonRet = gson.toJson(comments);
			return ok(jsonRet, response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route addComment = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if(jws == null)
		{
			return forbidden("Invalid login", response);
		}
		
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if(guest == null)
			{
				return forbidden("Invalid guest", response);
			}
			
			String json = request.body();
			Comment comment = gson.fromJson(json, Comment.class);
			comment.setGuestId(guest.getUsername());
			
			if(!addCommentToApartment(comment))
			{
				return forbidden("Don't have a rejected or cancelled reservation!", response);
			}
			return ok("Comment added", response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	private static boolean addCommentToApartment(Comment comment) 
	{
		Apartment apartment = apartmentDAO.get(comment.getApartmentId());
		ArrayList<Reservation> reservations = (ArrayList<Reservation>) reservationDAO.getForApartment(apartment.getId());
		
		if(apartment.getReservations() != null)
		{
			for(Reservation reservation : reservations)
			{
				if(reservation.getGuestId().equals(comment.getGuestId()))
				{
					if(reservation.getStatus() == ReservationStatus.CANCELLED || reservation.getStatus() == ReservationStatus.REJECTED)
					{
						commentDAO.add(comment);
						apartment.getComments().add(comment);
						apartmentDAO.update(apartment);
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
