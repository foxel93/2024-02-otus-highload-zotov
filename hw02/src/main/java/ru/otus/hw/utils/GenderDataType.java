package ru.otus.hw.utils;

import com.vaadin.exampledata.DataType;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import ru.otus.hw.models.Gender;

public class GenderDataType extends DataType<Gender> {
    private static final List<Gender> GENDERS = Arrays.stream(Gender.values()).toList();

    public static final GenderDataType GENDER_DATA_TYPE = new GenderDataType();

    @Override
    public Gender getValue(Random random, int i, LocalDateTime localDateTime) {
        var pos = random.nextInt(GENDERS.size());
        return GENDERS.get(pos);
    }
}
