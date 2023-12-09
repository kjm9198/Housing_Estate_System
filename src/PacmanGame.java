import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class PacmanGame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int CELL_SIZE = 30;
    private static final int UP = 1, Down = 2, LEFT = 3, RIGHT = 4;

    private static final int BOARD_SIZE_MIN = 10;
    private static final int BOARD_SIZE_MAX = 100;
    private static final int SPAWN_POWER_UP_INTERVAL = 5000; // 5 sekund w milisekundach
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.25;

    private JTable gameBoard;
    private PacmanTableModel tableModel;
    private Timer timer;
    private int pacmanRow, pacmanCol, pacmanDirection;
    private int score, lives, time;
    private ArrayList<PowerUp> powerUps;

    public PacmanGame() {
        super("Pacman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setLocationRelativeTo(null);

        createMenu();
        initializeGame();
        setVisible(true);
        tableModel = new PacmanTableModel();
        char[][] initialGameBoard = generateGameBoard(getBoardSizeFromUserInput());
        tableModel.setGameBoard(initialGameBoard);

        gameBoard = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gameBoard);
        add(scrollPane);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem highScoresItem = new JMenuItem("High Scores");
        JMenuItem exitItem = new JMenuItem("Exit");

        newGameItem.addActionListener(e -> startNewGame());
        highScoresItem.addActionListener(e -> showHighScores());
        exitItem.addActionListener(e -> System.exit(0));

        menu.add(newGameItem);
        menu.add(highScoresItem);
        menu.add(exitItem);
        menuBar.add(menu);

        setJMenuBar(menuBar);

    }

    public void initializeGame() {
        int boardSize = getBoardSizeFromUserInput();
        char[][] gameBoard = generateGameBoard(boardSize);

        pacmanRow = boardSize / 2;
        pacmanCol = boardSize / 2;
        pacmanDirection = RIGHT;

        score = 0;
        lives = 3;
        time = 0;

        powerUps = new ArrayList<>();

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time++;
                spawnPowerUps();

            }
        });
        timer.start();
    }

    private void startNewGame() {
        // stopping the timer if it's running
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        initializeGame();

        char[][] newGameBoard = generateGameBoard(getBoardSizeFromUserInput());
        updateTableModel(newGameBoard);

        timer.restart();
    }

    private void updateTableModel(char[][] newGameBoard) {
        for (int i = 0; i < newGameBoard.length; i++) {
            for (int j = 0; j < newGameBoard[i].length; j++) {
                tableModel.setValueAt(newGameBoard[i][j], i, j);
            }
        }
        gameBoard.setModel(tableModel);
    }
    private void showHighScores() {

    }

    private void updateGame() {
        // Update pacman positionm check collisions, update power ups
    }
    private int getBoardSizeFromUserInput() {
        int boardSize = 0;
        boolean validInput = false;

        while(!validInput) {
            try {
                String input = JOptionPane.showInputDialog("Enter the size of the game board between " +
                        BOARD_SIZE_MIN + " and " + BOARD_SIZE_MAX + "):");
                boardSize = Integer.parseInt(input);

                if (boardSize >= BOARD_SIZE_MIN && boardSize <= BOARD_SIZE_MAX) {
                    validInput = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid board size, please enter the valid size between " +
                            BOARD_SIZE_MIN + " and " + BOARD_SIZE_MAX + "):");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid variable.");
            }
        }
        return boardSize;
    }

    private void handleKeyPress(int key) {
        // moving the pacman
    }



    private char[][] generateGameBoard (int boardSize) {
        char[][] gameBoard = new char[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                gameBoard[i][j] = '.';
            }
        }

        addWalls(gameBoard);
        // TODO: Add other initial elements (e.g., player, enemies) as needed

        return gameBoard;
    }
    
    private void addWalls(char[][] gameBoard) {
        int boardSize = gameBoard.length;
        for (int i = 0; i < boardSize; i++) {
            gameBoard[i][0] = '#'; // Left border
            gameBoard[i][boardSize - 1] = '#'; // Right border
        }
        for (int j = 0; j < boardSize; j++) {
            gameBoard[0][j] = '#'; // Top border
            gameBoard[boardSize - 1][j] = '#'; // Bottom border
        }
    }
    private void spawnPowerUps() {
        Random rand = new Random();
        if (rand.nextDouble() < POWER_UP_SPAWN_PROBABILITY) {
            // TODO: Implement logic to spawn power-ups
            // Example: powerUps.add(new PowerUp(row, col, PowerUpType.SPEED_BOOST));
        }
    }

    private class PowerUp {
        // TODO: Implement PowerUp class
    }

    private void collectPowerUp(int row, int col) {
        // upon touching get the powerup
    }

    private void endGame() {
        // upon the end show score save high scores etc
    }

    private class PacmanTableModel extends AbstractTableModel {
        private char[][] gameBoard = new char[0][0];

        public void setGameBoard(char[][] newGameBoard) {
            this.gameBoard = newGameBoard;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return gameBoard.length;
        }

        @Override
        public int getColumnCount() {
            return gameBoard.length > 0 ? gameBoard[0].length : 0;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return gameBoard[rowIndex][columnIndex];
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PacmanGame());
    }

}