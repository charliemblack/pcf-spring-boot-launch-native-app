/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
