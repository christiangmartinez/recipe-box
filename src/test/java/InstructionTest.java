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

public class InstructionTest {
  private Instruction testInstruction;

  @Before
  public void setUp() {
    testInstruction = new Instruction("Bake", 1);
    testInstruction.saveInstruction();
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void createsNewInstruction_True() {
    assertTrue(testInstruction instanceof Instruction);
  }

  @Test
  public void getters_returnObjectVariables() {
    assertEquals("Bake", testInstruction.getInstructionName());
    assertEquals(1, testInstruction.getInstructionRecipeId());
  }

  @Test
  public void returnsAllInstructions_True() {
    Instruction testInstruction2 = new Instruction("Bake", 1);
    testInstruction2.saveInstruction();
    assertTrue(Instruction.allInstructions().contains(testInstruction2));
  }

  @Test
  public void overrideEqualsWorks_True() {
    Instruction testInstruction2 = Instruction.findInstruction(testInstruction.getInstructionId());
    assertTrue(testInstruction.equals(testInstruction2));
  }

  @Test
  public void savesAllInstructions_True() {
    Instruction testInstruction2 = new Instruction("Bake", 1);
    testInstruction2.saveInstruction();
    assertTrue(testInstruction2.equals(Instruction.allInstructions().get(1)));
  }

   @Test
   public void instructionsAreCreatedWithId_True() {
     assertTrue(testInstruction.getInstructionId() > 0);
   }

   @Test
   public void findsInstructionBasedOnId_TR2() {
     Instruction testInstruction2 = new Instruction("Bake", 1);
     testInstruction2.saveInstruction();
     assertEquals(testInstruction2, Instruction.findInstruction(testInstruction2.getInstructionId()));
   }

   @Test
   public void updatesInstructionInformation_True() {
     testInstruction.updateInstruction("Cook");
     assertEquals(Instruction.findInstruction(testInstruction.getInstructionId()).getInstructionName(), "Cook");
   }

   @Test
   public void deleteInstruction_deletesInstructionFromDatabase_true() {
     int testInstructionId = testInstruction.getInstructionId();
     testInstruction.deleteInstruction();
     assertEquals(null, Instruction.findInstruction(testInstructionId));
   }

}
