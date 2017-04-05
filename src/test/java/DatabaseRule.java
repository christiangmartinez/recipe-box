import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  @Override
  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/recipe_box_test", null, null);
  }

  @Override
  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deleteCategoryQuery = "DELETE FROM categories *;";
      con.createQuery(deleteCategoryQuery).executeUpdate();
      String deleteInstructionQuery = "DELETE FROM instructions *;";
      con.createQuery(deleteInstructionQuery).executeUpdate();
      String deleteIngredientQuery = "DELETE FROM ingredients *;";
      con.createQuery(deleteIngredientQuery).executeUpdate();
      String deleteRecipeQuery = "DELETE FROM recipes *;";
      con.createQuery(deleteRecipeQuery).executeUpdate();
      String deleteTagsTableQuery = "DELETE FROM tags *;";
      con.createQuery(deleteTagsTableQuery).executeUpdate();
    }
  }
}
