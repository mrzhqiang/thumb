package thumb.interval;

/**
 * @author mrzhqiang
 */
public enum SystemInfo {
  ;

  public static String homeDir() {
    return System.getProperty("user.dir");
  }
}
