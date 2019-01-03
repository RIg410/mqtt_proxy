
public class Main {
    public static void main(String[] args) throws Exception {
        Env env = new Env();
        Lora lora = new Lora(env.get("LORA_BASE_URL"));
        Mqtt mqtt = new Mqtt(env.get("MQTT_HOST"), env.get("MQTT_LOGIN"), env.get("MQTT_PASSWORD"), lora);
        System.out.println("Start mqtt proxy");
        mqtt.join();
    }
}