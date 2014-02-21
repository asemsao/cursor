import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


//VS4E -- DO NOT REMOVE THIS LINE!
public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	public static final int ROWS = 30;
	public static final int COLUMNS = 30;
	public static final int CELL_SIZE = 16;
	private CursorCanvas canvas;
	private static final int CANVAS_WIDTH = COLUMNS * CELL_SIZE ;
	private static final int CANVAS_HEIGHT = ROWS * CELL_SIZE;
	private static int UPDATE_RATE = 5;
	private static long UPDATE_PERIODE = 1000000000L / UPDATE_RATE;
	private Status status;
	private Food food;
	private Cursor cursor;
	CursorThread cursorThread;
	
	public Main() {
		cursorInit();
		canvas = new CursorCanvas();				
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		this.setContentPane(canvas);		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int xW = (int) ((dimension.getWidth() - CANVAS_WIDTH) / 2);
		int yH = (int) ((dimension.getHeight() - CANVAS_HEIGHT) / 2);
		this.setLocation(xW, yH);
		this.setResizable(false);
		this.setTitle("Cursor");
		this.setVisible(true);
		this.pack();
		cursorStart();
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {				
				new Main();
			}
		});
	}
	
	public void cursorKeyPressed(int keyCode){
		switch (keyCode) {
		case KeyEvent.VK_UP:
			cursor.setDirection(Direction.UP);
			status = Status.PLAYING;
			break;
		case KeyEvent.VK_DOWN:
			cursor.setDirection(Direction.DOWN);	
			status = Status.PLAYING;
			break;
		case KeyEvent.VK_LEFT:
			cursor.setDirection(Direction.LEFT);
			status = Status.PLAYING;
			break;
		case KeyEvent.VK_RIGHT:
			cursor.setDirection(Direction.RIGHT);
			status = Status.PLAYING;
			break;
		}
	}
	
	public void curosrKeyReleased(int keyCode){
		
	}
	
	public void cursorKeyTyped(char keyChar){
		
	}
	
	public void cursorInit(){
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu settings = new JMenu("Settings");
		
        file.setMnemonic(KeyEvent.VK_F);
        settings.setMnemonic(KeyEvent.VK_T);
        
        JMenuItem rMenuItem = new JMenuItem("Restart");
        JMenuItem pMenuItem = new JMenuItem("Pause");
        JMenuItem eMenuItem = new JMenuItem("Exit");
        
        final JMenuItem sUPMenuItem = new JMenuItem("Speed UP");
        final JMenuItem sDOWNMenuItem = new JMenuItem("Speed DOWN");
        
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() {   
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
        });
        
        pMenuItem.setMnemonic(KeyEvent.VK_P);
        pMenuItem.setToolTipText("Pause application");
        pMenuItem.addActionListener(new ActionListener() {   
			@Override
			public void actionPerformed(ActionEvent e) {
				status = Status.PAUSED;
			}
        });
        
        rMenuItem.setMnemonic(KeyEvent.VK_R);
        rMenuItem.setToolTipText("R application");
        rMenuItem.addActionListener(new ActionListener() {   
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				UPDATE_RATE = 5;
				UPDATE_PERIODE = 1000000000L / UPDATE_RATE;
				new Main();
			}
        });
        
        sUPMenuItem.setMnemonic(KeyEvent.VK_U);
        sUPMenuItem.setToolTipText("Speed UP");
        sUPMenuItem.addActionListener(new ActionListener() {   
			@Override
			public void actionPerformed(ActionEvent e) {
				if (UPDATE_RATE > 15) {
					sUPMenuItem.setEnabled(false);
				} else {
					sDOWNMenuItem.setEnabled(true);
					UPDATE_RATE++;
					UPDATE_PERIODE = 1000000000L / UPDATE_RATE;
				}
				
			}
        });
        
        sDOWNMenuItem.setMnemonic(KeyEvent.VK_D);
        sDOWNMenuItem.setToolTipText("Speed UP");
        sDOWNMenuItem.addActionListener(new ActionListener() {   
			@Override
			public void actionPerformed(ActionEvent e) {
				if (UPDATE_RATE < 2) {
					sDOWNMenuItem.setEnabled(false);
				} else {
					sDOWNMenuItem.setEnabled(true);
					UPDATE_RATE--;
					UPDATE_PERIODE = 1000000000L / UPDATE_RATE;
				}
				
			}
        });
        
        file.add(rMenuItem);
        file.add(pMenuItem);
        file.add(eMenuItem);        
        
        settings.add(sUPMenuItem);
        settings.add(sDOWNMenuItem);

        menubar.add(file);
        menubar.add(settings);
        setJMenuBar(menubar);
        
		cursor = new Cursor();
		food = new Food();		
		status = Status.INITIALIZED;
	}
	
	class CursorThread extends Thread{
		public void run(){
			loopingCursor();
		}
	}
	public void cursorStart(){
		cursorThread = new CursorThread();
		cursorThread.start();
	}
	
	public void loopingCursor(){
		if (status == Status.INITIALIZED || status == Status.GAMEOVER) {
			cursor.regenerate();
			int x, y, x1, y1, x2, y2;
			do {
				food.regenerate();
				x = food.getX();
				y = food.getY();
				x1 = food.getX1();
				y1 = food.getY1();
				x2 = food.getX2();
				y2 = food.getY2();
			} while (cursor.contains(x, y)|| cursor.contains(x1, y1) || cursor.contains(x2, y2));
			status = Status.PAUSED;
		}
		
		long beginTime, timeTaken, timeLeft;
		while (true) {
			beginTime = System.nanoTime();
			if (status == Status.GAMEOVER) {
				break;
			}
			
			if (status  == Status.PLAYING) {
				cursorUpdate();
			}
			
			repaint();
			
			timeTaken = System.nanoTime() - beginTime;
			timeLeft = (UPDATE_PERIODE - timeTaken)/1000000L;
			
			if (timeLeft < 10) {
				timeLeft = 10;				
			}
			
			try{
				Thread.sleep(timeLeft);
			}catch(InterruptedException e){
				
			}
		}
	}
	
	public void cursorShutdown(){
		//......//
	}
	
	public void cursorUpdate(){
		cursor.update();
		processCollision();
	}
	
	public void processCollision(){
		int hx = cursor.getHX();
		int hy = cursor.getHY();
		
		if(hx == food.getX() && hy == food.getY()){
			int x, y;
				food.eatFood();
				x = food.getX();
				y = food.getY();				
		} else if(hx == food.getX1() && hy == food.getY1()){
			int x1, y1;
				food.eatFood1();
				x1 = food.getX1();
				y1 = food.getY1();				
		}else if(hx == food.getX2() && hy == food.getY2()){
			int x2, y2;
				food.eatFood2();
				x2 = food.getX2();
				y2 = food.getY2();				
		} else {
			cursor.shrink();
		}		
	}
	
	public void cursorDraw(Graphics g){
		switch (status) {
		case INITIALIZED:
			
			break;
		case PLAYING:
			cursor.draw(g);
			food.draw(g);					
			break;
		case PAUSED:
			cursor.draw(g);
			food.draw(g);
			break;
		case GAMEOVER:
			g.setFont(new Font("Dialog", Font.PLAIN, 14));
			g.setColor(Color.RED);
			g.drawString(" Game Over!", 5, 25);
			break;
		}
	}
	
	class CursorCanvas extends JPanel implements KeyListener{
		private static final long serialVersionUID = 1L;

		public CursorCanvas(){
			setFocusable(true);
			requestFocus();
			addKeyListener(this);
		}
		
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			setBackground(Color.WHITE);			
			cursorDraw(g);
		}
		
		@Override
		public void keyTyped(KeyEvent e) {
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			cursorKeyPressed(e.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		public boolean contains(int x, int y) {
			if ((x < 0) || (x >= ROWS)) {
				return false;
			}
			if ((y < 0) || (y >= COLUMNS)) {
				return false;
			}
			return true;
		}
	}
}

	
