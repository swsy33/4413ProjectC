package ctrl;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.org.apache.regexp.internal.RE;

import model.Mortgage;


/**
 * Servlet implementation class Start
 */
@WebServlet(urlPatterns="/Start")
//urlPatterns = {"/Start","/StartUp/*" })
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Start() {
		super();
	}

	public void init() throws ServletException 
	{
		super.init();
		try
		{
			Mortgage model = new Mortgage();
			this.getServletContext().setAttribute("model",model);		
		}
		catch(Exception e)
		{
			throw new ServletException("Init exception........");
		}
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Mortgage m = (Mortgage)this.getServletContext().getAttribute("model");
		HttpSession hs = request.getSession();
		//------------------------------------------------------
		String urlPath = request.getScheme()+ "://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getServletContext().getContextPath();
		request.setAttribute("urlPath",urlPath);
		//------------------------------------------------------
		String p = request.getParameter("principle");
		String a = request.getParameter("amortization");
		String r = request.getParameter("interest");
		String bank = request.getParameter("bank");
		String view = "";
		double payment = 0;
		//--------------------------------------------
		String UI = "UI.jspx";
		String RESULT = "Result.jspx";
		String DASHBOARD = "Dashboard.jspx";
		//--------------------------------------------
		
		
		if((p != null) && (a != null))
		{
			hs.setAttribute("sessionPrinciple", p);
			hs.setAttribute("sessionAmortization", a);
		}		
		
		if((request.getParameter("doit") != null) && (request.getParameter("goback") != null))
		{
			try
			{
				payment = m.computePayment(p,a, r, bank);
				request.setAttribute("monthly", payment);
				if((r.isEmpty()) && (!bank.isEmpty()))
				{
					request.setAttribute("interest", bank);				
				}
				else if(!r.isEmpty())
				{
					request.setAttribute("interest", r);
				}
				request.setAttribute("target", RESULT);
				view = DASHBOARD;
			}
			catch(Exception e)
			{
				request.setAttribute("target", UI);
				view = DASHBOARD;
				request.setAttribute("exception", e.getMessage());
			}
		} 
		else if((request.getParameter("doit") == null) || (request.getParameter("goback") != null))
		{	
			request.setAttribute("target", UI);
			view = DASHBOARD;
			populateBankList(view, request, m);
		}
		else if((request.getParameter("doit") != null) && (request.getParameter("goback") == null))
		{
			//no go back
			try{
								
				if(p == null)
				{			
					p = (String) hs.getAttribute("sessionPrinciple");					
					if(p == null)
					{
						p = this.getServletContext().getInitParameter("principle");
					}
				}
				
				if(a == null)
				{
					a = (String) hs.getAttribute("sessionAmortization");
					
					if(a== null)
					{
						a = this.getServletContext().getInitParameter("amortization");
					}
				}
				
				payment = m.computePayment(p, a, r, bank);
				request.setAttribute("monthly", payment);
				if((r.isEmpty()) && (!bank.isEmpty()))
				{
					request.setAttribute("interest", bank);				
				}
				else if(!r.isEmpty())
				{
					request.setAttribute("interest", r);
				}
				request.setAttribute("target", RESULT);
				view = DASHBOARD;
			}
			catch(Exception e)
			{

				request.setAttribute("target", UI);
				view = DASHBOARD;
				request.setAttribute("exception",e.getMessage());
				String tempP = request.getParameter("principle");
				String tempA = request.getParameter("amortization");
				if((tempP == null) && (tempA == null))
				{
					tempP = (String) hs.getAttribute("sessionPrinciple");
					tempA = (String) hs.getAttribute("sessionAmortization");				
				}
				request.setAttribute("principle",tempP);
				request.setAttribute("amortization", tempA);
				request.setAttribute("interest", request.getParameter("interest"));
				populateBankList(view, request, m);
			}

		}
		else
		{
			populateBankList("UI.jspx", request, m);
		}
		this.getServletContext().getRequestDispatcher("/" + view).forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

	private void populateBankList(String view, HttpServletRequest request, Mortgage m)
	{
		try
		{
			if(view.equals("Dashboard.jspx"))
			{
				List<String> banklist = m.getBankList();
				request.setAttribute("banklist", banklist);
			}
			
		} 
		catch (Exception e)
		{	
			view = "UI.jspx";
			request.setAttribute("target", view);
			request.setAttribute("exception",e.getMessage());
		}
	}

}