package errors;

public class NoFileFoundError extends Exception {

	public NoFileFoundError(String filename){
		super("No file found by the name '" + filename + "'");
	}

}
