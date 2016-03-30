package com.anashidgames.gerdoo.pages.home;

import com.anashidgames.gerdoo.core.service.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psycho on 3/30/16.
 */
public class CategoryWrapper extends Category{

    public static List<CategoryWrapper> convert(List<Category> categories){
        List<CategoryWrapper> result = new ArrayList<>();
        for (Category category : categories){
            result.add(new CategoryWrapper(category));
        }

        return result;
    }

    private boolean expanded = false;

    public CategoryWrapper(Category category) {
        super(category.getTitle(), category.getIconUrl(), category.getDataUrl(), category.hasSubCategory());
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void toggle() {
        expanded = !expanded;
    }
}
