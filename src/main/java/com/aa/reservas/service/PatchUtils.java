package com.aa.reservas.service;

import com.aa.reservas.exception.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

public class PatchUtils {
    private PatchUtils() {}

    public static <T> void applyPatch(T target, Map<String, Object> fields, ObjectMapper objectMapper) {
        fields.forEach((key, value) -> {
            if ("id".equalsIgnoreCase(key)) {
                throw new BadRequestException("No se puede modificar el id con PATCH");
            }
            Field field = ReflectionUtils.findField(target.getClass(), key);
            if (field == null) {
                throw new BadRequestException("El atributo '" + key + "' no existe en " + target.getClass().getSimpleName());
            }
            field.setAccessible(true);
            Object convertedValue = objectMapper.convertValue(value, field.getType());
            ReflectionUtils.setField(field, target, convertedValue);
        });
    }
}
