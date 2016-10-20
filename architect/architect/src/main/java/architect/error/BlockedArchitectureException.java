package architect.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK)
public class BlockedArchitectureException extends Exception {

	private static final long serialVersionUID = 9209036428435860973L;
	
	public BlockedArchitectureException(String message) {
		super(message);
	}

}
