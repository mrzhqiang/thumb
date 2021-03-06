package thumb.interval;

import java.io.File;
import java.util.Optional;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.annotation.Nullable;

/**
 * @author qiang.zhang
 */
public enum Chooser {
  ;

  public static Optional<File> directory(String title, @Nullable File workDir) {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle(title);
    if (workDir != null && workDir.exists()) {
      chooser.setInitialDirectory(workDir);
    }
    return Optional.ofNullable(chooser.showDialog(null))
        .filter(file -> file.exists() && file.isDirectory());
  }

  public static Optional<File> file(String title, @Nullable File workDir,
      @Nullable FileChooser.ExtensionFilter filter) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle(title);
    if (workDir != null && workDir.exists()) {
      chooser.setInitialDirectory(workDir);
    }
    if (filter != null) {
      chooser.getExtensionFilters().add(filter);
    }
    return Optional.ofNullable(chooser.showOpenDialog(null))
        .filter(file -> file.exists() && file.isFile());
  }
}
