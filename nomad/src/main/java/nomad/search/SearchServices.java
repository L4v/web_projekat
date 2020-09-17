package nomad.search;

import static nomad.Application.gson;
import static nomad.Application.searchDAO;
import static nomad.Application.apartmentDAO;
import static nomad.utils.Responses.ok;
import static nomad.utils.Responses.serverError;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.google.gson.reflect.TypeToken;

import nomad.apartment.Apartment;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchServices
{
	
	// NOTE(Jovan): Used as a metric for search results
	/*private static int levenshteinDistance(String firstWord, String secondWord)
	{
		if (firstWord.length() == 0)
			return secondWord.length();
		if (secondWord.length() == 0)
			return firstWord.length();

		if (firstWord.charAt(0) == secondWord.charAt(0))
		{
			return levenshteinDistance(firstWord.substring(1), secondWord.substring(1));
		}

		int a = levenshteinDistance(firstWord.substring(1), secondWord.substring(1));
		int b = levenshteinDistance(firstWord, secondWord.substring(1));
		int c = levenshteinDistance(firstWord.substring(1), secondWord);

		if (a > b)
			a = b;
		if (a > c)
			a = c;

		return a + 1;
	}*/
	
	public static Route searchApartment = (Request request, Response response) ->
	{
		SearchDTO search = gson.fromJson(request.body(), SearchDTO.class);
		ArrayList<Apartment> allApartments = (ArrayList<Apartment>) apartmentDAO.getAll();
		ArrayList<Apartment> results = new ArrayList<Apartment>(allApartments);
		
		if(search.getFromDate() != -1L)
		{
			results = (ArrayList<Apartment>) results.stream().filter(a -> 
				a.getAvailableDates().stream().filter(ad ->
					ad.getStart().getTime() >= search.getFromDate()).findAny().orElse(null) != null)
					.collect(Collectors.toList());
		}
		
		if(search.getToDate() != -1L)
		{
			results = (ArrayList<Apartment>) results.stream().filter(a -> 
				a.getAvailableDates().stream().filter(ad ->
					ad.getEnd().getTime() <= search.getToDate()).findAny().orElse(null) != null) 
					.collect(Collectors.toList());
		}
		
		if(!search.getArea().isBlank())
		{
			results = (ArrayList<Apartment>) results.stream().filter(a ->
				a.getLocation().getAddress().getArea().equalsIgnoreCase(search.getArea())).collect(Collectors.toList());
		}
		
		if(search.getFromPrice() != -1)
		{
			results = (ArrayList<Apartment>) results.stream().filter(a ->
			    a.getPrice() >= search.getFromPrice()).collect(Collectors.toList());
		}
		
		if(search.getToPrice() != -1)
		{
			results = (ArrayList<Apartment>) results.stream().filter(a ->
			    a.getPrice() <= search.getToPrice()).collect(Collectors.toList());
		}
		
		if(search.getFromRoom() != -1)
		{
			results = (ArrayList<Apartment>) results.stream().filter(a ->
			    a.getNoRooms() >= search.getFromRoom()).collect(Collectors.toList());
		}
		
		if(search.getToRoom() != -1)
		{
			results = (ArrayList<Apartment>) results.stream().filter(a ->
				a.getNoRooms() <= search.getToRoom()).collect(Collectors.toList());
		}
		
		if(search.getNoGuests() != -1)
		{
			results = (ArrayList<Apartment>) results.stream().filter(a ->
			    a.getNoGuests() == search.getNoGuests()).collect(Collectors.toList());
		}
		
		String json = gson.toJson(results);
		return ok(json, response);
	};
	
	public static Route saveSearch = (Request request, Response response) ->
	{
		Type collectionType = new TypeToken<Collection<String>>(){}.getType();
		ArrayList<String> results = gson.fromJson(request.body(), collectionType);
		if(results == null)
		{
			return serverError("Results were null", response);
		}
		searchDAO.save(results);
		return ok("Results saved", response);
	};
	
	public static Route getSearch = (Request request, Response response) ->
	{
		ArrayList<String> results = (ArrayList<String>) searchDAO.get();
		String json = gson.toJson(results);
		response.type("application/json");
		return ok(json, response);
	};
}
