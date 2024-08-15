package com.boxesanditems.controllers;

import com.boxesanditems.exception.MandatoryInputParamMissingException;
import com.boxesanditems.filters.ItemsSearchFilter;
import com.boxesanditems.service.BoxAndItemServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BoxAndItemController {
    private final static String BOX_ARGUMENT = "box";
    private final static String COLOR_ARGUMENT = "color";

    private final BoxAndItemServiceInterface boxAndItemServiceInterface;

    @PostMapping(value = "/items")
    public ResponseEntity<List<Integer>> getItems(@RequestBody ItemsSearchFilter filter) {
        checkMandatoryParams(filter);
        List<Integer> itemIds = boxAndItemServiceInterface.getItemsByBoxAndColor(filter);
        return CollectionUtils.isEmpty(itemIds)
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(itemIds, HttpStatus.OK);
    }

    private void checkMandatoryParams(ItemsSearchFilter filter) {
        Map<String, Object> map = new HashMap<>();
        map.put(BOX_ARGUMENT, filter.getBox());
        map.put(COLOR_ARGUMENT, filter.getColor());
        map.entrySet().stream().filter(entry -> entry.getValue() == null).forEach(entry -> {
            throw new MandatoryInputParamMissingException(entry.getKey());
        });
    }
}
