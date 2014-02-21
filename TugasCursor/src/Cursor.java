import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.datatype.Duration;


public class Cursor {
	private Color color = Color.BLUE;
	private Color colorHead = Color.GREEN;
	private Direction direction;
	
	private List<CursorSegment> cursorSegments = new ArrayList<CursorSegment>();
	private boolean directionUpdate;
	private Random random = new Random();
	
	public void regenerate(){
		cursorSegments.clear();
		int length = 1;
		int hx = 0;
		int hy = 0;
		direction = Direction.RIGHT;
		cursorSegments.add(new CursorSegment(hx, hy, length, direction));
		directionUpdate = false;
	}
	
	public void setDirection(Direction newDirection){
		if(!directionUpdate && (newDirection != direction)
				&& ((newDirection == Direction.UP && direction != Direction.DOWN)
				|| (newDirection == Direction.DOWN &&  direction != Direction.UP)
				|| (newDirection == Direction.LEFT && direction != Direction.RIGHT)
				|| (newDirection == Direction.RIGHT && direction != Direction.LEFT))){
			CursorSegment headSegment = cursorSegments.get(0);
			int x = headSegment.getHX();
			int y = headSegment.getHY();
			
			cursorSegments.add(0, new CursorSegment(x, y, 0, newDirection));
			direction = newDirection;
			directionUpdate = true;
		}
	}
	
	public void update(){
		CursorSegment headSegments;
		headSegments = cursorSegments.get(0);
		headSegments.grow();
		directionUpdate = false;
	}
	
	public void shrink(){
		CursorSegment tailSegment;
		tailSegment = cursorSegments.get(cursorSegments.size() - 1);
		tailSegment.shrink();
		if (tailSegment.getLength() == 0) {
			cursorSegments.remove(tailSegment);
		}
	}
	
	public int getHX(){
		return cursorSegments.get(0).getHX();
	}
	
	public int getHY(){
		return cursorSegments.get(0).getHY();
	}
	
	public int getLength(){
		int length = 0;
		for (CursorSegment segment : cursorSegments) {
			length += segment.getLength();
		}
		
		return length;
	}
	
	public boolean contains(int x, int y){
		for (int i = 0; i < cursorSegments.size(); i++) {
			CursorSegment segment = cursorSegments.get(i);
			if (segment.contains(x, y)) {
				return true;
			}
		}
		return false;
	}
	
	public void draw(Graphics g){
		g.setColor(color);
		for (int i = 0; i < cursorSegments.size(); i++) {
			cursorSegments.get(i).draw(g);
		}
		
		if (cursorSegments.size() > 0) {
			g.setColor(colorHead);
			g.fill3DRect(getHX()*Main.CELL_SIZE, getHY()*Main.CELL_SIZE,
					Main.CELL_SIZE - 1 , Main.CELL_SIZE - 1 , true);
		}
	}
}
