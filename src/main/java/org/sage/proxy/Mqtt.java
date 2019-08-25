package org.sage.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fusesource.mqtt.client.*;
import org.sage.proxy.home.HomeControllerApi;
import org.sage.proxy.model.*;

import java.io.IOException;

import static org.fusesource.hawtbuf.UTF8Buffer.utf8;

public class Mqtt {
    private final BlockingConnection connection;
    private final ObjectMapper mapper;
    private final HomeControllerApi homeControllerApi;
    private final Thread thread;

    public Mqtt(String host, String login, String password, HomeControllerApi homeControllerApi) throws Exception {
        this.homeControllerApi = homeControllerApi;
        MQTT mqtt = new MQTT();
        mqtt.setPassword(password);
        mqtt.setUserName(login);
        mqtt.setHost(host);

        connection = mqtt.blockingConnection();
        connection.connect();
        connection.subscribe(new Topic[]{new Topic(utf8("switch"), QoS.AT_LEAST_ONCE)});
        connection.subscribe(new Topic[]{new Topic(utf8("devices_req"), QoS.AT_LEAST_ONCE)});
        connection.subscribe(new Topic[]{new Topic(utf8("device_info_req"), QoS.AT_LEAST_ONCE)});
        connection.subscribe(new Topic[]{new Topic(utf8("update_device"), QoS.AT_LEAST_ONCE)});
        connection.subscribe(new Topic[]{new Topic(utf8("run_script"), QoS.AT_LEAST_ONCE)});

        thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
        mapper = new ObjectMapper();
    }

    private void run() {
        while (true) {
            try {
                Message msg = connection.receive();
                System.out.println("mqtt:" + msg.getTopic());
                switch (msg.getTopic()) {
                    case "switch":
                        handleSwitch(mapper.readValue(msg.getPayload(), Switch.class));
                        break;
                    case "devices_req":
                        handleDevices();
                        break;
                    case "device_info_req":
                        handleDeviceInfo(mapper.readValue(msg.getPayload(), DeviceInfo.class));
                        break;
                    case "update_device":
                        handleUpdateDevice(mapper.readValue(msg.getPayload(), NameWithArgs.class));
                        break;
                    case "run_script":
                        handleRunScript(mapper.readValue(msg.getPayload(), NameWithArgs.class));
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDevices() throws Exception {
        connection.publish("devices_resp", mapper.writeValueAsBytes(homeControllerApi.devices()), QoS.AT_LEAST_ONCE, false);
    }

    private void handleDeviceInfo(DeviceInfo info) throws Exception {
        connection.publish("device_info_req_resp", mapper.writeValueAsBytes(homeControllerApi.device(info.getName())), QoS.AT_LEAST_ONCE, false);
    }

    private void handleUpdateDevice(NameWithArgs update) throws IOException {
        homeControllerApi.update_device(update.getName(), update.getArgs());
    }

    private void handleRunScript(NameWithArgs update) throws IOException {
        homeControllerApi.run_script(update.getName(), update.getArgs());
    }

    private void handleSwitch(Switch cmd) throws IOException {
        System.out.println("handleSwitch: " + cmd);
        if (cmd.isOn()) {
            homeControllerApi.switchOn(cmd.getName());
        } else {
            homeControllerApi.switchOff(cmd.getName());
        }
    }

    public void join() throws InterruptedException {
        thread.join();
    }
}
