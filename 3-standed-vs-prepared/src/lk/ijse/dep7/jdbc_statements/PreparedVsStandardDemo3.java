package lk.ijse.dep7.jdbc_statements;

import java.sql.*;

public class PreparedVsStandardDemo3 {

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep7", "root", "prasad");
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO student (name) VALUES (?)");

            for (int i = 0; i < 10; i++) {
                pstm.setObject(1,"Manoj");
                long t1 = System.currentTimeMillis();
                int affectedRows = pstm.executeUpdate();
                long t2 = System.currentTimeMillis();

                System.out.println("Standard Statement : " + (t2 - t1));
            }

            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

}
