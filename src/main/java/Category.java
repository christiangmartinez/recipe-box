import org.sql2o.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

public class Category {
  private String name;
  private int id;

  public Category(String name) {
    this.name = name;
  }

  public String getCategoryName() {
    return name;
  }

  public int getCategoryId() {
    return id;
  }

  @Override
  public boolean equals(Object otherCategory) {
      if (!(otherCategory instanceof Category)) {
        return false;
      } else {
        Category newCategory = (Category) otherCategory;
        return this.getCategoryName().equals(newCategory.getCategoryName()) && this.getCategoryId() == newCategory.getCategoryId();
      }
  }

  public static List<Category> allCategories() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM categories;";
      return con.createQuery(sql)
        .executeAndFetch(Category.class);
    }
  }

  public void saveCategory() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories (name) VALUES (:name);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Category findCategory(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM categories WHERE id = :id;";
      Category newCategory  = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Category.class);
    return newCategory;
    }
  }

  public void updateCategory(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE categories SET name = :name WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("name", name)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }

  public void deleteCategory() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM categories WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("id", this.id)
      .executeUpdate();
      String joinDeleteQuery = "DELETE FROM tags WHERE category_id = :categoryId;";
      con.createQuery(joinDeleteQuery)
      .addParameter("categoryId", this.getCategoryId())
      .executeUpdate();
    }
  }

  public void addRecipe(Recipe recipe) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO tags (recipe_id, category_id) VALUES (:recipe_id, :category_id);";
      con.createQuery(sql)
        .addParameter("recipe_id", recipe.getRecipeId())
        .addParameter("category_id", this.getCategoryId())
        .executeUpdate();
    }
  }

  public List<Recipe> getRecipes() {
    try(Connection con = DB.sql2o.open()) {
      String joinQuery = "SELECT recipe_id FROM tags WHERE category_id = :category_id;";
      List<Integer> recipeIds = con.createQuery(joinQuery)
        .addParameter("category_id", this.getCategoryId())
        .executeAndFetch(Integer.class);

      List<Recipe> recipes = new ArrayList<Recipe>();

      for (Integer recipeId : recipeIds) {
        String recipeQuery = "SELECT * FROM recipes WHERE id = :recipeId";
        Recipe recipe = con.createQuery(recipeQuery)
          .addParameter("recipeId", recipeId)
          .executeAndFetchFirst(Recipe.class);
        recipes.add(recipe);
      }
      return recipes;
    }
  }

  public void removeRecipe(Recipe recipe){
    try(Connection con = DB.sql2o.open()){
      String joinRemovalQuery = "DELETE FROM tags WHERE category_id = :categoryId AND recipe_id = :recipeId;";
      con.createQuery(joinRemovalQuery)
        .addParameter("categoryId", this.getCategoryId())
        .addParameter("recipeId", recipe.getRecipeId())
        .executeUpdate();
    }
  }

}
