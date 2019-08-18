package thumb.interval;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常模型。
 *
 * @author qiang.zhang
 */
public final class Throwables {
  public static String print(Throwable e) {
    // --- expandable content
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    return sw.toString();
  }

  public static String printStackTrace() {
    StringBuilder builder = new StringBuilder();
    for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
      if (builder.length() > 0) {
        builder.append("\r\n");
      }
      builder.append(element.toString());
    }
    return builder.toString();
  }

  public static String parse(Throwable e) {
    if (e instanceof NullPointerException) {
      return "空指针：" + e.getMessage();
    }
    if (e instanceof UnsupportedOperationException) {
      return "不支持的请求：" + e.getMessage();
    }
    return "意料之外的错误";
  }
}
