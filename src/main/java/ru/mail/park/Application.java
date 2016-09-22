package ru.mail.park;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args){
        //старт приложения. Здесь стартует embedded jetty server.
        //Spring подключает к Jetty Dispatcher Servlet, который обрабатывает HTTP- запросы пользователей
        SpringApplication.run(Application.class,args);
    }
}
