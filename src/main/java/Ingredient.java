import org.sql2o.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

public class Ingredient {
  private String name;
  private int recipe_id;
  private int id;

  public Ingredient(String name, int recipe_id) {
    this.name = name;
    this.recipe_id = recipe_id;
  }

  public String getIngredientName() {
    return name;
  }

  public int getIngredientRecipeId() {
    return recipe_id;
  }

  public int getIngredientId() {
    return id;
  }

  @Override
  public boolean equals(Object otherIngredient) {
      if (!(otherIngredient instanceof Ingredient)) {
        return false;
      } else {
        Ingredient newIngredient = (Ingredient) otherIngredient;
        return this.getIngredientName().equals(newIngredient.getIngredientName()) && this.getIngredientId() == newIngredient.getIngredientId();
      }
  }

  public static List<Ingredient> allIngredients() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM ingredients;";
      return con.createQuery(sql)
        .executeAndFetch(Ingredient.class);
    }
  }

  public void saveIngredient() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO ingredients (name, recipe_id) VALUES (:name, :recipe_id);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("recipe_id", recipe_id)
        .executeUpdate()
        .getKey();
    }
  }

  public static Ingredient findIngredient(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM ingredients WHERE id = :id;";
      Ingredient newIngredient  = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Ingredient.class);
    return newIngredient;
    }
  }

  public void updateIngredient(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE ingredients SET name = :name WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("name", name)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }

  public void deleteIngredient() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM ingredients WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }

}
