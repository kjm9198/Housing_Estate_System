import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class PacmanGame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int BOARD_SIZE_MIN = 10;
    private static final int BOARD_SIZE_MAX = 100;
    private static final int SPAWN_POWER_UP_INTERVAL = 5000; // 5 sekund w milisekundach
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.25;

    private Image pacmanUp, pacmanDown, pacmanLeft, pacmanRight;
    private static final int UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4;
    private JTable gameBoard;
    private PacmanTableModel tableModel;
    private Timer timer;
    private int pacmanRow;
    private int pacmanCol;
    private Image pacmanDirection;
    private int score, lives, time, highscore;
    private ArrayList<PowerUp> powerUps;

    public PacmanGame() {
        super("Pacman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createMenu();
        initializeGame();
        loadImages();

        tableModel = new PacmanTableModel();
        char[][] initialGameBoard = generateGameBoard(getBoardSizeFromUserInput());
        tableModel.setGameBoard(initialGameBoard);

        gameBoard = new JTable(tableModel);
        gameBoard.setTableHeader(null);
        JScrollPane scrollPane = new JScrollPane(gameBoard);
        add(scrollPane);
        gameBoard.setFocusable(true);


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

    private void loadImages() {
        pacmanDown = loadImage("./Sprites/down.gif");
        pacmanUp = loadImage("./Sprites/up.gif");
        pacmanLeft = loadImage("./Sprites/left.gif");
        pacmanRight = loadImage("./Sprites/right.gif");
    }

    private Image loadImage(String path) {
        try {
            URL resource = getClass().getResource(path);
            if (resource != null) {
                return ImageIO.read(resource);
            } else {
                System.err.println("Failed to load image: " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        tableModel.setGameBoard(newGameBoard);

        tableModel.setPacmanPosition(pacmanRow, pacmanCol);
        tableModel.setPacmanDirection(pacmanDirection);

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

        int maxFrameSize = 600;  // Adjust the value as needed

        double scale = Math.min(1.0, maxFrameSize / (double) boardSize);
        boardSize = (int) (boardSize * scale);

        return boardSize;
    }


    private void returnToMainMenu() {
        // Implement logic to stop the game, return to the main menu, and handle any necessary cleanup
        // You may want to display a confirmation dialog before returning to the main menu
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to return to the main menu? Your progress will be lost.", "Return to Main Menu", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            timer.stop();
            initializeGame();
            char[][] newGameBoard = generateGameBoard(getBoardSizeFromUserInput());
            updateTableModel(newGameBoard);
        }
    }


    private char[][] generateGameBoard(int boardSize) {
        char[][] gameBoard = new char[boardSize][boardSize];

        // Fill the entire board with empty spaces
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                gameBoard[i][j] = ' ';
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

        private int pacmanRow;
        private int pacmanCol;
        private Image pacmanDirection;
        private char[][] gameBoard = new char[0][0];

        public void setGameBoard(char[][] newGameBoard) {
            this.gameBoard = newGameBoard;
            fireTableDataChanged();
        }

        public void setPacmanPosition(int row, int col) {
            this.pacmanRow = row;
            this.pacmanCol = col;
        }

        public void setPacmanDirection(Image direction) {
            this.pacmanDirection = direction;
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
            // Change this method to return the appropriate image for the given position
            char cellValue = gameBoard[rowIndex][columnIndex];
            switch (cellValue) {
                case 'U':
                    return UP;
                case 'D':
                    return DOWN;
                case 'L':
                    return LEFT;
                case 'R':
                    return RIGHT;
                // Add cases for other cell values (e.g., walls, power-ups)
                default:
                    return null;
            }
        }


        private void movePacman(int direction) {
            switch (direction) {
                case UP:
                    if (isValidMove(pacmanRow - 1, pacmanCol)) {
                        pacmanRow--;
                        tableModel.setPacmanDirection(pacmanUp);
                    }
                    break;
                case DOWN:
                    if (isValidMove(pacmanRow + 1, pacmanCol)) {
                        pacmanRow++;
                        tableModel.setPacmanDirection(pacmanDown);
                    }
                    break;
                case LEFT:
                    if (isValidMove(pacmanRow, pacmanCol - 1)) {
                        pacmanCol--;
                        tableModel.setPacmanDirection(pacmanLeft);
                    }
                    break;
                case RIGHT:
                    if (isValidMove(pacmanRow, pacmanCol + 1)) {
                        pacmanCol++;
                        tableModel.setPacmanDirection(pacmanRight);
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


