import com.fasterxml.jackson.databind.ObjectMapper;
import model.Dim;
import model.Device;
import model.Switch;
import org.fusesource.mqtt.client.*;

import java.io.IOException;

import static org.fusesource.hawtbuf.UTF8Buffer.utf8;

public class Mqtt {
    private final BlockingConnection connection;
    private final ObjectMapper mapper;
    private final Lora lora;
    private final Thread thread;

    public Mqtt(String host, String login, String password, Lora lora) throws Exception {
        this.lora = lora;
        MQTT mqtt = new MQTT();
        mqtt.setPassword(password);
        mqtt.setUserName(login);
        mqtt.setHost(host);

        connection = mqtt.blockingConnection();
        connection.connect();
        connection.subscribe(new Topic[]{new Topic(utf8("switch"), QoS.AT_LEAST_ONCE)});
        connection.subscribe(new Topic[]{new Topic(utf8("device"), QoS.AT_LEAST_ONCE)});
        connection.subscribe(new Topic[]{new Topic(utf8("dim"), QoS.AT_LEAST_ONCE)});

        thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
        mapper = new ObjectMapper();
    }

    private void run() {
        while (true) {
            try {
                Message msg = connection.receive();
                String payload = new String(msg.getPayload());
                switch (msg.getTopic()) {
                    case "switch":
                        handleSwitch(mapper.readValue(payload, Switch.class));
                        break;
                    case "dim":
                        handleDim(mapper.readValue(payload, Dim.class));
                        break;
                    case "device":
                        handleDevice(mapper.readValue(payload, Device.class));
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDevice(Device cmd) throws IOException {
        if (cmd.isOn()) {
            lora.deviceOn(cmd.getName(), cmd.getDim());
        } else {
            lora.deviceOff(cmd.getName(), cmd.getDim());
        }
    }

    private void handleDim(Dim cmd) throws IOException {
        lora.dim(cmd.getName(), cmd.getDim());
    }

    private void handleSwitch(Switch cmd) throws IOException {
        System.out.println("handleSwitch: " + cmd);
        if (cmd.isOn()) {
            lora.switchOn(cmd.getName());
        } else {
            lora.switchOff(cmd.getName());
        }
    }

    public void join() throws InterruptedException {
        thread.join();
    }
}
