import java.awt.Graphics;


public class CursorSegment {
	private int hx, hy;
	private int length;
	private Direction direction;
	
	public CursorSegment(int hx, int hy, int length, Direction direction){
		this.hx = hx;
		this.hy = hy;
		this.direction = direction;
		this.length = length;
	}
	
	public void grow(){
		++length;
		switch (direction) {
		case LEFT:
			if (hx == 0) {
				
			}else{
				--hx;
			}
			
			break;
		case RIGHT:
			if (hx == (Main.COLUMNS -1)) {
				
			}else{
				++hx;
			}
			break;
		case UP:
			if (hy == 0) {
				
			}else{
				--hy;
			}
			break;
		case DOWN:
			if (hy == (Main.ROWS -1)) {
				
			}else{
				++hy;
			}
			break;
		
		}
	}
	
	public void shrink(){
		length--;
	}
	
	public int getLength(){
		return length;
	}
	
	public int getHX(){
		return hx;
	}
	
	public int getHY(){
		return hy;
	}
	
	private int getTX(){
		if(direction == Direction.LEFT){
			return hx + length - 1;
		}else if(direction == Direction.RIGHT){
			return hx - length + 1;
		}else{
			return hx;
		}
	}
	
	private int getTY(){
		if(direction == Direction.UP){
			return hy - length + 1;
		}else if(direction == Direction.DOWN){
			return hy + length - 1;
		}else{
			return hy;
		}
	}
	
	public boolean contains(int x, int y){
		switch (direction) {
		case LEFT:
			return ((y == this.hy) && ((x >= this.hx) && (x <= getTX())));
		case RIGHT:
			return ((y == this.hy) && ((x <= this.hx) && (x >= getTX())));
		case UP:
			return ((x == this.hx) && ((y >= this.hy) && (x <= getTY())));
		case DOWN:
			return ((x == this.hx) && ((y <= this.hy) && (x >= getTY())));
		}
		return false;
	}
	
	public void draw(Graphics g){
		int x = hx;
		int y = hy;
		
		switch (direction) {
		case LEFT:
			for (int i = 0; i < length; ++i) {
				g.fill3DRect(x* Main.CELL_SIZE, y* Main.CELL_SIZE,
						Main.CELL_SIZE -1, Main.CELL_SIZE -1, true);
				++x;
			}
			break;
		case RIGHT:
			for (int i = 0; i < length; ++i) {
				g.fill3DRect(x* Main.CELL_SIZE, y* Main.CELL_SIZE,
						Main.CELL_SIZE -1, Main.CELL_SIZE -1, true);
				--x;
			}
			break;
		case UP:
			for (int i = 0; i < length; ++i) {
				g.fill3DRect(x* Main.CELL_SIZE, y* Main.CELL_SIZE,
						Main.CELL_SIZE -1, Main.CELL_SIZE -1, true);
				++y;
			}
			break;
		case DOWN:
			for (int i = 0; i < length; ++i) {
				g.fill3DRect(x* Main.CELL_SIZE, y* Main.CELL_SIZE,
						Main.CELL_SIZE -1, Main.CELL_SIZE -1, true);
				--y;
			}
			break;
		}
	}	
}
