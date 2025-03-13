package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import projects.entity.Project;
import projects.exception.DbException;
import provided.util.DaoBase;

public class ProjectDao extends DaoBase{
 private static final String CATEGORY_TABLE = "category";
 private static final String MATERIAL_TABLE = "material";
 private static final String PROJECT_TABLE = "project";
 private static final String PROJECT_CATEGORY_TABLE = "project_category";
 private static final String STEP_TABLE = "step";
 

	public Project insertProject(Project project) {
		//@formatter:off
	      String sql = ""
	      + "INSERT INTO " + PROJECT_TABLE + " "
	      + "(project_name, estimated_hours, actual hours, diffculty, notes) "
	      + "VALUES "
	      +"(?,?,?,?,?)";
	    //@formatter:on 
        System.out.println(sql);     
	      try (Connection conn = DbConnection.getConnection()) { 
		        startTransaction(conn); 
		        
		        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
		        setParameter(stmt, 1, project.getProjectName(), String.class);
		        setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
		        setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
		        setParameter(stmt, 4, project.getDifficulty(), Integer.class);
		        setParameter(stmt, 5, project.getNotes(), String.class);
		        
		        stmt.executeUpdate(); 
		        System.out.println("line 40");
		        Integer projectId = getLastInsertId(conn, PROJECT_TABLE);
		        commitTransaction(conn); 
		        System.out.println("line 43");
		        project.setProjectId(projectId);
		        return project;
		        } 
		        catch(Exception e) {
		          rollbackTransaction(conn);
		          throw new DbException(e);
		        
		       } 
		      
		    }
	      catch(SQLException e) {
	      throw new DbException(e);
	      }
	    
	      
	      	
	}

}
