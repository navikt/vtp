package no.nav.fager.exceptions;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.ArrayList;
import java.util.List;

public class FagerFunctionalException extends RuntimeException implements GraphQLError {

	public FagerFunctionalException() {
		super();
	}

	public FagerFunctionalException(String message) {
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
