package thumb;

import com.google.common.collect.Lists;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import thumb.interval.ChooserViewModel;
import thumb.interval.SystemInfo;
import thumb.viewmodel.ThumbViewModel;

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

  @FXML TextField imagePathTextField;
  @FXML TextField watermarkPathTextField;
  @FXML TextField outputPathTextField;
  @FXML CheckBox prefixCheckBox;
  @FXML CheckBox resolutionCheck;
  @FXML TextField resolutionXTextField;
  @FXML TextField resolutionYTextField;
  @FXML Label pictureCountLabel;

  private final ThumbViewModel viewModel = new ThumbViewModel();

  @FXML void initialize() {
    imagePathTextField.textProperty().bindBidirectional(viewModel.imagePath);
    watermarkPathTextField.textProperty().bindBidirectional(viewModel.watermarkPath);
    outputPathTextField.textProperty().bindBidirectional(viewModel.outputPath);
    prefixCheckBox.selectedProperty().bindBidirectional(viewModel.prefixCheck);
    resolutionCheck.selectedProperty().bindBidirectional(viewModel.resolutionCheck);
    resolutionXTextField.disableProperty().bindBidirectional(viewModel.resolutionXDisabled);
    resolutionYTextField.disableProperty().bindBidirectional(viewModel.resolutionYDisabled);
    resolutionXTextField.textProperty().bindBidirectional(viewModel.resolutionX);
    resolutionYTextField.textProperty().bindBidirectional(viewModel.resolutionY);
    pictureCountLabel.textProperty().bindBidirectional(viewModel.pictureCount);
  }

  void dispose() {
    viewModel.dispose();
  }

  @FXML void onImagePathClicked() {
    ChooserViewModel.directory("选择图片目录", WORK_DIR)
        .map(File::getAbsolutePath)
        .ifPresent(viewModel.imagePath::set);
  }

  @FXML void onWatermarkPathClicked() {
    ChooserViewModel.file("选择水印图片", WORK_DIR, PICTURE_EXTENSION_FILTER)
        .map(File::getAbsolutePath)
        .ifPresent(viewModel.watermarkPath::set);
  }

  @FXML void onOutputPathClicked() {
    ChooserViewModel.directory("选择输出目录", WORK_DIR)
        .map(File::getAbsolutePath)
        .ifPresent(viewModel.outputPath::set);
  }

  @FXML void onExecutionClicked() {
    viewModel.execution();
  }
}
