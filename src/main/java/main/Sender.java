package main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import utils.DataGenerator;
import utils.Runner;

/*
 * by http://github.com/marinesco
 */

public class Sender extends AbstractVerticle {
  private static final String ADDRESS = AddressList.address.name();

  public static void main(String[] args) {
    Runner.runClusteredExample(Sender.class);
  }

  @Override
  public void start() throws Exception {

    EventBus eb = vertx.eventBus();
    DataGenerator dg = new DataGenerator();

    System.out.println("INFO: Sender ready!");

    // Send a new data every second. This schedule will be canceled after 60 seconds

    vertx.setTimer(60000, c -> {
      vertx.deploymentIDs().forEach(vertx::undeploy);
    });

    vertx.setPeriodic(1000, m -> {
      eb.publish(ADDRESS, dg.getData());
    });
  }
}
