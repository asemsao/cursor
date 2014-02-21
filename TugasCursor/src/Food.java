import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;


public class Food {
	private int x, y, x1, y1, x2, y2;
	private Color color = Color.BLUE;
	private Color colorFeed = Color.GRAY;
	private Random random = new Random();
	Graphics gr= null;
	
	public Food(){
		x = -1;
		y = -1;
		x1 = -1;
		y1 = -1;
		x2 = -1;
		y2 = -1;
	}
	
	public void regenerate(){
		x = random.nextInt(Main.COLUMNS - 4) + 2;
		y = random.nextInt(Main.ROWS - 4) + 2;
		x1 = random.nextInt(Main.COLUMNS - 4) + 2;
		y1 = random.nextInt(Main.ROWS - 4) + 2;
		x2 = random.nextInt(Main.COLUMNS - 4) + 2;
		y2 = random.nextInt(Main.ROWS - 4) + 2;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getX1() {
		return x1;
	}
	
	public int getY1(){
		return y1;
	}
	
	public int getX2() {
		return x2;
	}
	
	public int getY2(){
		return y2;
	}
	
	public void draw(Graphics g){
		this.gr = g;
		g.setColor(color);
		g.fill3DRect(x* Main.CELL_SIZE, y * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE, true);
		g.fill3DRect(x1* Main.CELL_SIZE, y1 * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE, true);
		g.fill3DRect(x2* Main.CELL_SIZE, y2 * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE, true);
	}
	
	public void eatFood(){
		gr.setColor(colorFeed);
		gr.fill3DRect(x* Main.CELL_SIZE, y * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE, true);
		x = -1;
		y = -1;
	}
	
	public void eatFood1(){
		gr.setColor(colorFeed);
		gr.fill3DRect(x1* Main.CELL_SIZE, y1 * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE, true);
		x1 = -1;
		y1 = -1;
	}
	
	public void eatFood2(){
		gr.setColor(colorFeed);
		gr.fill3DRect(x2* Main.CELL_SIZE, y1 * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE, true);
		x2 = -1;
		y2 = -1;
	}
}
