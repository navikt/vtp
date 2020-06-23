package no.nav.saf.graphql;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * @author Joakim Bjørnstad, Jbit AS
 */
final class DateScalar {
	static final GraphQLScalarType DATE = GraphQLScalarType.newScalar()
			.name("Date")
			.description("Identifikasjon av et døgn i kalenderen etter ISO-8601 standarden.")
			.coercing(new Coercing() {
				@Override
				public Object serialize(Object dataFetcherResult) throws CoercingSerializeException {
					if (dataFetcherResult instanceof LocalDate) {
						return dataFetcherResult.toString();
					}
					throw new CoercingSerializeException("Serialisering av " + dataFetcherResult.getClass() + " til " + DATE.getName() + " er ikke implementert.");
				}

				@Override
				public Object parseValue(Object input) throws CoercingParseValueException {
					return parseDateFromString(input);
				}

				@Override
				public Object parseLiteral(Object input) throws CoercingParseLiteralException {
					return parseDateFromString(input);
				}
			})
			.build();

	private DateScalar() {
		// ingen instansiering
	}
	
	private LocalDate createLocalDateFromString(Object input) throws CoercingParseLiteralException {
		if (input instanceof StringValue) {
			try {
				return LocalDate.parse(((StringValue) input).getValue());
			} catch (DateTimeParseException e) {
				throw new CoercingParseLiteralException("Verdi er ikke en gyldig Date: " + input.toString());
			}
		}
		throw new CoercingParseLiteralException("Verdi er ikke en gyldig Date: " + input.toString());
	}
}
