package no.nav.pdl;

import java.util.ArrayList;
import java.util.List;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class PdlFunctionalException extends RuntimeException implements GraphQLError {

	public PdlFunctionalException(String message, Throwable cause) {
		super(message, cause);
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
