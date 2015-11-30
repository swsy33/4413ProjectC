package ctrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class Admin
 */
@WebServlet(urlPatterns="/Admin")
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Admin() {
		super();
	}

	public void init() throws ServletException 
	{
		super.init();
		//MaxPrinciple mp = new MaxPrinciple();
		//this.getServletContext().setAttribute("listener",mp);

	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String urlPath = request.getScheme()+ "://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getServletContext().getContextPath();
		request.setAttribute("urlPath",urlPath);
		HttpSession hs = request.getSession();
		String sessionMP = (String)hs.getAttribute("sessionMaxPrinciple");
		request.setAttribute("maxPrinciple", sessionMP);
		String DASHBOARD =  "Dashboard.jspx";
		String MAXP = "MaxP.jspx";
		//String LOGIN = "Login.jspx";
		boolean isValid = false;
		String view = DASHBOARD;
		//request.setAttribute("target", LOGIN);
		//---------------------------------------------
		String user = request.getParameter("user");
		String pswd = request.getParameter("pswd");	
System.out.println("user...." + user);
		//---------------------------------------------
		isValid = (user != null);
		//send open auth
		if(!isValid)
		{
			//System.out.println("in oauth");
			String oauth = "https://www.eecs.yorku.ca/~cse31018/auth/Auth.cgi";
			response.sendRedirect(oauth);
		}
		else
		{
			//System.out.println("back");
			//System.out.println("backuser...." + user);
			view = "Dashboard.jspx";		
			request.setAttribute("target", MAXP);
			request.setAttribute("user", user);
			this.getServletContext().getRequestDispatcher("/" + view).forward(request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private String getHashPassword(String data, String algorithm) throws NoSuchAlgorithmException
	{
		String hash = javax.xml.bind.DatatypeConverter.printHexBinary(MessageDigest.getInstance(algorithm).digest((data).getBytes()));
		return hash;
	}

	private boolean validLogin(String user, String pwd) throws FileNotFoundException
	{
		boolean result = false;
		boolean flag = true;
		String path = this.getServletContext().getRealPath("/WEB-INF/passwords");
		//System.out.println("...." + path);
		File file = new File(path);
		Scanner sc = new Scanner(file);
		while(sc.hasNext() && flag)
		{
			if(sc.next().equalsIgnoreCase(user))
			{
				if(sc.next().equals(pwd))
				{
					result = true;
					flag = false;
					System.out.println("credential valid");
				}
			}
		}
		sc.close();
		return result;
	}
}