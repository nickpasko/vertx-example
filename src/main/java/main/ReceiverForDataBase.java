package main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import utils.Runner;

/*
 * by http://github.com/marinesco
 */

public class ReceiverForDataBase extends AbstractVerticle {
  private static final String ADDRESS = AddressList.address.name();

  // Database configuration

  private static final String HOST = "localhost";
  private static final int PORT = 3306;
  private static final String DATABASE = "mydbtest";
  private static final String TABLE = "vertx_db";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "admin";
  private static final int MAX_POOL_SIZE = 30;

  public static void main(String[] args) {
    Runner.runClusteredExample(ReceiverForDataBase.class);
  }

  @Override
  public void start() throws Exception {

    EventBus eb = vertx.eventBus();

    final SQLClient mySQLClient = MySQLClient.createShared(vertx, new JsonObject()
                    .put("host", HOST)
                    .put("port", PORT)
                    .put("username", USERNAME)
                    .put("password",PASSWORD)
                    .put("database", DATABASE)
                    .put("maxPoolSize", MAX_POOL_SIZE));

    // Set a connection

    mySQLClient.getConnection(conn -> {
        if (conn.failed()) {
          System.out.println("ERROR: Connection to Database failed. Cause: " + conn.cause().getMessage());
          return;
        }

        final SQLConnection connection = conn.result();

        // Drop & Create Table TABLE

        connection.execute(String.format("DROP TABLE %s", TABLE), r -> {
            if (!r.failed()) {
                System.out.format("INFO: TABLE %s was dropped\n", TABLE);
            }
            connection.execute(String.format("CREATE TABLE %s(id int auto_increment primary key, num int, data varchar(45))", TABLE), res -> {
                if (res.failed()) {
                    System.out.println(String.format("ERROR: Table %s wasn't created. Cause: ", TABLE) + res.cause().getMessage());
                    throw new RuntimeException(res.cause());
                }
                else {
                    System.out.format("INFO: Table %s was created\n", TABLE);
                }
                System.out.println("INFO: ReceiverForDataBase ready!");
            });
        });

        // Get a message and put it to the TABLE (Notice auto_commit is true by default)

        eb.consumer(ADDRESS, message -> {
            String[] data = message.body().toString().split("-");
            connection.execute(String.format("INSERT INTO %s (num, data) VALUES (%d, '%s')", TABLE, Integer.parseInt(data[0]), data[1]), res -> {
              if (res.failed()) {
                System.out.println("ERROR: Insert execution failed: " + res.cause().getMessage());
                throw new RuntimeException(res.cause());
              }
            });
        });

        // Close connection

        connection.close(res -> {
            if (res.failed()) {
              System.out.println("ERROR: Connection closing failed. Cause: " + res.cause().getMessage());
              throw new RuntimeException(res.cause());
            }
        });
    });
  }
}
