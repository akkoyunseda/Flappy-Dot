package flappyDot;
import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JFrame;




public class FlappyDot implements ActionListener, MouseListener, KeyListener
{
	public static FlappyDot flappyDot;
	public Renderer renderer;
	public Random random;
	
	public final int WIDTH = 800, HEIGHT =800;
	public Rectangle dot;
	public ArrayList<Rectangle> columns;
	
	public int ticks, yMotion, score;
	public boolean gameOver, started, darkmode;
	
	Timer timer = new Timer(20, this);
	
	public FlappyDot()
	{
		JFrame jframe = new JFrame();
			
		renderer = new Renderer();
		random = new Random();
		
		//JFrame design
		jframe.add(renderer);
		jframe.setTitle("Flappy Dot");
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		dot = new Rectangle(WIDTH /2 - 10, HEIGHT /2 - 10, 20, 20);
		columns = new ArrayList<Rectangle>();
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		timer.start();
	}
	
	
	//Column adding
	public void addColumn(boolean start)
	{	
		//Column design
		int space = 300;
		int widht = 100;
		int height = 50+random.nextInt(300);
		
		if(start)
		{
			//First column
			columns.add(new Rectangle(WIDTH+ widht+ columns.size()*300,HEIGHT-height-120, widht, height));
			//Second column
			columns.add(new Rectangle(WIDTH+ widht+ (columns.size()-1)*300,0, widht,HEIGHT-height-space));
		}
		else 
		{
			columns.add(new Rectangle(columns.get(columns.size()-1).x + 600,HEIGHT-height-120, widht, height));
			columns.add(new Rectangle(columns.get(columns.size()-1).x,0, widht,HEIGHT-height-space));
		}
	}
	
	//Column painting
	public void paintColumn(Graphics g, Rectangle column)
	{
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y,column.width, column.height);
	}
	
	//Jump action
	public void jump()
	{
		if(gameOver)
		{
			dot = new Rectangle(WIDTH /2 - 10, HEIGHT /2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;
		
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
						
			gameOver = false;
		}
		if(!started)
		{
			started = true;
		}
		else if(!gameOver)
		{
			if(yMotion > 0)
			{
				yMotion = 0;
			}
			yMotion -= 8;
		}
	}
	
	//Game paused
	public void pause()
	{
		timer.stop();
	}
	
	//Game resume
	public void resume()
	{
		timer.restart();
	}
	
	//Actions
	public void actionPerformed(ActionEvent arg0) 
	{		
		int speed = 8;
		ticks++;
		
		if(started)
		{
			for(int i=0; i<columns.size(); i++)
			{
				Rectangle column= columns.get(i);
				column.x -= speed;        
							
			}
						
			if(ticks % 2 == 0 && yMotion < 15)
			{
				yMotion += 2;
			}
			
			for(int i=0; i<columns.size(); i++)
			{
				Rectangle column= columns.get(i);
				if(column.x + column.width < 0)
				{
					columns.remove(column);
					if(column.y == 0)
					{
						addColumn(false);
					}			
				}
			}
			
			dot.y += yMotion;
			for(Rectangle column : columns)
			{
				if(column.y == 0 && dot.x + dot.width / 2 > column.x + column.width/ 2 - 8 && dot.x + dot.width / 2 < column.x + column.width/2 + 8)
				{
					score +=1;
				}
				
				//Kolonla kesiþme durumu
				if(column.intersects(dot))
				{
					gameOver = true;
					
					if(dot.x <= column.x)
					{
						dot.x = column.x - dot.width;
					}
					else 
					{
						if(column.y != 0)
						{
							dot.y = column.y - dot.height;
						}
						else if(dot.y < column.height)
						{
							dot.y = column.height;
						}
					}
				}
			}
			
			if(dot.y > HEIGHT - 120 || dot.y < 0)
			{
				gameOver = true;
			}
			
			if(dot.y + yMotion >= HEIGHT - 120)
			{
				dot.y = HEIGHT - 120 - dot.height;
			}
		}		
		renderer.repaint();
	}
	
	//Main
	public static void main(String[]args)
	{
		flappyDot = new FlappyDot();	
	}
	
	
	//Columns, background, messages
	
	public void repaint(Graphics g) 
	{
		if(darkmode)
		{
			g.setColor(Color.cyan.darker().darker().darker());
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}
		if(!darkmode)
		{
			g.setColor(Color.cyan);
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}
		
		
		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT-120, WIDTH, 150);
		
		g.setColor(Color.green);
		g.fillRect(0, HEIGHT-120, WIDTH, 20);
		
		g.setColor(Color.red);
		g.fillRect(dot.x, dot.y, dot.width, dot.height);
		
		
		for(Rectangle column : columns)
		{
			paintColumn(g, column);
		}
		     
		g.setColor(Color.white);
		g.setFont(new Font("Arial",1,80));
		
		
		if(!started)
		{
			g.drawString("Click to start!", 130, HEIGHT / 2 - 50);
		}	
		if(gameOver)
		{
			g.drawString("Game Over!", 150, HEIGHT / 2 - 60);
			g.drawString("Score: "+ score, 150, HEIGHT/2 +50);
		}	
		if(!gameOver && started)
		{
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
		
		
	}
	
	// Click and space key function

	public void mouseClicked(MouseEvent arg0)
	{	
		jump();
	}

	public void mouseEntered(MouseEvent arg0) 
	{
	}

	public void mouseExited(MouseEvent arg0) 
	{
	}

	public void mousePressed(MouseEvent arg0) 
	{
	}

	public void mouseReleased(MouseEvent arg0) 
	{
	}

	public void keyPressed(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()== KeyEvent.VK_SPACE)
		{
			jump();
		}
		if(e.getKeyCode() == KeyEvent.VK_P)
		{
			pause();
		}
		if(e.getKeyCode() == KeyEvent.VK_R)
		{
			resume();
		}
		if(e.getKeyCode() == KeyEvent.VK_D)
		{
			darkmode = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_B)
		{
			darkmode = false;
		}
	}
	
	public void keyTyped(KeyEvent arg0) {
	}
}