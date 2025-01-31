package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.geometry.Pos;
import controller.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatController {

    public static Client clientInstance = getInstance();

    public static Client getInstance() {
        return new Client("Carlos");
    }

    @FXML
    private ListView<String> list_users; // Lista de usuarios
    @FXML
    private ListView<Message> list_conversation; // Lista de mensajes
    @FXML
    private TextArea messageInput; // Campo de texto para escribir
    @FXML
    private Button sendButton; // Botón de enviar

    private ObservableList<Message> messages = FXCollections.observableArrayList(
            new Message("Hola, ¿cómo estás?", false),
            new Message("¡Hola! Estoy bien, ¿y tú?", true),
            new Message("También bien, gracias. ¿Qué tal tu día?", false),
            new Message("Bastante ocupado, pero bien. ¿Y el tuyo?", true));

    public void initialize() {
        list_conversation.setItems(messages);

        // Personalizar la visualización de los mensajes en la lista
        list_conversation.setCellFactory(listView -> new MessageCell());

        // Evento del botón de enviar
        sendButton.setOnAction(event -> sendMessage());
    }

    private void sendMessage() {
        String text = messageInput.getText().trim();
        if (!text.isEmpty()) {
            messages.add(new Message(text, true)); // Mensaje enviado por el usuario
            messageInput.clear(); // Limpiar el campo de texto
            list_conversation.scrollTo(messages.size() - 1); // Auto-scroll al último mensaje
            // TODO aqui tiene que ir el metodo de enviar mensaje al servidor
        }
    }

    // Clase para representar los mensajes
    public static class Message {
        private final String text;
        private final boolean isUser;

        public Message(String text, boolean isUser) {
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
    private static class MessageCell extends ListCell<Message> {
        @Override
        protected void updateItem(Message message, boolean empty) {
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
}
