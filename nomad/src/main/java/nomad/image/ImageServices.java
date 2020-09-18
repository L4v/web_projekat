package nomad.image;

import static nomad.utils.Responses.serverError;

import java.nio.file.Files;
import java.nio.file.Paths;

import spark.Request;
import spark.Response;
import spark.Route;

public class ImageServices
{

	public static Route getImage = (Request request, Response response) ->
	{
		String filename = request.queryParams("filename");
		try
		{
		    return Files.readAllBytes(Paths.get(filename));
		}
		catch (Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}

	};
}
