package GUI;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


import javax.swing.*;
import java.awt.*;

//subscriber
public class ClientGUI {
    private JPanel clientPanel;
    private JButton sendMessageButton;
    private JTextField messageInputArea;
    private JTextArea messageArea;
    private JTextField userNameInputArea;
    private JButton connectButton;

    JFrame frame = new JFrame("Cliente");

    private MqttClient client;
    private String topic;

    public ClientGUI(String topic){
        this.topic = topic;
        connectButton.addActionListener(e -> connectToServer());
        sendMessageButton.addActionListener(e -> sendMessage());

        sendMessageButton.setEnabled(false);
        messageArea.setText("Aguardando conexão...");
        instantiateClientGUIInterface();
    }

    public void connectToServer(){
        String name = userNameInputArea.getText();
        String broker = "tcp://localhost:1883";
        String clientId = MqttClient.generateClientId();
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            client = new MqttClient(broker, clientId, persistence);

            //toda vez que uma conexão é perdida, uma mensagem chega ou uma mensagem é
            //entregue, esses métodos são executados.
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection to MQTT broker lost!");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //System.out.println("Message received on topic: " + topic);
                    //System.out.println("Message content: " + new String(message.getPayload()));
                    String messageText = new String(message.getPayload());
                    messageArea.append("\n" + messageText);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used in subscriber
                }
            });

            client.connect();
            System.out.println("Connected to MQTT broker: " + broker);

            client.subscribe(this.topic);
            System.out.println("Subscribed to topic: aula/exemplo");

        } catch (MqttException e) {
            e.printStackTrace();
        }


        frame.setTitle(name);
        sendMessageButton.setEnabled(true);
    }

    public void sendMessage(){

        String data = messageInputArea.getText();

        try{
            MqttMessage message = new MqttMessage();
            message.setPayload(String.valueOf(data).getBytes());
            message.setQos(1);

            // Publish the message to the topic
            client.publish(topic, message);
        }catch (MqttException e) {
            e.printStackTrace();
        }

        messageInputArea.setText("");

    }

    private void instantiateClientGUIInterface(){

        frame.setContentPane(this.clientPanel); // Define o conteúdo da janela como o painel do cliente
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define o comportamento padrão ao fechar a janela
        frame.setPreferredSize(new Dimension(400, 300)); // Define as dimensões preferenciais da janela
        frame.pack(); // Ajusta o tamanho da janela automaticamente
        frame.setVisible(true); // Torna a janela visível
    }


}

/*
import org.eclipse.paho.client.mqttv3.*;

public class publishers.Transmissor {
    private MqttClient client;
    private String broker;
    private String topic;
    private int qos;

    public publishers.Transmissor(String broker, String topic, int qos) {
        this.broker = broker;
        this.topic = topic;
        this.qos = qos;
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

                // Create an MQTT message with the Integer data
                MqttMessage message = new MqttMessage();
                message.setPayload(String.valueOf(data).getBytes());
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

    public static void main(String[] args) {
        // MQTT broker details
        String broker = "tcp://mqtt.eclipse.org:1883";

        // Topic to publish data to
        String topic = "your/topic";

        // QoS (Quality of Service) level
        int qos = 1;

        // Create an instance of publishers.Transmissor and start transmitting data
        publishers.Transmissor transmissor = new publishers.Transmissor(broker, topic, qos);
        transmissor.startTransmitting();
    }
}

 */
