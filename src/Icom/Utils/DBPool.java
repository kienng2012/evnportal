package Icom.Utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DBPool {
	private static BoneCP connectionPoolGW = null;
	private static BoneCP connectionPoolSub = null;
	private static BoneCP connectionPoolLog = null;

	public DBPool() {

	}

	public static void ConfigDB() {
		try {
			// load the database driver (make sure this is in your classpath!)
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		try {
			Utils.logger.info("BoneCpDBHelper -> loadConfigDb()");

			String fileConfig = "bonecp-config.xml";

			InputStream inputStream = new FileInputStream(fileConfig);

			BoneCPConfig configGW = new BoneCPConfig(inputStream, Constants.POOLNAME_GATEWAY);
			connectionPoolGW = new BoneCP(configGW);
			inputStream.close();

			inputStream = new FileInputStream(fileConfig);
			BoneCPConfig configSub = new BoneCPConfig(inputStream, Constants.POOLNAME_SUB);
			connectionPoolSub = new BoneCP(configSub);
			inputStream.close();

			inputStream = new FileInputStream(fileConfig);
			BoneCPConfig configNguoc = new BoneCPConfig(inputStream, Constants.POOLNAME_LOG);
			connectionPoolLog = new BoneCP(configNguoc);
			inputStream.close();
			
			Utils.logger.info("BoneCpDBHelper -> loadConfigDb success");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnectionByPool(String poolName) {
		if (poolName.equalsIgnoreCase(Constants.POOLNAME_GATEWAY)) {
			return getConnectionGateway();
		}
		if (poolName.equalsIgnoreCase(Constants.POOLNAME_SUB)) {
			return getConnectionSub();
		}
		if (poolName.equalsIgnoreCase(Constants.POOLNAME_LOG)) {
			return getConnectionLog();
		} else {
			return getConnectionGateway();
		}
	}

	public static Connection getConnectionGateway() {
		try {
			return connectionPoolGW.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getConnectionSub() {
		try {
			return connectionPoolSub.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getConnectionLog() {
		try {
			return connectionPoolLog.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getConnectionSucriber(){
		try {
			return connectionPoolSub.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void cleanup(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
		} catch (Exception ex) {
		}
	}

	public static void cleanup(PreparedStatement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
		} catch (Exception ex) {
		}
	}

	public static void cleanup(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
		} catch (Exception e) {
		}
	}

	public static void cleanup(ResultSet rs, PreparedStatement statement) {
		cleanup(rs);
		cleanup(statement);
	}

	public static void cleanup(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
		} catch (Exception e) {
		}
	}

	public static void cleanup(Connection con, PreparedStatement statement) {
		cleanup(statement);
		cleanup(con);
	}
}
