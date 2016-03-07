package emaestro.testtelecommande;
import java.util.HashMap;
import java.util.Map;


public class Collection_images {
  private static Map<String,String> images_permanentes = new HashMap<String,String>();
  static {
     images_permanentes.put("4.1","Image<4.1>");
     images_permanentes.put("4.2","Image<4.2>");
     images_permanentes.put("4.3","Image<4.3>");
     images_permanentes.put("4.4","Image<4.4>");
     images_permanentes.put("5.1","Image<5.1>");
     images_permanentes.put("5.2","Image<5.2>");
     images_permanentes.put("5.3","Image<5.3>");
     images_permanentes.put("5.4","Image<5.4>");
     images_permanentes.put("5.5","Image<5.5>");
     images_permanentes.put("7.1","Image<7.1>");
     images_permanentes.put("7.2","Image<7.2>");
     images_permanentes.put("7.3","Image<7.3>");
     images_permanentes.put("7.4","Image<7.4>");
     images_permanentes.put("7.5","Image<7.5>");
     images_permanentes.put("7.6","Image<7.6>");
     images_permanentes.put("7.7","Image<7.7>");
  }
  
  static public Map<String,String> getMap(){
	  return images_permanentes;
  }

}
