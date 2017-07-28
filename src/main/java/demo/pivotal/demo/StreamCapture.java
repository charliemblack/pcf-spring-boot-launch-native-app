package demo.pivotal.demo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class StreamCapture implements Callable<String> {

    private InputStream stream;

    public StreamCapture(InputStream stream) {
        this.stream = stream;
    }

    @Override
    public String call() throws Exception {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(stream))) {
            return buffer.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
