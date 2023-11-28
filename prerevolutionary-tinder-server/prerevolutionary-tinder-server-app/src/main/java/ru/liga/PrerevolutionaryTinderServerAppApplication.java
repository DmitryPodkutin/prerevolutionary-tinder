package ru.liga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import ru.liga.dto.converter.FavouriteEntityToDtoConverter;
import ru.liga.dto.converter.ProfileEntityToProfileDtoConverter;
import ru.liga.dto.converter.ProfileToDtoWithImageConverter;

import java.util.HashSet;

@SpringBootApplication
public class PrerevolutionaryTinderServerAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrerevolutionaryTinderServerAppApplication.class, args);
    }

    @Bean(name = "customConversionService")
    public ConversionService getConversionService(ApplicationContext context) {

        final ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        final HashSet<Converter> converters = new HashSet<>();
        converters.add(context.getBean(ProfileEntityToProfileDtoConverter.class));
        converters.add(context.getBean(FavouriteEntityToDtoConverter.class));
        converters.add(context.getBean(ProfileToDtoWithImageConverter.class));
        bean.setConverters(converters);
        bean.afterPropertiesSet();
        return bean.getObject();
    }
}
