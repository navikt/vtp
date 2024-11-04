package no.nav.fager.graphql;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.GraphQLScalarType;

public final class LocalDateTimeScalar {
	public static final GraphQLScalarType ISO_8601_LOCAL_DATE_TIME = GraphQLScalarType.newScalar()
			.name("ISO8601LocalDateTime")
			.description("Identifikasjon av et døgn i kalenderen etter ISO-8601 standarden.")
			.coercing(new Coercing() {
                @Override
                public String serialize(Object dataFetcherResult) {
                    if (dataFetcherResult instanceof LocalDateTime localDateTime) {
                        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    }
                    throw new CoercingParseValueException("Expected a LocalDateTime object.");
                }

                @Override
                public LocalDateTime parseValue(Object input) {
                    try {
                        return LocalDateTime.parse(input.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    } catch (Exception e) {
                        throw new CoercingParseValueException("Invalid ISO8601 LocalDateTime format.");
                    }
                }

                @Override
                public LocalDateTime parseLiteral(Object input) {
                    if (input instanceof StringValue stringValue) {
                        return LocalDateTime.parse(stringValue.getValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    }
                    throw new CoercingParseLiteralException("Expected ISO8601 formatted local date-time string.");
                }
			})
			.build();

	private LocalDateTimeScalar() {
		// ingen instansiering
	}
}
