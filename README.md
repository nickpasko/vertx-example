• utils/DataGenerator generates some data. In this case - current date and time with index number (index number will help
us to check if messages are missing or not)

• util/Runner runs a verticles

• main/Sender sends every second a message with generated data by DataGenerator and works during 60 seconds, then all
verticles will be undeployed

• main/ReceiverForConsole receives a messages and print them to the console

• main/ReceiverForDataBase receives a messages and put them to the table in database. Also create (or drop and create) a table
with preset configuration. In this case it uses MySQL driver.

![Sender result](https://github.com/marinesco/vertx-example/raw/master/images/S.jpg)

![ReceiverForConsole result](https://github.com/marinesco/vertx-example/raw/master/images/RC.jpg)

![ReceiverForDatabase result](https://github.com/marinesco/vertx-example/raw/master/images/RB.jpg)