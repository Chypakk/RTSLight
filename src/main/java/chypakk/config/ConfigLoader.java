package chypakk.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.InputStream;

public class ConfigLoader {
    private static final String CONFIG_FILE = "game_config.json";
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static GameConfig load() {
        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is == null) {
                throw new RuntimeException("Конфигурационный файл не найден: " + CONFIG_FILE);
            }

            return objectMapper.readValue(is, GameConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки конфигурации: " + e.getMessage(), e);
        }
    }

    /**
     * Для тестирования - загрузка конфигурации из строки
     */
    public static GameConfig loadFromString(String json) {
        try {
            return objectMapper.readValue(json, GameConfig.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка обработки JSON: " + e.getMessage(), e);
        }
    }
}
