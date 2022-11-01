import java.io.*;
import java.net.*;
import java.util.Scanner;
public class ChatServer {
    private ServerSocket Server;
    private Socket SSocket;
    private DataOutputStream OStream;
    private DataInputStream IStream;
    private int ConnectionStatus=0;
    public Thread ReadThread;
    private String InMsg;
    private Scanner scanner;
    public void GetConnection(){
        ReadThread = new Thread(new Runnable(){
            @Override
            public void run(){
                System.out.println("Inicializando Socket de Servidor para comunicación");
                try {
                    Server= new ServerSocket(9999);
                    System.out.println("Esperando conexión");
                    SSocket= Server.accept();
                    setConnectionStatus(1);
                    System.out.println("Conexión a puerto: 9999");
                    iniStream();
                    System.out.println("******************* Comunicación Inicializada *******************");
                    ReadMessages();
                } catch (IOException e) {
                    System.out.println("Error en creación de socket :"+e.getMessage());
                }
            }
        });
        ReadThread.start();
    }

    private void iniStream(){
        try {
            OStream= new DataOutputStream(SSocket.getOutputStream());
            IStream= new DataInputStream(SSocket.getInputStream());
            System.out.println("Inicializaición de flujo de datos correcta ");
        } catch (IOException e) {
            System.out.println("Error: "+e.getMessage());
            System.out.println("No es posible iniciar el flujo de datos");
        }
    }
    private String SetMsg(){
        String OutString="";
        scanner= new Scanner(System.in);
        try {
            System.out.println("Ingrese el mensaje que desea enviar: ");
            OutString= scanner.nextLine();
            return OutString;
        } catch (Exception e) {
            System.out.println("No es posible el envio de mensajes");
            System.out.println(e.getMessage());
            return null;
        }  
    }
    public void WriteMessages(){
        String OutMsg;
        if(getConnectionStatus()==1){
            try {
                OutMsg= SetMsg();
                if(getConnectionStatus()==1){
                    OStream.writeUTF(OutMsg);
                    System.out.println("Server====>\t" + OutMsg);
                }
            } catch (IOException e) {
                System.out.println("No es posible realizar la escritura de mensajes: "+e.getMessage());
            } catch (NullPointerException e){
                System.out.println("Conexión hacia el socket no válida");
            }
        }
    }
    public void ReadMessages() {
        try {
            while (true){
                if(getConnectionStatus()==1){
                    System.out.println("Esperando mensajes del cliente...");
                    InMsg= IStream.readUTF();
                    System.out.println("\nCliente===>\t"+InMsg);
                }else{
                    setConnectionStatus(0);
                }
                
            }
        } catch (IOException e) {
            setConnectionStatus(0);
            System.out.println("No es posible realizar la lectura de datos de entrada: "+e.getMessage());
            e.getStackTrace();
        } finally {
            System.out.println("******************* Comunicación terminada *******************\nPresione Enter para continuar...");
            closeCommunication();
        }
    }
    private void closeCommunication(){
        try {
            OStream.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            IStream.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            SSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            Server.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setConnectionStatus(int Connectionstatus){
        this.ConnectionStatus=Connectionstatus;
    }
    public int getConnectionStatus(){
        return this.ConnectionStatus;
    }
}

