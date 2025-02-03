javac --module-path "lib" --add-modules javafx.controls,javafx.fxml,comunication -d bin src\com\MainView.java src\com\controller\*.java src\com\GUI\*.java
java --module-path "lib" --add-modules javafx.controls,javafx.fxml,comunication -cp bin com.MainView
    