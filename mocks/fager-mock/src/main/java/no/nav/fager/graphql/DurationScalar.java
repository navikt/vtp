package no.nav.fager.graphql;

import graphql.language.StringValue;
import graphql.schema.*;

import java.time.Duration;

public final class DurationScalar {
	public static final GraphQLScalarType ISO_8601_DURATION = GraphQLScalarType.newScalar()
			.name("ISO8601Duration")
			.description("Identifikasjon av et døgn i kalenderen etter ISO-8601 standarden.")
			.coercing(new Coercing() {
                @Override
                public String serialize(Object dataFetcherResult) {
                    if (dataFetcherResult instanceof Duration) {
                        return dataFetcherResult.toString(); // Duration.toString returns ISO8601 format
                    }
                    throw new CoercingParseValueException("Expected a Duration object.");
                }

                @Override
                public Duration parseValue(Object input) {
                    try {
                        return Duration.parse(input.toString());
                    } catch (Exception e) {
                        throw new CoercingParseValueException("Invalid ISO8601 Duration format.");
                    }
                }

                @Override
                public Duration parseLiteral(Object input) {
                    if (input instanceof StringValue stringValue) {
                        return Duration.parse(stringValue.getValue());
                    }
                    throw new CoercingParseLiteralException("Expected ISO8601 formatted duration string.");
                }
			})
			.build();

	private DurationScalar() {
		// ingen instansiering
	}
}
