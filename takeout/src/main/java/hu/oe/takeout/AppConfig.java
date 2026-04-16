package hu.oe.takeout;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<UUID, String> uuidToString = new Converter<>() {
            public String convert(MappingContext<UUID, String> context) {
                return context.getSource() == null ? null : context.getSource().toString();
            }
        };

        // String -> UUID konverter
        Converter<String, UUID> stringToUUID = new Converter<>() {
            public UUID convert(MappingContext<String, UUID> context) {
                return context.getSource() == null ? null : UUID.fromString(context.getSource());
            }
        };

        // Globálisan regisztráljuk a konvertereket
        modelMapper.addConverter(uuidToString);
        modelMapper.addConverter(stringToUUID);

        return modelMapper;
    }
}
