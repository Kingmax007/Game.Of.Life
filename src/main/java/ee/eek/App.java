package ee.eek;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Transient;
import java.util.Random;

public final class App {
    private App() {
    }

    static class Main extends JPanel {
        //Generation
        private final int[][] grid;
        private static final Random rnd = new Random();
        private int generationCounter;

        public Main(int width, int height) {
            this.grid = new int[width / 4][height / 4];
            setupGrid();
        }

        //GRID
        private void setupGrid() {
            for (int[] row : grid) {
                for (int j = 0; j < row.length; j++) {
                    if (rnd.nextDouble() < 0.92)
                        continue;
                    row[j] = rnd.nextInt( 2 );
                }
            }
        }

        //NewGrid
        public void updateGrid() {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    applyRule( i, j );
                }
            }
        }
        //RULES:
        //Any live cell with two or three live neighbours survives.
        //Any dead cell with three live neighbours becomes a live cell.
        //All others live cells die in the next generation. Similarly, all other dead cells stay dead.

        private void applyRule(int i, int j) {
            int left = 0, right = 0, up = 0, down = 0;
            int upperLeft = 0, upperRight = 0, lowerLeft = 0, lowerRight = 0;

            if (j < grid.length - 1) {
                right= grid[i][j + 1];
                if (i > 0)
                    upperRight = grid[i - 1][j + 1];
                if (i < grid.length - 1)
                    lowerRight = grid[i + 1][j + 1];
            }

            if (j > 0) {
                left = grid[i][j - 1];
                if (i > 0)
                    upperLeft = grid[i - 1][j - 1];
                if (i < grid.length - 1)
                    lowerLeft = grid[i + 1][j - 1];
            }

            if (i > 0)
                up = grid[i - 1][j];
            if (i < grid.length - 1)
                down = grid[i + 1][j];

            int sum = left + right + up + down + upperLeft + upperRight
                    + lowerLeft
                    + lowerRight;

            if (grid[i][j] == 1) {
                if (sum < 2)
                    grid[i][j] = 0;
                if (sum > 3)
                    grid[i][j] = 0;
            } else {
                if (sum == 3)
                    grid[i][j] = 1;
            }
        }

        //Dimension of the board
        @Override
        @Transient
        public Dimension getPreferredSize() {
            return new Dimension( grid.length * 4, grid[0].length * 4 );
        }

        //Colour
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent( g );
            Color gColor = g.getColor();

            g.drawString( "Generation: " + generationCounter++, 0, 10 );
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == 1) {
                        g.setColor( Color.GREEN );
                        g.fillRect( j * 4, i * 4, 4, 4 );
                    }
                }
            }

            g.setColor( gColor );
        }

        public static void main(String[] args) {
            final Main c = new Main( 700, 800 );
            JFrame frame = new JFrame();
            frame.getContentPane().add( c );
            frame.pack();
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frame.setLocationByPlatform( true );
            frame.setVisible( true );
            new Timer( 100, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    c.updateGrid();
                    c.repaint();
                }
            } ).start();
        }
    }
}