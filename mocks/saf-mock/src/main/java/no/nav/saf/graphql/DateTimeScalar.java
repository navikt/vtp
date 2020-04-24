package no.nav.saf.graphql;

import static java.time.temporal.ChronoUnit.SECONDS;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Joakim Bjørnstad, Jbit AS
 */
final class DateTimeScalar {
	static final GraphQLScalarType DATE_TIME = GraphQLScalarType.newScalar()
			.name("DateTime")
			.description("Identifikasjon av dato og tidspunkt etter ISO-8601 standarden.")
			.coercing(new Coercing() {
				@Override
				public Object serialize(Object dataFetcherResult) throws CoercingSerializeException {
					if (dataFetcherResult instanceof LocalDateTime) {
						return ((LocalDateTime) dataFetcherResult).truncatedTo(SECONDS).toString();
					}
					// TODO: En custommapping i pom er satt opp for denne, må fikses. Fikser her gjennom hardkoding
					if (dataFetcherResult instanceof Date) {
						return ((Date)dataFetcherResult).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().truncatedTo(SECONDS).toString();
					}
					throw new CoercingSerializeException("Serialisering av " + dataFetcherResult.getClass() + " til " + DATE_TIME.getName() + " er ikke implementert.");
				}

				@Override
				public Object parseValue(Object input) throws CoercingParseValueException {
					throw new CoercingParseValueException("Parsing av query variabel " + input.getClass() + " til " + DATE_TIME.getName() + " er ikke implementert.");
				}

				@Override
				public Object parseLiteral(Object input) throws CoercingParseLiteralException {
					throw new CoercingParseLiteralException("Parsing av literal " + input.getClass() + " til " + DATE_TIME.getName() + " er ikke implementert.");
				}
			})
			.build();

	private DateTimeScalar() {
		// ingen instansiering
	}
}
