///////////////////////////////////////
//--CS 1083 Lab: JavaFile Class--
//Last Edited by: N/A
//Last Edit Date/Time: N/A
//Changelog:
//*Add next editlist here*
///////////////////////////////////////
import java.util.Scanner;
import java.io.*;

class JavaFile
{
 	private String name;
	private String contents;
	
	// Constructor
	public JavaFile(String name)
	{
	 	this.name = name;
		contents = "";
	}

	// Mutator for contents
	public void setContents(String newContents)
	{
		contents = newContents;
	}

	public void save() throws FileNotFoundException
	{
		// Save contents into the javafile
		PrintWriter outFile = new PrintWriter(new File(name));
		outFile.println(contents);
		
		if (outFile != null)
			outFile.close();
	}
	
	// Load the contents of a file into its "contents" instance variable
	public void load() throws Exception
	{
		Scanner scan = new Scanner(new File(name));
	
		while(scan.hasNextLine())
		{
			contents += scan.nextLine() + "\n";
		}
	}
	
	// Returns the contents of a javafile in the project
	// NOTE: Should we load every time in case the file has changed since last load? -Ben
	public String getContents() throws Exception
	{
		if ((contents.equals("")))
		{
			load();
		}
		
		return contents;
	}

	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return name;
	}
}