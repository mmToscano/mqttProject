package GUI;

import javax.swing.*;
import java.awt.*;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class TransmissorAnalystGUI {
    private JPanel transmissorReadingsPanel;
    private JTextArea allReadings;
    private JTextField lastReadArea;
    private JTextField lastCriticalReadArea;

    JFrame frame = new JFrame("analista do transmissor");

    private MqttClient client;
    private String topicToReceive;
    private String topicToSend;

    public TransmissorAnalystGUI(String topicToReceive, String topicToSend){
        this.topicToReceive = topicToReceive;
        this.topicToSend = topicToSend;
        connectToServer();
        instantiateTransmissorAnalistGUI();
    }

    public void connectToServer(){
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
                    String messageText = new String(message.getPayload());

                    allReadings.append(messageText + "\n");
                    lastReadArea.setText("última leitura: " + messageText);

                    if(Integer.valueOf(messageText) >= 70){
                        lastCriticalReadArea.setText("última leitura crítica: " + messageText);
                        sendMessage();
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });

            client.connect();
            System.out.println("Connected to MQTT broker: " + broker);

            client.subscribe(this.topicToReceive);
            System.out.println("Subscribed to topic: aula/exemplo");

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(){

        String data = lastCriticalReadArea.getText().substring(24);

        try{
            MqttMessage message = new MqttMessage();
            message.setPayload(String.valueOf(data).getBytes());
            message.setQos(1);

            // Publish the message to the topic
            client.publish(topicToSend, message);
        }catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void instantiateTransmissorAnalistGUI(){
        frame.setContentPane(this.transmissorReadingsPanel); // Define o conteúdo da janela como o painel do cliente
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define o comportamento padrão ao fechar a janela
        frame.setPreferredSize(new Dimension(400, 300)); // Define as dimensões preferenciais da janela
        frame.pack(); // Ajusta o tamanho da janela automaticamente
        frame.setVisible(true); // Torna a janela visível
    }
}
