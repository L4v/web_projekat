package nomad;

import static spark.Spark.*;

public class Application{

	public static void main(String args[])
	{
		staticFileLocation("/static");
		port(8080);
	}
	
}
