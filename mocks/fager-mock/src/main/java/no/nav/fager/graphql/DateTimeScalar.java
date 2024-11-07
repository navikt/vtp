package no.nav.fager.graphql;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

public final class DateTimeScalar {
	public static final GraphQLScalarType ISO_8601_DATE_TIME = GraphQLScalarType.newScalar()
			.name("ISO8601DateTime")
			.description("Identifikasjon av dato og tidspunkt etter ISO-8601 standarden.")
			.coercing(new Coercing() {
				@Override
				public Object serialize(Object dataFetcherResult) throws CoercingSerializeException {
                    if (dataFetcherResult instanceof OffsetDateTime offsetDateTime) {
                        return offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    }
                    throw new CoercingParseValueException("Expected an OffsetDateTime object.");
				}

				@Override
				public Object parseValue(Object input) throws CoercingParseValueException {
                    try {
                        return OffsetDateTime.parse(input.toString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    } catch (Exception e) {
                        throw new CoercingParseValueException("Invalid ISO8601 DateTime format.");
                    }
				}

				@Override
				public Object parseLiteral(Object input) throws CoercingParseLiteralException {
                    if (input instanceof StringValue stringValue) {
                        return OffsetDateTime.parse(stringValue.getValue(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    }
                    throw new CoercingParseLiteralException("Expected ISO8601 formatted date-time string.");
				}
			})
			.build();

	private DateTimeScalar() {
		// ingen instansiering
	}
}
