package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.dao.DbConnection;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;


public class ProjectsApp {
 private Scanner scanner = new Scanner(System.in);
 private ProjectService projectService = new ProjectService();
private Project curProject;
 
 //@formatter:off
 private List<String> operations = List.of( 
	"1) Add a project",
	 "2) List Project", 
	  "3) Select a project"
	
);
//@formatter:on
 
 
 public static void main(String[] args) {
	 new ProjectsApp().processUserSelections();
    DbConnection.getConnection();
  }
 private void processUserSelections() { 
	boolean done = false;
	
	while(!done) {
		try {
	    int selection = getUserSelection();
        
        
      
        switch (selection) { 
        case -1:
        done = exitMenu();
        break; 
        
        case 1:
        createProject();
         break;  
         
        case 2:
        listProjects();
        break;
        
        case 3:
        selectProject();
        break;
       
         
         default:
        	 System.out.println("\n" + selection + "is not valid. try again");
        	 
        
        
        }
        }catch(Exception e) { 
        System.out.println("\nError: " + e + " Try again.");
        	
        }
		}
 }


private void selectProject() {
listProjects();
Integer projectId = getIntInput("Enter a project ID to select a project"); 

/* Unselect the current project. */
 curProject = null; 
	
	/* This will throw an exception if an invalid project ID is entered. */
	curProject = projectService.fetchProjectById(projectId);

}



private int getUserSelection() {
	printOperations();
	
	Integer input = getIntInput("Enter a menu selection");
	
	return Objects.isNull(input) ? -1 : input;
}
private void createProject() {
  String projectName = getStringInput("Enter the project name");
 BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
 BigDecimal actualHours = getDecimalInput("Enter the actual hours");
 Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
 String notes = getStringInput("Enter the Project notes"); 
 
 Project project = new Project(); 
 
  project.setProjectName(projectName);
  project.setEstimatedHours(estimatedHours);
  project.setActualHours(actualHours);
  project.setDifficulty(difficulty);
  project.setNotes(notes); 
 
  Project dbproject = projectService.addProject(project);
  System.out.println("You have sucessfully created a project: " + dbproject);

   
	
} 

private void listProjects() {
 List<Project> projects = projectService.fetchAllProjects();
  
 System.out.println("\nProjects:");
 
 projects.forEach(project -> System.out.println
		 (" " + project.getProjectId()
		 + ": " + project.getProjectName()));
}

private BigDecimal getDecimalInput(String prompt) {
String input = getStringInput(prompt);

if(Objects.isNull(input)) {
return null;
}

try {
return new BigDecimal (input).setScale(2);

}

catch(NumberFormatException e) {
 throw new DbException(input + "is not a valid decimal number.");
}
}

private boolean exitMenu() {
	System.out.println("\nexitingthemenu. TTFN");
			
	return true;
}
private Integer getIntInput(String prompt) {
	String input = getStringInput(prompt);
	
	if(Objects.isNull(input)) {
	return null; 
	} 
	
	try {
		   return Integer.valueOf(input);
		} 
		 catch(NumberFormatException e) {
		  throw new DbException(input + " is not a valid number.");
		 }
	
}
private Double getDoubleInput(String prompt) {
	String input = getStringInput(prompt);
	if(Objects.isNull(input)) { 
	return null;
	}
	
	try {
	   return Double.parseDouble(input);
	} 
	 catch(NumberFormatException e) {
	  throw new DbException(input + " is not a valid number.");
	 }
}
private String getStringInput(String prompt) {
	System.out.print(prompt + ": ");
	String input = scanner.nextLine(); 
	
	return input.isBlank() ? null : input.trim();
}
private void printOperations() {
	System.out.println();
	System.out.println ("\nThese are the available selections. Press the enter key to quit");
	
	
operations.forEach(line -> System.out.println(" " + line)); 

if(Objects.isNull(curProject)) { 
	System.out.println ("\nYou are not working with a project");
		
}
else { 
	System.out.println ("\nYou are working with project: " + curProject);
}
}

}


