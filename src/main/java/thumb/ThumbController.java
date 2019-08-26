package thumb;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
import thumb.interval.Dialog;
import thumb.interval.Chooser;
import thumb.interval.Files;
import thumb.interval.SystemInfo;
import thumb.interval.ThrowableDialog;
import thumb.model.Thumb;

/**
 * 控制器。
 * <p>
 * 温馨提示：使用 public 修饰的的属性和方法无需 @FXML 注解。
 *
 * @author mrzhqiang
 */
public final class ThumbController {
  private static final File WORK_DIR = new File(SystemInfo.homeDir());
  private static final String[] SUPPORT_PICTURE = {"*.jpeg", "*.jpg", "*.png"};
  private static final FileChooser.ExtensionFilter PICTURE_EXTENSION_FILTER =
      new FileChooser.ExtensionFilter("Picture", Lists.newArrayList(SUPPORT_PICTURE));
  private static final String REGEX_PICTURE = "\\.(png|jpg|jpeg)$";
  private static final String FORMAT_PICTURE_COUNT = "找到 %s 张图片";

  @FXML TextField imagePathTextField;
  @FXML TextField watermarkPathTextField;
  @FXML TextField outputPathTextField;
  @FXML CheckBox prefixCheckBox;
  @FXML CheckBox resolutionCheck;
  @FXML TextField resolutionXTextField;
  @FXML TextField resolutionYTextField;
  @FXML Label pictureCountLabel;

  private final Thumb thumb = new Thumb();
  private final CompositeDisposable compositeDisposable = new CompositeDisposable();
  private final FilenameFilter pictureFilter = (dir, name) -> {
    if (Strings.isNullOrEmpty(name) || !name.contains(".")) {
      return false;
    }
    return Pattern.matches(REGEX_PICTURE, name.substring(name.lastIndexOf(".")));
  };

  @FXML void initialize() {
    bindEvent();
    initLayout();
  }

  void dispose() {
    compositeDisposable.dispose();
  }

  @FXML void onImagePathClicked() {
    Chooser.directory("选择图片目录", WORK_DIR)
        .map(File::getAbsolutePath)
        .ifPresent(s -> imagePathTextField.setText(s));
  }

  @FXML void onWatermarkPathClicked() {
    Chooser.file("选择水印图片", WORK_DIR, PICTURE_EXTENSION_FILTER)
        .map(File::getAbsolutePath)
        .ifPresent(s -> watermarkPathTextField.setText(s));
  }

  @FXML void onOutputPathClicked() {
    Chooser.directory("选择输出目录", WORK_DIR)
        .map(File::getAbsolutePath)
        .ifPresent(s -> outputPathTextField.setText(s));
  }

  @FXML void onExecutionClicked() {
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
        .subscribe(aBoolean -> Dialog.waitConfirm("操作完成", "缩略图已制作完毕"), ThrowableDialog::show));
  }

  private void bindEvent() {
    compositeDisposable.addAll(
        Files.list(imagePathTextField.textProperty(), pictureFilter)
            .observeOn(JavaFxScheduler.platform())
            .doOnNext(files -> pictureCountLabel.setText(
                String.format(FORMAT_PICTURE_COUNT, files == null ? 0 : files.length)))
            .subscribe(thumb::setPictureList, ThrowableDialog::show),
        Files.find(watermarkPathTextField.textProperty())
            .observeOn(JavaFxScheduler.platform())
            .subscribe(thumb::setWatermark, ThrowableDialog::show),
        Files.find(outputPathTextField.textProperty())
            .observeOn(JavaFxScheduler.platform())
            .subscribe(thumb::setOutput, ThrowableDialog::show),
        JavaFxObservable.valuesOf(resolutionXTextField.textProperty())
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .observeOn(JavaFxScheduler.platform())
            .subscribe(thumb::setResolutionX, ThrowableDialog::show),
        JavaFxObservable.valuesOf(resolutionXTextField.textProperty())
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .observeOn(JavaFxScheduler.platform())
            .subscribe(thumb::setResolutionY, ThrowableDialog::show),
        JavaFxObservable.valuesOf(prefixCheckBox.selectedProperty())
            .observeOn(JavaFxScheduler.platform())
            .subscribe(thumb::setPrefixEnabled, ThrowableDialog::show),
        JavaFxObservable.valuesOf(resolutionCheck.selectedProperty())
            .doOnNext(aBoolean -> resolutionXTextField.setDisable(!aBoolean))
            .doOnNext(aBoolean -> resolutionYTextField.setDisable(!aBoolean))
            .observeOn(JavaFxScheduler.platform())
            .subscribe(thumb::setResolutionEnabled, ThrowableDialog::show)
    );
  }

  private void initLayout() {
    imagePathTextField.setText(SystemInfo.homeDir());
    outputPathTextField.setText(SystemInfo.homeDir());
    prefixCheckBox.setSelected(true);
    resolutionCheck.setSelected(false);
  }
}
