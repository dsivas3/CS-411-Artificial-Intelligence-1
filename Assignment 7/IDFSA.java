import java.io.*;
import java.util.*;

/*
Iterative Deepening A* Search
*/
/*
procedure ida_star(root)
   bound := h(root)
   path := [root]
   loop
     t := search(path, 0, bound)
     if t = FOUND then return (path, bound)
     if t = ∞ then return NOT_FOUND
     bound := t
   end loop
 end procedure
 
 function search(path, g, bound)
   node := path.last
   f := g + h(node)
   if f > bound then return f
   if is_goal(node) then return FOUND
   min := ∞
   for succ in successors(node) do
     if succ not in path then
       path.push(succ)
       t := search(path, g + cost(node, succ), bound)
       if t = FOUND then return FOUND
       if t < min then min := t
       path.pop()
     end if
   end for
   return min
 end function


*/

class Node {
 int[][] state = new int[4][4];
 int exploredLevel = 0;
 int row_blankspace = -1;
 int column_blankspace = -1;
 Node parent, moveUp, moveDown, moveLeft, moveRight;
 char currentMove = '\0';
 long timeTaken = 0L;
 long memory = 0L;
 long noOfNodes = 0L;
}

public class IDFSA {
  public static int non = 0;

 public static void main(String[] args) throws Exception {
  Node node = new Node();
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


 boolean isnewSolution = ida_star(node);
  if (isnewSolution)
   System.out.println("Solution exists");
  else {
   System.out.println("Can't find a solution");
  }
 }


public static int retrieveManhattanDistance(Node currentNode){
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

/* Function that performs iterative deppening Search */
private static boolean ida_star(Node root) {
Node solution = new Node();
long startTime = System.currentTimeMillis();
long timeTaken = 0L;

List<Node> successPath = new ArrayList<>();

int threshold=retrieveManhattanDistance(root);
successPath.add(root);

while(true) {

    int t = searchSuccessPath(root,0,threshold);
    if(t==0){
     timeTaken = System.currentTimeMillis() - startTime; 
    long afterUsedMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/2;
    solution.memory = afterUsedMem / 1024;
    display(solution, timeTaken);
    return true;
    }
    else if(t == Integer.MAX_VALUE){
        return false;
    }
    
    threshold = t;

}

}

/* Function that iteratively increases the threshold */

private static int searchSuccessPath(Node rootNode, int cost, int threshold) {
    
    int totalCost = cost+retrieveManhattanDistance(rootNode);

    if(totalCost > threshold) { //returns new threshold if current cost exceeds
        return totalCost;
    }

    if(isGoalNodeReached(rootNode.state)) { //checks goal state
         retrieveMoves(rootNode);
        return 0;
    }  

    int min = Integer.MAX_VALUE;

    // Node minimumThresHoldNode = findNodeLinks(expandedNode);
    // successPath.add(minimumThresHoldNode);
    // System.out.println(Arrays.deepToString(minimumThresHoldNode.state) + retrieveManhattanDistance(expandedNode));

    for(Node x: findNodeLinks(rootNode)) { //find all successors

        int t= searchSuccessPath(x,cost+x.exploredLevel,threshold);
        //System.out.println(Arrays.deepToString(x.state) + retrieveManhattanDistance(x));
      
      if(t==0){    
        return 0;
      }

      if(t<min){
        non++;
        min=t;
      }
       
         

}

return min;



}

/* Find all successors reached from the node in one step */

private static List<Node> findNodeLinks(Node expandedNode) {
   CHILDNODE(expandedNode);
   List<Node>  minimumThresHoldNode = new ArrayList<>();
   int minimumThresHoldValue =Integer.MAX_VALUE;


   if(expandedNode.moveLeft!=null) {
    int leftNodeThreshold = expandedNode.moveLeft.exploredLevel + retrieveManhattanDistance(expandedNode.moveLeft);

        minimumThresHoldNode.add(expandedNode.moveLeft);
     
   }

    if(expandedNode.moveRight!=null) {
    int rightNodeThreshold = expandedNode.moveRight.exploredLevel + retrieveManhattanDistance(expandedNode.moveRight);
   
        minimumThresHoldNode.add(expandedNode.moveRight);

     
   }

   if(expandedNode.moveUp!=null) {
    int upNodeThreshold = expandedNode.moveUp.exploredLevel + retrieveManhattanDistance(expandedNode.moveUp);
        minimumThresHoldNode.add(expandedNode.moveUp);

     }
   

   if(expandedNode.moveDown!=null) {
    int downNodeThreshold = expandedNode.moveDown.exploredLevel + retrieveManhattanDistance(expandedNode.moveDown);
 
        minimumThresHoldNode.add(expandedNode.moveDown);
      
   }

   return minimumThresHoldNode;


}

private static boolean isGoalNodeReached(int[][] state) {
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

  public static void CHILDNODE(Node currentNode) {
  int row = currentNode.row_blankspace;
  int column = currentNode.column_blankspace;


  if (currentNode.column_blankspace != 0) {
   currentNode.moveLeft = new Node();
   Node alteredPuzzle = new Node();
   currentNode.moveLeft.currentMove = 'L';
   currentNode.moveLeft.exploredLevel = currentNode.exploredLevel + 1;
   currentNode.moveLeft.row_blankspace = currentNode.row_blankspace;
   currentNode.moveLeft.column_blankspace = currentNode.column_blankspace - 1;
   currentNode.moveLeft.parent = currentNode;
   alteredPuzzle = alteredState(currentNode.state, currentNode.row_blankspace, currentNode.column_blankspace, currentNode.moveLeft.currentMove);
   currentNode.moveLeft.state = alteredPuzzle.state;
  }
  if (currentNode.column_blankspace != 3) {
   currentNode.moveRight = new Node();
   Node alteredPuzzle = new Node();
   currentNode.moveRight.currentMove = 'R';
   currentNode.moveRight.exploredLevel = currentNode.exploredLevel + 1;
   currentNode.moveRight.row_blankspace = currentNode.row_blankspace;
   currentNode.moveRight.column_blankspace = currentNode.column_blankspace + 1;
   currentNode.moveRight.parent = currentNode;

   alteredPuzzle = alteredState(currentNode.state, currentNode.row_blankspace, currentNode.column_blankspace, currentNode.moveRight.currentMove);
   currentNode.moveRight.state = alteredPuzzle.state;
  }
  if (currentNode.row_blankspace != 0) {
   currentNode.moveUp = new Node();
   Node alteredPuzzle = new Node();
   currentNode.moveUp.currentMove = 'U';
   currentNode.moveUp.exploredLevel = currentNode.exploredLevel + 1;
   currentNode.moveUp.row_blankspace = currentNode.row_blankspace - 1;
   currentNode.moveUp.column_blankspace = currentNode.column_blankspace;
   currentNode.moveUp.parent = currentNode;
   alteredPuzzle = alteredState(currentNode.state, currentNode.row_blankspace, currentNode.column_blankspace, currentNode.moveUp.currentMove);
   currentNode.moveUp.state = alteredPuzzle.state;

  }
  if (currentNode.row_blankspace != 3) {
   currentNode.moveDown = new Node();
   Node alteredPuzzle = new Node();
   currentNode.moveDown.currentMove = 'D';
   currentNode.moveDown.exploredLevel = currentNode.exploredLevel + 1;
   currentNode.moveDown.row_blankspace = currentNode.row_blankspace + 1;
   currentNode.moveDown.column_blankspace = currentNode.column_blankspace;
   currentNode.moveDown.parent = currentNode;
   alteredPuzzle = alteredState(currentNode.state, currentNode.row_blankspace, currentNode.column_blankspace, currentNode.moveDown.currentMove);
   currentNode.moveDown.state = alteredPuzzle.state;
  }
 }


  public static Node alteredState(int[][] currentState, int row, int column, char move) {
  Node alteredPuzle = new Node();
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

 public static void display(Node solution, long timeTaken) {
  non = non/4;
  System.out.println("Time taken " + timeTaken + " mS");
  System.out.println("Expanded nodes " + non);
  System.out.println("Memory Used " + solution.memory + " kB");

 }

 public static void retrieveMoves(Node currentNode) {
  LinkedList <Node> movesMade = new LinkedList <Node> ();
  String movesmade = "";
  while (currentNode != null) {
   movesMade.addFirst(currentNode);
   currentNode = currentNode.parent; // traversing from node through the parent of each node 
  }
  while (!movesMade.isEmpty()) {
   Node temp = movesMade.removeFirst();
   movesmade = movesmade + temp.currentMove;
  }
  System.out.println("Moves: " + movesmade);
 }


}
