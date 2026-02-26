# SJQL (Simple Java Query Library)

## Example
```java
public record Coffee(
        @NotNull String name,
        double price
) {
    public static final Table<Coffee> TABLE = new Table<>("coffee", Coffee.class);
    public static final Column<String> NAME = TABLE.stringColumn("name", 64).primaryKey();
    public static final Column<Double> PRICE = TABLE.doubleColumn("price").defaultValue(4.5);
}
```

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