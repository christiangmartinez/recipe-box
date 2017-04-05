import org.sql2o.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

public class Instruction {
  private String name;
  private int id;
  private int recipe_id;

  public Instruction(String name, int recipe_id) {
    this.name = name;
    this.recipe_id = recipe_id;
  }

  public String getInstructionName() {
    return name;
  }

  public int getInstructionId() {
    return id;
  }

  public int getInstructionRecipeId() {
    return recipe_id;
  }

  @Override
  public boolean equals(Object otherInstruction) {
      if (!(otherInstruction instanceof Instruction)) {
        return false;
      } else {
        Instruction newInstruction = (Instruction) otherInstruction;
        return this.getInstructionName().equals(newInstruction.getInstructionName()) && this.getInstructionId() == newInstruction.getInstructionId();
      }
  }

  public static List<Instruction> allInstructions() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM instructions;";
      return con.createQuery(sql)
        .executeAndFetch(Instruction.class);
    }
  }

  public void saveInstruction() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO instructions (name, recipe_id) VALUES (:name, :recipe_id);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("recipe_id", recipe_id)
        .executeUpdate()
        .getKey();
    }
  }

  public static Instruction findInstruction(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM instructions WHERE id = :id;";
      Instruction newInstruction  = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Instruction.class);
    return newInstruction;
    }
  }

  public void updateInstruction(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE instructions SET name = :name WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("name", name)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }

  public void deleteInstruction() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM instructions WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }

}
