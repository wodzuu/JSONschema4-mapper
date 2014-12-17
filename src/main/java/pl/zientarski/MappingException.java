package pl.zientarski;

public class MappingException extends RuntimeException {

    private static final long serialVersionUID = 4197588085531294416L;

    public MappingException(final Throwable innerException) {
        super(innerException);
    }

    public MappingException(final String message) {
        super(message);
    }

}
