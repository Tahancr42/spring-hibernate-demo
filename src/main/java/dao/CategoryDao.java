package dao;

import entities.Category;

public interface CategoryDao extends IDao<Category> {
    Category findByName(String name);
}
