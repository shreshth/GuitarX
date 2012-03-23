public class Temp
{
	public static void main(String[] args)
	{
		//findNote(int string, int fret)
		System.out.println(" " + Integer.parseInt(args[0]) + " " + Integer.parseInt(args[1]));
		System.out.println(GuitarX.findNote(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
		
	}
}