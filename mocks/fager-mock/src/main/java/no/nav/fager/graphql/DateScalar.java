package no.nav.fager.graphql;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

public final class DateScalar {
	public static final GraphQLScalarType DATE = GraphQLScalarType.newScalar()
			.name("ISO8601Date")
			.description("Identifikasjon av et døgn i kalenderen etter ISO-8601 standarden.")
			.coercing(new Coercing() {
				@Override
				public Object serialize(Object dataFetcherResult) throws CoercingSerializeException {
					if (dataFetcherResult instanceof LocalDate) {
						return dataFetcherResult.toString();
					}
					if (dataFetcherResult instanceof String) {
						return dataFetcherResult;
					}
					throw new CoercingSerializeException("Serialisering av " + dataFetcherResult.getClass() + " til " + DATE.getName() + " er ikke implementert.");
				}

				@Override
				public Object parseValue(Object input) throws CoercingParseValueException {
					throw new CoercingParseValueException("Parsing av query variabel " + input.getClass() + " til " + DATE.getName() + " er ikke implementert.");
				}

				@Override
				public Object parseLiteral(Object input) throws CoercingParseLiteralException {
					if (input instanceof StringValue stringValue) {
						try {
							return LocalDate.parse(stringValue.getValue());
						} catch (DateTimeParseException e) {
							throw new CoercingParseLiteralException("Verdi er ikke en gyldig Date: " + input);
						}
					}
					throw new CoercingParseLiteralException("Verdi er ikke en gyldig Date: " + input.toString());
				}
			})
			.build();

	private DateScalar() {
		// ingen instansiering
	}
}
