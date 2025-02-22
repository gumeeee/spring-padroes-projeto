package com.guilherme.api_padroes_projeto_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Projeto Spring Boot gerado via Spring Initializr.
 * Os seguintes módulos foram selecionados:
 * - Spring Data JPA
 * - Spring Web
 * - H2 Database
 * - OpenFeign
 *
 * @author gumeeee
 */
@EnableFeignClients
@SpringBootApplication
public class ApiPadroesProjetoSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiPadroesProjetoSpringApplication.class, args);
    }

}
