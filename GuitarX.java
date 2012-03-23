import java.awt.*;

// TO DO
// Identify notes in a chord

public class GuitarX
{
	// if extended version (notes to be written)
	private static boolean extended;

	// note list
	private static final String[] NOTES= new String[]{"A", "A#/Bb", "B", "C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab"};
	private static final String[] OPENNOTES = new String[]{"E", "B", "G", "D", "A", "E"};

	// string constants
	private static final String
	// filenames
			CHORDFILE = "chordlistx.txt",
			SAVEDIRNAME = "/Chords/",
			SAVEDIRNAMEX = "/ChordsX/",
			ROOTSTRING = "R",
			OPENSTRING = "O",
			CLOSEDSTRING = "X"
			;

	// integer constants
	private static final int
	// guitar drawing constants	
			NUMSTRINGS = 6,
			NUMFRETS = 5,
			NUMFINGERS = 5,
	// time constants
			TIMELAG = 0,
	// note constant
			TOTALNOTES = 12,
			TOTALFRETS = 20
			;
			
	// double constants
	private static final double
	// canvas constants
			XMIN = 0,
			XMAX = 100,
			YMIN = 0,
			YMAX = 100,
	// guitar drawing constants
			NUTWIDTH = 2,
			XSTRINGDIST = 7.5,
			XBOARDMIN = 32,
			XBOARDMAX = XBOARDMIN + (XSTRINGDIST * (NUMSTRINGS - 1)),
			YFRETDIST = 10,
			YBOARDMIN = 32,
			YBOARDMAX = YBOARDMIN + (YFRETDIST * (NUMFRETS)),
			FRETRADIUS = 0.005, 
			DOTRADIUS = 2,
			XFRETNUMBEROFFSET = 2,
			YSTRINGNUMBEROFFSET = 2,
			XFINGERTEXTOFFSET = 3.5,
			YFINGERTEXTOFFSET = 2,
			XNOTETEXTOFFSET = 3.5,
			YNOTETEXTOFFSET = -2,
			YNAMETEXTOFFSET = 20,
			YOPENCLOSEOFFSET = 8
			;
	
	// object instantiations
	private static In in = new In(CHORDFILE);
	private static Draw draw = new Draw();
	
	/*******************************************************************/				

	// find note for a given string and a given fret
	private static String findNote(int string, int fret)
	{
		int fretindex = 0;
		int index = 0;
		int i = 0;
		
		// checks
		if ((string < 1) || (string > NUMSTRINGS)) return "O";
		if ((fret < 0) || (fret > TOTALFRETS)) return "O";
		
		// find initial index of open string note
		for (i = 0; i < TOTALNOTES; i++)
		{
			if (NOTES[i].equals(OPENNOTES[string - 1]))
			{
				index = i;
				break;
			}	
		}
		
		// find note index
		fretindex = 0;
		while (true)
		{
			if (fretindex == fret) return NOTES[index];
			fretindex++;
			index = (index + 1) % TOTALNOTES;
		}
	}

	// draw the 6 strings and 5 frets
	private static void drawBoard(int initFret)
	{
		int i = 0;
		double upperend = (initFret == 1) ? YBOARDMAX + NUTWIDTH : YBOARDMAX; // draw nut or not?
		
		// draw strings
		for (i = 0; i < NUMSTRINGS; i++)
		{
			draw.line(XBOARDMIN + (i * XSTRINGDIST),
						 YBOARDMIN,
						 XBOARDMIN + (i * XSTRINGDIST),
						 upperend);
			draw.text(XBOARDMIN + (i * XSTRINGDIST), upperend + YSTRINGNUMBEROFFSET, Integer.toString(NUMSTRINGS - i));
		}
		
		// draw frets	 
		for (i = 0; i <= NUMFRETS; i++)
		{
			if (i != NUMFRETS) 
				draw.setPenRadius(FRETRADIUS);
				
			draw.line(XBOARDMIN, 
						 YBOARDMIN + (i * YFRETDIST),
						 XBOARDMAX,
						 YBOARDMIN + (i * YFRETDIST));
						 
			draw.setPenRadius();
			if (i != NUMFRETS)			 
				draw.text(XBOARDMAX + XFRETNUMBEROFFSET, YBOARDMIN + (i * YFRETDIST), Integer.toString(NUMFRETS + initFret - i - 1)); 
		}
						 
		// draw nut, if necessary
		if (initFret == 1) 
			draw.line(XBOARDMIN, 
					  YBOARDMAX + NUTWIDTH,
					  XBOARDMAX,
					  YBOARDMAX + NUTWIDTH);
	} // end drawBoard()

	// put a dot for a given finger on a given fret on a given string
	private static void putDot(int initFret, int finger, int fret, int string)
	{
		draw.filledCircle(XBOARDMIN + ((NUMSTRINGS - string) * XSTRINGDIST),
					YBOARDMIN + (YFRETDIST / 2) + ((NUMFRETS + initFret - fret - 1) * YFRETDIST),
					DOTRADIUS);		
		draw.text(XBOARDMIN + ((NUMSTRINGS - string) * XSTRINGDIST) - XFINGERTEXTOFFSET,
				  YBOARDMIN + (YFRETDIST / 2) + ((NUMFRETS + initFret - fret - 1) * YFRETDIST) - YFINGERTEXTOFFSET,
				  Integer.toString(finger));
		if (extended)
			draw.text(XBOARDMIN + ((NUMSTRINGS - string) * XSTRINGDIST) - XNOTETEXTOFFSET,
				  YBOARDMIN + (YFRETDIST / 2) + ((NUMFRETS + initFret - fret - 1) * YFRETDIST) - YNOTETEXTOFFSET,
				  findNote(string, fret));	
	} // end putDot()
	
	// put a barre for a given finger on a given fret
	private static void putBarre(int initFret, int finger, int fret)
	{
		int string_begin = in.readInt();
		int string_end = 0;
		
		if ((string_begin < 1) || (string_begin > 6)) return;
		
		// draw circles for all the required strings
		draw.filledCircle(XBOARDMIN + ((NUMSTRINGS - string_begin) * XSTRINGDIST),
					YBOARDMIN + (YFRETDIST / 2) + ((NUMFRETS + initFret - fret - 1) * YFRETDIST),
					DOTRADIUS);
		if (extended)
			draw.text(XBOARDMIN + ((NUMSTRINGS - string_begin) * XSTRINGDIST) - XNOTETEXTOFFSET,
				  YBOARDMIN + (YFRETDIST / 2) + ((NUMFRETS + initFret - fret - 1) * YFRETDIST) - YNOTETEXTOFFSET,
				  findNote(string_begin, fret));
		while (true)
		{
			int string_temp = in.readInt();
			if (string_temp == 0) break;
			if ((string_temp < 1) || (string_temp > 6)) return;
			string_end = string_temp;
			draw.filledCircle(XBOARDMIN + ((NUMSTRINGS - string_end) * XSTRINGDIST),
					YBOARDMIN + (YFRETDIST / 2) + ((NUMFRETS + initFret - fret - 1) * YFRETDIST),
					DOTRADIUS);
			if (extended)
				draw.text(XBOARDMIN + ((NUMSTRINGS - string_end) * XSTRINGDIST) - XNOTETEXTOFFSET,
				  YBOARDMIN + (YFRETDIST / 2) + ((NUMFRETS + initFret - fret - 1) * YFRETDIST) - YNOTETEXTOFFSET,
				  findNote(string_end, fret));
		}	
		
		// draw barre line
		draw.line(XBOARDMIN + ((NUMSTRINGS - string_begin) * XSTRINGDIST),
				 YBOARDMIN + (YFRETDIST / 2) + ((NUMFRETS + initFret - fret - 1) * YFRETDIST),
				 XBOARDMIN + ((NUMSTRINGS - string_end) * XSTRINGDIST),
				 YBOARDMIN + (YFRETDIST / 2) + ((NUMFRETS + initFret - fret - 1) * YFRETDIST));
				
		// write finger
		draw.text(XBOARDMIN + ((NUMSTRINGS - string_end) * XSTRINGDIST) - XFINGERTEXTOFFSET,
				  YBOARDMIN + (YFRETDIST / 2) + ((NUMFRETS + initFret - fret - 1) * YFRETDIST) - YFINGERTEXTOFFSET,
				  Integer.toString(finger));	
		
	} // end putBarre()


	/*******************************************************************/

	// main() function
	public static void main(String[] args)
	{
		String temp;
		draw.setXscale(XMIN, XMAX);
		draw.setYscale(YMIN, YMAX);
		draw.setPenColor(Color.BLACK);
		
		if ((args.length == 1) && (args[0].equals("X"))) extended = true;
		else extended = false;
		
		while (!in.isEmpty())
		{
			String chordName = in.readString();
			int initFret = in.readInt();
			
			draw.clear();
			
			// draw the fretboard
			drawBoard(initFret);
			
			// write name of chord
			Font f = new Font("SansSerif", Font.PLAIN, 40);
			draw.setFont(f);
			draw.text((XMAX - XMIN) / 2,
					  YBOARDMIN - YNAMETEXTOFFSET,
					  chordName);		
			draw.setFont();			  
			
			// draw fingers
			// format: <finger> <fret> <isbarred?> <string begin> [<strings> ...] <string end> [0]
			for (int i = 1; i <= NUMFINGERS; i++)
			{
				// read in values
				if (in.readInt() != i) return;
				int fret = in.readInt();
				if (fret == 0) continue;
				boolean isBarred = in.readBoolean();
				
				// checks
				if ((fret < initFret) || (fret > initFret + NUMFRETS)) return;
				
				// draw fingering positions
				if (!isBarred)
				{ 
					int string = in.readInt();
					if ((string < 1) || (string > 6)) return;
					putDot(initFret, i, fret, string);
				}
				else 
					putBarre(initFret, i, fret);				
			}
			
			// set root note
			temp = in.readString();
			if (!temp.equals(ROOTSTRING)) return;
			int root_fret = in.readInt();
			if (root_fret != 0)
			{
				int root_string = in.readInt();
				draw.setPenColor(Color.WHITE);
				draw.text(XBOARDMIN + ((NUMSTRINGS - root_string) * XSTRINGDIST),
					YBOARDMIN + (YFRETDIST / 2) + ((NUMFRETS + initFret - root_fret - 1) * YFRETDIST),
					"R");
				draw.setPenColor();
			}
			
			// set open strings
			temp = in.readString();
			if (!temp.equals(OPENSTRING)) return;
			while (true)
			{
				int open_string = in.readInt();
				if (open_string == 0) break;
				draw.text(XBOARDMIN + ((NUMSTRINGS - open_string) * XSTRINGDIST), YBOARDMAX + YOPENCLOSEOFFSET, "O");
			}
			
			// set closed strings
			temp = in.readString();
			if (!temp.equals(CLOSEDSTRING)) return;
			while (true)
			{
				int closed_string = in.readInt();
				if (closed_string == 0) break;
				draw.text(XBOARDMIN + ((NUMSTRINGS - closed_string) * XSTRINGDIST), YBOARDMAX + YOPENCLOSEOFFSET, "X");
				
			}
			
			// save the chord image
			if (extended) draw.save(System.getProperty("user.dir") + SAVEDIRNAMEX, chordName + ".jpg");
			else draw.save(System.getProperty("user.dir") + SAVEDIRNAME, chordName + ".jpg");
			
			draw.show(TIMELAG);
			
		} // end while()
		return;
	} // end main()		
			
} // end class
