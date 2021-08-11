package lk.ijse.dep7.jdbc_statements.jdbc_crud.controller;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dep7.jdbc_statements.jdbc_crud.tm.CustomerTM;

import java.sql.*;

public class CustomerFormController {
    public TableView<CustomerTM> tblCustomer;
    public TextField txtID;
    public TextField txtNIC;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSearch;
    public Button btnDelete;
    public Button btnNew;
    public Button btnSave;
    private Connection connection;
    private ResultSet rlt;

    public void initialize() {

        tblCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomer.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblCustomer.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("nic"));

        btnSave.setDisable(true);
        btnDelete.setDisable(true);
        btnSave.setDefaultButton(true);

        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
            String id = txtID.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String nic = txtNIC.getText();
            btnSave.setDisable(!(id.matches("C\\d{3}") && name.matches("([A-Za-z]{3,}\\s)|([A-Za-z]+)")
                    && address.matches(".{4,}") && nic.matches("\\d{9}[Vv]")));

        };

        txtID.textProperty().addListener(listener);
        txtName.textProperty().addListener(listener);
        txtAddress.textProperty().addListener(listener);
        txtNIC.textProperty().addListener(listener);

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedItem) -> {
            if (selectedItem != null) {
                txtID.setText(selectedItem.getId());
                txtName.setText(selectedItem.getName());
                txtAddress.setText(selectedItem.getAddress());
                txtNIC.setText(selectedItem.getNic());
                txtID.setDisable(true);
                btnSave.setDisable(false);
                btnDelete.setDisable(false);
                btnSave.setText("Update");
            } else {
                btnSave.setText("Save");
                txtID.setDisable(false);
                btnSave.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            try {

                Statement stm = connection.createStatement();
                String sql = "SELECT * FROM customer WHERE id LIKE '%" + newValue + "%' OR name LIKE '%" + newValue + "%' OR address LIKE '%" + newValue + "%' OR nic LIKE '%" + newValue + "%'";

                ResultSet resultSet = stm.executeQuery(sql);
                tblCustomer.getItems().clear();
                while (resultSet.next()) {
                    tblCustomer.getItems().add(new CustomerTM(resultSet.getNString(1), resultSet.getNString(3), resultSet.getNString(4),
                            resultSet.getNString(2)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep7", "root", "prasad");
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to connect to the DB").showAndWait();
            e.printStackTrace();
            System.exit(1);
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

        try {
            Statement stm = connection.createStatement();
            rlt = stm.executeQuery("SELECT * FROM customer");

            while (rlt.next()) {
                String id = rlt.getNString(1);
                String name = rlt.getNString("name");
                String address = rlt.getNString("address");
                String nic = rlt.getNString("nic");

                tblCustomer.getItems().add(new CustomerTM(id, name, address, nic));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        CustomerTM selectedItem = tblCustomer.getSelectionModel().getSelectedItem();

        try {
            Statement stm = connection.createStatement();
            String sql = "DELETE FROM customer WHERE id='" + selectedItem.getId() + "'";
            System.out.println(sql);
            int affectedRowsForDelete = stm.executeUpdate(sql);

            if (affectedRowsForDelete == 1) {
                tblCustomer.getItems().remove(selectedItem);
                tblCustomer.refresh();
                btnNew.fire();

            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete the customer...").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }


    }

    public void btnNew_OnAction(ActionEvent actionEvent) {
        txtID.clear();
        txtName.clear();
        txtAddress.clear();
        txtNIC.clear();
        txtSearch.clear();
        txtID.requestFocus();

        tblCustomer.getSelectionModel().clearSelection();
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        String id = txtID.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String nic = txtNIC.getText();
        Statement stm = null;
        String sql;

        try {

            stm = connection.createStatement();

            if (btnSave.getText().equals("Save")) {
                sql = "SELECT id FROM customer WHERE id='" + id + "';";

                if (stm.executeQuery(sql).next()) {
                    new Alert(Alert.AlertType.WARNING, "Invalid ID ...!").show();
                    txtID.requestFocus();
                    return;
                }

                sql = "SELECT nic FROM customer WHERE nic='" + nic + "';";

                if (stm.executeQuery(sql).next()) {
                    new Alert(Alert.AlertType.WARNING, "Invalid NIC ...!").show();
                    txtNIC.requestFocus();
                    return;
                }

                sql = "INSERT INTO customer VALUES('%s','%s','%s','%s');";
                sql = String.format(sql, id, name, address, nic);

                int affectedRows = stm.executeUpdate(sql);

                if (affectedRows == 1) {
                    tblCustomer.getItems().add(new CustomerTM(id, name, address, nic));
                    new Alert(Alert.AlertType.INFORMATION, "Successful !").show();
                    btnNew.fire();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save the customer...").show();
                }

            } else {

                sql = "SELECT nic FROM customer WHERE nic='" + nic + "'";//without modification of sql statements.
                // = " SELECT * FROM customer WHERE nic='%s' AND id<>'%s';";
                // sql = String.format(sql, nic, id);

                if (!nic.equals(tblCustomer.getSelectionModel().getSelectedItem().getNic())) { //without modification of sql statements.
                    System.out.println("geeeeeeeeee");
                    if (stm.executeQuery(sql).next()) {
                        new Alert(Alert.AlertType.WARNING, "Invalid NIC............!").show();
                        txtNIC.requestFocus();
                        return;
                    }
                }

                System.out.println("heeeeeeeeee");
                sql = "UPDATE customer SET name='%s',address='%s',nic='%s' WHERE id='%s'";
                sql = String.format(sql, name, address, nic, id);

                int affectedRows = stm.executeUpdate(sql);
                System.out.println(affectedRows);

                if (affectedRows == 1) {
                    CustomerTM selectedItem = tblCustomer.getSelectionModel().getSelectedItem();

                    selectedItem.setNic(txtNIC.getText());
                    selectedItem.setName(txtName.getText());
                    selectedItem.setAddress(txtAddress.getText());
                    tblCustomer.refresh();

                    new Alert(Alert.AlertType.INFORMATION, "Successful !").show();

                } else {
                    new Alert(Alert.AlertType.ERROR, "Unable to update ! retry...").show();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed...").show();
        }
    }
}