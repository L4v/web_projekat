package nomad;

import static spark.Spark.*;

import spark.servlet.SparkApplication;

public class Application implements SparkApplication{

	@Override
	public void init()
	{
		staticFileLocation("/static");
		
	}
	
}
