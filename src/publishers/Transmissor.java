package publishers;

import org.eclipse.paho.client.mqttv3.*;

public class Transmissor {

    private MqttClient client;
    private String broker;
    private String topic;
    private int qos;

    public Transmissor(String topic) {
        this.broker = "tcp://localhost:1883";
        this.topic = topic;
        this.qos = 1;
    }

    public void startTransmitting() {
        try {
            // Create a new MQTT client
            client = new MqttClient(broker, MqttClient.generateClientId());

            // Set the callback for receiving messages
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // Not used in this example
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used in this example
                }
            });

            // Connect to the MQTT broker
            client.connect();

            // Start transmitting data
            while (true) {
                // Generate random Integer data
                int data = (int) (Math.random() * 100);
                String dataString =String.valueOf(data);

                // Create an MQTT message with the Integer data
                MqttMessage message = new MqttMessage();
                message.setPayload((dataString).getBytes());
                message.setQos(qos);

                // Publish the message to the topic
                client.publish(topic, message);

                // Wait for one second before sending the next message
                Thread.sleep(1000);
            }
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
