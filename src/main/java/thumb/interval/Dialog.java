package thumb.interval;

import com.google.common.base.Preconditions;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.annotation.Nullable;

/**
 * @author qiang.zhang
 */
public enum Dialog {
  ;

  public static Optional<ButtonType> waitConfirm(String message, @Nullable String content) {
    Preconditions.checkNotNull(message, "message == null");
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("确认");
    alert.setHeaderText(message);
    alert.setContentText(content);
    return alert.showAndWait();
  }

  public static void showError(Throwable error) {
    Preconditions.checkNotNull(error, "error == null");
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("错误");
    alert.setHeaderText(error.getMessage());
    alert.setContentText(Throwables.print(error));
    alert.show();
  }
}
