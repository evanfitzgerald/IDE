///////////////////////////////////////
//--CS 1083 Lab: Project Class--
//Last Edited by: N/A
//Last Edit Date/Time: N/A
//Changelog:
//*Add next editlist here*
///////////////////////////////////////
import java.util.Scanner;
import java.io.IOException;

class Project
{
	private String name;
	private JavaFile[] files;
	private int count;
	private int currentFile; 
	
	// Constructor
	public Project(String name)
	{
	 	this.name = name;
		files = new JavaFile[1];
		count = 0;
		currentFile = -1;
	}
	
	public int getCurrentFile()
	{
		return currentFile;
	}
	
	public void setCurrentFile(int newFile)
	{
		currentFile = newFile;
	}


	public JavaFile getJavaFile(int index)
	{
		return files[index];
	}
	
	public void addFile(JavaFile newFile)
	{
		// if the array is full, double its size
		if (files.length == count)
		{
		 	JavaFile[] temp = new JavaFile[files.length * 2];
			for(int i=0; i < files.length; i++)
			{
			 	temp[i] = files[i];
			}
			
			files = temp;
		}
		
		// add new file to array 
		files[count] = newFile;
		count++;
	}

	// Searches an array of files based on the name and returns the index of the file
	public int search(String name)
	{
		boolean found = false;
		int i=0;

		while(i<count && !found)
		{
			if (name.equals(files[i].getName()))
			{
				found = true;
			}
			else
				i++;
		}

		if (found)
			return i;
		else
			// Didnt find file with passed name
			return -1;
	}

	// Remove a file from an array of files based on the name
	public int removeFile(String fileName) throws IndexOutOfBoundsException, Exception
	{
		// Find the index of the given file
		int index = search(fileName);
		// remove the index

		if (index != -1)
		{
			// Shift files up to replace the removed file
			for (int i=index; i<count-1; i++)
				files[i] = files[i+1];
			
			count--;
		}
		else
			GUI.output("\nFile not found");

		return index;
	}
	
	// Read and print the contents of a given file
	public void readFile(String fileName, int index) throws IndexOutOfBoundsException, Exception
	{
		// use the choicebox index as the index of the array (cindex-1 = projindex)
		GUI.fileText(files[index].getContents());
	}
	
	// Print a list of the files in the current project
	public void printFiles()
	{
		System.out.println("\nProject name: " + name);
		System.out.println("-----------------------");
		System.out.println("File names: ");
		
	 	for(int i=0; i<count; i++)
			System.out.println("\t"+files[i]);
	}
	
	// Compile all files in the project
	public void compile() throws Exception
	{
		boolean flag = true;
		int i = 0;
		while(flag && i < count)
		{
			Process p1 = Runtime.getRuntime().exec("javac " + files[i].getName());
			Scanner scan = new Scanner(p1.getErrorStream());
			GUI.output("\n\nCompiling "+ files[i].getName());
			if (scan.hasNextLine())
			{
				while(scan.hasNextLine())
				{
					GUI.output(scan.nextLine() + "\n");
				}
				flag = false;
			}
			else
			{
				GUI.output("\nCompiled "+ files[i].getName()+" successfully");
			}
			i++;	
		}
	}
	
	//Run the last file in array
	public void run() throws IOException, Exception
	{
		//int nameLength = files[count-1].getName().length();
		Process p1 = Runtime.getRuntime().exec("java " + name);
		Scanner scan = new Scanner(p1.getInputStream());
		Scanner err = new Scanner(p1.getErrorStream());

		if (err.hasNextLine())
		{
			throw new IOException("Must compile first");
			
		}
		else
		{
			GUI.output("\n");
			while(scan.hasNextLine())
			{
				GUI.output("\n" + scan.nextLine());
			}
			GUI.output("\n");	
		}
				
	}
}