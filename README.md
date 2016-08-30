# Omnicron
Simple, fast, zero-dependency Cron expression evaluator for Java 8.

**Features:**
* Check if a date matches against a Cron expression;
* Compute next and previous execution dates of some Cron expression.

**Design choices:**
* `0 0 * * *` won't match DST gap
* `0 23 * * *` will match DST overlap twice

This means, actually that a cron expression will only match exactly what its
values explicitly say.

## Usage

*Soon: Maven dependency declaration.*

```java
Cron cron = new Cron("*/5 * * * *");
ZonedDateTime date = ZonedDateTime.now();

System.out.println("Next execution: " + cron.next(date));
System.out.println("Prev execution: " + cron.prev(date));
```
