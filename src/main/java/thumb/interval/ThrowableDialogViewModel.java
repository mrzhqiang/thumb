package thumb.interval;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * 异常对话框视图模型。
 *
 * @author qiang.zhang
 */
public final class ThrowableDialogViewModel {
  private static final URL FXML =
      ThrowableDialogViewModel.class.getResource("/thumb/throwable-dialog.fxml");
  private static final URL CSS =
      ThrowableDialogViewModel.class.getResource("/thumb/thumb.css");

  public static void show(Throwable throwable) {
    try {
      Stage stage = new Stage();
      //stage.setTitle("错误");
      FXMLLoader loader = new FXMLLoader(FXML);
      Scene scene = new Scene(loader.load());
      scene.getStylesheets().add(CSS.toExternalForm());
      stage.setScene(scene);
      ThrowableDialogViewModel viewModel = loader.getController();
      viewModel.errorDialogTitle.setText(Throwables.parse(throwable));
      viewModel.errorDialogContent.setText(throwable.getMessage());
      viewModel.debugInfo = Throwables.print(throwable);
      viewModel.stage = stage;
      stage.showAndWait();
    } catch (Exception e) {
      AlertViewModel.showError(e);
    }
  }

  @FXML Label errorDialogTitle;
  @FXML Label errorDialogContent;

  private Stage stage;
  private String debugInfo;

  @FXML void onShowDetailsClicked() {
    TextDialogViewModel.show(debugInfo);
  }

  @FXML void onCloseClicked() {
    stage.close();
  }
}