package nomad.user;

import static nomad.Application.apartmentDAO;
import static nomad.Application.gson;

import java.util.ArrayList;
import java.util.Collection;

import nomad.apartment.Apartment;
import nomad.apartment.ApartmentStatus;
import spark.Request;
import spark.Response;
import spark.Route;

public class GuestServices
{
	
	private static Collection<Apartment> getActiveApartments()
	{
		ArrayList<Apartment> activeApartments = new ArrayList<Apartment>();
		
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) apartmentDAO.getAll();
		for (Apartment apartment : apartments) 
		{
			if(apartment.getType().equals(ApartmentStatus.ACTIVE))
			{
				activeApartments.add(apartment);
			}
		}
		return activeApartments;
	}
	
	public static Route allApartments = (Request request, Response response) ->
	{
		response.type("application/json");
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) getActiveApartments();
		return gson.toJson(apartments);
	};

}
