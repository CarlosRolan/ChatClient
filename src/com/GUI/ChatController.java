package com.GUI;

import com.controller.Client;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.geometry.Pos;
import java.util.HashMap;
import com.comunication.*;

public class ChatController {

    @FXML
    private ListView<String> list_users; // Lista de usuarios
    @FXML
    private ListView<ChatMessage> list_conversation; // Lista de mensajes
    @FXML
    private TextArea txtArea_msgInput; // Campo de texto para escribir
    @FXML
    private Button btn_send; // Botón de enviar
    @FXML
    private VBox no_selected_conversation; // Panel de "No hay usuario seleccionado"
    @FXML
    private SplitPane chat_pane; // Panel de chat visible solo cuando hay usuario seleccionado
    @FXML
    private VBox no_user_panel; // Panel de "No hay nadie conectado"

    private HashMap<String, ObservableList<ChatMessage>> conversations_history = new HashMap<>();
    private String currentChatterId = "-1";

    private ObservableList<String> currentUsers = FXCollections.observableArrayList();

    public void initialize() {

        toggleChatPane(false);
        toggleUserList(false);

        try {
            Client.getClientInstance().requestOnlineUsers();
            Client.getClientInstance().notifyImOnline();
        } catch (NullPointerException e) {
            System.out.println("U are offline");
        }

        startMessageReaderThread();

        // Personalizar la visualización de los mensajes en la lista
        list_conversation.setCellFactory(listView -> new MessageCell());

        // Agregar evento de selección en la lista de usuarios
        list_users.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                openConversation(newValue);
            }
        });

        // Evento del botón de enviar
        btn_send.setOnAction(event -> sendMessage());
    }

    private void sendMessage() {
        String text = txtArea_msgInput.getText().trim();
        if (!text.isEmpty()) {
            ChatMessage chatMsg = new ChatMessage(text, true);
            conversations_history.computeIfAbsent(currentChatterId, k -> FXCollections.observableArrayList())
                    .add(chatMsg);

            list_conversation.scrollTo(conversations_history.get(currentChatterId).size() - 1); // Auto-scroll al último
                                                                                                // mensaje
            txtArea_msgInput.clear(); // Limpiar el campo de texto

            // Enviar mensaje al servidor
            Client.getClientInstance().sendMsgToChat(currentChatterId, text);
        }
    }

    private void toggleChatPane(boolean show) {
        chat_pane.setVisible(show);
        no_selected_conversation.setVisible(!show);
    }

    private void toggleUserList(boolean show) {
        list_users.setVisible(show);
        no_user_panel.setVisible(!show);
    }

    // Clase para representar los mensajes
    public static class ChatMessage {
        private final String text;
        private final boolean isUser;

        public ChatMessage(String text, boolean isUser) {
            this.text = text;
            this.isUser = isUser;
        }

        public String getText() {
            return text;
        }

        public boolean isUser() {
            return isUser;
        }
    }

    // Personalizar la apariencia de los mensajes en la lista
    private static class MessageCell extends ListCell<ChatMessage> {
        @Override
        protected void updateItem(ChatMessage message, boolean empty) {
            super.updateItem(message, empty);

            if (empty || message == null) {
                setText(null);
                setGraphic(null);
            } else {
                Text text = new Text(message.getText());
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-padding: 10px; -fx-background-radius: 10px; -fx-max-width: 250px;");

                if (message.isUser()) {
                    textFlow.setStyle("-fx-background-color: #DCF8C6; -fx-padding: 10px; -fx-background-radius: 10px;");
                } else {
                    textFlow.setStyle("-fx-background-color: #EAEAEA; -fx-padding: 10px; -fx-background-radius: 10px;");
                }

                HBox hbox = new HBox(textFlow);
                hbox.setAlignment(message.isUser() ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
                setGraphic(hbox);
            }
        }
    }

    // Método para leer mensajes en un hilo separado
    private void startMessageReaderThread() {
        Thread messageReaderThread = new Thread(() -> {
            while (true) {
                Message responseMessage = Client.getClientInstance().readMessage();

                Platform.runLater(() -> {
                    switch (responseMessage.getAction()) {
                        case RequestAPI.SHOW_ALL_ONLINE:
                            updateOnlineUsers(responseMessage.getText());
                            break;
                        case "FROM_CHAT":
                            handleIncomingMessage(responseMessage);
                            break;
                        default:
                            break;
                    }
                });
            }
        });

        // Iniciar el hilo de lectura de mensajes
        messageReaderThread.start();
    }

    // Método para manejar los usuarios conectados
    private void updateOnlineUsers(String usersText) {
        String[] onlineUsers = usersText.split(",");
        currentUsers.clear();

        for (String iterator : onlineUsers) {
            String[] userInfo = iterator.split("_");
            if (!userInfo[0].equals(Client.getClientInstance().getServerId())) {
                currentUsers.add(iterator);
            }
        }

        list_users.setItems(currentUsers);

        toggleUserList(!currentUsers.isEmpty());
    }

    // Método para manejar mensajes entrantes
    private void handleIncomingMessage(Message responseMessage) {
        String senderID = responseMessage.getEmisor();
        ChatMessage receivedMessage = new ChatMessage(responseMessage.getText(), false);

        // Si la conversación ya existe, se agrega el mensaje. Si no, se crea una nueva.
        conversations_history.computeIfAbsent(senderID, k -> FXCollections.observableArrayList()).add(receivedMessage);
        addNewMsgAlert(senderID);
    }

    private void addNewMsgAlert(String senderID) {
        // Si el usuario NO está en la conversación activa, actualizar el usurio para
        // añadir el indicador de nuevo mensaje también
        if (!currentChatterId.equals(senderID) || currentChatterId.equals("-1")) {
            for (int i = 0; i < currentUsers.size(); i++) {
                String iterator = currentUsers.get(i);
                String[] userInfo = iterator.split("_");

                if (userInfo[0].equals(senderID)) {
                    if (!iterator.endsWith("*")) {
                        currentUsers.set(i, iterator + "*");
                        break;
                    } else {

                    }
                    // Salimos del ciclo una vez que encontramos y actualizamos al usuario
                }
            }

        }
    }

    private void removeNewMsgAlert(String pickedUser) {
        if (pickedUser.endsWith("*")) {
            int i = currentUsers.indexOf(pickedUser);
            currentUsers.set(i, pickedUser.replace("*", ""));
        }
    }

    private void openConversation(String pickedUser) {
        String[] pickedUserInfo = pickedUser.split("_");
        currentChatterId = pickedUserInfo[0];

        // Vinculamos la lista con la UI
        list_conversation.setItems(conversations_history.computeIfAbsent(currentChatterId,
                k -> FXCollections.observableArrayList()));

        removeNewMsgAlert(pickedUser);

        toggleChatPane(true);
    }
}
