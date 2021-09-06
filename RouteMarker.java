package module6;

import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class RouteMarker extends SimpleLinesMarker {
	
	protected boolean clicked = false;
	public static HashMap<Integer, Location> airports;
		public RouteMarker(List<Location> route,HashMap<String,Object> props){
		super(route,props);
		}
		List<Location> route;
		public boolean getClicked() {
			return clicked;
		}
		
		public void setClicked(boolean state) {
			clicked = state;
		}
		
		public MapPosition GetMapPosition(List<Location> route) {
			MapPosition Position= new MapPosition();
			
			
			for (Location loc:route) {
				int i=route.size();
				int j=0;
				float  x= loc.getLon();
				float  y= loc.getLat();
				
				float[] k=new float[2];
				k[0]= x;
				k[1]=y;
				//Position.set(j,k);
				PVector o=new PVector();
						o.set(x,y);
				Position.add( o);				
			}
			return Position;
			}
			
		
		
		List<Location> loc= this.getLocations();
		
		
		//MapPosition Position= new MapPosition(loc);
		//List<MapPosition> mapPositions

	 void draw(PGraphics pg) {
	
			// For starter code just drawMaker(...)
		 	//	loc.getLocations();
		//System.out.println("Drawing");
			if (!hidden) {
				drawMarker( pg );
				System.out.println("Hiden");
				if (selected) {
					System.out.println("MarkerSelected");
					showTitle(pg );
				}

			}
		}
	
		public void drawMarker(PGraphics pg) {
			pg.pushStyle();
	//	pg.strokeWeight(8);
			System.out.println(route.get(0).getLat());
		 pg.line(route.get(0).getLat(),route.get(0).getLon(),route.get(1).getLat(),route.get(1).getLat());
		// System.out.println(x1+ y1+ x2+ y2);
		 
		 pg.popStyle();
	 }
		public void showTitle(PGraphics pg)
		
		{
			 // show rectangle with title
			
			String title = getSource();
			String cod= getDestination();
			pg.pushStyle();
			
			pg.rectMode(PConstants.CORNER);
			
			pg.stroke(110);
			pg.fill(255,255,255);
			pg.rect(route.get(0).getLat(),route.get(0).getLon() + 15,Math.max(pg.textWidth(title), pg.textWidth(cod))  +6, 18, 5);
			
			pg.textAlign(PConstants.LEFT, PConstants.TOP);
			pg.fill(0);
			pg.text(title+" "+cod, route.get(0).getLat() + 3 , route.get(0).getLon() +18);
			
			
			pg.popStyle();
		}
		
		public String getSourceAdestination() {
			
			
		return "ss";
		}
		
		public String getSource() {
			return (String) getProperty("source");	
		}
		public String getDestination() {
			return (String) getProperty("destination");	
		}
	
		
	
	
}
