import java.sql.*;

public class DataBaseAccess {
    private String dbURL = "jdbc:oracle:thin:@localhost:1521/orcl.cb.bbvabancomer.com.mx"; 
    private String user = "CECC";
    private String password = "Cac26598";
    private Connection con;

    public void DBConnect(){
        System.out.println("Accediendo a Base de datos");
        try{
            con=DriverManager.getConnection(dbURL,user,password);
            System.out.println("Conexión exitosa a servidor");
        }catch (Exception e) {
            System.out.println("Error de conexión al servidor");
            e.printStackTrace();
        }
    }
    public void DBDisconnect(){
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error en desconexión de base de datos : "+e.getMessage());
        }
    }
    public void regData(String NewUserName, String NewUserPswd, int NewUserAccess) throws SQLException{
        DBConnect();
        try {
            String sql = "INSERT INTO USERSCHAT (USERNAME,USERPSWD,USERACCESS) VALUES (?,?,?)";
            PreparedStatement statement= this.con.prepareStatement(sql);
            statement.setString(1,NewUserName);
            statement.setString(2,NewUserPswd);
            statement.setInt(3,NewUserAccess);
            statement.executeUpdate();
            System.out.println("Registro Completo");
        } catch (SQLException e) {
            switch (e.getErrorCode()){
                case 1:
                    System.out.println("Nombre de usuario no disponible");
                break;
                default:
                    System.out.println("Error");

            }
            System.out.println("Fallo en registro");
        }
        DBDisconnect();
    }
    public String[] ValidateAccess(String USERNAME, String USERPSWD, String IPADDR) throws SQLException{
        String UserData[] = new String[3];
        DBConnect();
        System.out.println("Validando Usuario");
        SearchUser(USERNAME, USERPSWD);
        System.out.println("Usuario validado");
        if (UpdateIP(IPADDR,USERNAME)==1){
            System.out.println("Actualización de IP correcta");
        }else{
            System.out.println("Actualización de IP fallida");
        }
        if (UpdateLvlAccess(USERNAME,1)==1){
            System.out.println("Actualización de Nivel de acceso correcto");
        }else{
            System.out.println("Actualización de Nivel de acceso fallido");
        }
        UserData= GetUserData(USERNAME);
        System.out.println("Lectura de datos Completa");
        System.out.println("Usuario: \t\t"+UserData[0]);
        System.out.println("Nivel de Acceso: \t"+UserData[1]);
        System.out.println("Dirreción IP: \t\t"+ UserData[2]);
        DBDisconnect();
        return UserData;
    }

    public void SearchUser(String USERNAME, String USERPSWD) throws SQLException{
        String sql = "SELECT * FROM USERSCHAT WHERE USERNAME= ? AND USERPSWD= ?";
        PreparedStatement statement;
        statement = this.con.prepareStatement(sql);
        statement.setString(1,USERNAME);
        statement.setString(2,USERPSWD);
        ResultSet rs= statement.executeQuery();
        rs.next();
        rs.getString(1);
    }
    public String[] GetUserData(String USERNAME) throws SQLException{
        String UserData[] = new String[3];
        String sql = "SELECT * FROM USERSCHAT WHERE USERNAME= ?";
        PreparedStatement statement= this.con.prepareStatement(sql);
        statement.setString(1,USERNAME);
        ResultSet rs= statement.executeQuery();
        rs.next();
        UserData[0]=rs.getString(1);
        UserData[1]=rs.getString(3);
        UserData[2]=rs.getString(4);
        return UserData ;
    }
    public int UpdateIP(String IPAddr, String USERNAME){
        String sql ="UPDATE USERSCHAT SET USERIPADDR=? WHERE USERNAME= ?";
        PreparedStatement statement;
        try {
            GetUserData(USERNAME);
            statement = this.con.prepareStatement(sql);
            statement.setString(1, IPAddr);
            statement.setString(2,USERNAME);
            statement.executeQuery();
            return 1;
        } catch (SQLException e) {
            return 0;
        }
        
    }

    public int UpdateLvlAccess(String USERNAME, int LVLACCESS){
        String sql ="UPDATE USERSCHAT SET USERACCESS=? WHERE USERNAME= ?";
        PreparedStatement statement;
        try {
            GetUserData(USERNAME);
            statement = con.prepareStatement(sql);
            statement.setInt(1, LVLACCESS);
            statement.setString(2, USERNAME);
            statement.execute();
            return 1;
        } catch (SQLException e) {
            return 0;
        } 
    }
    public String getUserIP(String SELECTEDUSER){
        String UserIP=null;
        DBConnect();
        String sql = "SELECT USERIPADDR FROM USERSCHAT WHERE USERNAME= ? AND USERACCESS = 1";
        PreparedStatement statement;
        try{
            statement= con.prepareStatement(sql);
            statement.setString(1, SELECTEDUSER);
            ResultSet rs = statement.executeQuery();
            rs.next();
            UserIP=rs.getString(1);
            System.out.println("IP de usuario elegido:"+UserIP);

        }catch (SQLException e){
            System.out.println("Error de lectura");
        }
        DBDisconnect();
        return UserIP;
    }

    public int getActiveUser(){
        int NActiveUsers=0;
        String sql = "SELECT COUNT(*) FROM USERSCHAT WHERE USERACCESS= 1";
        PreparedStatement statement;
        DBConnect();
        try {
            statement = con.prepareStatement(sql);
            ResultSet rs= statement.executeQuery();
            rs.next();
            NActiveUsers=rs.getInt(1);
            if (NActiveUsers>0){
                sql= "SELECT * FROM USERSCHAT WHERE USERACCESS=1";
                statement = con.prepareStatement(sql);
                rs=statement.executeQuery();
                while(rs.next()){
                    System.out.println("Usuario Activo:"+rs.getString(1));
                }
            }else{
                System.out.println("Ningun Usuario Activo");
                
            }
        } catch (SQLException e) {
            System.out.println("Error en lectura de usuarios Activos, Error: "+e.getMessage());
        }
        DBDisconnect();
        return NActiveUsers;
    }
    
}

