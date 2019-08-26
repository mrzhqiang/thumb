package thumb.interval;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * @author qiang.zhang
 */
public enum ThrowableDialog {
  ;

  private static final URL FXML =
      ThrowableDialog.class.getResource("/thumb/throwable-dialog.fxml");
  private static final URL CSS =
      ThrowableDialog.class.getResource("/thumb/thumb.css");

  public static void show(Throwable throwable) {
    try {
      Stage stage = new Stage();
      //stage.setTitle("错误");
      FXMLLoader loader = new FXMLLoader(FXML);
      Scene scene = new Scene(loader.load());
      scene.getStylesheets().add(CSS.toExternalForm());
      stage.setScene(scene);
      ThrowableDialog viewModel = loader.getController();
      viewModel.errorDialogTitle.setText(Throwables.parse(throwable));
      viewModel.errorDialogContent.setText(throwable.getMessage());
      viewModel.debugInfo = Throwables.print(throwable);
      viewModel.stage = stage;
      stage.showAndWait();
    } catch (Exception e) {
      Dialog.showError(e);
    }
  }

  @FXML Label errorDialogTitle;
  @FXML Label errorDialogContent;

  private Stage stage;
  private String debugInfo;

  @FXML void onShowDetailsClicked() {
    TextDialog.show(debugInfo);
  }

  @FXML void onCloseClicked() {
    stage.close();
  }
}
