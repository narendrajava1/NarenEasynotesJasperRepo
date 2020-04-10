/*
 *
 */
package com.easynotes.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.easynotes.model.Error;
import com.easynotes.model.Note;
import com.easynotes.model.ServiceResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @ControllerAdvice(assignableTypes = { NoteController.class })
@RestControllerAdvice
public class ServiceExceptionHandler {

	static final String CONTEXT_PATH="Error occurred , request path : {}";



	@ExceptionHandler(Exception.class)
	public ServiceResponse<Note> handleAllExceptions(Exception e,
			WebRequest request) {
		final ServiceResponse<Note> response = new ServiceResponse<>();
		response.setCode(-1);
		final Error error = new Error();
		error.setError(ExceptionUtils.getStackTrace(e));
		error.setMessage(String.valueOf(request));
		response.setError(error);
		return response;
	}
	// @formatter:off
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ServiceResponse<Note> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			WebRequest request) {
		final StringBuilder errors = new StringBuilder();
		ex.getBindingResult().getAllErrors().stream().forEach(objectError->{
			errors.append(objectError.getDefaultMessage());
			errors.append("");
		});

		final ServiceResponse<Note> response = new ServiceResponse<>();
		response.setCode(-1);
		final Error error = new Error();
		error.setError(errors.toString());
		error.setMessage(String.valueOf(request));
		response.setError(error);
		return response;
	}

	@ExceptionHandler(NotesBaseException.class)
	public ServiceResponse<Note> handleNotesBaseException(NotesBaseException e, WebRequest request){
		ServiceExceptionHandler.log.info(ServiceExceptionHandler.CONTEXT_PATH, request.getContextPath());
		ServiceExceptionHandler.log.error(e.getMessage(), e);
		final ServiceResponse<Note> response = new ServiceResponse<>();
		response.setCode(-1);
		final Error error = new Error();
		error.setError(ExceptionUtils.getStackTrace(e));
		error.setMessage(e.getMessage());
		response.setError(error);
		return response;

	}


	// @formatter:off
}