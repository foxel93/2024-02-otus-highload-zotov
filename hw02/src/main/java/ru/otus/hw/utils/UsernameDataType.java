package ru.otus.hw.utils;

import com.vaadin.exampledata.DataType;
import java.time.LocalDateTime;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

public class UsernameDataType extends DataType<String> {
    public static final UsernameDataType USERNAME_DATA_TYPE = new UsernameDataType();

    @Override
    public String getValue(Random random, int i, LocalDateTime localDateTime) {
        return RandomStringUtils.random(20, true, true);
    }
}
