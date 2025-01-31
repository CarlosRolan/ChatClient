

javac --module-path "lib" --add-modules javafx.controls,javafx.fxml -d bin src\MainView.java src\controller\*.java src\GUI\*.java

java --module-path "lib" --add-modules javafx.controls,javafx.fxml -cp bin MainView
