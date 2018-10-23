package no.nav.foreldrepenger.autotest.util.http.rest;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class JsonRest extends Rest{
    
    private static String ACCEPT_JSON_HEADER = "application/json";

    public JsonRest(HttpSession session) {
        super(session);
    }
    
    
    
    /*
     * POST
     */
    protected HttpResponse postJson(String url, Object object) throws IOException {
        return postJson(url, hentObjectMapper().writeValueAsString(object));
    }
    
    protected HttpResponse postJson(String url, Object object, Map<String, String> headers) throws IOException {
        return postJson(url, hentObjectMapper().writeValueAsString(object), headers);
    }

    protected HttpResponse postJson(String url, String json) throws IOException {
        Map<String, String> headers = new HashMap<>();
        return postJson(url, json, headers);
    }

    protected HttpResponse postJson(String url, String json, Map<String, String> headers) throws IOException {
        headers.put("Accept", ACCEPT_JSON_HEADER);
        return post(url, hentJsonPostEntity(json), headers);
    }
    
    protected <T> T postOgHentJson(String url, Object requestData, JavaType returnType, StatusRange expectedStatusRange) throws IOException {
        return postOgHentJson(url, requestData, new HashMap<>(), returnType, expectedStatusRange);
    }
    
    protected <T> T postOgHentJson(String url, Object requestData, Class<T> returnType, StatusRange expectedStatusRange) throws IOException {
        return postOgHentJson(url, requestData, new HashMap<>(), returnType, expectedStatusRange);
    }
    
    protected <T> T postOgHentJson(String url, Object requestData, Map<String, String> headers, Class<T> returnType, StatusRange expectedStatusRange) throws IOException {
        String json = postOgVerifiser(url, requestData, headers, expectedStatusRange);
        return json.equals("") ? null : hentObjectMapper().readValue(json, returnType);
    }
    
    protected <T> T postOgHentJson(String url, Object requestData, Map<String, String> headers, JavaType returnType, StatusRange expectedStatusRange) throws IOException {
        String json = postOgVerifiser(url, requestData, headers, expectedStatusRange);
        return json.equals("") ? null : hentObjectMapper().readValue(json, returnType);
    }
    
    protected String postOgVerifiser(String url, Object requestData, StatusRange expectedStatusRange) throws IOException {
        return postOgVerifiser(url, requestData, new HashMap<>(), expectedStatusRange);
    }
    
    protected String postOgVerifiser(String url, Object requestData, Map<String, String> headers, StatusRange expectedStatusRange) throws IOException {
        String request = hentObjectMapper().writeValueAsString(requestData);
        HttpResponse response = postJson(url, request, headers);
        String json = hentResponseBody(response);
        if(expectedStatusRange != null) {
            ValidateResponse(response, expectedStatusRange, url + "\n" + request +"\n\n" + json);
        }
        return json;
    }

    
    /*
     * GET
     */
    protected HttpResponse getJson(String url) throws IOException {
        return getJson(url, new HashMap<>());
    }

    protected HttpResponse getJson(String url, Map<String, String> headers, Map<String, String> data) throws IOException {
        return getJson(url + UrlEncodeQuery(data), headers);
    }
    
    protected HttpResponse getJson(String url, Map<String, String> headers) throws IOException {
        headers.put("Accept", ACCEPT_JSON_HEADER);
        return get(url, headers);
    }
    
    protected <T> T getOgHentJson(String url, Class<T> returnType, StatusRange expectedStatusRange) throws IOException {
        return getOgHentJson(url, new HashMap<>(), returnType, expectedStatusRange);
    }
    
    protected <T> T getOgHentJson(String url, JavaType returnType, StatusRange expectedStatusRange) throws IOException {
        return getOgHentJson(url, new HashMap<>(), returnType, expectedStatusRange);
    }
    
    protected <T> T getOgHentJson(String url, Map<String, String> headers, Class<T> returnType, StatusRange expectedStatusRange) throws IOException {
        HttpResponse response = getJson(url, headers);
        String json = hentResponseBody(response);
        ValidateResponse(response, expectedStatusRange, url + "\n\n" + json);
        return hentObjectMapper().readValue(json, returnType);
    }
    
    protected <T> T getOgHentJson(String url, Map<String, String> headers, JavaType returnType, StatusRange expectedStatusRange) throws IOException {
        HttpResponse response = getJson(url, headers);
        String json = hentResponseBody(response);
        ValidateResponse(response, expectedStatusRange, url + "\n\n" + json);
        return hentObjectMapper().readValue(json, returnType);
    }
    
    
    /*
     * PUT
     */
    protected HttpResponse putJson(String url, Object requestData, StatusRange expectedStatusRange) throws IOException {
        String json = hentObjectMapper().writeValueAsString(requestData);
        return putJson(url, json, expectedStatusRange);
    }
    
    protected HttpResponse putJson(String url, String json, StatusRange expectedStatusRange) throws IOException {
        HttpResponse response = put(url, hentJsonPostEntity(json));
        ValidateResponse(response, expectedStatusRange);
        return response;
    }

    
    protected StringEntity hentJsonPostEntity(String json) {
        try {
            return new StringEntity(json, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    protected ObjectMapper hentObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
