# ResBill - Datacenter Resource Billing System
Copyright (c) 2015, CIT-VSB-TUO

ResBill is a Java-based web application designed for an accounting of resource (CPU, RAM, HDD, backup) consumption of the datacenter virtual infrastucture.

## Requirements
* Java 1.7 (http://www.oracle.com/technetwork/java/)
  * Java SDK - deployment only
  * JRE - necessary runtime platform
* Maven 3 (https://maven.apache.org/) - deployment only
* Tomcat 7 (http://tomcat.apache.org/)
* PostgreSQL 9.3 (http://www.postgresql.org/)
  * JDBC 4.1 Driver


## Installation

### Prepare environment

Install Java, Maven, Tomcat and PostgreSQL according to its respective documentation.

#### Prepare PostgreSQL database

According to PostgreSQL's documentation create ResBill application specific user account `$RESBILL_USER_NAME` and specific databse `$RESBILL_DB_NAME`.

Execute initialization script
```
psql -U $RESBILL_USER_NAME -d $RESBILL_DB_NAME -a -f $RESBILL_HOME/init.sql
```

#### Prepare Tomcat server

Download PostgreSQL JDBC 4 driver (https://jdbc.postgresql.org/) `postgresql-*.jdbc41.jar` and deploy it to `$TOMCAT_HOME/lib` directory. 

According to Tomcat's documentation create ResBill application specific (or update existing global) context file. Context files are located in `$TOMCAT_HOME/conf` directory.

Add new resource definition to context.

```xml
<!-- Data source for ResBill app -->
<Resource name="jdbc/ResBillDS"
  auth="Container"
  factory="org.apache.commons.dbcp.BasicDataSourceFactory"
  type="javax.sql.DataSource"
  driverClassName="org.postgresql.Driver"
  url="jdbc:postgresql://$POSTGRE_SQL_HOST/$RESBILL_DB_NAME"
  username="$RESBILL_USER_NAME"
  password="$RESBILL_USER_PASSWORD"/>
```

### Application customization

Edit ResBill configuration to fit your environment settings. All settings are located in `config.properties` file in `$RESBILL_HOME/src/main/resources/`.

### Application deployment

Create deployment package.
```
cd $RESBILL_HOME
maven install
```

Copy created WAR package to Tomcat's web application directory.
```
cp $RESBILL_HOME/target/resbill*.war $TOMCAT_HOME/webapps/
```

Restart Tomcat server.

## Maintanance

ResBill application logs its operation in the text file `resbill.log` located in `$TOMCAT_HOME/logs` directory.
