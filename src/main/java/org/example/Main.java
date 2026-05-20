package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Main extends Application {

    private TableView<Empleado> tableView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ejercicio 23 - TableView con JDBC");

        tableView = new TableView<>();

        // Definir columnas
        TableColumn<Empleado, Integer> idCol = new TableColumn<>("ID");
        TableColumn<Empleado, String> nombreCol = new TableColumn<>("Nombre");
        TableColumn<Empleado, Integer> salarioCol = new TableColumn<>("Salario");

        // Asignar las propiedades del modelo a las columnas
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        salarioCol.setCellValueFactory(new PropertyValueFactory<>("salario"));

        tableView.getColumns().addAll(idCol, nombreCol, salarioCol);

        // Boton que lanza la carga de datos al pulsarlo
        Button btnCargar = new Button("Cargar datos");
        btnCargar.setOnAction(event -> cargarDatos());

        VBox vbox = new VBox(10, tableView, btnCargar);
        vbox.setStyle("-fx-padding: 20;");
        Scene scene = new Scene(vbox, 450, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void cargarDatos() {
        String url      = "jdbc:oracle:thin:@localhost:1521:xe";
        String user     = "RIBERA";     // Cambia si es necesario
        String password = "ribera";     // Cambia si es necesario

        // Limpiar la tabla antes de recargar
        tableView.getItems().clear();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs   = stmt.executeQuery("SELECT id, nombre, salario FROM EJEMPLOCONEXION");

            while (rs.next()) {
                int    id      = rs.getInt("id");
                String nombre  = rs.getString("nombre");
                int    salario = rs.getInt("salario");
                tableView.getItems().add(new Empleado(id, nombre, salario));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Clase modelo con los mismos campos que las columnas de la tabla
    public static class Empleado {
        private final int    id;
        private final String nombre;
        private final int    salario;

        public Empleado(int id, String nombre, int salario) {
            this.id      = id;
            this.nombre  = nombre;
            this.salario = salario;
        }

        public int    getId()      { return id; }
        public String getNombre()  { return nombre; }
        public int    getSalario() { return salario; }
    }
}