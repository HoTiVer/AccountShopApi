package gleb.dresher.AccountShopApi.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Разрешаем CORS для всех путей
                .allowedOrigins("*") // Разрешаем запросы с фронтенда на порту 3000
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Разрешаем необходимые методы
                .allowedHeaders("*"); // Разрешаем все заголовки
    }
}
