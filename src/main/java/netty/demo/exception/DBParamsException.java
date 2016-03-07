package netty.demo.exception;

public class DBParamsException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public DBParamsException(String key, String value){
	super("DB Connection with the params "+key+"["+value+"]");
    }
}
