package org.sage.proxy.home;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class HomeControllerApi {
    private final String baseUrl;
    private final ObjectMapper mapper;

    public HomeControllerApi(String baseUrl) {
        this.baseUrl = baseUrl;
        mapper = new ObjectMapper();
    }

    public List<String> devices() throws IOException {
        return mapper.readValue(get(format("%s/odin/api/v1/devices/list", baseUrl)).get(), ArrayList.class);
    }

    public JsonNode device(String name) throws IOException {
        return mapper.readTree(get(format("%s/odin/api/v1/device/%s/info", baseUrl, name)).get());
    }

    public void update_device(String name, JsonNode devInfo) throws IOException {
        post(format("%s/odin/api/v1/device/%s/update", baseUrl, name), devInfo);
    }

    public void run_script(String name, JsonNode devInfo) throws IOException {
        post(format("%s/odin/api/v1/script/%s", baseUrl, name), devInfo);
    }

    public void switchOn(String name) throws IOException {
        get(format("%s/odin/api/v1/switch/%s/On", baseUrl, name));
    }

    public void switchOff(String name) throws IOException {
        get(format("%s/odin/api/v1/switch/%s/Off", baseUrl, name));
    }

    private Optional<InputStream> get(String url) throws IOException {
        HttpResponse resp = Request.Get(url)
                .execute()
                .returnResponse();
        if (resp.getStatusLine().getStatusCode() != 200) {
            System.out.println("Error: [" + url + "]" + resp.getStatusLine().getStatusCode());
            return Optional.empty();
        } else {
            return Optional.of(resp.getEntity().getContent());
        }
    }

    private Optional<InputStream> post(String url, JsonNode devInfo) throws IOException {
        HttpResponse resp = Request.Post(url)
                .body(new ByteArrayEntity(mapper.writeValueAsBytes(devInfo), ContentType.APPLICATION_JSON))
                .execute()
                .returnResponse();
        if (resp.getStatusLine().getStatusCode() != 200) {
            System.out.println("Error: [" + url + "]" + resp.getStatusLine().getStatusCode());
            return Optional.empty();
        } else {
            return Optional.of(resp.getEntity().getContent());
        }
    }
}
