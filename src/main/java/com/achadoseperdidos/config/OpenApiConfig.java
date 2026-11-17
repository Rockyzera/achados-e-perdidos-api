package com.achadoseperdidos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI achadosEPerdidosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Achados e Perdidos")
                        .version("1.0.0")
                        .description("API REST para registrar, consultar e acompanhar itens perdidos ou encontrados. "
                                + "Os cadastros exigem uma categoria e um local existentes."));
    }
}
