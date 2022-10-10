

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerGUI extends javax.swing.JFrame{
    private CheckersServer server = null;

    public ServerGUI() {
        initComponents();
        server = new CheckersServer(this);

        addWindowListener(new serverWindowListener());
    }

    private void initComponents() {

        portField = new javax.swing.JTextField();
        listenButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        connectionLabel = new javax.swing.JLabel();
        ScrollPane = new javax.swing.JScrollPane();
        mainText = new javax.swing.JTextArea();
        clientList = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CheckersServer");

        portField.setText("8989");
        portField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portFieldActionPerformed(evt);
            }
        });

        listenButton.setText("Listen");
        listenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listenButtonActionPerformed(evt);
            }
        });

        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        connectionLabel.setText("No connection");
        connectionLabel.setFocusable(false);

        mainText.setColumns(20);
        mainText.setEditable(false);
        mainText.setRows(5);
        mainText.setFocusable(false);
        ScrollPane.setViewportView(mainText);

        clientList.setText("Client List:");
        clientList.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(95, 95, 95)
                                                                .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(listenButton)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(stopButton))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(clientList)))
                                                .addGap(0, 103, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(ScrollPane)
                                                        .addComponent(connectionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(listenButton)
                                        .addComponent(stopButton))
                                .addGap(10, 10, 10)
                                .addComponent(clientList)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(connectionLabel))
        );

        pack();
    }
    private void listenButtonActionPerformed(java.awt.event.ActionEvent evt) {
        startListening();
    }

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (server.stopListening() == 1) {
            connectionLabel.setText("No connection");
            listenButton.setEnabled(true);
            stopButton.setEnabled(false);
        } else {
            connectionLabel.setText("Unable to stop connection");
        }
    }

    private void portFieldActionPerformed(java.awt.event.ActionEvent evt) {
        if (listenButton.isEnabled()) {
            startListening();
        }
    }

    private void startListening() {
        int port = Integer.parseInt(portField.getText());
        if (server.startListener(port) == 1) {
            connectionLabel.setText("Listening on port " + port);
            listenButton.setEnabled(false);
            stopButton.setEnabled(true);
        } else {
            connectionLabel.setText("Unable to listen to port: " + port);
        }
    }

    public void writeClientList(String[] clientList) {
        mainText.setText("");
        for (int i = 0; i < clientList.length; i++) {
            mainText.append(clientList[i] + "\n");
        }
    }


    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ServerGUI().setVisible(true);
            }
        });
    }

    private javax.swing.JLabel clientList;
    private javax.swing.JLabel connectionLabel;
    private javax.swing.JButton listenButton;
    private javax.swing.JScrollPane ScrollPane;
    private javax.swing.JTextArea mainText;
    private javax.swing.JTextField portField;
    private javax.swing.JButton stopButton;


    private class serverWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            server.stopListening();
        }
    }
}
