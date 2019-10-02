package io;

import errors.NoFileFoundError;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileIn {

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
	public FileIn(String filename) {
		this.filename = filename;
	}

	/**
	 * read()
	 *
	 * @return String corresponding to content within the file.
	 * @throws NoFileFoundError - Thrown if missing file by name
	 */
	public String read() throws NoFileFoundError {
		if( filename == null )
			throw new NoFileFoundError("");

		try{
			return new String(Files.readAllBytes(Paths.get(filename)));
		} catch (IOException e) {
			throw new NoFileFoundError(filename);
		}
	}
}
