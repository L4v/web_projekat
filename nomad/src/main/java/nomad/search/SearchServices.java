package nomad.search;

public class SearchServices
{
	
	// NOTE(Jovan): Used as a metric for search results
	private static int levenshteinDistance(String firstWord, String secondWord)
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
	}
}
