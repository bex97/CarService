import java.sql.*;

public class SqlConnection {

   Connection conn;

   public SqlConnection() {
      try {
         conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/warsztatdb?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", "user",
               "user");

      } catch (SQLException ex) {
         ex.printStackTrace();
      }
   }

   public Boolean addEmployee(Employee employee) {
      try {
         int id = getLastIdEmployee();
         if(id == -1) id = 0;
         else id++;
         String strInsert = "INSERT INTO employee (id, name, surname, login, password, payout, position) VALUES (?,?,?,?,?,?,?);";
         PreparedStatement stmt = conn.prepareStatement(strInsert);
         stmt.setInt(1, id);
         stmt.setString(2, employee.getName());
         stmt.setString(3, employee.getSurname());
         stmt.setString(4, employee.getLogin());
         stmt.setString(5, employee.getPassword());
         stmt.setInt(6, employee.getPayout());
         stmt.setString(7, employee.getPosition());
         stmt.execute();
         return true;
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }
   }

   private int getLastIdEmployee() {
      try {
         Statement stmt = conn.createStatement();
         String strSelect = "select MAX(id) from employee";
         ResultSet rset = stmt.executeQuery(strSelect);

         rset.next();
         int id_sql = rset.getInt(1);
         System.out.println(id_sql);
         return id_sql; 
      } catch (Exception e) {
         e.printStackTrace();
         return -1;
      }
   }
}
