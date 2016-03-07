package netty.demo.exception;

public class UnknownTypeException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public UnknownTypeException(Object value,Class<?> clazz)
    {
	super("UnKnown Enum Type ["+value.toString()+"] in "+clazz.getName());
    }
    
}
