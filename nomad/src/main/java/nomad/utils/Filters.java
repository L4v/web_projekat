package nomad.utils;

import spark.Filter;
import spark.Request;
import spark.Response;

// NOTE(Jovan): Different types of filters for handling responses
public class Filters {

	public static Filter addGzipHeader = (Request request, Response response) ->
	{
		response.header("Content-Encoding", "gzip");
	};
}
