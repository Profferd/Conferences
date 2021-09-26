package com.hrushko.util.page;

import java.util.List;

public interface Pagination<T> {
    List<T> getPage(List<T> item, int page);

    int getCountOfPages();

    int getPage();
}
