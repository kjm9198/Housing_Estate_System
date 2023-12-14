import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class PacmanGame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int CELL_SIZE = 30;
    private static final int UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4;
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

        createMenu();
        initializeGame();

        tableModel = new PacmanTableModel();
        char[][] initialGameBoard = generateGameBoard(getBoardSizeFromUserInput());
        tableModel.setGameBoard(initialGameBoard);

        gameBoard = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gameBoard);
        add(scrollPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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

        while (!validInput) {
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

        // Set a fixed maximum size for the JFrame
        int maxFrameSize = 600;  // Adjust the value as needed

        // Scale the board size to fit within the maximum frame size
        double scale = Math.min(1.0, maxFrameSize / (double) boardSize);
        boardSize = (int) (boardSize * scale);

        return boardSize;
    }



    private void returnToMainMenu() {
        // Implement logic to stop the game, return to the main menu, and handle any necessary cleanup
        // You may want to display a confirmation dialog before returning to the main menu
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to return to the main menu? Your progress will be lost.", "Return to Main Menu", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // Stop the game, return to the main menu, and perform any necessary cleanup
            timer.stop();
            initializeGame();
            char[][] newGameBoard = generateGameBoard(getBoardSizeFromUserInput());
            updateTableModel(newGameBoard);
        }
    }


    private char[][] generateGameBoard(int boardSize) {
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
            gameBoard[i][0] = '|'; // Left border
            gameBoard[i][boardSize - 1] = '|'; // Right border
        }
        for (int j = 0; j < boardSize; j++) {
            gameBoard[0][j] = '-'; // Top border
            gameBoard[boardSize - 1][j] = '-'; // Bottom border
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

    private boolean isValidMove(int row, int col) {
        // Check if the move is within the game board boundaries
        if (row < 0 || row >= tableModel.getRowCount() || col < 0 || col >= tableModel.getColumnCount()) {
            return false;
        }

        // Check if the move does not collide with walls ('#')
        if (tableModel.getValueAt(row, col).equals("#")) {
            return false;
        }

        // Additional checks for other potential obstacles or game rules can be added here

        return true;
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

        private void movePacman(int direction) {
            // TODO: Do the moving into one direction until it hits the wall

            switch (direction) {
                case UP:
                    if (isValidMove(pacmanRow - 1, pacmanCol)) {
                        // Move Pacman up
                        pacmanRow--;
                    }
                    break;
                case DOWN:
                    if (isValidMove(pacmanRow + 1, pacmanCol)) {
                        // Move Pacman down
                        pacmanRow++;
                    }
                    break;
                case LEFT:
                    if (isValidMove(pacmanRow, pacmanCol - 1)) {
                        // Move Pacman left
                        pacmanCol--;
                    }
                    break;
                case RIGHT:
                    if (isValidMove(pacmanRow, pacmanCol + 1)) {
                        // Move Pacman right
                        pacmanCol++;
                    }
                    break;
            }
        }

        private void handleKeyPress(int key) {
            switch (key) {
                case KeyEvent.VK_A:
                    movePacman(UP);
                    break;
                case KeyEvent.VK_DOWN:
                    movePacman(DOWN);
                    break;
                case KeyEvent.VK_LEFT:
                    movePacman(LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    movePacman(RIGHT);
                    break;
                case KeyEvent.VK_CONTROL:
                    // Handle control key (Ctrl) for additional actions if needed
                    break;
                case KeyEvent.VK_SHIFT:
                    // Handle shift key for additional actions if needed
                    break;
                case KeyEvent.VK_Q:
                    // Handle 'Q' key for quitting the game
                    if ((key & KeyEvent.CTRL_MASK) != 0 && (key & KeyEvent.SHIFT_MASK) != 0) {
                        returnToMainMenu();
                    }
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PacmanGame());
    }

}
