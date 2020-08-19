package nomad.comment;

import static nomad.Application.parseJws;
import static nomad.Application.invalidResponse;

import spark.Request;
import spark.Response;
import spark.Route;

public class CommentServices
{
	public static Route hostViewComments = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if(jws == null)
		{
			return invalidResponse("Invalid login", response);
		}
		
		
		return null;
	};
}
