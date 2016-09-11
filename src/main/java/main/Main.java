package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args){
        //старт приложения. Здесь стартует embedded jetty server.
        //Spring подключает к Jetty Dispatcher Servlet, который обрабатывает HTTP- запросы пользователей
        SpringApplication.run(Main.class,args);
    }
}
