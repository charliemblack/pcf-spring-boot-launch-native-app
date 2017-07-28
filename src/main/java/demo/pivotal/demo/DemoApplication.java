package demo.pivotal.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootApplication
@RestController
public class DemoApplication {

    private ExecutorService executorService = Executors.newCachedThreadPool();


    @RequestMapping
    public String runEngine(String arg1, String arg2, int arg3) throws IOException, ExecutionException, InterruptedException {
        System.out.println("DemoApplication.runEngine");
        System.out.println("arg1 = [" + arg1 + "], arg2 = [" + arg2 + "], arg3 = [" + arg3 + "]");
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);
        processBuilder.command("bash", "/home/vcap/app/BOOT-INF/classes/scripts/something", arg1, arg2, Integer.toString(arg3));
        Process p = processBuilder.start();
        Future<String> input = executorService.submit(new StreamCapture(p.getInputStream()));

        return "std input = " + input.get();

    }
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
