import GUI.ClientGUI;
import GUI.TransmissorAnalystGUI;
import publishers.Transmissor;

public class Program {

    public static void main(String[] args) {
        String transmissorTopic = "projeto/transmissor";
        String clientsTopic = "projeto/clientes";

        TransmissorAnalystGUI analyst = new TransmissorAnalystGUI(transmissorTopic, clientsTopic);
        Transmissor transmissor = new Transmissor(transmissorTopic);
        ClientGUI client = new ClientGUI(clientsTopic);
        ClientGUI client2 = new ClientGUI(clientsTopic);
        transmissor.startTransmitting(); //última coisa que faz
    }
}
