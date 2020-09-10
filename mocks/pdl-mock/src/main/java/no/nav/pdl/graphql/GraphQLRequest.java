package no.nav.pdl.graphql;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class GraphQLRequest {
    private final String query;
    private final String operationName;
    private final Map<String, Object> variables;

    @JsonCreator
    public GraphQLRequest(@JsonProperty("query") String query,
						  @JsonProperty("operationName") String operationName,
						  @JsonProperty("variables") Map<String, Object> variables) {
        this.query = query;
        this.operationName = operationName;
        this.variables = variables;
    }

    private GraphQLRequest(Builder builder) {
        query = builder.query;
        operationName = builder.operationName;
        variables = builder.variables;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(GraphQLRequest copy) {
        Builder builder = new Builder();
        builder.query = copy.getQuery();
        builder.operationName = copy.getOperationName();
        builder.variables = copy.getVariables();
        return builder;
    }

    public String getQuery() {
        return query;
    }

    public String getOperationName() {
        return operationName;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public static final class Builder {
        private String query;
        private String operationName;
        private Map<String, Object> variables;

        private Builder() {
        }

        public Builder withQuery(String query) {
            this.query = query;
            return this;
        }

        public Builder withOperationName(String operationName) {
            this.operationName = operationName;
            return this;
        }

        public Builder withVariables(Map<String, Object> variables) {
            this.variables = variables;
            return this;
        }

        public GraphQLRequest build() {
            return new GraphQLRequest(this);
        }
    }
}
