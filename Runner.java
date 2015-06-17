import java.io.IOException;

public class Runner {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scraper s = new Scraper();
		s.getURL("http://stackoverflow.com/questions/5120171/extract-links-from-a-web-page");
		//s.scrapeItem("https://en.wikipedia.org/wiki/Id%C3%A9e_fixe_%28psychology%29", "IdeFixe.html");
		//String[] filt = s.filter(s.imageFormats);
		
		//s.download(filt);
		
	}

}
