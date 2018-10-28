import java.util.*;
import java.io.*;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.11, page
 * 82.<br>
 * <br>
 * 
 * <pre>
 * <pre>
 * Artificial Intelligence A Modern Approach (3rd Edition): page 93.<br>
 * <br>
 * The most widely known form of best-first search is called A* Search
 * (pronounced "A-star search"). It evaluates nodes by combining g(n), the cost
 * to reach the node, and h(n), the cost to get from the node to the goal:<br>
 * f(n) = g(n) + h(n).<br>
 * <br>
 * Since g(n) gives the path cost from the start node to node n, and h(n) is the
 * estimated cost of the cheapest path from n to the goal, we have<br>
 * f(n) = estimated cost of the cheapest solution through n.
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

public class FifteenPuzzleAStar {



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
  boolean isSolution = aStarSearchMisplacedTiles(node);
  if (isSolution)
   System.out.println("Solution exists");
  else {
   System.out.println("Can't find a solution");
  }

  System.out.println();
  System.out.println();

 boolean isnewSolution = aStarSearchManhattanDistance(node);
  if (isnewSolution)
   System.out.println("Solution exists");
  else {
   System.out.println("Can't find a solution");
  }
 }



 /* Function that performs the IDDS for the fifteen puzzle */
 public static boolean aStarSearchMisplacedTiles(PuzzleNode node) {
  System.out.println("Misplaced Tiles Heuristic");
  //System.out.println();
 // LinkedList < PuzzleNode > linkedList = new LinkedList < PuzzleNode > ();
  PuzzleNode solution = new PuzzleNode();
  long startTime = System.currentTimeMillis();
  long timeTaken = 0L;
  int depth = 0;
  int maxDepth = 1000; //have decided upon 1000 as random max depth
  LinkedList < PuzzleNode > frontier = new LinkedList < PuzzleNode > (); //starting from the begining again because Iterative depth explores again and again
  frontier.add(node);// frontier &lt;- a LIFO stack with node as the only element
  PuzzleNode currentNode = new PuzzleNode();
  while (timeTaken < 180000) {
    int maxtiles = 10000;
   timeTaken = System.currentTimeMillis() - startTime;
   //frontier.remove(); //LIFO removes the last inserted element
    for (int i = 0; i < frontier.size() ; i++) {     //Update Node based on f(n) value
                PuzzleNode updateNode = frontier.get(i);
                if(noOfTilesMisplaced(updateNode) + updateNode.exploredLevel < maxtiles){
                    maxtiles =  updateNode.exploredLevel + noOfTilesMisplaced(updateNode); // f(n) = g(n)(explored Level) + h(n) (no.of misplaced tiled)
                    currentNode = updateNode;
                }
              }
              frontier.removeFirstOccurrence(currentNode);

   //System.out.println("in while");
   if (noOfTilesMisplaced(currentNode) == 0) {

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

   else //checking cutoff 
   {
    // System.out.println("in child expansion");
    CHILDNODE(currentNode); // evaluate child function
    solution.noOfNodes++;

    if (currentNode.moveLeft != null)
     frontier.add(currentNode.moveLeft);
    if (currentNode.moveRight != null)
     frontier.add(currentNode.moveRight);
    if (currentNode.moveUp != null)
     frontier.add(currentNode.moveUp);
    if (currentNode.moveDown != null)
     frontier.add(currentNode.moveDown);

   }
   
}
return false;
}


public static boolean aStarSearchManhattanDistance(PuzzleNode node) {
   System.out.println("Manhattan Distance Heuristic");
  //System.out.println();
 // LinkedList < PuzzleNode > linkedList = new LinkedList < PuzzleNode > ();
  PuzzleNode solution = new PuzzleNode();
  long startTime = System.currentTimeMillis();
  long timeTaken = 0L;
  int depth = 0;
  int maxDepth = 1000; //have decided upon 1000 as random max depth
  LinkedList < PuzzleNode > frontier = new LinkedList < PuzzleNode > (); //starting from the begining again because Iterative depth explores again and again
  frontier.add(node);// frontier &lt;- a LIFO stack with node as the only element
  PuzzleNode currentNode = new PuzzleNode();


  while (timeTaken < 180000) {
    int maxtiles = 10000;
   timeTaken = System.currentTimeMillis() - startTime;
   //frontier.remove(); //LIFO removes the last inserted element
    for (int i = 0; i < frontier.size() ; i++) {     //Update Node based on f(n) value
                PuzzleNode updateNode = frontier.get(i);
                if(retrieveManhattanDistance(updateNode) + updateNode.exploredLevel < maxtiles){
                    maxtiles =  updateNode.exploredLevel + retrieveManhattanDistance(updateNode); // f(n) = g(n)(explored Level) + h(n) (no.of misplaced tiled)
                    currentNode = updateNode;
                }
              }
              frontier.removeFirstOccurrence(currentNode);

   //System.out.println("in while");
   if (retrieveManhattanDistance(currentNode) == 0) {
   // System.out.println("in goal test");

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

   else //checking cutoff 
   {
    // System.out.println("in child expansion");
    CHILDNODE(currentNode); // evaluate child function
    solution.noOfNodes++;

    if (currentNode.moveLeft != null)
     frontier.add(currentNode.moveLeft);
    if (currentNode.moveRight != null)
     frontier.add(currentNode.moveRight);
    if (currentNode.moveUp != null)
     frontier.add(currentNode.moveUp);
    if (currentNode.moveDown != null)
     frontier.add(currentNode.moveDown);

   }
   
}
return false;
}
  
/* Method to find the Manhattan Distance given the initial state */
   public static int retrieveManhattanDistance(PuzzleNode currentNode){
        int manhattanDistance = 0;
        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 4 ; j++ ) {
                int row_val;
                int column_val;
                int state_value = currentNode.state[i][j];
                
                if(state_value >= 1 && state_value <= 4){ //First row 1 to 4
                    row_val = 0;
                    column_val = state_value - 1;
                }
                else if(state_value >= 5 && state_value <= 8){ //second row 4 to 8
                    row_val = 1;
                    column_val = state_value - 5;
                }
                else if(state_value >= 9 && state_value <= 12){ //third row 9 to 12
                    row_val = 2;
                    column_val = state_value - 9;
                }
                else if(state_value >= 13 && state_value <= 15){ //fourth row 13 to 15
                    row_val = 3;
                    column_val = state_value - 13;
                }
                else if(state_value == 0){ 
                    row_val = 3;
                    column_val = 3;
                }
                else{
                  return 0;
                }

                manhattanDistance = manhattanDistance + Math.abs(i - row_val) + Math.abs(j - column_val);
            }
        }
        return manhattanDistance;
}

/* Method to find the Number of tiles misplaced */
  public static int noOfTilesMisplaced(PuzzleNode currentNode){
     int[][] goal = new int[4][4];
  int count = 1;
  // Goal state initialized
  for (int i = 0; i < 4; i++) {
   for (int j = 0; j < 4; j++) {
    goal[i][j] = count++;
   }
  }
  goal[3][3] = 0;
    int noOfTiles = 0;
    for (int i = 0; i<4; i++){
      for (int j = 0; j < 4; j++){
          if(goal[i][j] != currentNode.state[i][j])
            noOfTiles++;                                          //For each misplaced tile add number of tiles misplaced
          }
        }
        return noOfTiles;
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