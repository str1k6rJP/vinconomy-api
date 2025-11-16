$env:JAVA_HOME = "DRIVE:\path\to\your\JDK";
# the rest of the values are not required if you specified the valid values in the application.properties
# localhost if running locally or IP of the server with MySQL DB
$env:VINCONOMY_DB_HOST="localhost";
$env:VINCONOMY_DB_NAME="name";
$env:VINCONOMY_DB_PASS="password";
$env:VINCONOMY_DB_USR="user";
$env:VINCONOMY_DB_PORT="3307";

try {
    .\mvnw clean package -P docker;
}
finally {
    Remove-Item Env:JAVA_HOME, Env:SPRING_SERVER_PORT, Env:VINCONOMY_DB_HOST, Env:VINCONOMY_DB_NAME, Env:VINCONOMY_DB_PASS, Env:VINCONOMY_DB_PORT, Env:VINCONOMY_DB_USR -ErrorAction SilentlyContinue
}