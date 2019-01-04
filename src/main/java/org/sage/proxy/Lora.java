package org.sage.proxy;

import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

import static java.lang.String.format;

public class Lora {
    private final String baseUrl;

    public Lora(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void switchOn(String name) throws IOException {
        perform(format("%s/odin/api/switch/%s/ON", baseUrl, name));
    }

    public void switchOff(String name) throws IOException {
        perform(format("%s/odin/api/switch/%s/OFF", baseUrl, name));
    }

    public void dim(String name, int dim) throws IOException {
        perform(format("%s/odin/api/dimmer/%s/%d", baseUrl, name, dim));
    }

    public void deviceOn(String name, int dim) throws IOException {
        perform(format("%s/odin/api/device/%s/ON/%d", baseUrl, name, dim));
    }

    public void deviceOff(String name, int dim) throws IOException {
        perform(format("%s/odin/api/device/%s/OFF/%d", baseUrl, name, dim));
    }

    private void perform(String url) throws IOException {
        StatusLine resp = Request.Get(url)
                .execute()
                .returnResponse()
                .getStatusLine();
        if (resp.getStatusCode() != 200) {
            System.out.println("Error: [" + url + "]" + resp.getStatusCode());
        }
    }
}
