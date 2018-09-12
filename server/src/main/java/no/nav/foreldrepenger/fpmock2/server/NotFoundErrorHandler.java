package no.nav.foreldrepenger.fpmock2.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Fang alle NOT_FOUND og log og send respons.  For å forenkle implementasjon av manglende tjenester. */
public class NotFoundErrorHandler extends ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(NotFoundErrorHandler.class);

    @Override
    protected void generateAcceptableResponse(Request baseRequest, HttpServletRequest req, HttpServletResponse response, int code, String message)
            throws IOException {
        if (code != HttpServletResponse.SC_NOT_FOUND) {
            super.generateAcceptableResponse(baseRequest, req, response, code, message);
            return;
        }

        String fullUrl = getFullURL(req);

        // skip known unkonwns

        if (fullUrl.contains("favicon.ico")) {
            return;
        }

        StringBuilder logMsg = new StringBuilder("NOT_FOUND: ").append(req.getMethod()).append(" ").append(fullUrl);
        for (String header : Collections.list(req.getHeaderNames())) {
            logMsg.append("\n\t").append(header).append("=").append(req.getHeader(header));
        }

        try (BufferedReader br = req.getReader()) {
            br.lines().forEach(line -> logMsg.append("\n\t").append(line));
        }

        String logMessage = logMsg.toString();
        log.warn(logMessage);
        
        baseRequest.setHandled(true);
        @SuppressWarnings("resource")
        Writer writer = getAcceptableWriter(baseRequest,req,response);
        if (writer!=null)
        {
            response.setContentType(MimeTypes.Type.TEXT_PLAIN.asString());
            writer.write(logMessage);
        }
        baseRequest.getResponse().closeOutput();
    }

    public static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

}
