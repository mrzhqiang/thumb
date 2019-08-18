package thumb.viewmodel;

import com.google.common.base.Strings;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
import thumb.interval.AlertViewModel;
import thumb.interval.FileModel;
import thumb.interval.SystemInfo;
import thumb.interval.ThrowableDialogViewModel;
import thumb.model.Thumb;

/**
 * @author mrzhqiang
 */
public final class ThumbViewModel {
  private static final String REGEX_PICTURE = "\\.(png|jpg|jpeg)$";
  private static final String FORMAT_PICTURE_COUNT = "找到 %s 张图片";

  public final StringProperty imagePath = new SimpleStringProperty(SystemInfo.homeDir());
  public final StringProperty watermarkPath = new SimpleStringProperty("");
  public final StringProperty outputPath = new SimpleStringProperty(SystemInfo.homeDir());
  public final BooleanProperty prefixCheck = new SimpleBooleanProperty(true);
  public final BooleanProperty resolutionCheck = new SimpleBooleanProperty(false);
  public final BooleanProperty resolutionXDisabled = new SimpleBooleanProperty(false);
  public final BooleanProperty resolutionYDisabled = new SimpleBooleanProperty(false);
  public final StringProperty resolutionX = new SimpleStringProperty("");
  public final StringProperty resolutionY = new SimpleStringProperty("");
  public final StringProperty pictureCount = new SimpleStringProperty("");

  private final CompositeDisposable compositeDisposable = new CompositeDisposable();
  private final Thumb thumb = new Thumb();

  public ThumbViewModel() {
    FilenameFilter pictureFilter = (dir, name) -> {
      if (Strings.isNullOrEmpty(name) || !name.contains(".")) {
        return false;
      }
      return Pattern.matches(REGEX_PICTURE, name.substring(name.lastIndexOf(".")));
    };
    compositeDisposable.addAll(
        FileModel.list(imagePath, pictureFilter)
            .observeOn(JavaFxScheduler.platform())
            .doOnNext(files -> pictureCount.set(
                String.format(FORMAT_PICTURE_COUNT, files == null ? 0 : files.length)))
            .subscribe(thumb::setPictureList, ThrowableDialogViewModel::show),
        FileModel.find(watermarkPath)
            .observeOn(JavaFxScheduler.platform())
            .subscribe(thumb::setWatermark, ThrowableDialogViewModel::show),
        FileModel.find(outputPath)
            .observeOn(JavaFxScheduler.platform())
            .subscribe(thumb::setOutput, ThrowableDialogViewModel::show),
        JavaFxObservable.valuesOf(resolutionX)
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .observeOn(JavaFxScheduler.platform())
            .subscribe(thumb::setResolutionX, ThrowableDialogViewModel::show),
        JavaFxObservable.valuesOf(resolutionY)
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .observeOn(JavaFxScheduler.platform())
            .subscribe(thumb::setResolutionY, ThrowableDialogViewModel::show),
        JavaFxObservable.valuesOf(prefixCheck)
            .observeOn(JavaFxScheduler.platform())
            .subscribe(thumb::setPrefixEnabled, ThrowableDialogViewModel::show),
        JavaFxObservable.valuesOf(resolutionCheck)
            .doOnNext(aBoolean -> resolutionXDisabled.set(!aBoolean))
            .doOnNext(aBoolean -> resolutionYDisabled.set(!aBoolean))
            .observeOn(JavaFxScheduler.platform())
            .subscribe(thumb::setResolutionEnabled, ThrowableDialogViewModel::show)
    );
  }

  public void dispose() {
    compositeDisposable.dispose();
  }

  public void execution() {
    compositeDisposable.add(Observable.just(thumb)
        .subscribeOn(Schedulers.io())
        .flatMap(thumb -> Observable.just(thumb.getPictureList())
            .map(Thumbnails::of)
            .map(builder -> {
              if (thumb.getResolutionEnabled()) {
                return builder.size(thumb.getResolutionX(), thumb.getResolutionY());
              } else {
                return builder.scale(0.5);
              }
            }))
        .map(builder -> {
          File watermark = thumb.getWatermark();
          if (watermark != null) {
            BufferedImage read = ImageIO.read(thumb.getWatermark());
            return builder.watermark(new Watermark(Positions.BOTTOM_RIGHT, read, 0.5F));
          }
          return builder;
        })
        .map(builder -> {
          if (thumb.getPrefixEnabled()) {
            builder.toFiles(thumb.getOutput(), Rename.PREFIX_HYPHEN_THUMBNAIL);
          } else {
            builder.toFiles(thumb.getOutput(), Rename.NO_CHANGE);
          }
          return true;
        })
        .observeOn(JavaFxScheduler.platform())
        .subscribe(aBoolean -> AlertViewModel.show("缩略图制作完毕"), ThrowableDialogViewModel::show));
  }
}
