import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GuessingGame extends JFrame {
    private int targetNumber;
    private int remainingGuesses;
    private StringBuilder guessHistory;
    private Timer numberDisplayTimer;
    private JLabel messageLabel;
    private JLabel numberLabel;
    private JTextField guessField;
    private JButton submitButton;
    private JButton newGameButton;
    private JButton historyButton;
    private JTextArea historyArea;

    public GuessingGame() {
        initializeUI();
        initializeGame();
    }

    private void initializeUI() {
        setTitle("Number Guessing Game");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Font boldFont = new Font(Font.SANS_SERIF, Font.BOLD, 14);

        messageLabel = new JLabel("Guess a number between 1 and 100:");
        messageLabel.setFont(boldFont);

        numberLabel = new JLabel();
        numberLabel.setFont(boldFont);

        guessField = new JTextField(10);

        submitButton = new JButton("Submit");
        newGameButton = new JButton("New Game");
        historyButton = new JButton("History");

        historyArea = new JTextArea(10, 20);
        historyArea.setEditable(false);
        historyArea.setFont(boldFont);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 1, 10, 10));
        inputPanel.add(messageLabel);
        inputPanel.add(numberLabel);
        inputPanel.add(guessField);
        inputPanel.add(submitButton);
        inputPanel.add(newGameButton);
        inputPanel.add(historyButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(historyArea), BorderLayout.CENTER);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkGuess();
            }
        });

        newGameButton.setEnabled(false);
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializeGame();
                submitButton.setEnabled(true);
                newGameButton.setEnabled(false);
                historyButton.setEnabled(false); // Disable history button on new game
            }
        });

        historyButton.setEnabled(false);
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, guessHistory.toString(), "Guess History", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void initializeGame() {
        guessHistory = new StringBuilder();
        historyArea.setText("");
        targetNumber = generateRandomNumber();
        remainingGuesses = 10;
    
        updateHistoryArea("Try to guess the number!");
        updateHistoryArea("You have " + remainingGuesses + " guesses remaining.");
    
        numberDisplayTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayNumber();
                updateHistoryArea("Try to guess the number!");
                updateHistoryArea("You have " + remainingGuesses + " guesses remaining.");
                historyButton.setEnabled(true); // Enable history button after number is shown
                
                Timer hideNumberTimer = new Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        hideNumber();
                    }
                });
                hideNumberTimer.setRepeats(false);
                hideNumberTimer.start();
            }
        });
        numberDisplayTimer.setRepeats(false);
        numberDisplayTimer.start();
    
        newGameButton.setEnabled(false);
        historyButton.setEnabled(false);
    }
    private void hideNumber() {
        numberLabel.setText("");
    }
    private int generateRandomNumber() {
        return new Random().nextInt(100) + 1;
    }

    private void displayNumber() {
        numberLabel.setText("Number: " + targetNumber);
    }
    private void updateHistoryArea(String message) {
        historyArea.append(message + "\n\n"); // Add two new lines for spacing
    }
    

    private void checkGuess() {
        numberDisplayTimer.stop();
        numberLabel.setText("");

        try {
            int guess = Integer.parseInt(guessField.getText());

            remainingGuesses--;
            guessHistory.append(guess + " ");

            if (guess == targetNumber) {
                updateHistoryArea("Congratulations! You guessed the correct number: " + targetNumber);
                endGame();
            } else if (remainingGuesses == 0) {
                updateHistoryArea("Sorry, you're out of guesses. The correct number was: " + targetNumber);
                endGame();
            } else {
                String hint = (guess < targetNumber) ? "Higher" : "Lower";
                updateHistoryArea("Incorrect guess. Try " + hint + ". You have " + remainingGuesses + " guesses remaining.");
            }

            updateHistoryArea("Your previous guesses: " + guessHistory.toString());
            guessField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }

    private void endGame() {
        updateHistoryArea("Game over. The correct number was: " + targetNumber);
        updateHistoryArea("Your guesses: " + guessHistory.toString());
        submitButton.setEnabled(false);
        newGameButton.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GuessingGame().setVisible(true);
            }
        });
    }
}
