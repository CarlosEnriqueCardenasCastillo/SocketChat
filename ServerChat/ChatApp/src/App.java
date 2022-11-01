public class App {
    public static void main(String[] args) {
        while(true){
            ChatServer chatServer = new ChatServer();
            System.out.println("Generando Socket en espera de comunicación");
            chatServer.GetConnection();
            while(chatServer.getConnectionStatus()==0){
            }
            while(chatServer.getConnectionStatus()==1){
                chatServer.WriteMessages();
            }
        }
    }
}

