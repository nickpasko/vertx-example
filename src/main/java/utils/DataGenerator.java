package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataGenerator {
  private static int counter = 0;

  // Generate current date && time with counter as some unique random data

  public String getData() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss");
    return ++counter + "-" + dateFormat.format(new Date());
  }
}
