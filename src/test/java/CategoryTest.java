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

public class CategoryTest {
  private Category testCategory;

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Before
  public void setUp() {
    testCategory = new Category("Dessert");
    testCategory.saveCategory();
  }

  @Test
  public void createsNewCategory_True() {
    assertTrue(testCategory instanceof Category);
  }

  @Test
  public void getters_returnObjectVariables() {
    assertEquals("Dessert", testCategory.getCategoryName());
  }

  @Test
  public void returnsAllCategorys_True() {
    Category testCategory2 = new Category("Mexican");
    testCategory2.saveCategory();
    System.out.println(testCategory2.getCategoryName());
    assertTrue(Category.allCategories().contains(testCategory2));
  }

  @Test
  public void overrideEqualsWorks_True() {
    Category testCategory2 = Category.findCategory(testCategory.getCategoryId());
    assertTrue(testCategory.equals(testCategory2));
  }

  @Test
  public void savesAllCategories_True() {
    Category testCategory2 = new Category("Dessert");
    testCategory2.saveCategory();
    assertTrue(testCategory2.equals(Category.allCategories().get(1)));
  }

   @Test
   public void ingredientsAreCreatedWithId_True() {
     assertTrue(testCategory.getCategoryId() > 0);
   }

   @Test
   public void findsCategoryBasedOnId_TR2() {
     Category testCategory2 = new Category("Dessert");
     testCategory2.saveCategory();
     assertEquals(testCategory2, Category.findCategory(testCategory2.getCategoryId()));
   }

   @Test
   public void updatesCategoryInformation_True() {
     testCategory.updateCategory("Mediterranean");
     assertEquals(Category.findCategory(testCategory.getCategoryId()).getCategoryName(), "Mediterranean");
   }

   @Test
   public void deleteCategory_deletesCategoryFromDatabase_true() {
     int testCategoryId = testCategory.getCategoryId();
     testCategory.deleteCategory();
     assertEquals(null, Category.findCategory(testCategoryId));
   }

   @Test
   public void deleteCategory_deletesAllTagsAssociations() {
     Recipe testRecipe = new Recipe("Cake", 10);
     testRecipe.saveRecipe();
     testCategory.addRecipe(testRecipe);
     testCategory.deleteCategory();
     assertEquals(0, testRecipe.getCategories().size());
   }

   @Test
   public void addRecipeToCategory_True() {
     Recipe testRecipe = new Recipe("Cake", 10);
     testRecipe.saveRecipe();
     testCategory.addRecipe(testRecipe);
     Recipe savedRecipe = testCategory.getRecipes().get(0);
     assertTrue(testRecipe.equals(savedRecipe));
   }

   @Test
   public void getRecipes_returnAllReciptes_List() {
     Recipe testRecipe = new Recipe("Cake", 10);
     testRecipe.saveRecipe();
     testCategory.addRecipe(testRecipe);
     Recipe testRecipe2 = new Recipe("Pie", 9);
     testRecipe2.saveRecipe();
     testCategory.addRecipe(testRecipe2);
     List savedRecipes = testCategory.getRecipes();
     assertEquals(savedRecipes.size(), 2);
   }

   @Test
   public void removeRecipe_removesRecipeFromCategory() {
     Recipe testRecipe = new Recipe("Cookies", 8);
     testRecipe.saveRecipe();
     testCategory.addRecipe(testRecipe);
     testCategory.removeRecipe(testRecipe);
     List savedRecipes = testCategory.getRecipes();
     assertEquals(0, savedRecipes.size());
   }
}
