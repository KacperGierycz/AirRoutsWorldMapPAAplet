# AirRoutsWorldMapPAAplet


I present a screenshot of my program and explain it. This is an Air route map that is displaying 3 kinds of markers in changing configurations. First, you are starting with all markers on a map with is a little overwhelming but if you click on a City or an Airport suddenly almost everything is gone with exception of markers of interest. There is aCity click logic and visualization to show the nearest Airfields and no roust is visible then.
If you click on an Airport there all other markers are gone besides those going out or to chosen airport. with an airport of destination shown. You can freely mouse over a Cites and be shown airports to get a Tittle from them. Key is adopted to a new Markers
There is a reset like in previous versions if you click anywhere on a screen all markers will be viable again.


![AirRouteMap](https://user-images.githubusercontent.com/57790974/132207707-f960d84f-50f9-47d4-9b6b-6b656a98c0d2.png)

Technical inside of mine program It's an adaptation of an Air route and Airports data. 
1. I modify the parser to get an Id of a Point feature like additional properties for easier getId() method while iterating over data.2.Second I made a RouteMarker Class with a constructor that extends SimpleLinesMarker implements methods for Routes like a getSource() getDestination() and draw method witch draw marker if not hidden and selected to show the title of a route.3. AirportMarker class I modify adding methods to get properties: Id(this one comes from modification to a parser, Name, City, Country, Code, Radius. There are little modify classes of draw marker class and show title class 
4. I added a CityMarker class

5. The AirportMap class handles most user interactions (clicks, mouse moves, etc) features.


 <br>
 * a) creation of a RouteMarker type List objects in a constructor.
 * 
 * b) mouseMoved class with modification of an airport list and addition of a route list. 
 * c) selectMarkerIfHover I modify to accommodate new route list and AirporList 
 * d) mouse clicked there is an addition of checkAirportsForAirClick helper method 
 * e) checkCitiesForClicke) checkCitiesForClick there is a distance to the closest Airports set to make them not Hiden on click 
 * f) checkAirportsForAirClick This one is new and logic is added to show an AirportMarker on click with its routs and a Airports of their destinations when all others are hidden.
 * g) unhideMarkers like a original one 
 * h) The key I adapted to new Makers.

<br>

The PAAPlet is unfortunatly dead technology now and it's use is limited to java 1.6  Working applikation is use like a import to the IDE and runn in the best of circumstancess. The main calsses of project included to view. There is a greate funn creating GUIs



