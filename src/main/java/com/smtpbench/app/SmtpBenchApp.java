package com.smtpbench.app;

import jakarta.mail.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SmtpBenchApp {
    public static void main(String[] args) {
        try {
            System.out.println("Benchmark iniziato...");
            ConfigLoader config = new ConfigLoader();

            SmtpMailSender mailSender = new SmtpMailSender(config);

            int count = config.getInt("smtp.benchmark.count", 10);
            int messageSize = config.getInt("smtp.message.size", 1024);
            boolean reuseConnection = Boolean.parseBoolean(config.get("smtp.reuse.connection"));

            long[] metrics = runBenchmark(mailSender, count, messageSize, reuseConnection);

            printMetrics(metrics, count);
            saveMetricsToFile(metrics, count, config, "benchmark_results.txt");
            System.out.println(
                    count + " mail inviate con successo. Risultati completi salvati su benchmark_results.txt.");

        } catch (Exception e) {
            System.err.println("Errore: " + e.getMessage());
        }
    }

    /**
     * Stampa le metriche del benchmark
     */
    private static void printMetrics(long[] metrics, int count) {
        double avg = (double) metrics[2] / count;
        System.out.println("========================");
        System.out.println("Tempo minimo: " + metrics[0] + " ms");
        System.out.println("Tempo massimo: " + metrics[1] + " ms");
        System.out.println("Tempo medio: " + avg + " ms");
    }

    /**
     * Esegue il benchmark di invio email
     * 
     * @param mailSender      istanza di SmtpMailSender
     * @param count           numero di email da inviare
     * @param messageSize     dimensione del messaggio in byte
     * @param reuseConnection se true, riutilizza la connessione SMTP
     * @return array contenente min, max e sum dei tempi di invio
     */
    private static long[] runBenchmark(SmtpMailSender mailSender, int count, int messageSize, boolean reuseConnection)
            throws Exception {
        long min = Long.MAX_VALUE, max = Long.MIN_VALUE, sum = 0;

        if (reuseConnection) {
            Session session = mailSender.createSession();
            Transport transport = session.getTransport("smtp");
            transport.connect();

            for (int i = 0; i < count; i++) {
                String subject = "Benchmark Test " + (i + 1);
                String body = generateBody(messageSize);
                long time = mailSender.sendMailOnTransport(session, transport, subject, body);
                min = Math.min(min, time);
                max = Math.max(max, time);
                sum += time;
            }

            transport.close();
        } else {
            for (int i = 0; i < count; i++) {
                String subject = "Benchmark Test " + (i + 1);
                String body = generateBody(messageSize);
                long time = mailSender.sendMailAndMeasure(subject, body);
                min = Math.min(min, time);
                max = Math.max(max, time);
                sum += time;
            }
        }
        return new long[] { min, max, sum };
    }

    /**
     * Genera un corpo email di dimensione specificata
     */
    static String generateBody(int size) {
        if (size <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append('A');
        }
        return sb.toString();
    }

    /**
     * Salva le metriche del benchmark su file
     */
    private static void saveMetricsToFile(long[] metrics, int count, ConfigLoader config, String filename) {
        double avg = (double) metrics[2] / count;
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write("==== Benchmark " + timestamp + " ====\n");
            writer.write("SMTP Host: " + config.get("smtp.host") + "\n");
            writer.write("SMTP Port: " + config.get("smtp.port") + "\n");
            writer.write("SMTP Username: " + config.get("smtp.username") + "\n");
            writer.write("Number of Emails: " + count + "\n");
            writer.write("Message Size: " + config.getInt("smtp.message.size", 1024) + " bytes\n");
            writer.write("Reuse Connection: " + config.get("smtp.reuse.connection") + "\n");
            writer.write("\n"); // Riga vuota tra config e risultati
            writer.write("Tempo minimo: " + metrics[0] + " ms\n");
            writer.write("Tempo massimo: " + metrics[1] + " ms\n");
            writer.write("Tempo medio: " + avg + " ms\n");
            writer.write("======================================\n\n");
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio dei risultati: " + e.getMessage());
        }
    }
}