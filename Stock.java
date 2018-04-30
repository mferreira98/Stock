import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;


//https://iextrading.com/developer/docs/#batch-requests

public class Stock {
    private String SYM;
    private JsonElement tickerJson;


    public Stock(String SYM) {
      //  ticker = ticker.replaceAll(" ", "_");
        this.SYM = SYM;
       tickerJson = apiReturn("conditions");
    }

    private JsonElement apiReturn(String apiEndpoint) {
        try {
            String apiURL = "https://api.iextrading.com/1.0/stock/" + SYM + "/batch?types=quote,news,chart&range=1m&last=10";
            URL bitlyURL = new URL(apiURL);
            InputStream is = bitlyURL.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            JsonParser parser = new JsonParser();
            return parser.parse(isr);
        } catch (java.net.MalformedURLException mue) {
            System.out.println("Malformed URL");
        } catch (java.io.IOException ioe) {
            System.out.println("IO Error");
        }
        return null;
    }

    public String getStockPrice()
    {
        return tickerJson.getAsJsonObject().get("quote").getAsJsonObject().get("latestPrice").getAsString();
        //String substr = time.substring(11);
    }

    public String getPE(){

        return tickerJson.getAsJsonObject().get("quote").getAsJsonObject().get("peRatio").getAsString();
    }

    public String getDollarAmount(String x){
        StringBuilder res = new StringBuilder(x);
        String y ="";
        if(res.length() == 7){
            res.insert(0, "00");

        }
        if(res.length() == 8){
            res.insert(0, "0");
        }
        if(res.length() == 9){
            y = res.toString();
            String sub1 = y.substring(0,3);
            String sub2 = y.substring(3,4);
            y = sub1 + "." + sub2 + " million";
        }

        if(res.length() == 10){
            res.insert(0, "00");

        }
        if(res.length() == 11){
            res.insert(0, "0");
        }
        if(res.length() == 12){
            y = res.toString();
            String sub1 = y.substring(0,3);
            String sub2 = y.substring(3,4);
            y = sub1 + "." + sub2 + " billion";
        }

        return y;
    }

    public String getMktCap(){

        String mc = tickerJson.getAsJsonObject().get("quote").getAsJsonObject().get("marketCap").getAsString();
        /**
        String result = "";
        char comma = ',';

        for(int i = mc.length(); i >= 1; i--){
            char ch = mc.charAt(i-1);
            result = ch + result;

            if(i % 3 == 0){
                result = "," + result;
            }

        }

        StringBuilder res = new StringBuilder(result);
        if(res.charAt(0) == comma){
            res.deleteCharAt(0);
        }
        else if(res.charAt(res.length()-1) == comma){
            res.deleteCharAt(res.length() -1 );
        }
        mc = res.toString();
         **/


        return getDollarAmount(mc);
    }
    public static void main(String[] args)
    {

        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a stock symbol: ");
        String sym = reader.next();
        reader.close();
        
        Stock s = new Stock(sym);
        String price = s.getStockPrice();
        String mktcap = s.getMktCap();
        String pe = s.getPE();

        System.out.println("Latest stock price for " + sym.toUpperCase() + ": " + price + " dollars/share");
        System.out.println("Latest P/E for " + sym.toUpperCase() + ":         " + pe);
        System.out.println(sym.toUpperCase() + " market capitalization:  " + mktcap + " dollars");
    }
}
