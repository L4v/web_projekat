package nomad;

import static spark.Spark.*;

public class Application{

	public static void main(String args[])
	{
		port(8080);
		staticFiles.location("/static");
		get("/hello", (request, response) -> "Hello");
	}
	
}
