package driver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class FileIn {

	/**
	 * filename
	 *
	 * String of the filename
	 */
	private final String filename;

	/**
	 * Constructor( String )
	 *
	 * @param filename - String of the filename
	 */
	FileIn(String filename) {
		this.filename = filename;
	}

	/**
	 * read()
	 *
	 * @return String corresponding to content within the file.
	 */
	String read(){
		if( filename == null )
			return null;

		try{
			return new String(Files.readAllBytes(Paths.get(filename)));
		} catch (IOException e) {
			return null;
		}
	}
}
