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

public class RecipeTest {
  private Recipe testRecipe;

  @Before
  public void setUp() {
    testRecipe = new Recipe("Cake", 10);
    testRecipe.saveRecipe();
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void recipe_instantiatesCorrectly_true() {
    assertTrue(testRecipe instanceof Recipe);
  }

  @Test
  public void getters_returnObjectVariables() {
    assertEquals(testRecipe.getRecipeName(), "Cake");
    assertEquals(testRecipe.getRecipeRating(), 10);
  }

  @Test
  public void returnsAllRecipes_True() {
    Recipe testRecipe2 = new Recipe("Pie", 9);
    testRecipe2.saveRecipe();
    assertTrue(Recipe.allRecipes().contains(testRecipe2));
  }

  @Test
  public void overrideEqualsWorks_True() {
    Recipe testRecipe2 = Recipe.findRecipe(testRecipe.getRecipeId());
    assertTrue(testRecipe.equals(testRecipe2));
  }

  @Test
  public void savesAllRecipes_True() {
    Recipe testRecipe2 = new Recipe("Cake", 10);
    testRecipe2.saveRecipe();
    assertTrue(testRecipe2.equals(Recipe.allRecipes().get(1)));
  }

   @Test
   public void recipesAreCreatedWithId_True() {
     assertTrue(testRecipe.getRecipeId() > 0);
   }

   @Test
   public void findsRecipeBasedOnId_TR2() {
     Recipe testRecipe2 = new Recipe("Cake", 10);
     testRecipe2.saveRecipe();
     assertEquals(testRecipe2, Recipe.findRecipe(testRecipe2.getRecipeId()));
   }

   @Test
   public void createsRecipeWithCreationDate_Yes() {
     Timestamp testRecipeCreationDate = Recipe.findRecipe(testRecipe.getRecipeId()).getDateCreated();
     Timestamp rightNow = new Timestamp(new Date().getTime());
     assertEquals(testRecipeCreationDate.getDay(), rightNow.getDay());
   }

   @Test
   public void updatesRecipeInformation_True() {
     testRecipe.updateRecipe("Pie", 9);
     assertEquals(Recipe.findRecipe(testRecipe.getRecipeId()).getRecipeName(), "Pie");
     assertEquals(Recipe.findRecipe(testRecipe.getRecipeId()).getRecipeRating(), 9);
   }

   @Test
   public void deleteRecipe_deletesRecipeFromDatabase_true() {
     int testRecipeId = testRecipe.getRecipeId();
     testRecipe.deleteRecipe();
     assertEquals(null, Recipe.findRecipe(testRecipeId));
   }

   @Test
   public void getInstructions_returnsListOfInstructions() {
     Instruction testInstruction = new Instruction("Bake", testRecipe.getRecipeId());
     testInstruction.saveInstruction();
     Instruction secondInstruction = new Instruction("Fry", testRecipe.getRecipeId());
     secondInstruction.saveInstruction();
     Instruction[] instructions = new Instruction[] {testInstruction, secondInstruction};
     assertTrue(testRecipe.getInstructions().containsAll(Arrays.asList(instructions)));
   }

   @Test
   public void getIngredients_returnsListOfIngredients() {
     Ingredient testIngredient = new Ingredient("Saffron", testRecipe.getRecipeId());
     testIngredient.saveIngredient();
     Ingredient secondIngredient = new Ingredient("Cumin", testRecipe.getRecipeId());
     secondIngredient.saveIngredient();
     Ingredient[] ingredients = new Ingredient[] {testIngredient, secondIngredient};
     assertTrue(testRecipe.getIngredients().containsAll(Arrays.asList(ingredients)));
   }

   @Test
   public void getCategories_returnsAllCategories_List() {
     Category testCategory = new Category("Dessert");
     testCategory.saveCategory();
     testCategory.addRecipe(testRecipe);
     List savedCategories = testRecipe.getCategories();
     assertEquals(1, savedCategories.size());
   }

}
