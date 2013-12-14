import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Store
{
	public static int shopWidth = 8, buttonSize = 52, cellSpace = 2, awayFromRoom = 29,
			iconSize = 20, iconSpace = 6, iconTextY = 17, itemIn = 4, heldID = -1,
			realID = 0;
	public static int[] buttonID = {Value.airTowerLaser, Value.airAir, Value.airAir, Value.airAir, Value.airAir, Value.airAir, Value.airAir, Value.airTrashCan};
	public static int[] buttonPrice = {10, 0, 0, 0, 0, 0, 0, 0};
	public Rectangle[] button = new Rectangle[shopWidth];
	public Rectangle buttonHealth, buttonCoins;
	public boolean holdItem = false;

	public Store()
	{
		define();	
	}
	
	public void click(int mouseButton)
	{
		if(mouseButton == 1)
		{
			for(int i = 0; i < button.length; i++)
			{
				if(button[i].contains(Screen.point))
				{
					if(buttonID[i] != Value.airAir)
					{
						if(buttonID[i] == Value.airTrashCan)
						{
							holdItem = false;
						}
						else
						{
							heldID = buttonID[i];
							realID = i;
							holdItem = true;
						}
					}
				}
			}
			
			if(holdItem)
			{
				if(Screen.coins >= buttonPrice[realID])
				{
					for(int y = 0; y < Screen.room.block.length; y++)
					{
						
						for(int x = 0; x < Screen.room.block[0].length; x++)
						{
							if(Screen.room.block[y][x].contains(Screen.point))
							{
								if(Screen.room.block[y][x].groundID != Value.groundRoad && Screen.room.block[y][x].airID == Value.airAir)
								{
									Screen.room.block[y][x].airID = heldID;
									Screen.coins -= buttonPrice[realID];
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void define()
	{
		for(int i = 0; i < button.length; i++)
		{
			button[i] = new Rectangle((Screen.myWidth / 2) - ((shopWidth * (buttonSize + cellSpace)) / 2) + ((buttonSize + cellSpace) * i), (Screen.room.block[Screen.room.worldHeight - 1][0].y) + Screen.room.blockSize + awayFromRoom, buttonSize, buttonSize);
		}
		
		buttonHealth = new Rectangle(Screen.room.block[0][0].x - 1, button[0].y, iconSize, iconSize);
		buttonCoins = new Rectangle(Screen.room.block[0][0].x - 1, button[0].y + button[0].height - iconSize, iconSize, iconSize);
	}
	
	public void paint(Graphics g)
	{
		
		for(int i = 0; i < button.length; i++)
		{
			if(button[i].contains(Screen.point))
			{
				g.setColor(new Color(255, 255, 255, 75));
				g.fillRect(button[i].x, button[i].y, button[i].width, button[i].height);
			}
			
			g.drawImage(Screen.tileset_cell[0], button[i].x, button[i].y, button[i].width, button[i].height, null);
			
			if(buttonID[i] != Value.airAir)
			{
				g.drawImage(Screen.tileset_air[buttonID[i]], button[i].x + itemIn, button[i].y + itemIn, button[i].width - (itemIn * 2), button[i].height - (itemIn * 2), null);
				if(buttonPrice[i] > 0)
				{
					g.setColor(new Color(255, 255, 255));
					g.setFont(new Font("Minecraft", Font.BOLD, 10));
					g.drawString("$" + buttonPrice[i], button[i].x + itemIn, button[i].y + itemIn + 10);
				}
			}
		}
		
		g.drawImage(Screen.tileset_heart[0], buttonHealth.x, buttonHealth.y, buttonHealth.width, buttonHealth.height, null);
		g.drawImage(Screen.tileset_coin[0], buttonCoins.x, buttonCoins.y, buttonCoins.width, buttonCoins.height, null);
		g.setFont(new Font("Minecraft", Font.BOLD, 14));
		g.setColor(Color.WHITE);
		g.drawString("" + Screen.health, buttonHealth.x + buttonHealth.width + iconSpace, buttonHealth.y + iconTextY);
		g.drawString("" + Screen.coins, buttonCoins.x + buttonCoins.width + iconSpace, buttonCoins.y + iconTextY);
		
		if(holdItem)
		{
			g.drawImage(Screen.tileset_air[heldID], Screen.point.x - ((button[0].width - (itemIn * 2)) / 2) + itemIn, Screen.point.y - ((button[0].width - (itemIn * 2)) / 2) + itemIn, button[0].width - (itemIn * 2), button[0].height - (itemIn * 2), null);
		}
	}
	
}
