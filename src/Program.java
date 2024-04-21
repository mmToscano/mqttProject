import GUI.ClientGUI;
import publishers.Transmissor;

public class Program {

    public static void main(String[] args) {
        String topic = "aula/exemplo";
        Transmissor transmissor = new Transmissor(topic);
        ClientGUI client = new ClientGUI(topic);
        ClientGUI client2 = new ClientGUI(topic);
        transmissor.startTransmitting(); //última coisa que faz
    }
}
