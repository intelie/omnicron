# Omnicron
Simple, fast, zero-dependency Cron expression evaluator for Java 8.

Features:
* Check if a date matches against a Cron expression;
* Compute next and previous execution dates of some Cron expression.


## Usage

*Soon: Maven dependency declaration.*

```java
Cron cron = new Cron("*/5 * * * *");
ZonedDateTime date = ZonedDateTime.now();

System.out.println("Next execution: " + cron.next(date));
System.out.println("Prev execution: " + cron.prev(date));
```
