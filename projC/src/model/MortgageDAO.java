package model;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;



public class MortgageDAO
{
	private DataSource dataSource;
	private final String TABLE = "MORTGAGE";

	public MortgageDAO() throws Exception
	{
		super();
		this.dataSource = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
	}

	public MortgageBean getRate(double principle, int amort, String bank) throws Exception
	{
		MortgageBean mb = null;
		String query_rate = "SELECT rate FROM " + TABLE
				+ " where ABOVE<=" + principle+ " AND " 
				+ principle + "<=UPTO AND "
				+"AMORT=" + amort + " AND "
				+ "BANK=\'" + bank +"\'";
		String query_amort = "SELECT amort FROM " + TABLE
				+ " where AMORT=" + amort
				+ " AND BANK=\'" + bank + "\'";
		String query_principle = "SELECT rate FROM " + TABLE
				+" where ABOVE<=" + principle + " AND "
				+ principle + "<=UPTO AND "
				+ "BANK=\'" + bank + "\'";
		//System.out.println("dao 33 query" + query);
		Statement stmt = null;
		try
		{
			Connection con = this.dataSource.getConnection();
			stmt = con.createStatement();
			stmt.executeUpdate("set schema Roumani");
			ResultSet rs = stmt.executeQuery(query_amort);
			if(!rs.next())
			{
				throw new Exception("The entered amortization is not offered!");
			}
			rs = stmt.executeQuery(query_principle);
			if(!rs.next())
			{
				throw new Exception("Principle is not fell into any of the offered ranges!");
			}
			rs = stmt.executeQuery(query_rate);
			if(rs.next())
			{
				double r = rs.getDouble("RATE");
				mb = new MortgageBean(bank, r);
			}
			else
			{
				throw new Exception("no offer from this bank");
			}
		}
		catch(Exception e)
		{
			System.out.println("exception in dao getRate()");
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
		finally 
		{
			if(stmt != null) 
			{ 
				stmt.close(); 
			}
		}
		return mb;
	}

	public List<MortgageBean> getBanks() throws Exception
	{
		//grab all available banks
		List<MortgageBean> mbList = new LinkedList<MortgageBean>();
		String query = "SELECT distinct bank FROM " + TABLE;
		Connection con = this.dataSource.getConnection();;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.createStatement();
			stmt.executeUpdate("set schema Roumani");
			rs = stmt.executeQuery(query);		

			while(rs.next())
			{
				String bank = rs.getString("BANK");
				mbList.add(new MortgageBean(bank));
			}
		}
		catch(Exception e)
		{
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                	System.out.println("exception in dao create()");
		}
		finally 
		{
			rs.close(); stmt.close(); con.close();
		}

		return mbList;		
	}

}


