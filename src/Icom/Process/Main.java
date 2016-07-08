package Icom.Process;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import Icom.ActiveMQ.MsgQueue;
import Icom.Entities.RequestDTO;
import Icom.Dao.BlacklistDAO;
import Icom.Dao.BrandNameDAO;
import Icom.Dao.MTQueueDao;
import Icom.Dao.MTQueueGWDAO;
import Icom.Dao.TestlistDAO;
import Icom.Utils.Config;
import Icom.Utils.Constants;
import Icom.Utils.Utils;

public class Main extends Thread {
	public static boolean getData = true;
	public static MsgQueue msgQueue;
	public static MsgQueue msgQueuePendding;
	public static MsgQueue msgQueueWaitting;
	public static MsgQueue queueLog;
	public static MsgQueue queueResponse;

	public static Config config = null;
	
	public static Hashtable<String, String> listBrandName = new Hashtable<String, String>();
	public static Hashtable<String, String> lstBlackList = new Hashtable<String, String>();
	public static Hashtable<String, Timestamp> listUserSend = new Hashtable<String, Timestamp>();
	public static Hashtable<String, AtomicInteger> remainQuota = new Hashtable<String, AtomicInteger>();
	public static ConcurrentHashMap<String, ArrayList<RequestDTO>> listMsisdn = new ConcurrentHashMap<String, ArrayList<RequestDTO>>();
	public static ConcurrentHashMap<String, String> testlist = new ConcurrentHashMap<String, String>();
	
	public static String VMS_SESSION_ID = null;
	
	public void run() {
		try {
			
			MTQueueGWDAO[] threadMtQueueGw = new MTQueueGWDAO[Constants.NUM_THREAD_LOADMT_GW];
			for (int i = 0; i < threadMtQueueGw.length; i++) {
				threadMtQueueGw[i] = new MTQueueGWDAO(msgQueueWaitting, i, threadMtQueueGw.length, Constants.TABLE_MT_WAITTING);
				threadMtQueueGw[i].setPriority(Thread.NORM_PRIORITY);
				threadMtQueueGw[i].start();
			}
			
			MTQueueDao[] threadMtQueue = new MTQueueDao[Constants.NUM_THREAD_LOADMT];
			for (int i = 0; i < threadMtQueue.length; i++) {
				threadMtQueue[i] = new MTQueueDao(msgQueue, i, threadMtQueue.length, Constants.TABLE_MT_TABLE);
				threadMtQueue[i].setPriority(Thread.MAX_PRIORITY);
				threadMtQueue[i].start();
			}
			
			ExcutePushMtGw[] pushMtGwSession2 = new ExcutePushMtGw[Constants.NUM_THREAD_PUSHMT_GW];
			for (int i = 0; i < pushMtGwSession2.length; i++) {
				pushMtGwSession2[i] = new ExcutePushMtGw(msgQueueWaitting, i, pushMtGwSession2.length, Constants.TABLE_MT_TABLE_GW_2);
				pushMtGwSession2[i].setPriority(Thread.MAX_PRIORITY);
				pushMtGwSession2[i].start();
			}
			
			ExcutePushMtGw[] pushMtGw = new ExcutePushMtGw[Constants.NUM_THREAD_PUSHMT_GW];
			for (int i = 0; i < pushMtGw.length; i++) {
				pushMtGw[i] = new ExcutePushMtGw(msgQueueWaitting, i, pushMtGw.length, Constants.TABLE_MT_TABLE_GW);
				pushMtGw[i].setPriority(Thread.MAX_PRIORITY);
				pushMtGw[i].start();
			}
			
			ExcutePushMt[] pushMt = new ExcutePushMt[Constants.NUM_THREAD_PUSHMT];
			for (int i = 0; i < pushMt.length; i++) {
				pushMt[i] = new ExcutePushMt(msgQueue, queueLog, i, pushMt.length);
				pushMt[i].setPriority(Thread.MAX_PRIORITY);
				pushMt[i].start();
			}
			
			ProcessLog[] processLog = new ProcessLog[Constants.NUM_THREAD_LOG];
			for (int i = 0; i < processLog.length; i++) {
				processLog[i] = new ProcessLog(queueLog, i);
				processLog[i].start();
			}			
			
			UpdateQuota updateQuota = new UpdateQuota();
			updateQuota.start();
			
			ReloadConfig reloadConfig = new ReloadConfig();
			reloadConfig.setPriority(Thread.MIN_PRIORITY);
			reloadConfig.start();
			
			ResetResource resetResource = new ResetResource();
			resetResource.start();
			
			Thread.sleep(100L);
			System.out.println("\n----------------------------------------------------------");
			System.out.println("------------- Process started -----------------------------");
			System.out.println("-----------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public int initProcess() {
		int checkLoad = 1;
		try {
			msgQueue = new MsgQueue();
			msgQueuePendding = new MsgQueue();
			msgQueueWaitting = new MsgQueue();
			queueLog = new MsgQueue();
			queueResponse = new MsgQueue();
			
			Utils.getInstance().loadConfig();		
			
			listBrandName = BrandNameDAO.getInstance().getListBrandName();
			//listUserSend = BrandNameDAO.getInstance().getListUserSend();
//			remainQuota = BrandNameDAO.getInstance().getListQuota();
			lstBlackList = BlacklistDAO.getInstance().getBlackList();			
			testlist = TestlistDAO.getInstance().getTestList();
			
			remainQuota = (Hashtable<String, AtomicInteger>) Utils.getInstance().loadData("remainQuota.dat");
			
			if(remainQuota == null){
				remainQuota = BrandNameDAO.getInstance().getListQuota();
			}
			
			Utils.getInstance().loadMessageQueueDataTable("messagequeue.dat", msgQueue);
			Utils.getInstance().loadMessageQueueDataTable("messagelog.dat", queueLog);
			Utils.getInstance().loadMessageQueueDataTable("msgQueuePendding.dat", msgQueuePendding);			
			Utils.getInstance().loadMessageQueueDataTable("msgQueueWaitting.dat", msgQueueWaitting);
			Utils.getInstance().loadMessageQueueDataTable("msgQueueResponse.dat", queueResponse);
			RequestDTO.initResource();
			
			Thread.sleep(1000L);
		} catch (InterruptedException ex) {
			checkLoad = -1;
			System.out.println(ex.toString());
		}

		return checkLoad;
	}

	public void endProcess() {
		getData = false;
		int nCount = 0;
		System.out.print("\nWaiting .....");
		Utils.logger.info("Waiting .....");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException ex) {
			System.out.println(ex.toString());
		}
		while ((msgQueue.getSize() > 0L) && (nCount < 5)) {
			nCount++;
			try {
				Utils.logger.info("...Queue(" + msgQueue.getSize() + ")");
				Thread.sleep(500L);
			} catch (InterruptedException ex) {
				System.out.println(ex.toString());
			}
		}

		Utils.getInstance().saveData(remainQuota, "remainQuota.dat");
		Utils.getInstance().saveMessageDataTable("messagequeue.dat", msgQueue);
		Utils.getInstance().saveMessageDataTable("messagelog.dat", queueLog);		
		Utils.getInstance().saveMessageDataTable("msgQueuePendding.dat", msgQueuePendding);		
		Utils.getInstance().saveMessageDataTable("msgQueueWaitting.dat", msgQueueWaitting);
		Utils.getInstance().saveMessageDataTable("msgQueueResponse.dat", queueResponse);
		
		RequestDTO.saveResource();
		
		Utils.logger.info("Shutdown");
		System.out.print("\nExit");
	}

	public static void main(String[] args) {
		try {
			System.out.println("\n------------------------------------------------------------");
			System.out.println("------  Starting Process send BrandName ICOM - version 1.0 29-01-2016  ----");
			System.out.println("------  Copyright 2016 - All Rights Reserved. ------------");
			System.out.println("------------------------------------------------------------");

			Main process = new Main();
			int result = process.initProcess();
			if (result == 1) {
				ShutdownInterceptor shutdownInterceptor = new ShutdownInterceptor(process);
				Runtime.getRuntime().addShutdownHook(shutdownInterceptor);
				process.start();
			} else {
				Utils.logger.error("****************Khong the start ung dung vi loi load cac thong so khoi tao***************");
			}

		} catch (Exception ex) {
			Utils.logger.error("Error: " + ex.getMessage());
		}
	}
}
