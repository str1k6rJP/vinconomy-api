#!/usr/bin/env bash

JAVA_HOME='/path/to/your/JDK'
VINCONOMY_DB_HOST='localhost'
VINCONOMY_DB_NAME='name'
VINCONOMY_DB_PASS='password'
VINCONOMY_DB_USR='user'
VINCONOMY_DB_PORT=3307

./mvnw clean package -P docker
