package Com.Package;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String city = request.getParameter("city");
		
		
		//API integration ->
		// Open Weather API website --> https://home.openweathermap.org
		String APIKey = "cf981034b8dcb66612bc7604fdb22f4a";
		
		String myURL = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+APIKey;
		
		URL finalURL = new URL(myURL);
		
		HttpsURLConnection myConnection = (HttpsURLConnection) finalURL.openConnection();
		myConnection.setRequestMethod("GET");
		
		
		InputStream inputstream = myConnection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputstream);
		
		
		Scanner sc = new Scanner(reader);
		StringBuilder weatherData = new StringBuilder();
		
		while(sc.hasNext()) {
			weatherData.append(sc.nextLine());
		}
		
		sc.close();
		
		
		//String to JSON
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(weatherData.toString(), JsonObject.class);
//		System.out.println(jsonObject);
		
		
		long dateTime = jsonObject.get("dt").getAsLong() * 1000;
		String date = new Date(dateTime).toString();
		
		double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelsius = (int) (temperatureKelvin - 273.15);
		
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		
		
		request.setAttribute("date", date);
		request.setAttribute("temperature", temperatureCelsius);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("city", city);
		
		myConnection.disconnect();
		
		RequestDispatcher reqDis = request.getRequestDispatcher("/currentWeather.jsp");
		reqDis.forward(request, response);
		
			
		
	}

}
