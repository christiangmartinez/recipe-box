$(function() {
  $("#addRecipe").click(function(){
    $("#recipeForm").text();
    $(this).before("<form action='/' method='post'>"+
      "<div class='form-group'>"+
      "<label for='recipeName'><h5>Enter Recipe Name</h5></label>"+
      "<input id='recipeName' name='recipeName' type='text' required>"+
      "</div>"+
      "<div class='form-group'>"+
      "<label for='recipeRating'><h5>Enter Recipe Rating</h5></label>"+
      "<input id='recipeRating' name='recipeRating' type='number' required min='1' max='5'>"+
      "</div>"+
      "<button type='submit' class='btn btn-success'>Add Recipe</button>"+
      "</form>");
  })
})

$(function() {
  $("#addCategory").click(function(){
    $("#categoryForm").text();
    $(this).before("<form action='/category' method='post'>"+
      "<div class='form-group'>"+
      "<label for='categoryName'><h5>Enter Categorical Name</h5></label>"+
      "<input id='categoryName' name='categoryName' type='text' required>"+
      "</div>"+
      "<button type='submit' class='btn btn-success'>Add Category</button>"+
      "</form>");
  })
})
