import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Block extends Rectangle
{
	public Rectangle towerSquare;
	
	public int groundID, airID, towerSquareSize = 130, shotMob = -1,
			loseTime = 100, loseFrame;
	public boolean isShooting = false;
	
	public Block(int x, int y, int width, int height, int groundID, int airID)
	{
		setBounds(x, y, width, height);
		towerSquare = new Rectangle(x - (towerSquareSize / 2), y - (towerSquareSize / 2), width + (towerSquareSize), height + (towerSquareSize));
		this.groundID = groundID;
		this.airID = airID;
	}
	
	public void paint(Graphics g)
	{
		g.drawImage(Screen.tileset_ground[groundID], x, y, width, height, null);
		
		if(airID != Value.airAir)
		{
			g.drawImage(Screen.tileset_air[airID], x, y, width, height, null);
		}
	}
	
	public void physics()
	{
		if(shotMob != -1 && Screen.mob[shotMob].intersects(towerSquare))
		{
			isShooting = true;
		}
		else
		{
			isShooting = false;
		}
		
		
		if(!isShooting)
		{
			if(airID == Value.airTowerLaser)
			{
				for(int i = 0; i < Screen.mob.length; i++)
				{
					if(Screen.mob[i].inGame)
					{
						if(Screen.mob[i].intersects(towerSquare))
						{
							isShooting = true;
							shotMob = i;
						}
					}
				}
			}
		}
		
		if(isShooting)
		{
			if(loseFrame >= loseTime)
			{
				Screen.mob[shotMob].loseHealth(1);
				loseFrame = 0;
			}
			else
			{
				loseFrame += 1;
			}

			if(Screen.mob[shotMob].isDead())
			{
				isShooting = false;
				shotMob = -1;
				Screen.killed += 1;
				Screen.hasWon();
			}
		}
	}
	
	public void getMoney(int mobID)
	{
		Screen.coins += Value.deathReward[mobID];
	}
	
	public void fight(Graphics g)
	{
		if(Screen.isDebug)
		{
			if(airID != Value.airAir)
			{
				g.drawRect(towerSquare.x, towerSquare.y, towerSquare.width, towerSquare.height);
			}
		}
			
			if(isShooting)
			{
				g.setColor(Color.RED);
				g.drawLine(x + (width / 2), y + (height / 2), Screen.mob[shotMob].x + (Screen.mob[shotMob].width / 2), Screen.mob[shotMob].y + (Screen.mob[shotMob].height / 2));
			}
	}
	
	
}
