import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class Main extends Application {

    private final String dbUrl = "jdbc:mysql://localhost:3306/student_db";
    private final String dbUser = "root"; 
    private final String dbPass = ""; 

    private TextField nameField;
    private DatePicker dobPicker;
    private TextField searchField;
    private TableView<Classmate> table;
    private ObservableList<Classmate> dataList;
    private int selectedId = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("সহপাঠীদের জন্মদিনের তালিকা");

        Label nameLabel = new Label("নাম:");
        nameField = new TextField();
        
        Label dobLabel = new Label("জন্ম তারিখ:");
        dobPicker = new DatePicker();

        Button addButton = new Button("যোগ করুন");
        Button updateButton = new Button("আপডেট করুন");
        Button deleteButton = new Button("মুছে ফেলুন");
        Button clearButton = new Button("রিসেট");

        addButton.setOnAction(e -> addClassmate());
        updateButton.setOnAction(e -> updateClassmate());
        deleteButton.setOnAction(e -> deleteClassmate());
        clearButton.setOnAction(e -> clearFields());

        HBox formBox = new HBox(10, nameLabel, nameField, dobLabel, dobPicker);
        formBox.setAlignment(Pos.CENTER);
        
        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton, clearButton);
        buttonBox.setAlignment(Pos.CENTER);

        searchField = new TextField();
        searchField.setPromptText("নাম দিয়ে খুঁজুন...");
        searchField.setOnKeyReleased(e -> searchClassmate());

        table = new TableView<>();
        TableColumn<Classmate, Integer> idCol = new TableColumn<>("আইডি");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Classmate, String> nameCol = new TableColumn<>("নাম");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Classmate, String> dobCol = new TableColumn<>("জন্ম তারিখ");
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));

        table.getColumns().addAll(idCol, nameCol, dobCol);
        
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getName());
                dobPicker.setValue(LocalDate.parse(newSelection.getDob()));
                selectedId = newSelection.getId();
            }
        });

        dataList = FXCollections.observableArrayList();
        loadData();
        table.setItems(dataList);

        VBox layout = new VBox(15, formBox, buttonBox, searchField, table);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        checkTodaysBirthday();
    }

    private Connection connect() {
        try {
            return DriverManager.getConnection(dbUrl, dbUser, dbPass);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadData() {
        dataList.clear();
        String query = "SELECT * FROM classmates ORDER BY dob ASC";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                dataList.add(new Classmate(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("dob").toString()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addClassmate() {
        String query = "INSERT INTO classmates (name, dob) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, nameField.getText());
            pstmt.setDate(2, Date.valueOf(dobPicker.getValue()));
            pstmt.executeUpdate();
            loadData();
            clearFields();
            showAlert("সফল", "তথ্য সফলভাবে সংরক্ষিত হয়েছে।");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateClassmate() {
        String query = "UPDATE classmates SET name = ?, dob = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, nameField.getText());
            pstmt.setDate(2, Date.valueOf(dobPicker.getValue()));
            pstmt.setInt(3, selectedId);
            pstmt.executeUpdate();
            loadData();
            clearFields();
            showAlert("সফল", "তথ্য আপডেট করা হয়েছে।");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteClassmate() {
        String query = "DELETE FROM classmates WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, selectedId);
            pstmt.executeUpdate();
            loadData();
            clearFields();
            showAlert("সফল", "তথ্য মুছে ফেলা হয়েছে।");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchClassmate() {
        dataList.clear();
        String query = "SELECT * FROM classmates WHERE name LIKE ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, "%" + searchField.getText() + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                dataList.add(new Classmate(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("dob").toString()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkTodaysBirthday() {
        LocalDate today = LocalDate.now();
        String query = "SELECT name FROM classmates WHERE MONTH(dob) = ? AND DAY(dob) = ?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, today.getMonthValue());
            pstmt.setInt(2, today.getDayOfMonth());
            ResultSet rs = pstmt.executeQuery();

            StringBuilder names = new StringBuilder();
            boolean found = false;
            while (rs.next()) {
                names.append(rs.getString("name")).append("\n");
                found = true;
            }

            if (found) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("শুভ জন্মদিন!");
                alert.setHeaderText("আজকের জন্মদিন:");
                alert.setContentText(names.toString());
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
        dobPicker.setValue(null);
        searchField.clear();
        selectedId = 0;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Classmate {
        private final int id;
        private final String name;
        private final String dob;

        public Classmate(int id, String name, String dob) {
            this.id = id;
            this.name = name;
            this.dob = dob;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getDob() { return dob; }
    }
}
