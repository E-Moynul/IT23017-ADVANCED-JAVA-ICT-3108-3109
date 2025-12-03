public class Question {
    String questionText;
    String optionA, optionB, optionC, optionD;
    String correctAnswer;

    public Question(String q, String a, String b, String c, String d, String ans) {
        this.questionText = q;
        this.optionA = a;
        this.optionB = b;
        this.optionC = c;
        this.optionD = d;
        this.correctAnswer = ans;
    }

    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
}
