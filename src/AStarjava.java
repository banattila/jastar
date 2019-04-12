import java.util.PriorityQueue;

public class AStarjava {

    public static final int DIAGONAL_COST = 14;
    public static final int V_H_COST = 10;

    private Cell[][] grid;

    private PriorityQueue<Cell> openCells;
    private boolean[][] closedCells;

    private int startI, startJ, endI, endJ;

    public AStarjava(int width, int height, int si, int sj, int ei, int ej, int[][] blocks){
        grid = new Cell[width][height];
        closedCells = new boolean[width][height];
        openCells = new PriorityQueue<>((Cell c1, Cell c2) ->
                c1.getFinalCost() < c2.getFinalCost() ? -1 : c1.getFinalCost() > c2.getFinalCost() ? 1 :0);

        startCell(si, sj);
        endCell(ei, ej);
        
        for (int i = 0; i< grid.length; i++){
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell(i, j);
                grid[i][j].setHeruisticCost(Math.abs(i - endI) + Math.abs(j -endJ));
                grid[i][j].setSolution(false);
            }
        }

        grid[startI][startJ].setFinalCost(0);

        for(int i = 0; i < blocks.length; i++){
            addBlockOnCell(blocks[i][0], blocks[i][1]);
        }
    }

    public void startCell(int i, int j){
        startI = i;
        startJ = j;
    }

    public void endCell(int i, int j){
        endJ = j;
        endI = i;
    }

    public void addBlockOnCell(int i, int j){
        grid[i][j] = null;
    }

    public void updateCostIfNeeded(Cell current, Cell t, int cost){
        if (t == null || closedCells[t.getI()][t.getJ()]){
            return;
        }

        int tFinalCost = t.getHeruisticCost() + cost;
        boolean isOpen = openCells.contains(t);

        if (!isOpen || t.getFinalCost() > tFinalCost){
            t.setFinalCost(tFinalCost);
            t.setParent(current);

            if (!isOpen)
                openCells.add(t);
        }
    }

    public void process(){

        openCells.add(grid[startI][startJ]);
        Cell current;

        while (true){

            current = openCells.poll();

            if (current == null)
                break;

            closedCells[current.getI()][current.getJ()] = true;

            if (current == grid[endI][endJ])
                return;

            Cell t;

            if(current.getI() - 1 >= 0){
                t = grid[current.getI() -1][current.getJ()];
                updateCostIfNeeded(current, t, current.getFinalCost() + V_H_COST);

                if (current.getJ() -1 >= 0){
                    t = grid[current.getI() -1][current.getJ() - 1];
                    updateCostIfNeeded(current, t, current.getFinalCost() + DIAGONAL_COST);
                }

                if (current.getJ() + 1 < grid[0].length){
                    t = grid[current.getI() -1][current.getJ() + 1];
                    updateCostIfNeeded(current, t, current.getFinalCost() + DIAGONAL_COST);
                }
            }
            if (current.getJ() -1 >= 0){
                t = grid[current.getI()][current.getJ() - 1];
                updateCostIfNeeded(current, t, current.getFinalCost() + V_H_COST);
            }

            if (current.getJ() + 1 < grid[0].length){
                t = grid[current.getI()][current.getJ() + 1];
                updateCostIfNeeded(current, t, current.getFinalCost() + V_H_COST);
            }

            if (current.getI() + 1 < grid.length){
                t = grid[current.getI() + 1][current.getJ()];
                updateCostIfNeeded(current, t, current.getFinalCost() + V_H_COST);

                if (current.getJ() -1 >= 0){
                    t = grid[current.getI() + 1][current.getJ() - 1];
                    updateCostIfNeeded(current, t, current.getFinalCost() + DIAGONAL_COST);
                }
                if (current.getJ() +1 < grid[0].length){
                    t = grid[current.getI() + 1][current.getJ() + 1];
                    updateCostIfNeeded(current, t, current.getFinalCost() + DIAGONAL_COST);
                }
            }
        }
    }

    public void display(){

        System.out.println("Grid");

        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j< grid[i].length; j++){
                if (i == startI && j == startJ)
                    System.out.print("Start   ");
                else if(i == endI && j == endJ)
                    System.out.print("DE      ");
                else if(grid[i][j] == null)
                    System.out.printf("%-3d ", 0);
                else
                    System.out.println("BL");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void displayScores(){
        System.out.println("\nScores for cells: ");

        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                if (grid[i][j] != null)
                    System.out.printf("%-3d", grid[i][j].getFinalCost());
                else
                    System.out.println("BL");
            }
        }
    }

    public void displaySolution(){
        System.out.println("Path: \n");
        Cell current = grid[endI][endJ];
        System.out.println(current);
        grid[current.getI()][current.getJ()].setSolution(true);

        while (current.parent != null){
            System.out.print(" -> " + current.parent);
            grid[current.parent.getI()][current.parent.getJ()].setSolution(true);
            current = current.parent;
        }

        System.out.println("\n");

        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j< grid[i].length; j++){
                if (i == startI && j == startJ)
                    System.out.println("Start: \n");
                else if(i == endI && j == endJ)
                    System.out.print("DE  ");
                else if(grid[i][j] != null)
                    System.out.printf("%-3s ", grid[i][j].getSolution() ?"X": "O");
                else
                    System.out.print("BL  ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        AStarjava aStar = new AStarjava(5, 5, 0, 0, 3, 2, new int[][]{
                {0, 4}, {2, 2}, {3, 1}, {3, 3}, {2, 1}, {2, 3}, {4, 1}
        });

        aStar.process();
        aStar.displaySolution();
    }
}


