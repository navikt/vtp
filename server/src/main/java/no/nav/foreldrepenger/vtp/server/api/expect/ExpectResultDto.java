package no.nav.foreldrepenger.vtp.server.api.expect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ExpectResultDto")
public class ExpectResultDto {

    @JsonInclude(content = Include.NON_EMPTY)
    @JsonProperty("isExpectationMet")
    @ApiModelProperty
    protected boolean isExpectationMet;

    @JsonProperty("resultData")
    @ApiModelProperty
    protected String resultData;

    public ExpectResultDto() {

    }

    public ExpectResultDto(boolean result, String resultData) {
        this.isExpectationMet = result;
        this.resultData = resultData;
    }
}
