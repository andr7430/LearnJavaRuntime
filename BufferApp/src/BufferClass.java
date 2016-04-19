
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.esri.runtime.ArcGISRuntime;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;

import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.LayerList;
import com.esri.map.MapOptions;
import com.esri.map.MapOptions.MapType;
import com.esri.map.MapOverlay;

import java.awt.event.MouseEvent;

public class BufferClass {

  private JFrame window;
  private JMap map;
  private GraphicsLayer graphicslayer;
  final static SimpleLineSymbol SYM_LINE   = new SimpleLineSymbol(Color.RED, 2.0f);
  final static SimpleMarkerSymbol SYM_POINT =
	      new SimpleMarkerSymbol(new Color(200, 0, 0, 200), 8, SimpleMarkerSymbol.Style.CIRCLE);
  final static SimpleFillSymbol SYM_BUFFER =
	      new SimpleFillSymbol(new Color(0, 0, 255, 80), SYM_LINE);
  private int buffer_distance;
  private JTextField pText = new JTextField();
 
  
  public class MyOverLay extends MapOverlay {
	  
	  JMap jMap;
	  GraphicsLayer gLayer;
	  Geometry bufferedArea = null;
	  @Override
	  public void onMouseClicked(MouseEvent event) {
		  super.onMouseClicked(event);
		  
		  // point clicked
	      Point currPoint = jMap.toMapPoint(event.getX(), event.getY());
	      if (event.getButton() == MouseEvent.BUTTON3) {
	    	  String size = pText.getText();
	    	  buffer_distance = Integer.parseInt(size);
	    	  bufferedArea = GeometryEngine.buffer(
	                  currPoint,
	                  jMap.getSpatialReference(),
	                  buffer_distance,
	                  jMap.getSpatialReference().getUnit());
	    	  Graphic pointGraphic = new Graphic(currPoint,BufferClass.SYM_POINT);
	    	  gLayer.addGraphic(pointGraphic);
	    	  Graphic bufferGraphic = new Graphic(bufferedArea,BufferClass.SYM_BUFFER);
	    	  gLayer.addGraphic(bufferGraphic);
	      }
		  
	  }
	  
	  MyOverLay(JMap jMap, GraphicsLayer graphicsLayer){
		  this.jMap = jMap;
	      this.gLayer = graphicsLayer;
	  }
  }
  
  

  public BufferClass() {
    window = new JFrame();
    window.setSize(800, 600);
    window.setLocationRelativeTo(null); // center on screen
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.getContentPane().setLayout(new BorderLayout(0, 0));

    // dispose map just before application window is closed.
    window.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent windowEvent) {
        super.windowClosing(windowEvent);
        map.dispose();
      }
    });

    // Before this application is deployed you must register the application on 
    // http://developers.arcgis.com and set the Client ID in the application as shown 
    // below. This will license your application to use Basic level functionality.
    // 
    // If you need to license your application for Standard level functionality, please 
    // refer to the documentation on http://developers.arcgis.com
    //
    //ArcGISRuntime.setClientID("your Client ID");

    // Using MapOptions allows for a common online basemap to be chosen
    MapOptions mapOptions = new MapOptions(MapType.TOPO,57.5000, -5.0000, 12);
    map = new JMap(mapOptions);
    LayerList layers = map.getLayers();
    graphicslayer = new GraphicsLayer();
    layers.add(graphicslayer);
    map.addMapOverlay(new MyOverLay(map, graphicslayer));
    
    pText.setBounds(10,200,200,50);
    pText.setText("100");
    
    window.add(pText);

    // If you don't use MapOptions, use the empty JMap constructor and add a tiled layer
    //map = new JMap();
    //ArcGISTiledMapServiceLayer tiledLayer = new ArcGISTiledMapServiceLayer(
    //  "http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");
    //map.getLayers().add(tiledLayer);

    // Add the JMap to the JFrame's content pane
    window.getContentPane().add(map);
  
//    public void onMouseClicked(event) {
//	  
//    }
	  
  }
  

  

  /**
   * Starting point of this application.
   * @param args
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        try {
          BufferClass application = new BufferClass();
          application.window.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
}

