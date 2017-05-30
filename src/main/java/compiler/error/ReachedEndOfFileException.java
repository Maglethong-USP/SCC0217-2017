package compiler.error;


public class ReachedEndOfFileException extends RuntimeException {

    private Error error;

    public ReachedEndOfFileException(Error error) {
        super(error.toString());
        this.error = error;
    }

    public Error getError() {return error;}
}
