package com.boxesanditems.utils;

import com.boxesanditems.service.BoxService;
import com.boxesanditems.service.ItemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;

import static org.mockito.ArgumentMatchers.any;

public class DbUtilsTest {
    private final static String PATH = "classpath:input.xml";
    private AutoCloseable closeable;
    @Mock
    private DataSource dataSource;
    @Mock
    private BoxService boxService;
    @Mock
    private ItemService itemService;
    private DbUtils dbUtils;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        dbUtils = new DbUtils(dataSource, boxService, itemService);
        Mockito.doNothing().when(boxService).saveAll(any());
        Mockito.doNothing().when(itemService).saveAll(any());
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    public void shouldParsePathAndSaveEntities() {
        dbUtils.parsePathAndSaveEntities(PATH);
        Mockito.verify(boxService).saveAll(any());
        Mockito.verify(itemService).saveAll(any());
    }
}
