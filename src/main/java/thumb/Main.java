package thumb;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import thumb.interval.ThrowableDialogViewModel;

public final class Main extends Application {
  private static final URL FXML = Main.class.getResource("/thumb/thumb.fxml");

  private ThumbController viewModel;

  @Override
  public void start(Stage primaryStage) {
    try {
      FXMLLoader loader = new FXMLLoader(FXML);
      Parent root = loader.load();
      primaryStage.setTitle("Thumb 缩略图");
      primaryStage.setScene(new Scene(root));
      viewModel = loader.getController();
      primaryStage.show();
    } catch (Exception e) {
      ThrowableDialogViewModel.show(e);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override public void stop() {
    try {
      viewModel.dispose();
    } catch (Exception e) {
      ThrowableDialogViewModel.show(e);
    }
  }
}
