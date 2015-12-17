# Restaurant Picker Service
A standalone sevice that provides REST API for Restaurant Voting application. See https://gist.github.com/juozapas/f20b55e4568d7f5c63b1 for the details

## Building & Running the service locally

### Prerequisites
  1. Interner connection
  2. Java 8

### Get soures
  To ckeckout the sources from GitHub run the command:

  git clone https://github.com/Hazehorizon/restaurantPicker.git

### Building java code & packaging
  To build the application, run

  gradlew build       (Windows)

    or
 
  ./gradlew build     (Linux)

### Start integration tests
  To start all the integration tests, run

  gradlew integrationTest      (Windows)

    or

  ./gradlew integrationTests   (Linux)

### Run the appilcation
  To build and run it from scrutch without integration tests starting, use command

  gradlew bootRun     (Windows)

    or

  ./gradlew bootRun   (Linux)

  If you have already built the sources with gradlew build command, just run

  java -jar build/libs/restaurantPicker-1.0.jar

  After startup the application listens to 8080 port for HTTP requests

## API Overview
  Initially database is filled with testing data. There is one 'SYSTEM' role user ("system"/"system"), one 'ADMIN' user ("admin"/"admin").
  And one testing restaurant with today's menu and '1' id.
  If you corrupt the database, e.g. remove all the users and can do nothing because of it just restart the application, all the predefined records will be restored during startup

### User management API
  This part is restricted for 'SYSTEM' role only. Use basic authentication to provide the credentials
  There is a predefined user ("system"/"system") for these operations

#### List all users 
  Example: curl -XGET -H "Content-type: application/json" -u "system:system" http://localhost:8080/api/v1/system/users

#### Read the user info by it id
  Url pattern: /api/v1/system/user/{login}
  Example: curl -XGET -H "Content-type: application/json" -u "system:system" http://localhost:8080/api/v1/system/user/admin

#### Create a new user
  Example: curl -XPOST -H "Content-type: application/json" -u "system:system" -d '{"login":"new", "passwd":"new","active":true, "roles":["ADMIN"]}' http://localhost:8080/api/v1/system/user

#### Update an existed user
  Url pattern: /api/v1/system/user/{login}
  Example: curl -XPUT -H "Content-type: application/json" -u "system:system" -d '{"login":"admin","passwd":"new","active":true, "roles":["SYSTEM","ADMIN"]}' http://localhost:8080/api/v1/system/user/admin

#### Delete user
  Url pattern: /api/v1/system/user/{login}
  Example: curl -XDELETE -H "Content-type: application/json" -u "system:system" http://localhost:8080/api/v1/system/user/admin

### Retaurant management API
  This part is available for 'ADMIN' role only. Use basic authentication to provide the credentials
  There is a predefined user ("admin"/"admin") for these operations
  There is a test restaurant with id='1' is also provided, it is active and has a today menu

#### List all restaurants
  Example: curl -XGET -H "Content-type: application/json" -u "admin:admin" http://localhost:8080/api/v1/settings/restaurants

#### Read the restaurant information
  Url pattern: /api/v1/settings/restaurant/{restaurantId}
  Example: curl -XGET -H "Content-type: application/json" -u "admin:admin" http://localhost:8080/api/v1/settings/restaurant/1

#### Create a new restaurant
  Example: curl -XPOST -H "Content-type: application/json" -u "admin:admin" -d '{"name":"TEST", "description":"DESC", "address": "Zoo", "phone":"2"}' http://localhost:8080/api/v1/settings/restaurant

#### Update an existed restaurant
  Url pattern: /api/v1/settings/restaurant/{restaurantId}
  Example: curl -XPUT -H "Content-type: application/json" -u "admin:admin" -d '{"name":"TEST", "description":"DESC", "address": "Park ave.", "phone":"767-67-65"}' http://localhost:8080/api/v1/settings/restaurant/1

#### Delete restaurant
  The restaurant record won't be removed from db competely after the operation, just deactivated. This is the way to have all the performed votes history.
  Only active restaurants can be voted
  Restaurant can be activated usin Restaurant management API by 'ADMIN' user
  Url pattern: /api/v1/settings/restaurant/{restaurantId}
  Example: curl -XDELETE -H "Content-type: application/json" -u "admin:admin" http://localhost:8080/api/v1/settings/restaurant/1

### Menu management API
  This part is available for 'ADMIN' role only. Use basic authentication to provide the credentials
  Initially there is one active restaurant id='1' with today's menu

#### Read a menu for the restaurant and date
  Url pattern: api/v1/settings/restaurant/{restaurantId}/menu/{date in pattern YYYYMMDD}
  Example: curl -XGET -H "Content-type: application/json" http://localhost:8080/api/v1/settings/restaurant/1/menu/20151217

#### Create a menu for the restaurant and date
  Example: curl -XPOST -H "Content-type: application/json" -d '{"name":"Vegitatian", "date":[2015,12,17], "items":[{"name":"Soup", "price": 2.99}, {"name":"Tea", "price": 9.99}]}' http://localhost:8080/api/v1/settings/restaurant/1/menu

#### Update the menu
  Url pattern: api/v1/settings/restaurant/{restaurantId}/menu/{date in pattern YYYYMMDD}
  Example: curl -XPUT -H "Content-type: application/json" -d '{"name":"Vegitatian", "date":[2015,12,17], "items":[{"name":"Soup", "price": 2.99}, {"name":"Tea", "price": 0.99}]}' http://localhost:8080/api/v1/settings/restaurant/1/menu

#### Delete menu
  Url pattern: api/v1/settings/restaurant/{restaurantId}/menu/{date in pattern YYYYMMDD}
  Example: curl -XDELETE -H "Content-type: application/json" http://localhost:8080/api/v1/settings/restaurant/1/menu/20151217

### Voting API
  This part isn't require any authentication and is available for anyone
  After startup the db contains one active restaurant with today's menu and id='1'. So, you are able test the API without any additional setups

#### Get restaurants with it today's menu for voting
  Only restaurants that can be voted are returned. If restaurant isn't active or doesn't have a menu for today it won't be returned
  Example: curl -XGET -H "Content-type: application/json" http://localhost:8080/api/v1/vote/restaurants

#### Vote for the restaurant
  If restaurant isn't active or doesn't have a menu for today it can be voted
  Url pattern: /api/v1/vote/restaurant/{restaurantId}
  Example: curl -XPOST -H "Content-type: application/json" http://localhost:8080/api/v1/vote/restaurant/1

## Technical overview
  1. The cornerstone for this app is SpringBoot and related Spring technoloies like spring-mvc, spring-security, spring-data, etc.
  2. hsqldb is used as a persistent storage. The db is created on startup if it wasn't created before in ./db/restaurantPickerDB folder. If something went wrong with the db you can just delete ./db folder and it will be restored after next startup
     If the db is existed all the predefined records will be checked and changed if it is nessesary during startup
  3. Building tool is gradle because it requires minimal prerequisites to start