package io;

import errors.NoFileFoundError;

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
	 * TODO: Handle File Reading With Errors
	 *
	 * @return String corresponding to content within the file.
	 * @throws NoFileFoundError - Thrown if missing file by name
	 */
	public String read() throws NoFileFoundError{
		if( filename == null )
			throw new NoFileFoundError("");
		return filename;
	}
}
