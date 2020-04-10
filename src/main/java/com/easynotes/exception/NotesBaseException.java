/*
 *
 */
package com.easynotes.exception;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotesBaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final int errorCode;

	public NotesBaseException(String message, int errorCode ) {
		super(message);
		this.errorCode = errorCode;
	}


}