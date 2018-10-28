import java.util.*;
import java.io.*;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.11, page
 * 82.<br>
 * <br>
 * 
 * <pre>
 * <pre>
 * function DEPTH-LIMITED-SEARCH(problem, limit) returns a solution, or failure/cutoff
 *   return RECURSIVE-DLS(MAKE-NODE(problem.INITIAL-STATE), problem, limit)
 *   
 * function RECURSIVE-DLS(node, problem, limit) returns a solution, or failure/cutoff
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   else if limit = 0 then return cutoff
 *   else
 *       cutoff_occurred? &lt;- false
 *       for each action in problem.ACTIONS(node.STATE) do
 *           child &lt;- CHILD-NODE(problem, node, action)
 *           result &lt;- RECURSIVE-DLS(child, problem, limit - 1)
 *           if result = cutoff then cutoff_occurred? &lt;- true
 *           else if result != failure then return result
 *       if cutoff_occurred? then return cutoff else return failure
 * function ITERATIVE-DEEPENING-SEARCH(problem) returns a solution, or failure
 *   for depth = 0 to infinity  do
 *     result &lt;- DEPTH-LIMITED-SEARCH(problem, depth)
 *     if result != cutoff then return result
 * </pre>
 * </pre>
 * 
 *
 */

/* Reference : 1. https://picoledelimao.github.io/blog/2015/12/06/solving-the-sliding-puzzle/
               2. https://stackoverflow.com/questions/37916136/how-to-calculate-memory-usage-of-java-program */

/* Class to store the initial configuration of the puzzle */
class PuzzleNode {
 int[][] state = new int[4][4];
 int exploredLevel = 0;
 int row_blankspace = -1;
 int column_blankspace = -1;
 PuzzleNode parent, moveUp, moveDown, moveLeft, moveRight;
 char currentMove = '\0';
 long timeTaken = 0L;
 long memory = 0L;
 long noOfNodes = 0L;
}


/* Class which contains the various operations carried out in 15 puzzle */

public class FifteenPuzzleIDS {



 public static void main(String[] args) throws Exception {
  PuzzleNode node = new PuzzleNode();
  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  System.out.println("Enter the initial configuration (4*4)");
  String input = br.readLine();
  String inputArray[] = new String[15];
  int arrayPointer = 0;
  if (input != null) {
   inputArray = input.split(" ");
  }

  for (int i = 0; i < 4; i++) {
   for (int j = 0; j < 4; j++) {
    int k = Integer.parseInt(inputArray[arrayPointer++]);
    if (k > 15 || k < 0) {
     throw new Exception("Inputs can't be greater than 15 or less than zero");
    }
    node.state[i][j] = k;
    if (node.state[i][j] == 0) {
     node.row_blankspace = i;
     node.column_blankspace = j;
    }


   }
  }
  boolean isSolution = iterativeDepthSearch(node);
  if (isSolution)
   System.out.println("Solution exists");
  else {
   System.out.println("Can't find a solution");
  }
 }



 /* Function that performs the IDDS for the fifteen puzzle */
 public static boolean iterativeDepthSearch(PuzzleNode node) {
  System.out.println("in iterativeDeepeningDepthSearch");
  LinkedList < PuzzleNode > linkedList = new LinkedList < PuzzleNode > ();
  PuzzleNode solution = new PuzzleNode();
  long startTime = System.currentTimeMillis();
  long timeTaken = 0L;
  int depth = 0;
  int maxDepth = 1000; //have decided upon 1000 as random max depth
  while (depth < maxDepth){
    LinkedList < PuzzleNode > frontierstack = new LinkedList < PuzzleNode > (); //starting from the begining again because Iterative depth explores again and again
    frontierstack.add(node);// frontier &lt;- a LIFO stack with node as the only element

  while (!frontierstack.isEmpty() && timeTaken < 180000) {
   timeTaken = System.currentTimeMillis() - startTime;
   PuzzleNode currentNode = frontierstack.removeLast(); //LIFO removes the last inserted element

   //System.out.println("in while");
   if (GOALTEST(currentNode.state)) {
    System.out.println("in goal test");

    retrieveMoves(currentNode);
    timeTaken = System.currentTimeMillis() - startTime; 
    // System.out.println(timeTaken);
    //long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    solution.memory = (afterUsedMem) / 1024;
    //System.out.println(solution.memory + "  " + beforeUsedMem + "  " + afterUsedMem);
    display(solution, timeTaken);

    return true;
   } 

   else if(currentNode.exploredLevel < depth) //checking cutoff 
   {
    // System.out.println("in child expansion");
    if (!linkedList.contains(currentNode)) //to check for repeated states
    linkedList.add(currentNode);
    CHILDNODE(currentNode); // evaluate child function
    solution.noOfNodes++;

    if (currentNode.moveLeft != null)
     frontierstack.add(currentNode.moveLeft);
    if (currentNode.moveRight != null)
     frontierstack.add(currentNode.moveRight);
    if (currentNode.moveUp != null)
     frontierstack.add(currentNode.moveUp);
    if (currentNode.moveDown != null)
     frontierstack.add(currentNode.moveDown);

   }

}

  depth++;

  }

  return false;
 }

 /* Function that expands the parentnode based on the actions recieved (move left,move right,move up,move down) */
 public static void CHILDNODE(PuzzleNode currentNode) {
  int row = currentNode.row_blankspace;
  int column = currentNode.column_blankspace;


  if (currentNode.column_blankspace != 0) {
   currentNode.moveLeft = new PuzzleNode();
   PuzzleNode alteredPuzzle = new PuzzleNode();
   currentNode.moveLeft.currentMove = 'L';
   currentNode.moveLeft.exploredLevel = currentNode.exploredLevel + 1;
   currentNode.moveLeft.row_blankspace = currentNode.row_blankspace;
   currentNode.moveLeft.column_blankspace = currentNode.column_blankspace - 1;
   currentNode.moveLeft.parent = currentNode;
   alteredPuzzle = alteredState(currentNode.state, currentNode.row_blankspace, currentNode.column_blankspace, currentNode.moveLeft.currentMove);
   currentNode.moveLeft.state = alteredPuzzle.state;
  }
  if (currentNode.column_blankspace != 3) {
   currentNode.moveRight = new PuzzleNode();
   PuzzleNode alteredPuzzle = new PuzzleNode();
   currentNode.moveRight.currentMove = 'R';
   currentNode.moveRight.exploredLevel = currentNode.exploredLevel + 1;
   currentNode.moveRight.row_blankspace = currentNode.row_blankspace;
   currentNode.moveRight.column_blankspace = currentNode.column_blankspace + 1;
   currentNode.moveRight.parent = currentNode;

   alteredPuzzle = alteredState(currentNode.state, currentNode.row_blankspace, currentNode.column_blankspace, currentNode.moveRight.currentMove);
   currentNode.moveRight.state = alteredPuzzle.state;
  }
  if (currentNode.row_blankspace != 0) {
   currentNode.moveUp = new PuzzleNode();
   PuzzleNode alteredPuzzle = new PuzzleNode();
   currentNode.moveUp.currentMove = 'U';
   currentNode.moveUp.exploredLevel = currentNode.exploredLevel + 1;
   currentNode.moveUp.row_blankspace = currentNode.row_blankspace - 1;
   currentNode.moveUp.column_blankspace = currentNode.column_blankspace;
   currentNode.moveUp.parent = currentNode;
   alteredPuzzle = alteredState(currentNode.state, currentNode.row_blankspace, currentNode.column_blankspace, currentNode.moveUp.currentMove);
   currentNode.moveUp.state = alteredPuzzle.state;

  }
  if (currentNode.row_blankspace != 3) {
   currentNode.moveDown = new PuzzleNode();
   PuzzleNode alteredPuzzle = new PuzzleNode();
   currentNode.moveDown.currentMove = 'D';
   currentNode.moveDown.exploredLevel = currentNode.exploredLevel + 1;
   currentNode.moveDown.row_blankspace = currentNode.row_blankspace + 1;
   currentNode.moveDown.column_blankspace = currentNode.column_blankspace;
   currentNode.moveDown.parent = currentNode;
   alteredPuzzle = alteredState(currentNode.state, currentNode.row_blankspace, currentNode.column_blankspace, currentNode.moveDown.currentMove);
   currentNode.moveDown.state = alteredPuzzle.state;
  }
 }



/* To alter the initial state and subsequent states depending on the direction you move */
 public static PuzzleNode alteredState(int[][] currentState, int row, int column, char move) {
  PuzzleNode alteredPuzle = new PuzzleNode();
  for (int i = 0; i < 4; i++)
   alteredPuzle.state[i] = currentState[i].clone();

  if (move == 'L') {
   alteredPuzle.state[row][column] = alteredPuzle.state[row][column - 1];
   alteredPuzle.state[row][column - 1] = 0;
  } else if (move == 'R') {
   alteredPuzle.state[row][column] = alteredPuzle.state[row][column + 1];
   alteredPuzle.state[row][column + 1] = 0;
  } else if (move == 'U') {
   alteredPuzle.state[row][column] = alteredPuzle.state[row - 1][column];
   alteredPuzle.state[row - 1][column] = 0;
  } else if (move == 'D') {
   alteredPuzle.state[row][column] = alteredPuzle.state[row + 1][column];
   alteredPuzle.state[row + 1][column] = 0;
  }
  return alteredPuzle;
 }


 /* To retrieve the path in which the goal is reached */

 public static void retrieveMoves(PuzzleNode currentNode) {
  LinkedList < PuzzleNode > movesMade = new LinkedList < PuzzleNode > ();
  String movesmade = "";
  while (currentNode != null) {
   movesMade.addFirst(currentNode);
   currentNode = currentNode.parent; // traversing from node through the parent of each node 
  }
  while (!movesMade.isEmpty()) {
   PuzzleNode temp = movesMade.removeFirst();
   movesmade = movesmade + temp.currentMove;
  }
  System.out.println("Moves: " + movesmade);
 }

/* Tests whether goal has been reached returns true or false */
 public static boolean GOALTEST(int[][] state) {
  int[][] goal = new int[4][4];
  int count = 1;
  for (int i = 0; i < 4; i++) {
   for (int j = 0; j < 4; j++) {
    goal[i][j] = count++;
   }
  }
  goal[3][3] = 0;
  return Arrays.deepEquals(state, goal);
 }



/* Displays the output in desired format */
 public static void display(PuzzleNode solution, long timeTaken) {
  System.out.println("Time taken " + timeTaken + " mS");
  System.out.println("Expanded nodes " + solution.noOfNodes);
  System.out.println("Memory Used " + solution.memory + " kB");

 }

}