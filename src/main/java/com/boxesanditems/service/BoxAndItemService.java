package com.boxesanditems.service;

import com.boxesanditems.filters.ItemsSearchFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoxAndItemService implements BoxAndItemServiceInterface {
    private final static Integer BATCH_SIZE = 1000;
    private final BoxService boxService;
    private final ItemService itemService;

    @Override
    public List<Integer> getItemsByBoxAndColor(ItemsSearchFilter filter) {
        // Получение списка ID коробок, содержащий исходную и все вложенные в неё
        List<Integer> resultBoxIdList = new ArrayList<>();
        collectBoxes(filter.getBox(), resultBoxIdList);

        // Получение списка ID предметов заданного цвета, содержащиеся в полученном списке коробок
        return collectItems(filter.getColor(), resultBoxIdList);
    }

    private void collectBoxes(Integer rootBoxId, List<Integer> resultBoxList) {
        if (rootBoxId != null) {
            resultBoxList.add(rootBoxId);
            List<Integer> childBoxList = boxService.findContainedBoxIds(rootBoxId);
            if (!CollectionUtils.isEmpty(childBoxList)) {
                childBoxList.forEach(childBoxId -> collectBoxes(childBoxId, resultBoxList));
            }
        }
    }

    private List<Integer> collectItems(String color, List<Integer> resultBoxIdList) {
        // При большой вложенности коробок, список разбивается на части для отправки запроса в БД
        if (!CollectionUtils.isEmpty(resultBoxIdList)) {
            if (resultBoxIdList.size() < BATCH_SIZE) {
                return itemService.findContainedItemIdsByColor(color, resultBoxIdList);
            } else {
                List<Integer> res = new ArrayList<>();
                int i = 0;
                while (i < resultBoxIdList.size()) {
                    int curSize = BATCH_SIZE <= resultBoxIdList.size() - i
                            ? BATCH_SIZE
                            : resultBoxIdList.size() % BATCH_SIZE;
                    res.addAll(itemService.findContainedItemIdsByColor(color, resultBoxIdList.subList(i, i + curSize)));
                    i += curSize;
                }
                return res;
            }
        } else {
            return Collections.emptyList();
        }
    }
}
