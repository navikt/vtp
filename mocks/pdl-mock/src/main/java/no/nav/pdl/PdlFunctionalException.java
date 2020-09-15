package no.nav.pdl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class PdlFunctionalException extends RuntimeException implements GraphQLError {
	private final HttpStatus httpStatus;

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public PdlFunctionalException(String message, Throwable cause) {
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
