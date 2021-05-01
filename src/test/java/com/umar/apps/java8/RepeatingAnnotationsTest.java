package com.umar.apps.java8;

import com.umar.apps.java8.repeating.annotations.Manufacturer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepeatingAnnotationsTest {

    private static final Logger LOGGER = Logger.getLogger(RepeatingAnnotationsTest.class.getName());

    @Manufacturer("Mercedes Benz")
    @Manufacturer("BMW")
    @Manufacturer("Range Rover")
    public interface Car {

    }

    @Test
    void given_interface_when_getAnnotationType_Manufacturer_then_gets_all_Manufacturers() {
        Manufacturer [] manufacturers = Car.class.getAnnotationsByType(Manufacturer.class);
        assertEquals(3, manufacturers.length);
        Arrays.stream(manufacturers).forEach(m -> LOGGER.info(m.value()));
    }
}
