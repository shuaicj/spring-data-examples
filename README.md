# Spring Data Examples

Examples for learning Spring Data.

#### Modules
- **persist-jpa** - usage of `spring data JPA`
- **persist-db-init** - use sql scripts to init db
- **persist-jdbc-template**- usage of spring `JdbcTemplate`
- **persist-transaction** - usage of spring `@Transactional`
- **persist-cache** - usage of spring `@CacheConfig`, `@Cacheable`, `@CacheEvict`, `@CachePut`
- **persist-mongo** - usage of `spring data MongoDB`
- **persist-kv** - usage of `spring data keyvalue`
- **persist-redis** - usage of `spring data redis`
- **persist-hive** - apache hive jdbc usage

#### Supported Maven Profiles
- **default**
    - Run: `$ mvn clean package`
- **h2**
    - Run: `$ mvn clean package -P h2`
- **mysql**
    - Install MySQL as need: `$ brew install mysql`
    - Start MySQL: `$ mysql.server start`
    - Run: `$ mvn clean package -P mysql`
- **mongoembed**
    - Run: `$ mvn clean package -P mongoembed`
- **mongodb**
    - Install MongoDB as need: `$ brew install mongodb`
    - Start MongoDB: `$ mongod --config /usr/local/etc/mongod.conf`
    - Connect MongoDB: `$ mongo`
    - Create user in MongoDB Shell:
      ```
      use hellomongo;
      db.createUser({
          user: "hellomongou", 
          pwd: "hellomongop", 
          roles: [{role: "readWrite", db: "hellomongo"}]
      });
      ```
    - The config file is located at `/usr/local/etc/mongod.conf`. Append: `security.authorization: enabled`
    - Restart MongoDB.
    - Run: `$ mvn clean package -P mongodb`
    > If there is already a collecton named 'user', drop it by `db.user.drop()`; Or the unique index will not work.
- **redis**
    - Install Redis as need: `$ brew install redis`
    - Start Redis: `$ redis-server /usr/local/etc/redis.conf`
    - Run: `$ mvn clean package -P redis`
- **hive**
    - Run: `$ mvn clean package -P hive`
