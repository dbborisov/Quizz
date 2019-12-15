package quiz.demo.service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import quiz.demo.data.model.Quiz;
import quiz.demo.service.service.QuizService;

import java.util.concurrent.CompletableFuture;
@Component
public class AsyncRunner implements CommandLineRunner {
    private final QuizServiceImpl quizService;
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationRunner.class);
    public AsyncRunner(QuizServiceImpl quizService) {
        this.quizService = quizService;
    }

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            // Start the clock
            long start = System.currentTimeMillis();

            // Kick of multiple, asynchronous lookups
            CompletableFuture<Quiz> page1 = quizService.asyncAllQuiz();


            // Join all threads so that we can wait until all are done
//        CompletableFuture.allOf(page1, page2, page3).join();

            // Print results, including elapsed time
            LOG.info("Elapsed time: " + (System.currentTimeMillis() - start));
            LOG.info("--> " + page1.get());

        }
    }

}
