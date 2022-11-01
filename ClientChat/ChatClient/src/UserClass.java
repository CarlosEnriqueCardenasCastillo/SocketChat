

public class UserClass {
    private String UserName;
    private int UserAccess;   // Desconectado=0; Conectado=1;  
    private String UserIP; 
    public String getName(){
        return UserName;
    }
    public void setName(String Username){
        this.UserName= Username; 
    }
    public String getIP(){
        return UserIP;
    }
    public void setIP(String UserIP){
        this.UserIP= UserIP; 
    }
    public int getUAccess(){
        return UserAccess;
    }
    public void setUAccess(int LvlAccess){
        this.UserAccess= LvlAccess; 
    }
}
