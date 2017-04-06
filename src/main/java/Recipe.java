import org.sql2o.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

public class Recipe {
  private String name;
  private int rating;
  private int id;
  private Timestamp date_created;

  public Recipe(String name, int rating) {
    this.name = name;
    this.rating = rating;
  }

  public String getRecipeName() {
    return name;
  }

  public int getRecipeRating() {
    return rating;
  }

  public int getRecipeId() {
    return id;
  }

  public Timestamp getDateCreated() {
    return date_created;
  }

  @Override
  public boolean equals(Object otherRecipe) {
      if (!(otherRecipe instanceof Recipe)) {
        return false;
      } else {
        Recipe newRecipe = (Recipe) otherRecipe;
        return this.getRecipeName().equals(newRecipe.getRecipeName()) && this.getRecipeId() == newRecipe.getRecipeId();
      }
  }

  public static List<Recipe> allRecipes() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM recipes;";
      return con.createQuery(sql)
        .executeAndFetch(Recipe.class);
    }
  }

  public void saveRecipe() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO recipes (name, rating, date_created) VALUES (:name, :rating, now());";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("rating", this.rating)
        .executeUpdate()
        .getKey();
    }
  }

  public static Recipe findRecipe(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM recipes WHERE id = :id;";
      Recipe newRecipe  = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Recipe.class);
    return newRecipe;
    }
  }

  public void updateRecipe(String name, int rating) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE recipes SET name = :name, rating = :rating WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("name", name)
      .addParameter("rating", rating)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }

  public void deleteRecipe() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM recipes WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("id", this.id)
      .executeUpdate();
      String joinDeleteQuery = "DELETE FROM tags WHERE recipe_id = :recipeId;";
      con.createQuery(joinDeleteQuery)
      .addParameter("recipeId", this.getRecipeId())
      .executeUpdate();
    }
  }

  public List<Instruction> getInstructions() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM instructions WHERE recipe_id = :id;";
      return con.createQuery(sql)
      .addParameter("id", this.id)
      .executeAndFetch(Instruction.class);
    }
  }

  public List<Ingredient> getIngredients() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM ingredients WHERE recipe_id = :id;";
      return con.createQuery(sql)
      .addParameter("id", this.id)
      .executeAndFetch(Ingredient.class);
    }
  }

  public List<Category> getCategories() {
    try(Connection con = DB.sql2o.open()) {
      String joinQuery = "SELECT category_id FROM tags WHERE recipe_id = :recipe_id;";
      List<Integer> categoryIds = con.createQuery(joinQuery)
      .addParameter("recipe_id", this.getRecipeId())
      .executeAndFetch(Integer.class);

      List<Category> categories = new ArrayList<Category>();

      for (Integer categoryId : categoryIds) {
        String categoryQuery = "SELECT * FROM categories WHERE id = :categoryId;";
        Category category = con.createQuery(categoryQuery)
        .addParameter("categoryId", categoryId)
        .executeAndFetchFirst(Category.class);
      categories.add(category);
      }
      return categories;
    }
  }

  public void removeCategory(Category category) {
    try(Connection con = DB.sql2o.open()) {
      String joinRemovalQuery = "DELETE FROM tags WHERE category_id = :categoryId AND recipe_id = :recipeId;";
      con.createQuery(joinRemovalQuery)
      .addParameter("categoryId", category.getCategoryId())
      .addParameter("recipeId", this.getRecipeId())
      .executeUpdate();
    }
  }

}
