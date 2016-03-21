package com.ibm.gswt.campus.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBConUtil {
	public static Connection getConn(){
		Connection conn = null;	
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("jdbc/campus");
			conn = ds.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  return conn;
	}
	
	/**
	 * Don't forget We need to use this way to close pstmt and connection in persistence layer.
	 * @param sql
	 * @param mapper SQL execution parameters
	 * @param conn
	 * @param pstmt
	 * @return
	 */
	
	public static int executeUpdate(List<String> paramList, Connection conn, PreparedStatement pstmt) {
		int returnCode = -100;
		try{
			Iterator<String> paramIter = paramList.iterator();
			int i=1;
			while(paramIter.hasNext()){
				String eachParam= (String)paramIter.next();
				pstmt.setString(i, eachParam);
				i++;
			}
			returnCode = pstmt.executeUpdate();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return returnCode;
	}
	
	public static int executeUpdate(PreparedStatement pstmt) {
		int returnCode = -100;
		try{
			returnCode = pstmt.executeUpdate();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return returnCode;
	}
	
	/**
	 * Don't forget: After execute query, we need to close resultset, 
	 * prepared statement and connection in
	 * persistence layer
	 * @param mapper query parameters
	 * @param conn
	 * @param pstmt
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet query(List<String> paramList, Connection conn, PreparedStatement pstmt )throws SQLException{
		ResultSet result = null;
		try{
			Iterator<String> paramIter = paramList.iterator();
			int i=1;
			while(paramIter.hasNext()){
				String eachParam= paramIter.next();
				pstmt.setString(i, eachParam);
				i++;
			}
			result = pstmt.executeQuery();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Don't forget: After execute query, we need to close resultset, 
	 * prepared statement and connection in
	 * persistence layer
	 * @param mapper query parameters
	 * @param conn
	 * @param pstmt
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet query(Connection conn, PreparedStatement pstmt )throws SQLException{
		ResultSet result = null;
		try{
			result = pstmt.executeQuery();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet result){
		//close connections.
		if(result != null){
			try{
				result.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(pstmt != null){
			try{
				pstmt.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(conn != null){
			try{
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void close(Connection conn, PreparedStatement pstmt){
		//close connections.
		if(pstmt != null){
			try{
				pstmt.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(conn != null){
			try{
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void close(Connection conn){
		if(conn != null){
			try{
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
