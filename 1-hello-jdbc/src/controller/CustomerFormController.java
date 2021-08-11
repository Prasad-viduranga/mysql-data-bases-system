package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerFormController {
    public TextField txtID;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtNIC;
    private Connection connection;

    public void initialize() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep7", "root", "prasad");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {

        String id = txtID.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String nic = txtNIC.getText();

        try {
            Statement stm = connection.createStatement();
            //INSERT,DELETE,UPDATE -> executeUpdate();  SELECT -> executeQuery
            String sql = "INSERT INTO customers(id,name,address,nic) VALUES ('%s','%s','%s','%s');";
            sql = String.format(sql, id, name, address, nic);
            System.out.println(sql);
            int affectedRow = stm.executeUpdate(sql);

            if (affectedRow==1){
                new Alert(Alert.AlertType.INFORMATION,"Successful").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Successful").show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Successful").show();
        }

    }
}
