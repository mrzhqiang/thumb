package thumb.model;

import java.io.File;

/**
 * @author mrzhqiang
 */
public final class Thumb {
  private File[] pictureList;
  private File watermark;
  private File output;
  private Boolean prefixEnabled;
  private Boolean resolutionEnabled;
  private Integer resolutionX;
  private Integer resolutionY;

  public Boolean getPrefixEnabled() {
    return prefixEnabled;
  }

  public void setPrefixEnabled(Boolean prefixEnabled) {
    this.prefixEnabled = prefixEnabled;
  }

  public Boolean getResolutionEnabled() {
    return resolutionEnabled;
  }

  public void setResolutionEnabled(Boolean resolutionEnabled) {
    this.resolutionEnabled = resolutionEnabled;
  }

  public File[] getPictureList() {
    return pictureList;
  }

  public void setPictureList(File[] pictureList) {
    this.pictureList = pictureList;
  }

  public File getWatermark() {
    return watermark;
  }

  public void setWatermark(File watermark) {
    this.watermark = watermark;
  }

  public File getOutput() {
    return output;
  }

  public void setOutput(File output) {
    this.output = output;
  }

  public Integer getResolutionX() {
    return resolutionX;
  }

  public void setResolutionX(Integer resolutionX) {
    this.resolutionX = resolutionX;
  }

  public Integer getResolutionY() {
    return resolutionY;
  }

  public void setResolutionY(Integer resolutionY) {
    this.resolutionY = resolutionY;
  }
}
