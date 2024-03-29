import java.awt.Graphics;


public class Room
{

	public int worldWidth = 12, worldHeight = 8, blockSize = 52;
	
	public Block[][] block;
	
	public Room()
	{
		define();
	}
	
	public void define()
	{
		block = new Block[worldHeight][worldWidth];
		
		for(int y = 0; y < block.length; y++)
		{
			for(int x = 0; x < block[0].length; x++)
			{
				block[y][x] = new Block((Screen.myWidth / 2) - ((worldWidth * blockSize) / 2) + (x * blockSize), y * blockSize, blockSize, blockSize, Value.groundGrass, Value.airAir);
			}
		}
	}
	
	public void physics()
	{
		for(int y = 0; y < block.length; y++)
		{
			for(int x = 0; x < block[0].length; x++)
			{
				block[y][x].physics();
			}
		}
	}
	
	public void paint(Graphics g)
	{
		for(int y = 0; y < block.length; y++)
		{
			for(int x = 0; x < block[0].length; x++)
			{
				block[y][x].paint(g);
			}
		}
		
		for(int y = 0; y < block.length; y++)
		{
			for(int x = 0; x < block[0].length; x++)
			{
				block[y][x].fight(g);
			}
		}
	}
	
}
