import java.net.*;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    private static DataBaseAccess DBAUser;
    public static void main(String[] args) throws Exception {
        String LoginData[] = new String[4];
        DBAUser = new DataBaseAccess();
        UserClass UserData = new UserClass();
        int LoginStatus= -1;
        System.out.println("Interfaz cliente ");
        System.out.println("Iniciando Login");
        while(LoginStatus!=1){
            System.out.println("Ejecutando Login");
            LoginData=Login(DBAUser);
            LoginStatus=Integer.valueOf(LoginData[3]);
            if(LoginStatus!=1){
                LoginStatus= SalirSistema(LoginStatus);         
            }
        }
        
        if (LoginStatus==1){
            UserData.setName(LoginData[0]);
            UserData.setUAccess(Integer.valueOf(LoginData[1]));
            UserData.setIP(LoginData[2]);
            System.out.println("Login Exitoso");
        }
        try {
            Client ClientConnection = new Client();
            if (LoginStatus==1){
                System.out.println("Obteniendo Usuarios Activos");
                SelActiveUser();
                ClientConnection.GetConnection(getIP()); 
            }
            while(LoginStatus==1){
                ClientConnection.WriteMessages();
                LoginStatus= ClientConnection.getConnectionStatus();
                
            }
        } catch (UnknownHostException e1) {
            System.out.println("Error 1");
            System.out.println(e1.getMessage());
        }
        DBAUser.DBConnect();
        DBAUser.UpdateLvlAccess(UserData.getName(), 0);
        System.out.println("Saliendo del programa");
        System.exit(0);
    }

   

    public static String[] Login(DataBaseAccess DBAUser){
        Scanner InputRead = new Scanner(System.in);
        System.out.println("******************* Inicio de sesion *******************");   
        System.out.println("Ingrese su usuario: ");
        String InUserName= InputRead.nextLine();
        char Password[] = System.console().readPassword("Ingrese su contraseña y enter para confirmar: ");
        String InUserPswd=String.valueOf(Password);
        String UserData[];
        String UserIP="";
        String LoginData[]= new String[4];
        try {
            UserIP= getIP();
            try {
                UserData= DBAUser.ValidateAccess(InUserName,InUserPswd,UserIP);
                LoginData[0]=UserData[0];
                LoginData[1]=UserData[1];
                LoginData[2]=UserData[2];
                LoginData[3]="1";
                return LoginData;
            } catch (SQLException e) {
                System.out.println("Credenciales Incorrectas");
                LoginData[0]="";
                LoginData[1]="";
                LoginData[2]="";
                LoginData[3]="0";
                return LoginData;                
            }
        } catch (UnknownHostException e) {
            System.out.println("IP no reconocida");
                LoginData[0]="";
                LoginData[1]="";
                LoginData[2]="";
                LoginData[3]="3";
                return LoginData;
        }
    }

    public static String getIP() throws UnknownHostException{
        // LOCAL IP ADDRESS
        InetAddress localhost = InetAddress.getLocalHost();
        String IPAddr= (localhost.getHostAddress()).trim();
        return IPAddr;
    }
    public static int SalirSistema(int LoginStatus){
        Scanner InputRead = new Scanner(System.in);
        char ExitKey;
        System.out.println("¿Desea salir del programa?[s/n]");
        ExitKey=InputRead.next().charAt(0);
        if  (ExitKey=='s'){
            System.out.println("Saliendo...");
            return 0;
        }
        else{
            return LoginStatus;
        }
    }
    public static void SelActiveUser(){
       Scanner scanner = new Scanner(System.in);
        String SelectedUser="";
        System.out.println("Ingrese el nombre de usuario con el que desea comunicarse:");
        DBAUser.getActiveUser();
        SelectedUser= scanner.nextLine();
        if (DBAUser.getUserIP(SelectedUser)==null){
            System.out.println("Usuario Ingresado no valido");
        }
    }
    
}
