package Icom.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Icom.ActiveMQ.MsgQueue;
import Icom.Entities.RequestDTO;
import Icom.Entities.MTQueueDTO;
import Icom.Process.Main;
import Icom.Utils.DBPool;
import Icom.Utils.TextConverter;
import Icom.Utils.Utils;

public class MTQueueDao extends Thread {
	MsgQueue queue;
	private String className = "MTQueueDao";
	private int processIndex;
	private int processNum;
	private String tableName;
	public MTQueueDao(MsgQueue queue,int index,int totalNum,String table)
	{
		this.queue=queue;
		this.processIndex=index;
		this.processNum=totalNum;
		this.tableName=table;
	}	
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public int getProcessIndex() {
		return processIndex;
	}

	public void setProcessIndex(int processIndex) {
		this.processIndex = processIndex;
	}

	public int getProcessNum() {
		return processNum;
	}

	public void setProcessNum(int processNum) {
		this.processNum = processNum;
	}

	public void run() {
		Utils.logger.info(className + " " + processIndex + " start");
		
		String SQL_LOAD = "select * from " + tableName + " where (mod(id," + processNum + ")=" + processIndex + ")" + " limit 1000";
		Utils.logger.info("MTQueueDao @sql:" + SQL_LOAD);

		while (Main.getData) {
			java.sql.Connection conn = null;
			ResultSet rs = null;
			PreparedStatement stmt = null;

			try {
				conn = DBPool.getConnectionSucriber();
				if (conn != null) {
					stmt = conn.prepareStatement(SQL_LOAD, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

					if (stmt.execute()) {
						rs = stmt.getResultSet();
						
						while (rs.next()) {
							
							MTQueueDTO mtObject = new MTQueueDTO();
							mtObject.setId(rs.getInt("ID"));
							mtObject.setUserId(rs.getString("USER_ID"));
							mtObject.setServiceId(rs.getString("SERVICE_ID"));
							mtObject.setMobileOperator(rs.getString("MOBILE_OPERATOR"));
							mtObject.setCommandCode(rs.getString("COMMAND_CODE"));
							mtObject.setContentType(rs.getInt("CONTENT_TYPE"));
							
							String info = rs.getString("INFO").trim();
							info = info.replaceAll("`", "");
							info = info.replaceAll("#", "");
							info = info.replaceAll("&", "_");
							boolean match = info.matches("[\\p{ASCII}]*");
							if(!match){
//								Utils.logger.info(className + " NOT MATCH REGEX UNICODE :[\\p{ASCII}]* ");
//								info = TextConverter.removeAccent(info);
								//remove unicode msg
								info = TextConverter.unAccent(info);
							}
							
							mtObject.setInfo(info);
							mtObject.setMessageType(rs.getInt("MESSAGE_TYPE"));
							mtObject.setRequestId(rs.getString("REQUEST_ID"));
							mtObject.setChannelType(rs.getInt("CHANNEL_TYPE"));
							mtObject.setAmount(0);
							mtObject.setSumitDate(rs.getTimestamp("SUBMIT_DATE"));
							mtObject.setDoneDate(rs.getTimestamp("DONE_DATE"));
							mtObject.setCpId(rs.getString("CPId"));
							mtObject.setMd5(rs.getString("MD5"));
							mtObject.setCountSms(rs.getInt("COUNT_SMS"));
							mtObject.setMsgId(rs.getString("MSGID"));
							mtObject.setAmountSale(rs.getInt("AMOUNT_SALE"));
							mtObject.setAmountPurchase(rs.getInt("AMOUNT_PURCHASE"));
							
							try {
								
								rs.deleteRow();
								
								RequestDTO request = new RequestDTO(mtObject.getMsgId(), Utils.getInstance().getMD5(info), new Timestamp(System.currentTimeMillis()), mtObject.getUserId());
								if(request.check24Hour()){
									
									MTQueueDTO msgObj = new MTQueueDTO(mtObject);
									msgObj.setProcessResult(305);
									Main.queueLog.add(msgObj);
									Utils.logger.info(className + " Send the same content in short time @msisdn="+msgObj.getUserId() + ", serviceId=" + msgObj.getServiceId() + ", content=" + msgObj.getInfo());
									
			    	    		}else if(info.equalsIgnoreCase("null") && info.length() == 0){
			    	    			
			    	    			MTQueueDTO msgObj = new MTQueueDTO(mtObject);
									msgObj.setProcessResult(305);
									Main.queueLog.add(msgObj);
									Utils.logger.info(className + " Send the null content @msisdn="+msgObj.getUserId() + ", serviceId=" + msgObj.getServiceId() + ", content=" + msgObj.getInfo());
			    	    		}
								else{
			    	    			if(BlacklistDAO.getInstance().checkBlacklist(mtObject.getUserId(), mtObject.getServiceId(), Main.lstBlackList)){
										Utils.logger.info(className + " @msisdn: "+mtObject.getUserId()+" in blacklist");
									}else{
										Utils.logger.info(className + " add to queue @msisdn="+mtObject.getUserId() + ", serviceId="+mtObject.getServiceId() +", operator="+mtObject.getMobileOperator()+", info="+mtObject.getInfo() + ", queue size="+Main.msgQueue.getSize());
										queue.add(mtObject);
										RequestDTO.updateListMsisdn(request, mtObject);
									}
			    	    		}					

							} catch (SQLException ex) {
								Utils.logger.error("@MOQueueDAO error at function getMoqueue: " + ex.getMessage());
								Utils.logger.printStackTrace(ex);
							} catch (Exception ex1) {
								Utils.logger.error("@MOQueueDAO error at function getMoqueue: " + ex1.getMessage());
								Utils.logger.printStackTrace(ex1);
							}
						}
					}
				} else {
					Utils.logger.error("@LoadMTOnline.LoadMT. Connection is null.");
				}
			} catch (SQLException ex3) {
				Utils.logger.error("@INProcess@LoadMT.SQLException:" + ex3.toString());
			} catch (Exception ex3) {
				Utils.logger.error("@INProcess@LoadMT.Exception:" + ex3.toString());
			} finally {
				DBPool.cleanup(rs);
				DBPool.cleanup(conn, stmt);
			}
			
			// cho ung dung ngu 1/2 giay
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/*
	public static void main (String [] args)
	{
		String info ="Dien luc Nghi Loc du kien ngung cap dien Trạm 3 Bác Yen (KF) tu 5h0_14/07/2016 den 18h0_14/07/2016 de Sua chua luoi dien . Tran trong";
		boolean match = info.matches("[\\p{ASCII}]*");
		if(!match)
		{
			System.out.println("NOT PASS");
			String info1 = TextConverter.removeAccent(info);
			String info2 = TextConverter.unAccent(info);
			
			System.out.println(info1);
			System.out.println(info2);
			
		}
		System.out.println(info);
		
	}
	*/
	
	public static  String getInsertPlaceholders(int placeholderCount) {
        final StringBuilder builder = new StringBuilder("(");
        for (int i = 0; i < placeholderCount; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append("?");
        }
        return builder.append(")").toString();
    }
	
	public static String getQueryMTLog(long iteration_count) {
		
		DateFormat simple = new SimpleDateFormat("yyyyMMdd");
		String tableName = "mt" + simple.format(new Date());
		
        StringBuilder builder = new StringBuilder("INSERT INTO "
                + tableName + "(`USER_ID`,`SERVICE_ID`,`MOBILE_OPERATOR`,`COMMAND_CODE`,`CONTENT_TYPE`,`INFO`,`SUBMIT_DATE`,`DONE_DATE`,`PROCESS_RESULT`,`MESSAGE_TYPE`,`REQUEST_ID`,`MESSAGE_ID`, NOTES, `CPId`,`COUNT_SMS`,`MSGID`, MD5, AMOUNT_SALE, AMOUNT_PURCHASE)  VALUES");
        					  //(USER_ID,SERVICE_ID,MOBILE_OPERATOR,COMMAND_CODE,CONTENT_TYPE,INFO,MESSAGE_TYPE,CHANNEL_TYPE,AMOUNT,SUBMIT_DATE,INSERT_DATE, MT_TYPE, MO_CONTENT, CPID)
        int COLUMN_COUNT = 19;
        String placeholders = getInsertPlaceholders(COLUMN_COUNT);
        for (int i = 0; i < iteration_count; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(placeholders);
        }
        return builder.toString();
    }
	
	public static boolean writeBatchMTLog(ArrayList<MTQueueDTO> messages, String sSQLInsert) {
		PreparedStatement statement = null;
		Connection connection = null;		
		
		try {
			connection = DBPool.getConnectionLog();
			statement = connection.prepareStatement(sSQLInsert);
			
			int parameterIndex = 1;
            for (MTQueueDTO message : messages) {
            	
            	/*StringBuilder builder = new StringBuilder("INSERT INTO "
                        + tableName + "(`USER_ID`,`SERVICE_ID`,`MOBILE_OPERATOR`,`COMMAND_CODE`,`CONTENT_TYPE`,`INFO`,`SUBMIT_DATE`,`DONE_DATE`,
                        `PROCESS_RESULT`, `MESSAGE_TYPE`,`REQUEST_ID`,`MESSAGE_ID`,`CPId`,`COUNT_SMS`,`MSGID`)  VALUES");
            	*/
            	statement.setString(parameterIndex++, message.getUserId());
            	statement.setString(parameterIndex++, message.getServiceId());
            	statement.setString(parameterIndex++, message.getMobileOperator());
            	statement.setString(parameterIndex++, message.getCommandCode());
            	statement.setInt(parameterIndex++, message.getContentType());
            	statement.setString(parameterIndex++, message.getInfo());
            	statement.setTimestamp(parameterIndex++, message.getSumitDate());
            	statement.setTimestamp(parameterIndex++, Utils.getInstance().getCurrentTime());
            	statement.setInt(parameterIndex++, message.getProcessResult());
            	statement.setInt(parameterIndex++, message.getMessageType());
            	statement.setString(parameterIndex++, message.getRequestId());
            	statement.setString(parameterIndex++, message.getMessageId());
            	statement.setString(parameterIndex++, message.getNotes());
            	statement.setString(parameterIndex++, message.getCpId());
            	statement.setInt(parameterIndex++, message.getCountSms());            	
            	statement.setString(parameterIndex++, message.getMsgId());
            	statement.setString(parameterIndex++, message.getMd5());
            	statement.setInt(parameterIndex++, message.getAmountSale());            	
            	statement.setInt(parameterIndex++, message.getAmountPurchase());
            	
            }
            statement.execute();
            Utils.logger.info("MTQueueDao@bulkInsert: insert "+messages.size()+" records success!");
            return true;
            
		} catch (SQLException e) {
			Utils.logger.error("writeBatchMTLog@bulkInsert @SQLException: " + e.getMessage());
	        return false;
		} catch (Exception e) {
			Utils.logger.error("writeBatchMTLog@bulkInsert @Exception: " + e.getMessage());
	        return false;
		} finally {
			DBPool.cleanup(statement);
			DBPool.cleanup(connection);
		}
	}
}
