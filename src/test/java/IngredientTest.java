import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

public class IngredientTest {

  private Ingredient testIngredient;

  @Before
  public void setUp() {
    testIngredient = new Ingredient("Saffron", 1);
    testIngredient.saveIngredient();
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void createsNewIngredient_True() {
    assertTrue(testIngredient instanceof Ingredient);
  }

  @Test
  public void getters_returnObjectVariables() {
    assertEquals("Saffron", testIngredient.getIngredientName());
    assertEquals(1, testIngredient.getIngredientRecipeId());
  }

  @Test
  public void returnsAllIngredients_True() {
    Ingredient testIngredient2 = new Ingredient("Saffron", 1);
    testIngredient2.saveIngredient();
    assertTrue(Ingredient.allIngredients().contains(testIngredient2));
  }

  @Test
  public void overrideEqualsWorks_True() {
    Ingredient testIngredient2 = Ingredient.findIngredient(testIngredient.getIngredientId());
    assertTrue(testIngredient.equals(testIngredient2));
  }

  @Test
  public void savesAllIngredients_True() {
    Ingredient testIngredient2 = new Ingredient("Saffron", 1);
    testIngredient2.saveIngredient();
    assertTrue(testIngredient2.equals(Ingredient.allIngredients().get(1)));
  }

   @Test
   public void ingredientsAreCreatedWithId_True() {
     assertTrue(testIngredient.getIngredientId() > 0);
   }

   @Test
   public void findsIngredientBasedOnId_TR2() {
     Ingredient testIngredient2 = new Ingredient("Saffron", 1);
     testIngredient2.saveIngredient();
     assertEquals(testIngredient2, Ingredient.findIngredient(testIngredient2.getIngredientId()));
   }

   @Test
   public void updatesIngredientInformation_True() {
     testIngredient.updateIngredient("Cumin");
     assertEquals(Ingredient.findIngredient(testIngredient.getIngredientId()).getIngredientName(), "Cumin");
   }

   @Test
   public void deleteIngredient_deletesIngredientFromDatabase_true() {
     int testIngredientId = testIngredient.getIngredientId();
     testIngredient.deleteIngredient();
     assertEquals(null, Ingredient.findIngredient(testIngredientId));
   }


}
