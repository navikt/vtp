package no.nav.foreldrepenger.vtp.server.api.expect;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import no.nav.foreldrepenger.vtp.felles.ExpectRepository.Mock;

/** Token som sier hvilken forventning som er gjort */
@ApiModel(value = "ExpectTokenDto")
public class ExpectTokenDto extends ExpectRequestDto{

    @JsonInclude(content = Include.NON_EMPTY)
    @JsonProperty("token")
    @ApiModelProperty(example = "string", required = true)
    protected String token;

    public ExpectTokenDto() {

    }

    public ExpectTokenDto(Mock mock, String webMethod) {
        super(mock, webMethod);
        token = LocalDate.now().toString(); //TODO find better token
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
