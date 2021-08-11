package lk.ijse.dep7.jdbc_statements;

import java.sql.*;

public class PreparedVsStandard {

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep7", "root", "prasad");

            connection.createStatement().executeQuery("SELECT 1");

            long t1 = System.currentTimeMillis();
            Statement stm = connection.createStatement();
            ResultSet rst1 = stm.executeQuery("SELECT * FROM student");
            long t2 = System.currentTimeMillis();

            System.out.println("Statndard Statement : "+(t2-t1)+"");

            long t3 = System.currentTimeMillis();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM student");
            ResultSet rst2 = pstm.executeQuery();
            long t4 = System.currentTimeMillis();

            System.out.println("Prepared Statement : "+(t4-t3)+"");


            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

}
