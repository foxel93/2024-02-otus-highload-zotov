package ru.otus.hw;

import static ru.otus.hw.utils.GenderDataType.GENDER_DATA_TYPE;
import static ru.otus.hw.utils.UsernameDataType.USERNAME_DATA_TYPE;

import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.UserRepository;

@Log4j2
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner createDemoDataIfNeeded(UserRepository repository) {
        return args -> {
            log.info("Generating demo data...");

            var generator = new ExampleDataGenerator<>(User.class, LocalDateTime.now());
            generator.setData(User::setBirthDate, DataType.DATE_OF_BIRTH);
            generator.setData(User::setUsername, USERNAME_DATA_TYPE);
            generator.setData(User::setFirstName, DataType.FIRST_NAME);
            generator.setData(User::setSecondName, DataType.LAST_NAME);
            generator.setData(User::setGender, GENDER_DATA_TYPE);
            generator.setData(User::setCity, DataType.CITY);
            generator.setData(User::setInterests, DataType.SENTENCE);

            var stopWatch = new StopWatch();
            stopWatch.start();
            List<User> users = generator.create(100000, ThreadLocalRandom.current().nextInt());
            repository.saveAll(users);
            stopWatch.stop();
            log.info("Demo data generated in " + stopWatch.getTime() + "ms.");
        };
    }
}