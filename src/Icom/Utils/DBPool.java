package Icom.Utils;


import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBPool
{
	private DBPool(){
		
	}
	
	private static DBPool mInstance;
	private static Object mLock = new Object();
	
	public static DBPool getInstance(){
		synchronized (mLock) {
			if(mInstance == null){
				mInstance = new DBPool();
			}
			return mInstance;
		}
	}
  private BoneCP connectionPoolSub = null;
  
  public void ConfigDB()
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
//    	Class.forName ("oracle.jdbc.driver.OracleDriver");
//      Class.forName ("oracle.jdbc.OracleDriver");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Utils.logger.info("Exception load configDB()" + e.getMessage());
      return;
    }
    try
    {
      Utils.logger.info("BoneCpDBHelper -> loadConfigDb()");
      
//      String fileConfig = "bonecp-config.xml";
      String fileConfig = Config.getInstance().getProperty("PATH_DB_CONFIG");
      
      InputStream inputStream = new FileInputStream(fileConfig);
      BoneCPConfig configSub = new BoneCPConfig(inputStream, "sub");
      connectionPoolSub = new BoneCP(configSub);
      inputStream.close();
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Utils.logger.info("Exception at load BoneCpDBHelper" + e.getMessage());
    }
  }
  
  public Connection getConnectionSub()
  {
    try
    {
      return connectionPoolSub.getConnection();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      Utils.logger.info("Exception at getConnectionSub" + e.getMessage());
    }
    return null;
  }
  
  public void cleanup(Statement statement) {
    try{
      if (statement != null) {
        statement.close();
      }
    }catch (SQLException e){
      Utils.logger.info("Exception at cleanup statement" + e.getMessage());
    }catch (Exception ex){
      Utils.logger.info("Exception at cleanup statement" + ex.getMessage());
    }
  }
  
  public void cleanup(PreparedStatement statement){
    try{
      if (statement != null) {
        statement.close();
      }
    }catch (SQLException e){
      Utils.logger.info("Exception at cleanup PrepareStatement" + e.getMessage());
    } catch (Exception ex){
      Utils.logger.info("Exception at cleanup PrepareStatement" + ex.getMessage());
    }
  }
  
  public void cleanup(ResultSet rs)
  {
    try
    {
      if (rs != null) {
        rs.close();
      }
    }
    catch (SQLException e)
    {
      Utils.logger.info("Exception at cleanup ResultSet" + e.getMessage());
    }
    catch (Exception e)
    {
      Utils.logger.info("Exception at cleanup ResultSet" + e.getMessage());
    }
  }
  
  public void cleanup(ResultSet rs, PreparedStatement statement)
  {
    cleanup(rs);
    cleanup(statement);
  }
  
  public void cleanup(ResultSet rs, CallableStatement statement)
  {
    cleanup(rs);
    cleanup(statement);
  }
  
  public void cleanup(Connection con)
  {
    try
    {
      if (con != null) {
        con.close();
      }
    }
    catch (SQLException e)
    {
      Utils.logger.info("Exception at cleanup Connection" + e.getMessage());
    }
    catch (Exception e)
    {
      Utils.logger.info("Exception at cleanup Connection" + e.getMessage());
    }
  }
  
  public void cleanup(Connection con, PreparedStatement statement)
  {
    cleanup(statement);
    cleanup(con);
  }
}
