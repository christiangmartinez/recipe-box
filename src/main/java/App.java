import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("categories",Category.allCategories());
      model.put("recipes", Recipe.allRecipes());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/recipes/:recipe_id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Recipe recipe = Recipe.findRecipe(Integer.parseInt(request.params(":recipe_id")));
      model.put("recipe", recipe);
      model.put("ingredients", Ingredient.allIngredients());
      model.put("instructions", Instruction.allInstructions());
      model.put("template", "templates/recipe.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String recipeName = request.queryParams("recipeName");
      int recipeRating = Integer.parseInt(request.queryParams("recipeRating"));
      Recipe newRecipe = new Recipe(recipeName, recipeRating);
      newRecipe.saveRecipe();
      String url = String.format("/");
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("recipes/:recipe_id/ingredient", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Recipe recipe = Recipe.findRecipe(Integer.parseInt(request.params(":recipe_id")));
      String ingredientName = request.queryParams("ingredientName");
      Ingredient newIngredient = new Ingredient(ingredientName, recipe.getRecipeId());
      newIngredient.saveIngredient();
      String url = String.format("/recipes/" + recipe.getRecipeId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("recipes/:recipe_id/instruction", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Recipe recipe = Recipe.findRecipe(Integer.parseInt(request.params(":recipe_id")));
      String instructionName = request.queryParams("instructionName");
      Instruction newInstruction = new Instruction(instructionName, recipe.getRecipeId());
      newInstruction.saveInstruction();
      String url = String.format("/recipes/" + recipe.getRecipeId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


  }
}
