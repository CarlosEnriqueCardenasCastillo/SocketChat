import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private Socket ClientSocket;
    private DataInputStream IStream;
    private DataOutputStream OStream;
    private int ConnectionStatus=0;
    private static int Port =9999;
    Scanner scanner = new Scanner(System.in);

    public void GetConnection(String ConIP){
        Thread ReadThread = new Thread(new Runnable(){
            @Override
            public void run(){
                System.out.println("Inicializando Socket para comunicación");
                try {
                    ClientSocket = new Socket(ConIP,Port);
                    System.out.println("Inicializando comunicación con "+ConIP);
                    setConnectionStatus(1);
                    iniStream();
                    System.out.println("******************* Comunicación Inicializada *******************");    
                    ReadMessages();
                } catch (UnknownHostException e) {
                    System.out.println("Error :"+ e.getMessage());
                    System.out.println("Error en la creación del Socket");
                } catch (IOException e) {
                    System.out.println("Error :"+ e.getMessage());
                    System.out.println("Error en la creación del Socket");
                }
                System.out.println("Termino de lectura");
            }
        });
        ReadThread.start();
    }
    public void WriteMessages(){
        String OutString;
        try {
            System.out.println("Ingrese el mensaje que desea enviar (Escriba logout para salir): ");
            OutString= scanner.nextLine();
        } catch (Exception e) {
            OutString=null;
            System.out.println("No es posible el envio de mensajes");
            System.out.println(e.getMessage());
        }
        if(OutString.equals("logout")){
            setConnectionStatus(0);
        }else{
            try {
                OStream= new DataOutputStream(ClientSocket.getOutputStream());
                OStream.writeUTF(OutString);
                System.out.println("Cliente====>\t" + OutString);
                OStream.flush();
            } catch (IOException e) {
                System.out.println("Error arrojado en escritura: "+e.getMessage());
                System.out.println("Error en escritura de mensaje ");
            } catch (NullPointerException e){
                System.out.println("Conexión hacia el socket no válida");
            }
        }  
    }

    private void iniStream(){
        try {
            OStream= new DataOutputStream(ClientSocket.getOutputStream());
            IStream= new DataInputStream(ClientSocket.getInputStream());
            OStream.flush();
            System.out.println("Inicializaición de flujo de datos correcta ");
        } catch (IOException e) {
            System.out.println("Error: "+e.getMessage());
            System.out.println("No es posible iniciar el flujo de datos");
        }
    }

    public void ReadMessages() {
        try {
            while (true){
                if(getConnectionStatus()==1){
                    System.out.println("Esperando mensajes del cliente...");
                    String InMsg= IStream.readUTF();
                    System.out.println("\nCliente===>\t"+InMsg);
                }else{
                    try {
                        OStream.close();
                        IStream.close();
                        ClientSocket.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                
            }
        } catch (IOException e) {
            scanner.close();
            System.out.println("No es posible realizar la lectura de datos de entrada: "+e.getMessage());
            e.getStackTrace();
        } finally {
            System.out.println("******************* Comunicación terminada *******************");
            setConnectionStatus(0);
            try {
                OStream.close();
                IStream.close();
                ClientSocket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void setConnectionStatus(int ConnectionStatus){
        this.ConnectionStatus=ConnectionStatus;
    }
    public int getConnectionStatus(){
        return this.ConnectionStatus;
    }
}
