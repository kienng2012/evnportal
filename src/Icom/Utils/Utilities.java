package Icom.Utils;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

/**
 * This class contains some common methods usually used in your programs.
 */
public class Utilities {

	static FileOutputStream fout = null;

	// A kind of trigger constant, used to display debug info.
	// Set this constant to the value of <code> true </code> for debugging
	static final boolean VERBOSE = true;

	// CONSTRUCTOR
	public Utilities() {
	}

	/**
	 * You want to connect to weblogicServer to lookup any object via its JNDI
	 * name.
	 * 
	 * @return a Context to connect to WeblogicServer
	 */

	/**
	 * This method will close the connection and statement if presented.
	 */
	public void cleanup(Connection con, PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
		}
	}

	public void cleanup(PreparedStatement ps, Statement stmt) {
		try {
			if (ps != null) {
				ps.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
		}
	}

	public void cleanup(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
		}
	}

	public void cleanup(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
		}

	}

	public void closeConnection(Connection connection, Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
		}
	}

	// Question: How can I convert any Java Object into byte array?
	// Answer : Very elegant way I found on SUN's web site:
	public static byte[] getBytes(Object obj) throws java.io.IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		bos.close();
		byte[] data = bos.toByteArray();
		return data;
	}

	// Fast convert a byte array to a hex string
	// with possible leading zero.
	public static String toHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			// look up high nibble char
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			// look up low nibble char
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	// table to convert a nibble to a hex char.
	static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Convert a hex string to a byte array. Permits upper or lower case hex.
	 * 
	 * @param s
	 *            String must have even number of characters. and be formed only
	 *            of digits 0-9 A-F or a-f. No spaces, minus or plus signs.
	 * @return corresponding byte array.
	 */
	public static byte[] fromHexString(String s) {
		int stringLength = s.length();
		if ((stringLength & 0x1) != 0) {
			throw new IllegalArgumentException(
					"fromHexString requires an even number of hex characters");
		}
		byte[] b = new byte[stringLength / 2];

		for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
			int high = charToNibble(s.charAt(i));
			int low = charToNibble(s.charAt(i + 1));
			b[j] = (byte) ((high << 4) | low);
		}
		return b;
	}

	/**
	 * convert a single char to corresponding nibble.
	 * 
	 * @param c
	 *            char to convert. must be 0-9 a-f A-F, no spaces, plus or minus
	 *            signs.
	 * 
	 * @return corresponding integer
	 */
	private static int charToNibble(char c) {
		if ('0' <= c && c <= '9') {
			return c - '0';
		} else if ('a' <= c && c <= 'f') {
			return c - 'a' + 0xa;
		} else if ('A' <= c && c <= 'F') {
			return c - 'A' + 0xa;
		} else {
			throw new IllegalArgumentException("Invalid hex character: " + c);
		}
	}

	// e.g: round(3.1537, 2) --> output is 3.15
	public static double round(double value, int decimalPlace) {
		double power_of_ten = 1;
		while (decimalPlace-- > 0) {
			power_of_ten *= 10.0;
		}
		return Math.round(value * power_of_ten) / power_of_ten;
	}

	public static double roundEx(double value, int decimalPlace) {
		BigDecimal bigValue = new BigDecimal(value);
		bigValue = bigValue.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bigValue.doubleValue();
	}
	static long validdate(String sday, String smonth) {
		try {

			java.util.Calendar calendarcur = java.util.Calendar.getInstance();

			int iday = Integer.parseInt(sday);
			int imonth = Integer.parseInt(smonth) - 1;
			if (iday > 31 && iday < 1) {
				return 0;
			}
			if (imonth > 11 && imonth < 0) {
				return 0;
			}

			java.util.Calendar calendar = java.util.Calendar.getInstance();
			int iyear = calendarcur.get(Calendar.YEAR);
			if (imonth == 0 && iday <= 19) {
				iyear = iyear + 1;
			}
			calendar.set(iyear, imonth, iday);
			return calendar.getTime().getTime();

		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	public static void main(String args[]) {
		// Utilities util = new Utilities();
		
		
		System.out.println("aa:" +validdate("19", "2"));
		System.out.println("aa:" +validdate("22", "12"));
		System.out.println("aa:" +validdate("10", "1"));
		System.out.println("aa:" + "A12345".substring(2));
		
		
	}
}
