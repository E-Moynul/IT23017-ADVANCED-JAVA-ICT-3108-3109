import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizApp extends Application {


    private final String DB_URL = "jdbc:mysql://localhost:3306/quiz_db";
    private final String DB_USER = "root"; 
    private final String DB_PASS = "";


    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;


    private Label lblQuestion = new Label();
    private Button btnA = new Button();
    private Button btnB = new Button();
    private Button btnC = new Button();
    private Button btnD = new Button();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        loadQuestionsFromDB();


        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20px; -fx-font-size: 16px;");


        btnA.setOnAction(e -> checkAnswer(btnA.getText()));
        btnB.setOnAction(e -> checkAnswer(btnB.getText()));
        btnC.setOnAction(e -> checkAnswer(btnC.getText()));
        btnD.setOnAction(e -> checkAnswer(btnD.getText()));


        btnA.setMinWidth(200); btnB.setMinWidth(200);
        btnC.setMinWidth(200); btnD.setMinWidth(200);

        root.getChildren().addAll(lblQuestion, btnA, btnB, btnC, btnD);


        showQuestion();

        Scene scene = new Scene(root, 400, 350);
        primaryStage.setTitle("Simple JavaFX Quiz");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void loadQuestionsFromDB() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM questions");

            while (rs.next()) {
                questionList.add(new Question(
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_answer")
                ));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error connecting to database!");
        }
    }


    private void showQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            Question q = questionList.get(currentQuestionIndex);
            lblQuestion.setText((currentQuestionIndex + 1) + ". " + q.getQuestionText());
            btnA.setText(q.getOptionA());
            btnB.setText(q.getOptionB());
            btnC.setText(q.getOptionC());
            btnD.setText(q.getOptionD());
        } else {
            showResult();
        }
    }


    private void checkAnswer(String selectedAnswer) {
        Question q = questionList.get(currentQuestionIndex);
        
        if (selectedAnswer.equals(q.getCorrectAnswer())) {
            score++;
        }

        currentQuestionIndex++;
        showQuestion();
    }


    private void showResult() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Finished");
        alert.setHeaderText("Game Over!");
        alert.setContentText("Your Score: " + score + " / " + questionList.size());
        alert.showAndWait();
        System.exit(0);
    }
}
