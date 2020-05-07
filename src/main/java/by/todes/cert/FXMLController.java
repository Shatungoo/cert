package by.todes.cert;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

public class FXMLController implements Initializable {

  // private Desktop desktop = Desktop.getDesktop();

  @FXML
  private Label label;

  Cert cert = new Cert();

  @FXML
  private void handleButtonAction(ActionEvent event) {
    System.out.println("You clicked me!");
    label.setText("Hello World!");
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }

  @FXML
  private void openFile(ActionEvent event) throws Exception {
    final FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("cer files", "*.cer");
    fileChooser.getExtensionFilters().add(extFilter);
    File file = fileChooser.showOpenDialog(new Stage());

    if (file != null) {
      cert.openCert(file);
    }
  }

  @FXML
  private void saveCert(ActionEvent event) throws Exception {
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialFileName("cert");

    File file = fileChooser.showSaveDialog(new Stage());
    if (file != null) {
      cert.toFile(file.getPath());
    }
  }

  @FXML
  private void loadCert(ActionEvent event) throws Exception {
    Dialog<Pair<String, String>> dialog = new Dialog<>();
    dialog.setTitle("Enter cert");
    dialog.setHeight(600);
    dialog.setWidth(1000);
    // Set the button types.
    ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
    GridPane gridPane = new GridPane();
    TextField from = new TextField();
    from.setPromptText("Enter cert here");
    gridPane.add(from, 0, 0);
    dialog.getDialogPane().setContent(gridPane);
    Optional<Pair<String, String>> result = dialog.showAndWait();
    Platform.runLater(() -> from.requestFocus());
  }

}
