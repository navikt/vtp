package no.nav.pdl.exceptions;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

public class SafFunctionalException extends RuntimeException implements GraphQLError {
	private final HttpStatus httpStatus;

	public SafFunctionalException() {
		super();
		httpStatus = null;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public SafFunctionalException(String message) {
		super(message);
		this.httpStatus = null;
	}

	public SafFunctionalException(HttpStatus httpStatus) {
		super();
		this.httpStatus = httpStatus;
	}

	public SafFunctionalException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public SafFunctionalException(String message, Throwable cause, HttpStatus httpStatus) {
		super(message, cause);
		this.httpStatus = httpStatus;
	}

	public SafFunctionalException(String message, Throwable cause) {
		super(message, cause);
		this.httpStatus = null;
	}

	@Override
	public List<SourceLocation> getLocations() {
		return new ArrayList<>();
	}

	@Override
	public ErrorType getErrorType() {
		return ErrorType.DataFetchingException;
	}
}
