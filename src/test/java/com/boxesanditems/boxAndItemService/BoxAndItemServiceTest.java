package com.boxesanditems.boxAndItemService;

import com.boxesanditems.filters.ItemsSearchFilter;
import com.boxesanditems.service.BoxAndItemService;
import com.boxesanditems.service.BoxService;
import com.boxesanditems.service.ItemService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BoxAndItemServiceTest {
    @Mock
    private BoxService boxService;
    @Mock
    private ItemService itemService;
    private ItemsSearchFilter filter = new ItemsSearchFilter();
    private BoxAndItemService boxAndItemService;

    @BeforeEach
    public void setUp() {
        boxAndItemService = new BoxAndItemService(boxService,itemService);
        when(boxService.findContainedBoxIds(any())).thenReturn(null);
        when(itemService.findContainedItemIdsByColor(anyString(), anyList())).thenReturn(Arrays.asList(3,5,10));
    }
    @Test
    public void collectItems() {
        Assertions.assertEquals(0, boxAndItemService.getItemsByBoxAndColor(filter).size(), "Should return empty array");
        filter.setBox(1);
        filter.setColor("green");
        Assertions.assertEquals(3, boxAndItemService.getItemsByBoxAndColor(filter).size(), "Should return array of size 3");
    }
}
