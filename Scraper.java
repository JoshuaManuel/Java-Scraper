import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Scraper {
	static String audioFormatsList = "3gp.act.aiff.aac.amr.au.awb.dct.dss.dvf.flac.gsm.iklax.ivs.m4a.m4p.mmf.mp3.mpc.msv.ogg.oga.opus.ra.rm.raw.sln.tta.vox.wav.wma.wv.webm";
	static String videoFormatsList = "avi.flv.wmv.mov.mp4.mpg.swf";
	static String imageFormatsList = "jpeg.jpg.exif.tiff.rif.gif.bmp.png";
	
	String[] audioFormats;
	String[] videoFormats;
	String[] imageFormats;
	
	String data = "";
	
	Scraper() {
		//create the audioArray, videoArray
		audioFormats = audioFormatsList.split("\\.");
		videoFormats = videoFormatsList.split("\\.");
		imageFormats = imageFormatsList.split("\\.");
		
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader("data.txt");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                data += line;
            }    

            // Always close files.
            bufferedReader.close();            
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file");        
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
	}
	
	//sets data var
	public boolean getURL(String url) {
		try {
		    URL myURL = new URL(url);
		    URLConnection connection = myURL.openConnection();
		    connection.connect();
		    
		    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		    String inputLine;
		    
		    while((inputLine = in.readLine())!= null) {
		    	inputLine += "\n";
		    	data += inputLine;
		    }
		    in.close();
		    System.out.println(data);
		    return true;
		} 
		catch (MalformedURLException ex) { 
		    // new URL() failed
		    ex.printStackTrace();
		} 
		catch (IOException ex) {   
		    // openConnection() failed
		    ex.printStackTrace();
		}
		return false;
	}
	
	//feed null for all links
	/**
	 * Returns a string array with the links on a webpage that begin with "http"
	 * Unfortunately, filter() cannot recognize relative links or those that don't begin with http
	 * @param suffixes the suffixes to filter. Pass NULL to get all links with "http"
	 * @return array of links on a page
	 */
	public String[] filter(String[] suffixes) {
		
		/*
		 * try brute forcing with this?
		 *  URL u = new URL(name); // this would check for the protocol
			u.toURI(); // does the extra checking required for validation of URI 
		 */
		String[] splitData = data.split("" + "\"" + "|'");
		ArrayList<String> links = new ArrayList<String>();
		
		for(String line: splitData) {
			if (line.length() >= 4 && line.substring(0, 4).equals("http")) {
				if(suffixes != null) {
					for(String suffix : suffixes) {
						if(line.substring(line.length()-suffix.length()).equals(suffix)) {
							links.add(line);
						}
					}
				}
				else {
					links.add(line);
				}
			}
		}
		
		//some embedded content has backslashed links. Deal with it:
		for(int i = 0; i < links.size(); i++) {
			StringBuilder sb = new StringBuilder(links.get(i));
			for(int charNum = 0; charNum < sb.length(); charNum++) {
				if (sb.charAt(charNum) == '\\') {
					sb.deleteCharAt(charNum);
				}
			}
			links.set(i, sb.toString());
		}
		
		return links.toArray(new String[links.size()]);
	}
	
	/**
	 * Downloads all items from an array of URLs
	 * @param items array of URLs
	 * @throws IOException
	 */
	public void download(String[] items) throws IOException {
		try{
			for(int i = 0; i < items.length; i++) {
				String[] a = items[i].split("/");
				scrapeItem(items[i], a[a.length-1]);
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Saves the linked item to the current directory
	 * @param item URL of item
	 * @param destinationFile 
	 * @throws IOException
	 */
	public void scrapeItem(String item, String destinationFile) throws IOException {
		URL url = new URL(item);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
}
