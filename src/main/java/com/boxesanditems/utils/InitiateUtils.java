package com.boxesanditems.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class InitiateUtils implements CommandLineRunner {
    private final DbUtils dbUtils;

    @Override
    public void run(String... args) throws Exception {
        String fileName = Arrays.stream(args).findAny().isPresent()
                ? args[0]
                : "";
        if (!fileName.isEmpty()) {
            dbUtils.parsePathAndSaveEntities(fileName);
            dbUtils.saveTablesToCSVFile();
        }
    }
}
