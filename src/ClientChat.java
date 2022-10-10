import java.awt.*;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;



public class ClientChat extends JFrame {
    private CheckersClient client = null;

    private ClientGUI gui = null;

    private String partner = "";

    private HashMap prevBoard = null;

    private boolean myTurn = false;

    private SimpleAttributeSet NameStyle1 = null;

    private SimpleAttributeSet NameStyle2 = null;

    private SimpleAttributeSet messageNameStyle = null;

    private StyledDocument chatDoc = null;

    private CheckersPanel gamePanel;

    private JSeparator centerSeparator;

    private JLabel chatLabel;

    private JScrollPane chatScrollPane;

    private JTextPane chatTextPane;

    private JButton confirmMoveButton;

    private JLabel connectionLabel;

    private JButton QuitButton;

    private JButton sendButton;

    private JTextField sendField;

    private JButton startNewGameButton;

    private JLabel turnLabel;



    public ClientChat(String partner, CheckersClient client, ClientGUI gui) {
        initComponents();
        setTitle("Game with " + partner);
        this.client = client;
        this.gui = gui;
        this.partner = partner;
        this.NameStyle1 = new SimpleAttributeSet();
        StyleConstants.setForeground(this.NameStyle1, Color.RED);
        StyleConstants.setBold(this.NameStyle1, true);
        this.NameStyle2 = new SimpleAttributeSet();
        StyleConstants.setForeground(this.NameStyle2, Color.BLUE);
        StyleConstants.setBold(this.NameStyle2, true);
        this.messageNameStyle = new SimpleAttributeSet();
        this.chatDoc = this.chatTextPane.getStyledDocument();
        addWindowListener(new chatWindowListener());
        this.chatTextPane.setEditable(false);
        DefaultCaret caret = (DefaultCaret)this.chatTextPane.getCaret();
        caret.setUpdatePolicy(2);
    }

    private void initComponents() {
        this.sendField = new JTextField();
        this.sendButton = new JButton();
        JPanel gamePanel = new CheckersPanel();
        this.gamePanel = (CheckersPanel)gamePanel;
        this.centerSeparator = new JSeparator();
        this.confirmMoveButton = new JButton();
        this.turnLabel = new JLabel();
        this.connectionLabel = new JLabel();
        this.chatLabel = new JLabel();
        this.QuitButton = new JButton();
        this.startNewGameButton = new JButton();
        this.chatScrollPane = new JScrollPane();
        this.chatTextPane = new JTextPane();
        setDefaultCloseOperation(2);
        setTitle("Chat with ...");
        setResizable(false);
        this.sendField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ClientChat.this.sendFieldActionPerformed(evt);
            }
        });
        this.sendButton.setText("Send");
        this.sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ClientChat.this.sendButtonActionPerformed(evt);
            }
        });
        gamePanel.setBackground(Color.white);
        gamePanel.setCursor(new Cursor(0));
        gamePanel.setFocusable(false);
        gamePanel.setMaximumSize(new Dimension(400, 400));
        gamePanel.setMinimumSize(new Dimension(400, 400));
        gamePanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                ClientChat.this.gamePanelMousePressed(evt);
            }
        });
        GroupLayout gamePanelLayout = new GroupLayout(gamePanel);
        gamePanel.setLayout(gamePanelLayout);
        gamePanelLayout.setHorizontalGroup(gamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 400, 32767));
        gamePanelLayout.setVerticalGroup(gamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        this.centerSeparator.setOrientation(1);
        this.confirmMoveButton.setText("Confirm Move");
        this.confirmMoveButton.setEnabled(false);
        this.confirmMoveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ClientChat.this.confirmMoveButtonActionPerformed(evt);
            }
        });


        this.turnLabel.setFont(new Font("DejaVu Sans", 0, 18));
        this.turnLabel.setText("Your turn:");
        this.connectionLabel.setText("You've started a new game");
        this.chatLabel.setText("Chat:");
        this.QuitButton.setText("Quit");
        this.QuitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ClientChat.this.QuitButtonActionPerformed(evt);
            }
        });
        this.startNewGameButton.setText("Start new game");
        this.startNewGameButton.setEnabled(false);
        this.startNewGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ClientChat.this.startNewGameButtonActionPerformed(evt);
            }
        });
        this.chatScrollPane.setHorizontalScrollBarPolicy(31);
        this.chatScrollPane.setAutoscrolls(true);
        this.chatScrollPane.setFocusable(false);
        this.chatScrollPane.setHorizontalScrollBar((JScrollBar)null);
        this.chatScrollPane.setMaximumSize(new Dimension(329, 400));
        this.chatScrollPane.setMinimumSize(new Dimension(329, 400));
        this.chatScrollPane.setPreferredSize(new Dimension(329, 400));
        this.chatTextPane.setEditable(false);
        this.chatTextPane.setAutoscrolls(true);
        this.chatTextPane.setCursor(new Cursor(0));
        this.chatTextPane.setFocusable(false);
        this.chatTextPane.setMaximumSize(new Dimension(329, 400));
        this.chatTextPane.setMinimumSize(new Dimension(329, 400));
        this.chatTextPane.setPreferredSize(new Dimension(329, 400));
        this.chatScrollPane.setViewportView(this.chatTextPane);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);


        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(16, 16, 16).addComponent(this.turnLabel, -2, 131, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(this.startNewGameButton).addGap(18, 18, 18).addComponent(this.QuitButton)).addComponent(gamePanel, GroupLayout.Alignment.LEADING, -2, -1, -2).addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(119, 119, 119).addComponent(this.confirmMoveButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))).addGap(12, 12, 12).addComponent(this.centerSeparator, -2, 6, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.sendField).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.sendButton)).addGroup(layout.createSequentialGroup().addComponent(this.chatLabel).addGap(0, 0, 32767)).addComponent(this.chatScrollPane, -2, 329, -2))).addComponent(this.connectionLabel, -1, -1, 32767)).addContainerGap()));


        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addComponent(this.turnLabel)).addComponent(this.chatLabel, GroupLayout.Alignment.TRAILING)).addGroup(GroupLayout.Alignment.TRAILING, layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.startNewGameButton).addComponent(this.QuitButton))).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.centerSeparator).addComponent(gamePanel, -1, -1, 32767).addComponent(this.chatScrollPane, -2, 400, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.sendButton).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.sendField, -2, -1, -2).addComponent(this.confirmMoveButton))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(this.connectionLabel, -2, 17, -2)));
        pack();
    }

    private void sendButtonActionPerformed(ActionEvent evt) {
        sendText();
    }

    private void sendFieldActionPerformed(ActionEvent evt) {
        if (this.sendButton.isEnabled())
            sendText();
    }

    private void sendText() {
        String txt = this.sendField.getText();
        if (!txt.equals(""))
            if (this.client.sendMessage(this.partner, txt) == 1) {
                try {
                    this.chatDoc.insertString(this.chatDoc.getLength(), "You: ", this.NameStyle1);
                    this.chatDoc.insertString(this.chatDoc.getLength(), txt + "\n", this.messageNameStyle);
                } catch (Exception e) {
                    System.err.println(e);
                }
                this.sendField.setText("");
            } else {
                System.err.println("Error writing message");
            }
    }

    public void addToChatField(String message) {
        try {
            this.chatDoc.insertString(this.chatDoc.getLength(), this.partner + ": ", this.NameStyle2);
            this.chatDoc.insertString(this.chatDoc.getLength(), message + "\n", this.messageNameStyle);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void partnerExists() {
        if (!this.sendButton.isEnabled()) {
            this.sendButton.setEnabled(true);
            this.connectionLabel.setText("You reconnecte");
        }
    }

    public void disconnect(String reason) {
        this.sendButton.setEnabled(false);
        this.connectionLabel.setText(reason);
    }

    private void confirmMoveButtonActionPerformed(ActionEvent evt) {
        this.myTurn = false;
        this.client.sendMessage(this.partner, "###new_move###new_board=" + this.gamePanel.getFormattedBoard() + "###");
        this.prevBoard = null;
        setTurn(false);
        this.confirmMoveButton.setEnabled(false);

        this.gamePanel.setSelected(-1, -1);
        this.connectionLabel.setText("Move submitted...");
    }

    private void gamePanelMousePressed(MouseEvent evt) {
        int x_pos = (int)Math.floor(evt.getX() / 50.0D);
        int y_pos = (int)Math.floor(evt.getY() / 50.0D);
        if (this.myTurn)
            if (this.gamePanel.selected == null && (this.gamePanel.getBoard()[y_pos][x_pos] == 1 || this.gamePanel.getBoard()[y_pos][x_pos] == 2)) {
                this.gamePanel.setSelected(x_pos, y_pos);
            } else if (this.gamePanel.selected != null) {
                int[] to = { x_pos, y_pos };
                int res = checkMove(this.gamePanel.getSelected(), to);
                if ((res == 1 ) || res == 2) {

                    int[] s = this.gamePanel.getSelected();
                    int type = this.gamePanel.removePiece(s[0], s[1]);
                    if (to[1] == 0) {
                        this.gamePanel.addPiece(to[0], to[1], 2);
                    } else {
                        this.gamePanel.addPiece(to[0], to[1], type);
                    }
                    this.confirmMoveButton.setEnabled(true);

                    if (res == 1) {
                        this.gamePanel.setSelected(-1, -1);
                    } else if (res == 2) {
                        int dir_x = (to[0] - s[0]) / 2;
                        int dir_y = (to[1] - s[1]) / 2;
                        this.gamePanel.removePiece(s[0] + dir_x, s[1] + dir_y);
                        this.gamePanel.setSelected(to[0], to[1]);
                    }
                } else  {
                    this.gamePanel.setSelected(-1, -1);
                }
            }
    }

    private void QuitButtonActionPerformed(ActionEvent evt) {
        this.client.sendMessage(this.partner, "###game_over###you_win###");
        this.turnLabel.setText("You lost!");
        this.myTurn = false;
        this.prevBoard = null;
        this.confirmMoveButton.setEnabled(false);

        this.gamePanel.setSelected(-1, -1);
        this.startNewGameButton.setEnabled(true);
        this.QuitButton.setEnabled(false);
    }

    private void startNewGameButtonActionPerformed(ActionEvent evt) {
        this.client.sendMessage(this.partner, "###new_game_restarted###");
        setTurn(true);
        this.startNewGameButton.setEnabled(false);
        this.QuitButton.setEnabled(true);
    }

    public void setTurn(boolean turn) {
        this.myTurn = turn;
        if (turn) {
            this.turnLabel.setText("Your turn:");
        } else {
            this.turnLabel.setText(this.partner + "'s turn:");
        }
    }


    public void writeBoard(int[][] board, String message) {
        this.gamePanel.setBoard(board);
        this.prevBoard = new HashMap<Object, Object>();
        this.connectionLabel.setText(message);
        if (message.contains("New move from "))

        if (checkEnd()) {
            this.turnLabel.setText("You lost");
            this.client.sendMessage(this.partner, "###game_over###you_win###");
            this.myTurn = false;
            this.prevBoard = null;
            this.confirmMoveButton.setEnabled(false);
            this.gamePanel.setSelected(-1, -1);
            this.startNewGameButton.setEnabled(true);
            JOptionPane.showMessageDialog(null, "Defeat", "You Lost", 1);
        }
    }

    public void restartGame() {
        this.startNewGameButton.setEnabled(false);
        this.QuitButton.setEnabled(true);
    }

    public void notifyWin() {
        this.turnLabel.setText("You won");
        this.myTurn = false;
        this.prevBoard = null;
        this.confirmMoveButton.setEnabled(false);
        this.gamePanel.setSelected(-1, -1);
        this.startNewGameButton.setEnabled(true);
        this.QuitButton.setEnabled(false);
        JOptionPane.showMessageDialog(null, "You won", "You Won", 1);
    }

    private int checkMove(int[] from, int[] to) {
        if (this.gamePanel.getBoard()[to[1]][to[0]] == 0) {
            if (this.gamePanel.getBoard()[from[1]][from[0]] == 1 && to[1] > from[1])
                return 0;
            if (Math.abs(to[0] - from[0]) == 1 && Math.abs(to[1] - from[1]) == 1)
                return 1;
            if (Math.abs(to[0] - from[0]) == 2 && Math.abs(to[1] - from[1]) == 2) {
                int dir_x = (to[0] - from[0]) / 2;
                int dir_y = (to[1] - from[1]) / 2;
                int enemy_num = this.gamePanel.getBoard()[from[1] + dir_y][from[0] + dir_x];
                if (enemy_num == 3 || enemy_num == 4)
                    return 2;
            }
        }
        return 0;
    }

    private boolean checkEnd() {
        int[][] b = this.gamePanel.getBoard();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (b[y][x] == 1 || b[y][x] == 2)
                    for (int j = 0; j < 8; j++) {
                        for (int k = 0; k < 8; k++) {
                            int[] from = { x, y };
                            int[] to = { k, j };
                            if (checkMove(from, to) != 0)
                                return false;
                        }
                    }
            }
        }
        return true;
    }

    private class CheckersPanel extends JPanel {
        private int[][] board = (int[][])null;

        private int[] selected = null;

        private Image r_piece = null;

        private Image b_piece = null;

        private Image r_piece_king = null;

        private Image b_piece_king = null;

        public CheckersPanel() {
            try {
                this.r_piece = ImageIO.read(Objects.requireNonNull(getClass().getResource("/Images/RPiece.jpg")));
                this.b_piece = ImageIO.read(Objects.requireNonNull(getClass().getResource("/Images/BPiece.jpg")));
                this.r_piece_king = ImageIO.read(Objects.requireNonNull(getClass().getResource("/Images/RKing.jpg")));
                this.b_piece_king = ImageIO.read(Objects.requireNonNull(getClass().getResource("/Images/BKing.jpg")));
            } catch (IOException e) {
                System.out.println("could not open file");
            }
        }

        public int[] getSelected() {
            return this.selected;
        }

        public void setSelected(int x, int y) {
            if (x == -1 && y == -1) {
                this.selected = null;
            } else {
                this.selected = new int[2];
                this.selected[0] = x;
                this.selected[1] = y;
            }
            repaint();
        }

        public void addPiece(int x, int y, int type) {
            if (this.board[y][x] == 0 && (type == 1 || type == 2 || type == 3 || type == 3)) {
                this.board[y][x] = type;
                repaint();
            } else {
                System.err.println("Error: A piece is already there");
            }
        }

        public int removePiece(int x, int y) {
            if (this.board[y][x] != 0) {
                int res = this.board[y][x];
                this.board[y][x] = 0;
                repaint();
                return res;
            }
            System.err.println("Error: no piece exists there");
            return 0;
        }

        public int[][] getBoard() {
            int[][] b = new int[8][8];
            for (int y = 0; y < 8; y++)
                System.arraycopy(this.board[y], 0, b[y], 0, 8);
            return b;
        }

        public String getFormattedBoard() {
            String res = "[";
            for (int[] y : this.board) {
                res = res + "[";
                for (int x : y)
                    res = res + x + ",";
                res = res.substring(0, res.length() - 1);
                res = res + "],";
            }
            res = res.substring(0, res.length() - 1);
            res = res + "]";
            return res;
        }

        public void setBoard(int[][] newBoard) {
            this.board = newBoard;
            repaint();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            int y;
            for (y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    if ((x + y) % 2 == 0)
                        g.fillRect(x * 50, y * 50, 50, 50);
                }
            }
            g.setColor(Color.GREEN);
            if (this.selected != null)
                g.fillRect(this.selected[0] * 50, this.selected[1] * 50, 50, 50);
            if (this.board != null)
                for (y = 0; y < this.board.length; y++) {
                    for (int x = 0; x < this.board.length; x++) {
                        if (this.board[y][x] == 1) {
                            g.drawImage(this.r_piece, x * 50 + 5, y * 50 + 5, x * 50 + 45, y * 50 + 45, 0, 0, this.r_piece.getWidth(null), this.r_piece.getHeight(null), null);
                        } else if (this.board[y][x] == 2) {
                            g.drawImage(this.r_piece_king, x * 50 + 5, y * 50 + 5, x * 50 + 45, y * 50 + 45, 0, 0, this.r_piece_king.getWidth(null), this.r_piece_king.getHeight(null), null);
                        } else if (this.board[y][x] == 3) {
                            g.drawImage(this.b_piece, x * 50 + 5, y * 50 + 5, x * 50 + 45, y * 50 + 45, 0, 0, this.b_piece.getWidth(null), this.b_piece.getHeight(null), null);
                        } else if (this.board[y][x] == 4) {
                            g.drawImage(this.b_piece_king, x * 50 + 5, y * 50 + 5, x * 50 + 45, y * 50 + 45, 0, 0, this.b_piece_king.getWidth(null), this.b_piece_king.getHeight(null), null);
                        }
                    }
                }
        }
    }

    private class chatWindowListener extends WindowAdapter {
        private chatWindowListener() {}

        public void windowClosing(WindowEvent e) {
            ClientChat.this.gui.cleanUpChatWindowClosed(ClientChat.this.partner);
        }
    }
}
