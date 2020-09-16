package nomad.search;

import spark.Request;
import spark.Response;
import spark.Route;

import static nomad.Application.gson;
import static nomad.Application.searchDAO;
import static nomad.utils.Responses.ok;
import static nomad.utils.Responses.serverError;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;

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
