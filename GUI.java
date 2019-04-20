import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import java.io.*;
import java.util.Optional;
import javafx.stage.WindowEvent;

public class GUI extends Application
{
   // Declaration of the controls needed
   Button compileButton;
	Button runButton;
	Button saveButton;
   static TextArea mainText;
   
   static String prevOutput = "";
   static Label outputLabel;
   static ScrollPane sp; 
	static ScrollPane tp;
	
   ChoiceBox<String> fileTab;
   // Space needed to fix initial button spacing
	String[] fileOptions = {"File                ", "New Project", "New", "Add", "Delete"};
	
	ChoiceBox<String> openTab;
	String[] openOptions = {"Open"};
	
	// Background code to drive
	Project proj = new Project("");
   boolean flag = false;
   
   Stage fileStage;

   public void start(Stage primaryStage) throws Exception
   {
      // code to indicate what the window will look like
      Font mainFont = new Font("courrier", 24);
      
      // File tab aesthetics, set options, initial selection.
		fileTab = new ChoiceBox<String>();
      fileTab.setStyle("-fx-font: 24px \"Courrier\";");  
      fileTab.getItems().addAll(fileOptions);
      fileTab.getSelectionModel().select(0);

      // openTab aesthetics, set options, initial selection.
		openTab = new ChoiceBox<String>();
      openTab.setStyle("-fx-font: 24px \"Courrier\";");  
      openTab.getItems().addAll(openOptions);
      openTab.getSelectionModel().select(0);

      // Main text area layout
		mainText = new TextArea();
      mainText.setFont(mainFont);
     // mainText.setPrefRowCount(15);
     // mainText.setPrefColumnCount(50);
      mainText.setWrapText(true);
		mainText.setMaxHeight(300);
		mainText.setMaxWidth(1000);

      // Create scrollable output area 
      sp = new ScrollPane();
      sp.setContent(outputLabel);
		tp = new ScrollPane();
      tp.setContent(mainText);
      
      // Setup initial output
		outputLabel = new Label("To begin, create a new project.");
      outputLabel.setFont(mainFont);
		outputLabel.setWrapText(true);
		
		// Call method when window is closed
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			public void handle(WindowEvent we) 
			{
				try
				{
		
			      if (!mainText.getText().equals(proj.getJavaFile(proj.getCurrentFile()).getContents()))
			      {
				      Alert alert = new Alert(AlertType.CONFIRMATION, 
				                     "Do you want to save this file?" , 
				                     ButtonType.YES, ButtonType.NO);
				      alert.showAndWait();
			
				      if (alert.getResult() == ButtonType.YES)
				      {
				        proj.getJavaFile(proj.getCurrentFile()).setContents(mainText.getText());
						  proj.getJavaFile((proj.getCurrentFile())).save();
						}
	      		}
     			}
			   catch (Exception e)
			   {
			  		output("Error saving file before close");
			   }
	   
			}
		});
      
      // Call handle method when fileTab is used
      fileTab.setOnAction(new EventHandler<ActionEvent>() 
      {
         public void handle(ActionEvent e) 
         {
            processfileTab(e);
         }
      });
        
      // Call handle method when the open tab is used
		openTab.setOnAction(new EventHandler<ActionEvent>() 
		{
         public void handle(ActionEvent e) 
         {
            processopenTab(e);
         }
      });

      // Create compile button
      compileButton = new Button("Compile");
      compileButton.setFont(mainFont);

      // When the compile button is clicked
      compileButton.setOnAction(new EventHandler<ActionEvent>() 
      {
         public void handle(ActionEvent e) 
         {
            processCompileButton(e);
         }
      });
        
      // Create run button
		runButton = new Button("Run");
      runButton.setFont(mainFont);

      // When the run button is clicked
      runButton.setOnAction(new EventHandler<ActionEvent>() 
      {
         public void handle(ActionEvent e) 
         {
            processRunButton(e);
         }
      });

      // Create Save button
		saveButton = new Button("Save");
      saveButton.setFont(mainFont);

      // When the Save button is clicked
      saveButton.setOnAction(new EventHandler<ActionEvent>() 
      {
         public void handle(ActionEvent e) 
         {
            processSaveButton(e);
         }
      });
      
      // Create main gridpane
		GridPane outerGrid = new GridPane();
		outerGrid.add(fileTab, 0, 0, 3, 1);
		outerGrid.add(openTab, 3, 0);
		outerGrid.add(compileButton, 4, 0);
		outerGrid.add(runButton, 5, 0);
		outerGrid.add(saveButton, 6, 0);
      outerGrid.add(mainText, 0, 3, 20, 4);
      outerGrid.add(sp, 0, 7, 20, 3);

      // Create Scene and set size of window
      Scene theScene = new Scene(outerGrid, 1200, 800);
      primaryStage.setTitle("Hutt");
      primaryStage.setScene(theScene);
      primaryStage.show();
   }
   
   // When the run button is clicked
   public void processRunButton(ActionEvent event)
   {
      // code from driver: 
      try
      {
      proj.run();
      }
		catch (IOException e)
		{
			Alert error = new Alert(AlertType.ERROR, e.getMessage() , ButtonType.OK);
         error.showAndWait();
		}
      catch (Exception exc)
      {
         //GUI.output("Run Error. See run method in project class");
			Alert error = new Alert(AlertType.ERROR, "Error: Run() in Project.java" , ButtonType.OK);
         error.showAndWait();	
      }
   }
   
   // When the Save button is clicked
   public void processSaveButton(ActionEvent event)
   {
      int fileIndex = openTab.getSelectionModel().getSelectedIndex()-1;
      JavaFile file = proj.getJavaFile(fileIndex);
      file.setContents(mainText.getText());

      try
      {
         file.save();
         output("Saved file");
      }
      catch(FileNotFoundException e)
      {
         output("File not found... Cannot save");
      }
   }
   
   // When the compile button is clicked
	public void processCompileButton(ActionEvent event)
   {  
      // Try to compile project
      try
      {
         proj.compile();
      }
      catch (Exception exc)
      {
         // If there is a compile error display this message
         Alert error = new Alert(AlertType.ERROR, "Error processing compile command" , ButtonType.OK);
         error.showAndWait();
      }
   }
   
   // When a fileTab option is clicked
	public void processfileTab(ActionEvent event)
	{	
      // if "new project" is clicked
		if (fileTab.getSelectionModel().getSelectedIndex() == 1)
		{
         // Create new project
			// make popup to take name of proj
			TextInputDialog dialog = new TextInputDialog();
      	dialog.setTitle("New Project");
      	dialog.setHeaderText("Enter the name of your project");
         Optional<String> result = dialog.showAndWait();
         if (result.isPresent())
         {
            String input = dialog.getEditor().getText();
				
				if (input.equals(""))
				{
					input = "Project";
				}
            
            proj = new Project(input);
            flag = true;
               
            output("\nNew project created: " + input + "\n");
            
            // Add setDisable(false) lines for every control that relies on a project.
            // Also add setDisable(true) to each of the instantiations of those controls.
         }
      }
      else if (flag && fileTab.getSelectionModel().getSelectedIndex() == 2)
      {
         // new file is selected
         // This function has not been implemented yet
			
			TextInputDialog dialog = new TextInputDialog();
      	dialog.setTitle("New Project");
      	dialog.setHeaderText("Enter the name of your new file (With extension)");
         Optional<String> result = dialog.showAndWait();
         if (result.isPresent())
         {
            String input = dialog.getEditor().getText();
				
				if (input.equals(""))
				{
					input = "File";
				}

				JavaFile newFile = new JavaFile(input);
				newFile.setContents("//New File");
            proj.addFile(newFile);
				
				openTab.getItems().add(newFile.getName());
            output("\nAdded file: " + newFile + "\n");
         }		
      }
      else if (flag && fileTab.getSelectionModel().getSelectedIndex() == 3)
      {
         // add file is selected
         // When adding a new file to open tab, use this code: filesChoice.getItems().add(newFileName); 
         
         // Implement code from driver: 
         // make popup to take name of proj
         
            File direct = new File(System.getProperty("user.dir"));
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            fileChooser.setInitialDirectory(direct);
            File selectedFile = fileChooser.showOpenDialog(fileStage);

            JavaFile newFile = new JavaFile(selectedFile.getAbsolutePath());
            proj.addFile(newFile);
				String openString = selectedFile.getAbsolutePath().substring(selectedFile.getAbsolutePath().lastIndexOf("\\")+1, selectedFile.getAbsolutePath().indexOf("."));
            openTab.getItems().add(openString);
            
            output("\nAdded file: " + openString + "\n");
         //}
         
      }
      else if (flag && fileTab.getSelectionModel().getSelectedIndex() == 4)
      {
         // This control should be permanently disabled

         // delete file is selected
         TextInputDialog dialog = new TextInputDialog();
      	dialog.setTitle("Delete File");
      	dialog.setHeaderText("Enter the name of the file to delete: ");
         Optional<String> result = dialog.showAndWait();
         if (result.isPresent())
         {
            String fileName = dialog.getEditor().getText();
				
				try
				{
            	int index = proj.removeFile(fileName);
				
	            // Remove file from openTab by shifting others on top of it
	            try
	            {
	               openTab.getItems().remove(index+1);
	            }
	            catch( Exception e)
	            {
	               //GUI.output("\nError removing from openTab");
						Alert error = new Alert(AlertType.ERROR, "Error: Cannot remove from OpenTab (File may have been removed in background" , ButtonType.OK);
	         		error.showAndWait();	
	            }
				}
				catch (Exception e)
				{
					// Error removing from array
					Alert error = new Alert(AlertType.ERROR, "Error: Error removing file" , ButtonType.OK);
         		error.showAndWait();	
				}
         }
      }
      else if(!flag)
      {
         //GUI.output("\nCreate project first");
			Alert error = new Alert(AlertType.ERROR, "Error: Must create project before other actions" , ButtonType.OK);
         error.showAndWait();	
      }
	}
   
   // When an option in the open tab is clicked
	public void processopenTab(ActionEvent event)
	{
      // first check if the content of the text area matches the content of the file. 
      // If it does not, ask the user if he wants to save the file first. If the answer is 
		try
		{
		
	      if (!mainText.getText().equals(proj.getJavaFile(proj.getCurrentFile()).getContents()))
	      {
		      Alert alert = new Alert(AlertType.CONFIRMATION, 
		                     "Do you want to save this file?" , 
		                     ButtonType.YES, ButtonType.NO);
		      alert.showAndWait();
		
		      if (alert.getResult() == ButtonType.YES)
		      {
		        proj.getJavaFile(proj.getCurrentFile()).setContents(mainText.getText());
				  proj.getJavaFile((proj.getCurrentFile())).save();
				}
	      }
     }
	  catch (Exception e)
	  {
	  	//output("Error saving file before close");
	  }
	   
      // This method should show the contents of the selected file
      try
      {
         String fileName = openTab.getSelectionModel().getSelectedItem();
         int fileIndex = openTab.getSelectionModel().getSelectedIndex();
         //GUI.output("\nFileName= "+ fileName + "\nFileIndex= " + fileIndex);   
			proj.readFile(fileName, fileIndex-1);
			proj.setCurrentFile(fileIndex-1);
      }
		catch (IndexOutOfBoundsException e)
		{
			//GUI.output("\nOut of bounds in Project->readFile()");
			//GUI.output("\nIndex used: " + index + "\nMaxIndex: " + count);
			Alert error = new Alert(AlertType.ERROR, "Error: Index out of bounds" , ButtonType.OK);
         error.showAndWait();	
		}
      catch (Exception e)
      {
         //GUI.output("\nError opening file \n");
			Alert error = new Alert(AlertType.ERROR, "Error: Cannot open file" , ButtonType.OK);
         error.showAndWait();	
      }
	}

   public static void output(String newOutput)
   {
      prevOutput += newOutput;
      outputLabel.setText(prevOutput);
      sp.setContent(outputLabel);
   }
	
	public static void fileText(String fileContents)
	{
		mainText.setText(fileContents);
      tp.setContent(mainText);
	}
	
   public static void main(String[] args)
   {
      launch(args);
   }
}