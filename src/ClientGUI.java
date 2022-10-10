import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


    public class ClientGUI extends JFrame {
        private CheckersClient client = null;

        public HashMap chats = null;

        private String name;
        DefaultListModel userListModel = null;
        private JButton chatButton;
        private JButton connectButton;
        private JLabel connectionLabel;
        private JButton disconnectButton;
        private JTextField ipAddressField;
        private JLabel ipAddressLabel;
        private JTextField nameField;
        private JLabel nameLabel;
        private JTextField portField;
        private JLabel portLabel;
        private JList userList;
        private JLabel usersLabel;
        private JScrollPane usersScrollPane;

        public ClientGUI() {
            this.userListModel = new DefaultListModel();
            this.chats = new HashMap<Object, Object>();
            initComponents();
            MouseListener mouseListener = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 &&
                            ClientGUI.this.userList.getSelectedValue() != null) {
                        ClientGUI.this.newChatWindow((String) ClientGUI.this.userList.getSelectedValue());
                        ClientGUI.this.userList.clearSelection();
                    }
                }
            };
            this.userList.addMouseListener(mouseListener);
            addWindowListener(new clientWindowListener());
        }

        private void initComponents() {
            this.ipAddressField = new JTextField();
            this.ipAddressLabel = new JLabel();
            this.connectButton = new JButton();
            this.portLabel = new JLabel();
            this.portField = new JTextField();
            this.connectionLabel = new JLabel();
            this.disconnectButton = new JButton();
            this.nameLabel = new JLabel();
            this.nameField = new JTextField();
            this.usersLabel = new JLabel();
            this.chatButton = new JButton();
            this.usersScrollPane = new JScrollPane();
            this.userList = new JList();
            setDefaultCloseOperation(3);
            setTitle("Checkers");
            setResizable(false);
            this.ipAddressField.setText("127.0.0.1");
            this.ipAddressField.setNextFocusableComponent(this.portField);
            this.ipAddressField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    ClientGUI.this.ipAddressFieldActionPerformed(evt);
                }
            });
            this.ipAddressLabel.setText("IP Address:");
            this.ipAddressLabel.setFocusable(false);
            this.connectButton.setText("Connect");
            this.connectButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    ClientGUI.this.connectButtonActionPerformed(evt);
                }
            });
            this.portLabel.setText("Port:");
            this.portLabel.setFocusable(false);
            this.portField.setText("8989");
            this.portField.setNextFocusableComponent(this.nameField);
            this.portField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    ClientGUI.this.portFieldActionPerformed(evt);
                }
            });
            this.connectionLabel.setText("Not connected");
            this.connectionLabel.setFocusable(false);
            this.disconnectButton.setText("Disconnect");
            this.disconnectButton.setEnabled(false);
            this.disconnectButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    ClientGUI.this.disconnectButtonActionPerformed(evt);
                }
            });
            this.nameLabel.setText("Name:");
            this.nameLabel.setFocusable(false);
            this.nameField.setNextFocusableComponent(this.connectButton);
            this.nameField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    ClientGUI.this.nameFieldActionPerformed(evt);
                }
            });
            this.usersLabel.setText("Users:");
            this.usersLabel.setFocusable(false);
            this.chatButton.setText("New Game");
            this.chatButton.setEnabled(false);
            this.chatButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    ClientGUI.this.chatButtonActionPerformed(evt);
                }
            });
            this.userList.setModel(this.userListModel);
            this.userList.setSelectionMode(0);
            this.userList.setFocusable(false);
            this.userList.setMaximumSize(new Dimension(0, 50));
            this.usersScrollPane.setViewportView(this.userList);
            GroupLayout layout = new GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.connectionLabel, -1, -1, 32767).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.usersScrollPane, -2, 355, -2).addComponent(this.usersLabel).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.ipAddressLabel).addComponent(this.portLabel, GroupLayout.Alignment.TRAILING).addComponent(this.nameLabel, GroupLayout.Alignment.TRAILING)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.ipAddressField).addComponent(this.portField).addComponent(this.nameField, -2, 121, -2)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(36, 36, 36).addComponent(this.connectButton)).addGroup(layout.createSequentialGroup().addGap(26, 26, 26).addComponent(this.disconnectButton))))).addContainerGap()))).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.chatButton).addGap(143, 143, 143)));
            layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.ipAddressLabel).addComponent(this.ipAddressField, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.portField, -2, -1, -2).addComponent(this.portLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.nameField, -2, -1, -2).addComponent(this.nameLabel))).addGroup(layout.createSequentialGroup().addGap(27, 27, 27).addComponent(this.connectButton).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.disconnectButton))).addGap(18, 18, 18).addComponent(this.usersLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.usersScrollPane, -2, 168, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.chatButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(this.connectionLabel)));
            pack();
        }

        private void ipAddressFieldActionPerformed(ActionEvent evt) {
            if (this.connectButton.isEnabled())
                connectClient();
        }

        private void portFieldActionPerformed(ActionEvent evt) {
            if (this.connectButton.isEnabled())
                connectClient();
        }

        private void connectButtonActionPerformed(ActionEvent evt) {
            connectClient();
        }

        private void connectClient() {
            String IPAddress = this.ipAddressField.getText();
            int port = Integer.parseInt(this.portField.getText());
            this.name = this.nameField.getText();
            if (this.name.equals("")) {
                this.connectionLabel.setText("Must enter a name");
                return;
            }
            CheckersClient c = new CheckersClient(this);
            if (c.connect(IPAddress, port, this.name) == 1) {
                this.client = c;
                this.connectionLabel.setText("Connected");
                this.disconnectButton.setEnabled(true);
                this.connectButton.setEnabled(false);
                this.chatButton.setEnabled(true);
                this.ipAddressField.setEnabled(false);
                this.portField.setEnabled(false);
                this.nameField.setEnabled(false);
                this.client.start();
            } else {
                this.connectionLabel.setText("Error connecting");
            }
        }

        public void setUserList(String[] names) {
            this.userListModel.clear();
            for (String userName : names) {
                if (!userName.equals(this.name))
                    this.userListModel.addElement(userName);
                if (this.chats.containsKey(userName)) {
                    ClientChat chat = (ClientChat)this.chats.get(userName);
                    chat.partnerExists();
                }
            }
        }

        public void recievedMessage(String sender, String message) {
            if (!this.chats.containsKey(sender))
                newChatWindow(sender);
            ClientChat chat = (ClientChat)this.chats.get(sender);
            chat.addToChatField(message);
        }

        public void updateGame(String sender, String board, String message) {
            updateGame(sender, board, this.name, message);
        }

        public void updateGame(String sender, String board, String turn, String message) {
            if (!this.chats.containsKey(sender)) {
                newChatWindow(sender, true);
            } else {
                int[][] realBoard = new int[8][8];
                String res = board.substring(1, board.length() - 1);
                String[] rows = res.split("\\],\\[");
                rows[0] = rows[0].substring(1, rows[0].length());
                rows[7] = rows[7].substring(0, rows[7].length() - 1);
                for (int y = 0; y < 8; y++) {
                    String[] chars = rows[y].split(",");
                    for (int x = 0; x < 8; x++)
                        realBoard[y][x] = Integer.parseInt(chars[x]);
                }
                ClientChat game = (ClientChat)this.chats.get(sender);
                String final_message = message;
                game.setTurn(turn.equals(this.name));
                if (message.equals("A new game has startd"))
                    if (turn.equals(this.name)) {
                        final_message = "New game started";
                    } else {
                        final_message = sender + " has started a new game with you!";
                        game.restartGame();
                    }
                game.writeBoard(realBoard, final_message);
            }
        }

        public void notifyWin(String partner) {
            if (!this.chats.containsKey(partner))
                newChatWindow(partner, true);
            ClientChat game = (ClientChat)this.chats.get(partner);
            game.notifyWin();
        }

        public void newChatWindow(String partner) {
            newChatWindow(partner, false);
        }

        public void newChatWindow(String partner, boolean auto) {
            if (!this.chats.containsKey(partner)) {
                final ClientChat newChat = new ClientChat(partner, this.client, this);
                this.chats.put(partner, newChat);
                if (auto)

                this.client.sendMessage(partner, "###new_game_window###");
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        newChat.setVisible(true);
                    }
                });
            } else {
                System.err.println("A game is already open");
                this.connectionLabel.setText("game already open");
            }
        }

        public void connectionDied(String partner) {
            if (this.chats.containsKey(partner)) {
                ClientChat chatWindow = (ClientChat)this.chats.get(partner);
                chatWindow.disconnect("player disconnected");
            }
        }

        public void cleanUpChatWindowClosed(String partner) {
            this.chats.remove(partner);
        }

        public void disconnect(String reason) {
            if (this.client.disconnect() == 1) {
                this.connectionLabel.setText(reason);
                this.disconnectButton.setEnabled(false);
                this.connectButton.setEnabled(true);
                this.chatButton.setEnabled(false);
                this.ipAddressField.setEnabled(true);
                this.portField.setEnabled(true);
                this.nameField.setEnabled(true);
                if (!this.chats.isEmpty())
                    for (ClientChat chat : (ClientChat[])this.chats.values().toArray((Object[])new ClientChat[0]))
                        chat.disconnect(reason);
                this.userListModel.clear();
                this.client = null;
                this.name = "";
            }
        }

        private void disconnectButtonActionPerformed(ActionEvent evt) {
            disconnect("Disconnected");
        }

        private void chatButtonActionPerformed(ActionEvent evt) {
            if (this.userList.getSelectedValue() != null) {
                newChatWindow((String) this.userList.getSelectedValue());
                this.userList.clearSelection();
            }
        }

        private void nameFieldActionPerformed(ActionEvent evt) {
            if (this.connectButton.isEnabled())
                connectClient();
        }

        public static void main(String[] args) {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, (String)null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, (String)null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, (String)null, ex);
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, (String)null, ex);
            }
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    (new ClientGUI()).setVisible(true);
                }
            });
        }

        private class clientWindowListener extends WindowAdapter {
            private clientWindowListener() {}

            public void windowClosing(WindowEvent e) {
                if (ClientGUI.this.client != null)
                    ClientGUI.this.disconnect("");
            }
        }
    }

