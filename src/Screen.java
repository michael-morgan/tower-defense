import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class Screen extends JPanel implements Runnable
{
	public Thread gameloop = new Thread(this);
	
	public static int myWidth, myHeight, coins = 30, health = 100,
					spawnTime = 1200, spawnFrame = 0, killed,
					killsToWin, level = 1, maxLevel = 3,
					winTime = 4000, winFrame = 0;
	
	public static Image[] tileset_ground = new Image[100],
			tileset_air = new Image[100], tileset_cell = new Image[100],
			tileset_heart = new Image[1], tileset_coin = new Image[1],
			tileset_mob = new Image[100];
	
	public static boolean isFirst = true, isDebug = false, isWin = false;
	
	public static Point point = new Point(0, 0);
	
	public static Room room;
	
	public static Save save;
	
	public static Store store;
	
	public static Mob[] mob = new Mob[100];
	
	public Screen(Frame frame)
	{
		frame.addMouseListener(new KeyHandler());
		frame.addMouseMotionListener(new KeyHandler());
		
		gameloop.start();
	}
	
	public void define()
	{
		room = new Room();
		save = new Save();
		store = new Store();
		
		coins = 30;
		health = 100;
		
		for(int i = 0; i < tileset_ground.length; i++)
		{
			tileset_ground[i] = new ImageIcon("res/tileset_ground.png").getImage();
			tileset_ground[i] = createImage(new FilteredImageSource(tileset_ground[i].getSource(), new CropImageFilter(0, 26 * i, 26, 26)));
		}
		for(int i = 0; i < tileset_air.length; i++)
		{
			tileset_air[i] = new ImageIcon("res/tileset_air.png").getImage();
			tileset_air[i] = createImage(new FilteredImageSource(tileset_air[i].getSource(), new CropImageFilter(0, 26 * i, 26, 26)));
		}
		
		tileset_cell[0] = new ImageIcon("res/cell.png").getImage();
		tileset_heart[0] = new ImageIcon("res/heart.png").getImage();
		tileset_coin[0] = new ImageIcon("res/coin.png").getImage();
		tileset_mob[0] = new ImageIcon("res/mob.png").getImage();
		
		save.loadSave(new File("save/mission" + level + ".towerdefence"));
		
		for(int i = 0; i < mob.length; i++)
		{
			mob[i] = new Mob();
		}
	}
	
	@Override
	public void paint(Graphics g)
	{
		if(isFirst)
		{
			myWidth = getWidth();
			myHeight = getHeight();
			define();
			
			isFirst = false;
		}
		
		g.setColor(new Color(70, 70, 70));
		g.fillRect(0, 0, getWidth(),getHeight());
		g.setColor(Color.BLACK);
		g.drawLine(room.block[0][0].x - 1, 0, room.block[0][0].x - 1, room.block[room.worldHeight - 1][0].y + room.blockSize + 1); //drawing left line
		g.drawLine(room.block[0][room.worldWidth - 1].x + room.blockSize, 0, room.block[0][room.worldWidth - 1].x + room.blockSize, room.block[room.worldHeight - 1][0].y + room.blockSize + 1); //drawing right line
		g.drawLine(room.block[0][0].x, room.block[room.worldHeight - 1][0].y + room.blockSize, room.block[0][room.worldWidth - 1].x + room.blockSize, room.block[room.worldHeight - 1][0].y + room.blockSize); //drawing bottom line
		
		room.paint(g); //drawing the room
		
		for(int i = 0; i < mob.length; i++)
		{
			if(mob[i].inGame)
			{
				mob[i].paint(g);
			}
		}
		
		store.paint(g); //drawing the store
		
		if(health < 1)
		{
			g.setColor(new Color(40, 20, 20));
			g.fillRect(0, 0, myWidth, myHeight);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Minecraft", Font.BOLD, 28));
			g.drawString("GAME OVER!", (myWidth / 2) - 100, (myHeight / 2));
			
		}
		
		if(isWin)
		{
			if(level == maxLevel)
			{
				g.setColor(new Color(40, 20, 20));
				g.fillRect(0, 0, myWidth, myHeight);
				g.setColor(Color.WHITE);
				g.setFont(new Font("Minecraft", Font.BOLD, 24));
				g.drawString("You are victorious! Thanks for playing Tower Defence!", (myWidth / 2) - 210, (myHeight / 2));
			}
			else
			{
				g.setColor(new Color(40, 20, 20));
				g.fillRect(0, 0, myWidth, myHeight);
				g.setColor(Color.WHITE);
				g.setFont(new Font("Minecraft", Font.BOLD, 24));
				g.drawString("Great work! Get ready for the next level!", (myWidth / 2) - 210, (myHeight / 2));
			}
		}
	}
	
	public static void hasWon()
	{
		if(killed == killsToWin)
		{
			isWin = true;
			killed = 0;
			coins = 0;
		}
	}
	
	public void mobSpawner()
	{
		if(spawnFrame >= spawnTime)
		{
			for(int i = 0; i < mob.length; i++)
			{
				if(!mob[i].inGame)
				{
					mob[i].spawnMob(Value.mobZombie);
					break;
				}
			}
			
			spawnFrame = 0;
		}
		else
		{
			spawnFrame += 1;
		}
	}
	
	@Override
	public void run()
	{
		
		while(true)
		{
			if(!isFirst && health > 0 && !isWin)
			{
				room.physics();
				mobSpawner();
				
				for(int i = 0; i < mob.length; i++)
				{
					if(mob[i].inGame)
					{
						mob[i].physics();
					}
						
				}
			}
			else
			{
				if(isWin)
				{
					if(winFrame >= winTime)
					{
						if(level == maxLevel)
						{
							System.exit(0);
						}
						else
						{
							level += 1;
							define();
							isWin = false;
						}
						
						winFrame = 0;
					}
					else
					{
						winFrame += 1;
					}
				}
			}
			
			repaint();
			
			try
			{
				Thread.sleep(1);
			}
			catch(Exception e)
			{
				
			}
		}
		
	}
	
}
