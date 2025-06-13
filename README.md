# SMTP Benchmark Tool

Questa applicazione Java esegue un benchmark su un servizio SMTP, misurando i tempi di invio email in diverse condizioni.

## Requisiti

- Java 17 o superiore
- Maven 3.x
- Un server SMTP con autenticazione username/password

## Configurazione

Tutte le configurazioni si trovano nel file:

```
src/main/resources/configuration.properties
```

### Esempio di configurazione

```properties
smtp.host=smtp.ethereal.email
smtp.port=587
smtp.username=tuo_username
smtp.password=la_tua_password
smtp.reuse.connection=true
smtp.benchmark.count=10
smtp.message.size=1024
```



**Parametri principali:**

- `smtp.host`: indirizzo del server SMTP
- `smtp.port`: porta SMTP

- `smtp.username`: username per autenticazione
- `smtp.password`: password per autenticazione

- `smtp.from`: email del mittente
- `smtp.to`: email del destinatario

- `smtp.benchmark.count`: numero di email da inviare per il benchmark
- `smtp.message.size`: dimensione (in byte) del corpo del messaggio
- `smtp.reuse.connection`: `true` per riutilizzare la connessione SMTP, `false` per aprirne una nuova ad ogni invio

> **Nota:** Per i test è stato utilizzato [Ethereal Email](https://ethereal.email/), un servizio SMTP gratuito pensato per test e sviluppo.

## Come eseguire il benchmark

1. Configura il file `configuration.properties` con i tuoi parametri SMTP.
2. Da terminale, posizionati nella cartella del progetto.
3. Esegui i seguenti comandi:

   ```sh
   mvn clean package
   java -cp target/smtp-bench-1.0-SNAPSHOT.jar com.smtpbench.app.SmtpBenchApp
   ```

   *(I nomi di versione e jar possono variare in base al tuo `pom.xml`)*

## Risultati

- Le metriche principali (tempo minimo, massimo, medio) vengono stampate a terminale.
- Tutti i dettagli del benchmark vengono salvati nel file `benchmark_results.txt` nella cartella principale del progetto.

## Test automatici

Per eseguire i test automatici:

```sh
mvn test
```

## Struttura del progetto

```
src/main/java/com/smtpbench/app/          --> Codice sorgente principale
src/main/resources/configuration.properties --> Configurazione
src/test/java/com/smtpbench/app/          --> Test automatici
```

## Autore

Luca Vignoli