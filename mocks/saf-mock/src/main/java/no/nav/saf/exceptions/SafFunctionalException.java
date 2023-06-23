package no.nav.saf.exceptions;

import java.util.ArrayList;
import java.util.List;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class SafFunctionalException extends RuntimeException implements GraphQLError {

	public SafFunctionalException() {
		super();
	}

	public SafFunctionalException(String message) {
		super(message);
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
