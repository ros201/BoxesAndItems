package com.boxesanditems.service;

import com.boxesanditems.filters.ItemsSearchFilter;

import java.util.List;

public interface BoxAndItemServiceInterface {
    List<Integer> getItemsByBoxAndColor(ItemsSearchFilter filter);
}
