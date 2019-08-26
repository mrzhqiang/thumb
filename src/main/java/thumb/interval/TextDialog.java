package thumb.interval;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

/**
 * @author qiang.zhang
 */
public enum TextDialog {
  ;

  private static final URL FXML = TextDialog.class.getResource("/thumb/text-dialog.fxml");
  private static final URL CSS = TextDialog.class.getResource("/thumb/thumb.css");

  public static void show(String text) {
    try {
      Stage stage = new Stage();
      stage.setTitle("详情");
      FXMLLoader loader = new FXMLLoader(FXML);
      Scene scene = new Scene(loader.load());
      scene.getStylesheets().add(CSS.toExternalForm());
      stage.setScene(scene);
      TextDialog viewModel = loader.getController();
      viewModel.stage = stage;
      viewModel.contentTextArea.setText(text);
      stage.showAndWait();
    } catch (Exception e) {
      Dialog.showError(e);
    }
  }

  @FXML TextArea contentTextArea;

  private Stage stage;

  @FXML void onCopyClicked() {
    Clipboard clipboard = Clipboard.getSystemClipboard();
    ClipboardContent content = new ClipboardContent();
    content.putString(contentTextArea.getText());
    clipboard.setContent(content);
  }

  @FXML void onCloseClicked() {
    stage.close();
  }
}
