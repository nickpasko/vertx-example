package main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import utils.Runner;

/*
 * by http://github.com/marinesco
 */

public class ReceiverForConsole extends AbstractVerticle {
  private static final String ADDRESS = AddressList.address.name();

  public static void main(String[] args) {
    Runner.runClusteredExample(ReceiverForConsole.class);
  }

  @Override
  public void start() throws Exception {

    EventBus eb = vertx.eventBus();
    System.out.println("INFO: ReceiverForConsole ready!");

    // Get message and print it to the console

    eb.consumer(ADDRESS, message -> {
      String[] data = message.body().toString().split("-");
      System.out.format("Received message (%s): %s \n", data[0], data[1]);
    });
  }
}
