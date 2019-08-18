package thumb.interval;

import com.google.common.base.Preconditions;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.StringProperty;

/**
 * @author mrzhqiang
 */
public enum FileModel {
  ;

  public static Observable<File[]> list(StringProperty property, FilenameFilter filter) {
    Preconditions.checkNotNull(property, "string property == null");
    Preconditions.checkNotNull(filter, "filename filter == null");
    return JavaFxObservable.valuesOf(property)
        .throttleWithTimeout(300, TimeUnit.MILLISECONDS)
        .filter(s -> !s.isEmpty())
        .observeOn(Schedulers.io())
        .map(File::new)
        .map(file -> file.listFiles(filter));
  }

  public static Observable<File> find(StringProperty property) {
    Preconditions.checkNotNull(property, "string property == null");
    return JavaFxObservable.valuesOf(property)
        .throttleWithTimeout(300, TimeUnit.MILLISECONDS)
        .filter(s -> !s.isEmpty())
        .map(File::new);
  }
}
