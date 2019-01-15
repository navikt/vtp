package no.nav.foreldrepenger.fpmock2.server.api.expect;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.fpmock2.felles.ExpectPredicate;
import no.nav.foreldrepenger.fpmock2.felles.ExpectRepository.Mock;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ExpectRequestDto")
public class ExpectRequestDto {
    
    @JsonInclude(content = Include.NON_EMPTY)
    @JsonProperty("mock")
    @ApiModelProperty(example = "ARBEIDSFORHOLD", required = true)
    protected Mock mock;
    
    @JsonInclude(content = Include.NON_EMPTY)
    @JsonProperty("webMethod")
    @ApiModelProperty(example = "string", required = true)
    protected String webMethod;
    
    @JsonInclude(content = Include.NON_EMPTY)
    @JsonProperty("predicate")
    @ApiModelProperty(example = "{ 'aktør' : '12345678' }", required = false)
    protected ExpectPredicate predicate;
    
    public ExpectRequestDto() {
        
    }
    
    public ExpectRequestDto(Mock mock, String webMethod) {
        this.mock = mock;
        this.webMethod = webMethod;
    }

    public Mock getMock() {
        return mock;
    }

    public String getWebMethod() {
        return webMethod;
    }

    public void setMock(Mock mock) {
        this.mock = mock;
    }

    public void setWebMethod(String webMethod) {
        this.webMethod = webMethod;
    }
    
    
}
