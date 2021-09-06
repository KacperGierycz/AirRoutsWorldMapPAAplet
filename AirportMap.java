package module6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Kacper Gierycz and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	private String cityFile = "city-data.json";
	private List<Marker> cityMarkers;
	public static HashMap<Integer, Location> airports = new HashMap<Integer, Location>();

	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	private RouteMarker lastSelectedRoute;
	private RouteMarker lastClickedRoute;
	
	public void setup() {
		// setting up PAppler
		size(800,600, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, 50, 50, 750, 550);
		MapUtils.createDefaultEventDispatcher(this, map);
			
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
			
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
	
			m.setRadius(5);
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());		
		}
				
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
	//		RouteMarker m = new RouteMarker(route,airports);
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}			
		//	SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
			RouteMarker m = new RouteMarker(route.getLocations(),route.getProperties());
		//	System.out.println(m.getProperties());
			
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			//System.out.println(m.getProperties());
		//	System.out.println(m.getLocations());	//
			routeList.add(m);
		}
		
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		map.addMarkers(routeList);
	    map.addMarkers(cityMarkers);
		map.addMarkers(airportList);
		
	}
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
	}
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		
		if (lastSelectedRoute != null) {
			lastSelectedRoute.setSelected(false);
			lastSelectedRoute = null;
		
		}
		
		selectMarkerIfHover(airportList);
		selectMarkerIfHover(cityMarkers);
		selectMarkerIfHover(routeList);
		//loop();
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		if (markers==cityMarkers||markers==airportList) {
		for (Marker m : markers) 
		{
			CommonMarker markerr = (CommonMarker)m;
			if (markerr.isInside(map,  mouseX, mouseY)) {
				lastSelected = markerr;
				markerr.setSelected(true);
				return;	
			}			
		}
	}
		if (markers==routeList) {
			for (Marker m : markers) 
			{
			//	System.out.println("Marker"+m+" selected");
				RouteMarker marker = (RouteMarker)m;
				if (marker.isInside(map,  mouseX, mouseY)) {
					lastSelectedRoute = marker;
					marker.setSelected(true);
					System.out.println("Marker"+marker+" selected");
					return;	
				}			
			}
			
		}
		
	}
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			unhideMarkers();
			lastClicked = null;
		}
		else if (lastClicked == null) 
		{
			checkAirportsForAirClick();
			if (lastClicked == null) {
				checkCitiesForClick();
			}
		}
	}
	private void checkCitiesForClick()
	{
		if (lastClicked != null) return;
		// Loop over the earthquake markers to see if one of them is selected
		for (Marker marker : cityMarkers) {
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = (CommonMarker)marker;
				// Hide all the other earthquakes and hide
				for (Marker mhide : cityMarkers) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
					}
				}
				for (Marker mhide : airportList) {
					AirportMarker portMarker = (AirportMarker)mhide;
					if (lastClicked.getLocation().getDistance(portMarker.getLocation())
							>500) {
						//	> portMarker.threatCircle()) {
						portMarker.setHidden(true);
						
	
				//return;
			}
		}		
	}
			for (Marker mhidee : routeList) {
				if (!((RouteMarker) mhidee).getSource().equals(marker.getId())) 
						{											
					//System.out.println(m.getId());
					//	> marker.threatCircle()) {
					mhidee.setHidden(true);									
						}	
			}
		
		}
		
	}
	
	// Helper method that will check if an earthquake marker was clicked on
	// and respond appropriately
	private void checkAirportsForAirClick()
	{
		if (lastClicked != null) return;
		// Loop over the earthquake markers to see if one of them is selected
		for (Marker m : airportList) {
			
			AirportMarker marker = (AirportMarker)m;
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = marker;
				// Hide all the other earthquakes and hide
				for (Marker mhide : airportList) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
					}
					
				}
			
				for (Marker mhidee : routeList) {
					if (!((RouteMarker) mhidee).getSource().equals(m.getId())) 
							{											
						//System.out.println(m.getId());
						//	> marker.threatCircle()) {
						mhidee.setHidden(true);									
							}	
				}
				//return;
			}
		//	AirDestiantionOn(marker);
		}
		for (Marker mi : airportList) {
			//System.out.println(mi);
			for (Marker mhidee : routeList) {
			if(!mhidee.isHidden()){
			//	System.out.println(mhidee);
			}
				if ((mhidee.isHidden()==false)&&(((RouteMarker) mhidee).getDestination().equals(mi.getId()))){
					//System.out.println(mi.getId());
					mi.setHidden(false);
				}
			}
			
		}
	}
	
	private void AirDestiantionOn(AirportMarker m) {
		for (Marker mhide : routeList) {
		
				
			//mhide.setHidden(true);
			if((lastClicked != null)&&
			//(((RouteMarker) mhide).getDestination().equals(m.getId())) 
				(lastClicked.getId().equals(((RouteMarker) mhide).getDestination())))
			{
				System.out.println(((RouteMarker) mhide).getDestination()+" "+m.getId());
				m.setHidden(false);
			}
		}

	
	}
	private void unhideMarkers() {
		for(Marker marker : airportList) {
			marker.setHidden(false);
		}
			
		for(Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
		for(Marker marker : routeList) {
			marker.setHidden(false);
		}
	}
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		rect(xbase, ybase, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Air routes", xbase+25, ybase+25);
		
		fill(150, 30, 30);
		int tri_xbase = xbase + 35;
		int tri_ybase = ybase + 50;
		triangle(tri_xbase, tri_ybase-CityMarker.TRI_SIZE, tri_xbase-CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE, tri_xbase+CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE);

		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		text("City Marker", tri_xbase + 15, tri_ybase);
		
		text("Airport", xbase+50, ybase+70);
		text("Route", xbase+70, ybase+100);
		
	//	text("Ocean Quake", xbase+50, ybase+90);
	//	text("Size ~ Magnitude", xbase+25, ybase+110);
		
	//	fill(255, 255, 255);
		ellipse(xbase+35, 
				ybase+70, 
				10, 
				10);
		
		line(xbase+25,ybase+100,xbase+55,ybase+100);
	/*	rect(xbase+35-5, ybase+90-5, 10, 10);
		
		fill(color(255, 255, 0));
		ellipse(xbase+35, ybase+140, 12, 12);
		fill(color(0, 0, 255));
		ellipse(xbase+35, ybase+160, 12, 12);
		fill(color(255, 0, 0));
		ellipse(xbase+35, ybase+180, 12, 12);
		
		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text("Shallow", xbase+50, ybase+140);
		text("Intermediate", xbase+50, ybase+160);
		text("Deep", xbase+50, ybase+180);

		text("Past hour", xbase+50, ybase+200);
		
		fill(255, 255, 255);
		int centerx = xbase+35;
		int centery = ybase+200;
		ellipse(centerx, centery, 12, 12);
	
		strokeWeight(2);
		line(centerx-8, centery-8, centerx+8, centery+8);
		line(centerx-8, centery+8, centerx+8, centery-8);
		*/
		
	}
}
