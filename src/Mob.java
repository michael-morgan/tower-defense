import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Mob extends Rectangle
{
	public int mobSize = 52, mobID = Value.mobAir, xC, yC,
			walkFrame = 0, walkSpeed = 10, mobWalk = 0,
			right = 2, left = 3, up = 0, down = 1, direction = right,
			health, healthSpace = 3, healthHeight = 6;
	
	public boolean inGame = false, hasUp = false, hasDown = false,
			hasLeft = false, hasRight = false;
	
	public Mob()
	{
		
	}
	
	public void spawnMob(int mobID)
	{
		for(int y = 0; y < Screen.room.block.length; y++)
		{
			if(Screen.room.block[y][0].groundID == Value.groundRoad)
			{
				setBounds(Screen.room.block[y][0].x, Screen.room.block[y][0].y, mobSize, mobSize);
				xC = 0;
				yC = y;
			}
		}
		
		this.mobID = mobID;
		this.health = mobSize;
		
		inGame = true;
	}
	
	public void deleteSuccessfulMob()
	{
		inGame = false;
		direction = right;
		mobWalk = 0;
	}
	
	public void deleteMob()
	{
		inGame = false;
		direction = right;
		mobWalk = 0;
		
		Screen.room.block[0][0].getMoney(mobID);
	}
	
	public void loseHealth()
	{
		Screen.health -= 1;
	}
	
	public void physics()
	{
		if(walkFrame >= walkSpeed)
		{
			if(direction == right)
			{
				x += 1;
			}
			else if(direction == up)
			{
				y -= 1;
			}
			else if(direction == down)
			{
				y += 1;
			}
			else if(direction == left)
			{
				x -= 1;
			}
			
			mobWalk += 1;
			
			if(mobWalk == Screen.room.blockSize)
			{
				if(direction == right)
				{
					xC += 1;
					hasRight = true;
				}
				else if(direction == up)
				{
					yC -= 1;
					hasUp = true;
				}
				else if(direction == down)
				{
					yC += 1;
					hasDown = true;
				}
				else if(direction == left)
				{
					xC -= 1;
					hasLeft = true;
				}
				
				if(!hasUp)
				{
					try
					{
						if(Screen.room.block[yC + 1][xC].groundID == Value.groundRoad)
						{
							direction = down;
						}
					}
					catch(Exception e)
					{
						
					}
				}
				
				if(!hasDown)
				{
					try
					{
						if(Screen.room.block[yC - 1][xC].groundID == Value.groundRoad)
						{
							direction = up;
						}
					}
					catch(Exception e)
					{
						
					}
				}
				
				if(!hasLeft)
				{
					try
					{
						if(Screen.room.block[yC][xC + 1].groundID == Value.groundRoad)
						{
							direction = right;
						}
					}
					catch(Exception e)
					{
						
					}
				}
				
				if(!hasRight)
				{
					try
					{
						if(Screen.room.block[yC][xC - 1].groundID == Value.groundRoad)
						{
							direction = left;
						}
					}
					catch(Exception e)
					{
						
					}
				}
				
				if(Screen.room.block[yC][xC].airID == Value.airCave)
				{
					loseHealth();
					deleteSuccessfulMob();
				}
				
				hasUp = false;
				hasDown = false;
				hasLeft = false;
				hasRight = false;
				mobWalk = 0;
			}
			
			walkFrame = 0;
		}
		else
		{
			walkFrame += 1;
		}
	}
	
	public void loseHealth(int amount)
	{
		health -= amount;
		
		checkDeath();
	}
	
	public void checkDeath()
	{
		if(health == 0)
		{
			deleteMob();
		}
	}
	
	public boolean isDead()
	{
		if(inGame)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public void paint(Graphics g)
	{
		if(inGame)
		{
			g.drawImage(Screen.tileset_mob[mobID], x, y, width, height, null);
			
			//health bar
			g.setColor(new Color(180, 50, 50));
			g.fillRect(x, y - (healthSpace + healthHeight), width, healthHeight);
			
			g.setColor(new Color(50, 180, 50));
			g.fillRect(x, y - (healthSpace + healthHeight), health, healthHeight);
			
			
			g.setColor(Color.BLACK);
			g.drawRect(x, y - (healthSpace + healthHeight), health - 1, healthHeight - 1);
		}
	}
}
