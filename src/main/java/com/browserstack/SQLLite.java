package com.browserstack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.sqlite.SQLiteDataSource;

///cp History /Users/raghunandan.gupta/Documents/History
public class SQLLite {

	public static void main(String[] args) throws Exception {
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl("jdbc:sqlite:/Users/raghunandan.gupta/Documents/History");
		Map<Integer,String> urlIdMap = new HashMap<Integer,String>();
		try {
			ResultSet executeQuery = dataSource.getConnection().createStatement()
					.executeQuery("select * from urls order by last_visit_time desc");
			int colCount = executeQuery.getMetaData().getColumnCount();
//			for(int i=1;i<=colCount;i++){
//				System.out.println(executeQuery.getMetaData().getColumnLabel(i));
//			}
			
			while (executeQuery.next()) {
				for(int i=1;i<=colCount;i++){
					System.out.println(executeQuery.getMetaData().getColumnLabel(i)+" Value : "+executeQuery.getString(executeQuery.getMetaData().getColumnLabel(i)));
				}
				urlIdMap.put(executeQuery.getInt("id"), executeQuery.getString("url"));
				System.out.println("######");
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		System.out.println("Done");
		
		System.out.println("========================");
		
		test(urlIdMap);
		
		
	}
	
	public static void test(Map<Integer,String> urlIdMap){
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl("jdbc:sqlite:/Users/raghunandan.gupta/Documents/History");
		try {
			ResultSet executeQuery = dataSource.getConnection().createStatement()
					.executeQuery("select * from visits order by visit_time desc");
			int colCount = executeQuery.getMetaData().getColumnCount();
//			for(int i=1;i<=colCount;i++){
//				System.out.println(executeQuery.getMetaData().getColumnLabel(i));
//			}
			while (executeQuery.next()) {
				for(int i=1;i<=colCount;i++){
					System.out.println(executeQuery.getMetaData().getColumnLabel(i)+" Value : "+executeQuery.getString(executeQuery.getMetaData().getColumnLabel(i)));
				}
				System.out.println("Url : "+urlIdMap.get(executeQuery.getInt("url")));
				System.out.println("######");
//				String time = executeQuery.getString("visit_time");
//				System.out.println(time);
//				System.out.println("Last visited Url : "+executeQuery.getString("url"));
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		System.out.println("Done");
	}
}
