package nomad.utils;

import spark.Response;

public class Responses
{
	// NOTE(Jovan): 401 Unauthorized
	public static Response unauthorized(String msg, Response response)
	{
		response.body(msg);
		response.status(401);
		return response;
	}

	// NOTE(Jovan): 404 Not found
	public static Response notFound(String msg, Response response)
	{
		response.body(msg);
		response.status(404);
		return response;
	}
}
