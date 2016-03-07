package netty.demo.exception;

import java.io.IOException;

public class ResourceNotFoundException extends IOException
{
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message)
    {
	super("Resource ["+message+"] not found.");
    }
    
}
