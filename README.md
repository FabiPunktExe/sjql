# SJQL (Simple Java Query Library)

## How to use

### Gradle
```kotlin
repositories {
    maven("https://repo.diruptio.de/repository/maven-public")
}
```

```kotlin
dependencies {
    implementation("de.fabiexe:sjql:0.4.0")
}
```

### Maven
```xml
<repositories>
    <repository>
        <id>diruptio-maven-public</id>
        <url>https://repo.diruptio.de/repository/maven-public</url>
    </repository>
</repositories>
```

```xml
<dependencies>
    <dependency>
        <groupId>de.fabiexe</groupId>
        <artifactId>sjql</artifactId>
        <version>0.4.0</version>
    </dependency>
</dependencies>
```

## Example

### Example table:
```java
public record Coffee(String name, double price, String description) {
    public static final Table<Coffee> TABLE = new Table<>(Coffee.class, "coffee");
    public static final Column<String> NAME = TABLE.stringColumn("name", 64).primaryKey();
    public static final Column<Double> PRICE = TABLE.doubleColumn("price").defaultValue(4.5).notNull();
    public static final Column<String> DESCRIPTION = TABLE.stringColumn("description", 256);
}
```

### Example table (multiple primary keys):
```java
public record UserRole(String username, String role) {
    public static final Table<UserRole> TABLE = new Table<>(UserRole.class, "user_roles");
    public static final Column<String> USERNAME = TABLE.stringColumn("username", 64).primaryKey();
    public static final Column<String> ROLE = TABLE.stringColumn("role", 64).primaryKey();
}
```

### Let's use our tables
```java
public void example() throws SQLException {
    Database database = Database.create(dataSource);
    database.createTable(Coffee.TABLE);

    database.throwingTransaction(() -> {
        long count = Coffee.TABLE.count(); // 0

        Coffee.TABLE.insert(row -> {
            row.set(Coffee.NAME, "Espresso");
            row.set(Coffee.PRICE, 2.5);
        });

        List<Coffee> coffees = Coffee.TABLE.select()
                .where(Coffee.NAME.eq("Espresso"))
                .execute(); // [Coffee[name=Espresso, price=2.5]]

        Coffee.TABLE.update(Coffee.PRICE, 2.8)
                .where(Coffee.NAME.eq("Espresso"))
                .execute();

        Coffee.TABLE.delete()
                .where(Coffee.NAME.eq("Espresso"))
                .execute();
    });
}
```