package ispc.hermes.model.Converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class DoubleListConverter implements AttributeConverter<List<Double>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<Double> attribute) {
        return attribute != null ? attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(DELIMITER))
                : null;
    }

    @Override
    public List<Double> convertToEntityAttribute(String dbData) {
        return dbData != null ? Arrays.stream(dbData.split(DELIMITER))
                .map(Double::parseDouble)
                .collect(Collectors.toList())
                : null;
    }
}
