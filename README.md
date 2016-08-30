# Omnicron
Simple, fast, zero-dependency Cron expression evaluator for Java 8.

**Features:**
* Check if a date matches against a Cron expression;
* Compute next and previous execution dates of some Cron expression.

**Design choices:**
* `0 0 * * *` won't match DST gap
* `0 23 * * *` will match DST overlap twice

In other words, a cron expression will only match exactly what its
values explicitly say.

## Usage

Omnicron is available through Maven Central repository, just add the following
dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>net.intelie.omnicron</groupId>
    <artifactId>omnicron</artifactId>
    <version>0.1</version>
</dependency>
```

Then, you can use it like that:

```java
Cron cron = new Cron("*/5 * * * *");
ZonedDateTime date = ZonedDateTime.now();

System.out.println("Next execution: " + cron.next(date));
System.out.println("Prev execution: " + cron.prev(date));
```
