# SMTP Benchmark Tool

This Java application benchmarks an SMTP service, measuring email sending times under different conditions.

## Requirements

- Java 17 or higher
- Maven 3.x
- An SMTP server with username/password authentication

## Configuration

All configurations are in the file:

```
src/main/resources/configuration.properties
```

### Example configuration

```properties
smtp.host = smtp.ethereal.email
smtp.port = 587

smtp.username = your_username
smtp.password = your_password

smtp.from = example@mail.com
smtp.to = example@mail.com

smtp.reuse.connection = true
smtp.benchmark.count = 10
smtp.message.size = 1024
```

**Main parameters:**

- `smtp.host`: SMTP server address
- `smtp.port`: SMTP port
- `smtp.username`: authentication username
- `smtp.password`: authentication password
- `smtp.from`: sender email address
- `smtp.to`: recipient email address
- `smtp.benchmark.count`: number of emails to send for the benchmark
- `smtp.message.size`: size (in bytes) of the message body
- `smtp.reuse.connection`: `true` to reuse the SMTP connection, `false` to open a new one for each send

> **Note:** [Ethereal Email](https://ethereal.email/), a free SMTP service for testing and development, was used for testing.

## How to run the benchmark

1. Configure the `configuration.properties` file with your SMTP parameters.
2. In the terminal, navigate to the project folder.
3. Run the following commands:

   ```sh
   mvn clean package
   mvn exec:java "-Dexec.mainClass=com.smtpbench.app.SmtpBenchApp"
   ```

   *(Version and jar names may vary depending on your `pom.xml`)*

## Results

- Main metrics (min, max, average time) are printed to the terminal.
- All benchmark details are saved in the `benchmark_results.txt` file in the project root.

## Automated tests

To run automated tests:

```sh
mvn test
```

## Project structure

```
src/main/java/com/smtpbench/app/          --> Main source code
src/main/resources/configuration.properties --> Configuration
src/test/java/com/smtpbench/app/          --> Automated tests
```

## Author

Luca Vignoli
