package Importer;

import java.io.File;
import java.util.*;

import javax.swing.JFileChooser;

import Geometry.*;
import Importer.*;
import UrbanModel.*;
import MovingAgents.*;
import processing.core.*;

public class importer extends PApplet{

	public Vector<Polyline> polyline;
	String[] lines;

	private boolean do_vertex = false;
	private boolean do_AcDbPolyFaceMeshVertex;
	private boolean do_AcDbFaceRecord;
	private boolean do_AcDbVertex;
	private boolean do_Block = false;
	private boolean sec_blocks;

	public UrbanMap UrbanMap;

	Vector<building> BuildingFunctionContainer;
	public Vector<String> functionsList;
	public Vector<building> buildings;
	public Vector<Geometry.solidObstacle> obstacles;
	private building provBuilding;

	private PVector vertexPoint;
	private Polyline pLine;

	public float dxf_maxx = -999999;
	public float dxf_minx = 99999;
	public float dxf_maxy = -999999;
	public float dxf_miny = 99999;


	public Vector<Polyline> dxf_polyline;
	private ArrayList<String> definedFunctions;
	private int size = 0;
	private float x = 0;
	private float y = 0;
	private boolean do_AcDbPolyFaceMesh = false;
	private boolean do_polyline = false;
	private boolean sec_entities = false;
	private int colourval;
	private boolean setBounds;
	public Vector<Mesh> dxf_meshes;
	private Mesh mm;
	private Face ff;
	private int numberOfLines = 0;
	private boolean do_Polylines = false;
	private int numberOfPoints;
	private int numberOfVertexes;
	private boolean do_thisLayer = false;
	public Vector<pedestrians> pedestrians;
	private UrbanModel urbanModel;
	PApplet p;
	private String function;
	public String fileextension;
	private boolean addBuildingFromPoint = false;
	private boolean addSinglePointDXF;
	private PVector v;
	private boolean addSinglePointBuilding = false;
	public PImage image;
	private boolean addSingleLineDXF;


	////////////////////////////////////////////////////////////
	////////////
	////////////			Constructor
	////////////
	////////////////////////////////////////////////////////////

	public importer(UrbanMap _urbanMap, boolean _setBounds,  UrbanModel _urbanModel, PApplet _p) 
	{
		//		System.out.println(_setBounds);

		UrbanMap = _urbanMap;
		setBounds = _setBounds;
		urbanModel = _urbanModel;
		p = _p;
		if (urbanModel.buildings !=null)buildings = urbanModel.buildings;
		if (urbanModel.obstacles !=null)obstacles = urbanModel.obstacles;

		// defining where the file is
		definedFunctions = new ArrayList<String>();
		//		importDXF(setBounds);
		JFileChooser jf = new JFileChooser(new File("/Users/gaschwanden/Desktop/Projects/2015_MelbUni_Pedestrians"));
		jf.showOpenDialog(this);
		File file = jf.getSelectedFile();
//		File file = new File("Users/GA/Desktop/Akademia/PhD/PhD_Projects/2013_Air/Generic_Example/Geometrical_setup/Obstacles.dxf");

		// load the file
		String ext = GetFileExtension(file.getName());
		System.out.println(ext);
		if (ext.toLowerCase().equals("dxf")) {
			//			System.out.println(ext);
			fileextension = "dxf";
			String lines[] = loadStrings(file);
			polyline = new Vector<Polyline>();
			if(lines == null) {
				System.out.println("file empty");
			}else{

				dxf(lines, setBounds);
			}
		}
		if (ext.toLowerCase().equals("jpg")) {
			image = p.loadImage(file.getAbsolutePath());
			System.out.println(file.getAbsolutePath());
			urbanModel.imageObject = image;
			urbanModel.pollutionMap = image;
		}
		if (ext.toLowerCase().equals("csv")) {
			fileextension = "csv";
			if(buildings == null)buildings = new Vector<Geometry.building>();
			csv(file);
		}
	}
	
	public importer(UrbanMap _urbanMap, boolean _setBounds,  UrbanModel _urbanModel, PApplet _p, String fileLocation) 
	{
		
		UrbanMap = _urbanMap;
		setBounds = _setBounds;
		urbanModel = _urbanModel;
		p = _p;
		if (urbanModel.buildings !=null)buildings = urbanModel.buildings;
		if (urbanModel.obstacles !=null)obstacles = urbanModel.obstacles;

		// defining where the file is
		definedFunctions = new ArrayList<String>();
		//		importDXF(setBounds);
//		JFileChooser jf = new JFileChooser(new File("/Users/GA/Desktop/Akademia/PhD/PhD_Projects/2013_Air/Generic_Example"));
//		jf.showOpenDialog(this);
		File file = new File("Users/GA/Desktop/Akademia/PhD/PhD_Projects/2013_Air/Generic_Example/Geometrical_setup/Obstacles.dxf");
		// load the file
		String ext = GetFileExtension(file.getName());
		System.out.println(ext);
		if (ext.toLowerCase().equals("dxf")) {
			//			System.out.println(ext);
			fileextension = "dxf";
			String lines[] = loadStrings(file);
			polyline = new Vector<Polyline>();
			if(lines == null) {
				System.out.println("file empty");
			}else{

				dxf(lines, setBounds);
			}
		}
		if (ext.toLowerCase().equals("jpg")) {
			image = p.loadImage(file.getAbsolutePath());
			System.out.println(file.getAbsolutePath());
			urbanModel.imageObject = image;
			urbanModel.pollutionMap = image;
		}
		if (ext.toLowerCase().equals("csv")) {
			fileextension = "csv";
			if(buildings == null)buildings = new Vector<Geometry.building>();
			csv(file);
		}
	}

	public importer() 
	{
		// defining where the file is
		definedFunctions = new ArrayList<String>();
		JFileChooser jf = new JFileChooser();
		jf.showOpenDialog(this);
		File file = jf.getSelectedFile();

		// load the file
		String ext = GetFileExtension(file.getName());
		System.out.println(ext);
		if (ext.toLowerCase().equals("dxf")) {
			String lines[] = loadStrings(file);
			polyline = new Vector<Polyline>();
			if(lines == null) {
				System.out.println("file empty");
			}else{
				System.out.println("I'm here lines is null");
				//				importDXF(setBounds);
				dxf(lines, setBounds);
			}
		}
		if (ext.toLowerCase().equals("csv")) {
			buildings = new Vector<Geometry.building>();
			csv(file);
		}
		if (ext.toLowerCase().equals("jpg")) {
			image = p.loadImage(file.getAbsolutePath());
			System.out.println(file.getAbsolutePath());
			urbanModel.pollutionMap = image;
		}
	}

	////////////////////////////////////////////////////////////
	////////////
	////////////			Method
	////////////
	////////////////////////////////////////////////////////////

	public String GetFileExtension(String fileName) {
		String fname="";
		String ext="";
		int mid= fileName.lastIndexOf(".");
		fname=fileName.substring(0,mid);
		ext=fileName.substring(mid+1,fileName.length());  
		return ext;
	}

	private void csv(File file) {
		String[] data = loadStrings(file);
		loadCSV(data);
	}

	private void loadCSV(String[] data) {
		parseHeader(data[0]);
		for (int i = 1; i < data.length; i++) {
			String dataString = data[i];
			parseCSV(dataString);
		}
	}

	private void parseHeader(String string) {
		String[] pieces = split(string, ',');
		boolean isNew = true;
		for (int i = 0; i <pieces.length; i++) {
			String k = pieces[i].replace("\"", "");
		}

		if(pieces.length>12) {
			definedFunctions.add(pieces[12]);
			for (int j = 13; j < pieces.length; j++) {
				for (int i = 0; i<definedFunctions.size(); i++) {
					String l = pieces[j].replace("\"", "");
					if(l == definedFunctions.get(i)) {
						isNew = false;
						break;
					}
				}
				if(isNew) {
					String k = pieces[j].replace("\"", "");
					definedFunctions.add(k);
					isNew = false;
				}
			}
		}
	}

	private building parseCSV(String dataString) {
		String[] pieces = split(dataString, ',');

		int Name =0;
		int Latitude = 1;
		int Longitude = 2;
		int function =3;
		int Photos = 4;
		int Size = 5;
		int Kind = 6;
		int privacy = 7;
		int Timecontrolled	= 8;
		int Description = 9;
		int Residential = 10;

		for (int i = 11 ; i< pieces.length; i++) {
			String f = pieces[i].replace("\"", "");     
			if (f.toLowerCase().equals("YES")) {
				if (functionsList==null) {
					functionsList = new Vector<String>();
				}
				functionsList.add(definedFunctions.get(i));
			}
		}
		int size = PApplet.parseInt(pieces[Size]);

		// Map GPS coordinates to
		float xGPS = PApplet.parseFloat(pieces[Longitude]);
		float yGPS = PApplet.parseFloat(pieces[Latitude]);
		float w_start = 103.849499f; // Longitude start largest longitude value
		float w_end = 103.856624f;
		float h_start = 1.296941f;
		float h_end = 1.303024f;
		int width = UrbanMap.width-1;
		int height = UrbanMap.height-1;

		x = map(xGPS, w_start, w_end, 0, width);
		y = map(yGPS, h_start, h_end, height, 0);	
		//		System.out.println(x+"  "+y);
		if (functionsList==null) {

			buildings.addElement(new Geometry.building(x, y, size, p, buildings, obstacles, (Vector<MovingAgents.pedestrians>) pedestrians, urbanModel));
			return buildings.elementAt(buildings.size()-1);
		}else {
			buildings.addElement(new Geometry.building(x, y,  size, p, buildings, obstacles, (Vector<MovingAgents.pedestrians>) pedestrians,functionsList, urbanModel));
			return buildings.elementAt(buildings.size()-1);
		}
	}	

	public void dxf(String[] _textFile, boolean _setBounds)
	{
		String[] textFile =_textFile;
		String dxfValue;
		String dxfKey;
		do_AcDbVertex = false;
		sec_blocks = false;
		do_Block = false;
		setBounds = _setBounds;

		// Cutting the textfile into pairs

		for (int i=0; i < textFile.length-1; i += 2) {// dxfKey is 1 // dxfValue is 2
			dxfValue = textFile[i+1].trim();
			dxfKey = textFile[i].trim();
			if (dxfValue.equals("BLOCKS")) {
				if(sec_blocks) {
					sec_blocks = false;
				}else {
					sec_blocks = true;
				}

			}
			if (dxfValue.compareTo("ENTITIES") == 0 && sec_blocks == false)
			{
				sec_entities = true;
			}

			if (sec_blocks == true) {

				
				if((dxfKey.compareTo( "0")==0 ) && (dxfValue.compareTo( "POLYLINE")==0 )) 
				{
					pLine = addPolyline();
//					System.out.println("polyline added POLYLINE");
					do_polyline = true;
				}

				if(do_polyline) {
					if((dxfValue.compareTo("VERTEX") ==0) && ((dxfKey.compareTo( "0")==0 ))) 
					{
						do_Block=true;
					}
					if((dxfValue.compareTo("AcDb2dPolyline") ==0) && ((dxfKey.compareTo( "0")==0 ))) 
					{
						do_Block=true;
					}
				}

				if(do_Block) {
					import_dxf_Block(dxfKey,dxfValue);

				}
			}

			if (sec_entities == true)
			{
				dxfKey = textFile[i].trim();
				if(dxfValue.compareTo("2011_navteq_ bldg") !=0 && (dxfKey.compareTo( "8")==0 )) {
					do_thisLayer = true;
				}
				if(dxfValue.compareTo("LBSTR_INT_BLDG") !=0 && (dxfKey.compareTo( "8")==0 )) {
					do_thisLayer = true;
				}
				if(dxfValue.compareTo("Transport_Stops") !=0 && (dxfKey.compareTo( "8")==0 )) {
					do_thisLayer = true;
					function = "PTStop";
				}
				//				if (dxfKey.compareTo( "62")==0 ) {
				//					colourval = parseInt(dxfValue); 
				//					pLine.color = colourval;
				//					//					System.out.println("colourval"+colourval);
				//				}
				if ((dxfKey.compareTo( "0")==0 ) && (dxfValue.compareTo( "POLYLINE")==0 )) {
					//					System.out.println("polyline");
					pLine = addPolyline();
//					System.out.println("polyline added POLYLINE 2");
					do_polyline = true;
				}

				//				if(do_thisLayer) {
				//					if ((dxfKey.compareTo( "100")==0 ) && (dxfValue.compareTo( "AcDbPoint")==0 )) {
				//						addBuildingFromPoint = true;
				//					}
				//				}

				if ((dxfKey.compareTo( "100")==0 ) && (dxfValue.compareTo( "AcDbPoint")==0 )) {
					if(do_thisLayer) {
						v = new PVector();
//						System.out.println("I'm here AcDbPoint");

						addSinglePointDXF = true;
					}
				}
				if ((dxfKey.compareTo( "100")==0 ) && (dxfValue.compareTo( "AcDb2dPolyline")==0 )) {
					if(do_thisLayer) {
						v = new PVector();
						//						System.out.println("I'm here AcDb2dPolyline");

						addSingleLineDXF = true;
					}
				}

				if(addSingleLineDXF) {
					import_dxf_Point(dxfKey, dxfValue);
				}

				if(addSinglePointDXF) {
					import_dxf_Point(dxfKey, dxfValue);
					if((dxfKey.compareTo( "0")==0 ) && (dxfValue.compareTo( "POINT")==0 )) {
						addSinglePointBuilding = true;
					}
				}


				if(addSinglePointBuilding) {

					if( v == null)System.out.println("v is null");
					if( buildings == null)System.out.println("buildings is null");
					if( obstacles == null)System.out.println("obstacles is null");
					if( urbanModel == null)System.out.println("urbanModel is null");

					if(function == "PTStop")functionsList.add("PTStop");

					if(buildings.size() < UrbanMap.maximumNRbuildings) {
						if(p ==null) {
							System.out.println("p is null");					
						}else {
							if(functionsList==null) {
								System.out.println(v.x+"/"+v.y+ " building added");
								buildings.addElement(new building(v.x, v.y,  size, p , buildings, obstacles, pedestrians, urbanModel));
								v = null;
							}else {
								buildings.addElement(new building(v.x, v.y,  size, p, buildings, obstacles, pedestrians,functionsList, urbanModel));
								v = null;
							}
						}
					}
					addSinglePointBuilding = false;
				}
				if ((dxfKey.compareTo( "0")==0 ) && (dxfValue.compareTo( "LWPOLYLINE")==0 )) {
					//					System.out.println("polyline");
					//					pLine = addPolyline();
					if(do_thisLayer) {
						pLine = addPolyline();
						System.out.println("polyline added do_thislayer");
						do_polyline = true;
					}
				}


				//				//check subclass: AcDbPolyFaceMesh

				if (do_polyline) 
				{
					if ((dxfKey.compareTo( "100")==0 ) && (dxfValue.compareTo( "AcDbPolyFaceMesh")==0 )) {
						mm = addMesh(); 
						do_AcDbPolyFaceMesh = true;
					}					

					if (do_AcDbPolyFaceMesh) {
						import_mesh(dxfKey, dxfValue, setBounds);
					}

					if(dxfValue.compareTo("AcDb2dVertex") !=0 && (dxfKey.compareTo( "100")==0 )) {
						//												System.out.println("AcDb2dVertex");
						do_AcDbVertex = true;
						//						do_Polylines = true;
					}

					if(dxfValue.compareTo("AcDbPolyline") !=0 && (dxfKey.compareTo( "100")==0 )) {
						//						System.out.println("AcDb2dVertex");
						do_AcDbVertex = true;
					}

					if(do_AcDbVertex && do_thisLayer) { 
						if(dxfKey.compareTo("90")==0 && parseFloat(dxfValue)>2) {
							numberOfPoints = parseInt(dxfValue);
							do_Polylines = true;
							System.out.println("I'm here do_AcDbVertex && do_thisLayer with nr points"+numberOfPoints);
						}
					}

					if(do_Polylines && (do_Block == false)) {
						addPointsofPolyline(dxfValue, dxfKey, numberOfPoints);
					}

					//end subclass AcDbPolyFaceMesh
				} //end polyline
				//end sequence read polyline
				if ((dxfKey.compareTo( "0")==0 ) && (dxfValue.compareTo( "SEQEND")==0 )) {
					do_polyline = false;
					do_AcDbPolyFaceMesh = false;
					do_vertex = false;
					do_AcDbVertex =  false;
				}
			}
		}	
	}

	private void addPointsofPolyline(String dxfValue, String dxfKey,int numberOfPoints) {
		numberOfVertexes = pLine.vertices.size();
		if (numberOfPoints == numberOfVertexes) {
			numberOfVertexes = 0;
			do_polyline = false;
			do_thisLayer = false;
		}
		if(do_Block) {

		}else {
			if(numberOfPoints>=numberOfVertexes-1) {

				if (dxfKey.compareTo( "10")==0 ) 
				{
					//					System.out.println(dxfValue);
					//					if(pLine.vertices == null)
					vertexPoint = pLine.addVertex();
					System.out.println("added vertex numberOfVertexes");
					//					if(pLine.vertices.lastElement() )
					vertexPoint.x = parseFloat(dxfValue); 
					if (vertexPoint.x>dxf_maxx && setBounds) dxf_maxx = vertexPoint.x; 
					if (vertexPoint.x<dxf_minx && setBounds) dxf_minx = vertexPoint.x;  
					//				System.out.println("Polyline Vertex x: "+dxfValue); 
					if(polyline.size()<10) {
						System.out.println(numberOfVertexes+" total "+vertexPoint.x);
					}
				}
				else if (dxfKey.compareTo( "20")==0 ) 
				{
					vertexPoint.y = -parseFloat(dxfValue); 
					if (vertexPoint.y>dxf_maxy && setBounds) dxf_maxy = vertexPoint.y; 
					if (vertexPoint.y<dxf_miny && setBounds) dxf_miny = vertexPoint.y; 
					//				System.out.println("Polyline Vertex y: "+dxfValue); 
				}
				else if (dxfKey.compareTo( "30")==0 ) 
				{
					vertexPoint.z = parseFloat(dxfValue);  
					//								System.out.println("Polyline Vertex z: "+dxfValue); 
				}

			}else {
				if (dxfKey.compareTo( "10")==0 ) 
				{
					vertexPoint = pLine.addVertex();
					System.out.println("added vertex dxfKey.compareTo( '10')==0");
					vertexPoint.x = parseFloat(dxfValue); 
					if (vertexPoint.x>dxf_maxx && setBounds) dxf_maxx = vertexPoint.x; 
					if (vertexPoint.x<dxf_minx && setBounds) dxf_minx = vertexPoint.x;  
				}
				else if (dxfKey.compareTo( "20")==0 ) 
				{
					vertexPoint.y = -parseFloat(dxfValue); 
					if (vertexPoint.y>dxf_maxy && setBounds) dxf_maxy = vertexPoint.y; 
					if (vertexPoint.y<dxf_miny && setBounds) dxf_miny = vertexPoint.y; 
					//				System.out.println("Polyline Vertex y: "+dxfValue); 
				}
				else if (dxfKey.compareTo( "30")==0 ) 
				{
					vertexPoint.z = parseFloat(dxfValue);  
					//								System.out.println("Polyline Vertex z: "+dxfValue); 
				}
				//			System.out.println(numberOfPoints+"total "+vertexPoint.x+"/"+vertexPoint.y);

				numberOfVertexes = 0;
				do_polyline = false;
				do_thisLayer = false;
			}
		}
	}

	Mesh addMesh()  //adder function that returns a pointer!
	{
		Mesh m = new Mesh();
		dxf_meshes.addElement(m);
		return (Mesh)dxf_meshes.elementAt(dxf_meshes.size()-1);
	}

	private void import_dxf_Point(String _dxfKey, String _dxfValue)
	{

		String dxfKey = _dxfKey;
		String dxfValue = _dxfValue;

		if (dxfKey.compareTo( "62")==0 ) {
			colourval = parseInt(dxfValue); 
			pLine.color = colourval;
		}

		if ((dxfKey.compareTo( "100")==0 ) && (dxfValue.compareTo( "AcDbPoint")==0 )) {
			do_vertex = true;
		}

		if (do_vertex) {

			if (dxfKey.compareTo( "10")==0 ) 
			{
				v.x = parseFloat(dxfValue); 
				if (v.x>dxf_maxx && setBounds) dxf_maxx = v.x; 
				if (v.x<dxf_minx && setBounds) dxf_minx = v.x;  
				//				if(setBounds)System.out.println("point Vertex x "+ " : "+dxfValue); 
			}
			else if (dxfKey.compareTo( "20")==0 ) 
			{
				v.y = -parseFloat(dxfValue); 
				if (v.y>dxf_maxy && setBounds) dxf_maxy = v.y; 
				if (v.y<dxf_miny && setBounds) dxf_miny = v.y; 
			}
			else if (dxfKey.compareTo( "30")==0 ) 
			{
				v.z = parseFloat(dxfValue);  
				//				System.out.println("Polyline Vertex z: "+dxfValue); 

			}
		}
	}

	private void import_dxf_Block(String _dxfKey, String _dxfValue)
	{
		String dxfKey = _dxfKey;
		String dxfValue = _dxfValue;
		if ((dxfKey.compareTo( "8")==0 ) && (dxfValue.compareTo( "2011_NAVTEQ__BLDG")==0 )) {
			//		if ((dxfKey.compareTo( "8")==0 ) && (dxfValue.compareTo( "VERTEX")==0 )) {
			vertexPoint = pLine.addVertex();
//			System.out.println("added Vertex dxfValue.compareTo( '2011_NAVTEQ__BLDG')==0");
			//						System.out.println(dxf_polyline.size()+" #Polylline has Length = "+pLine.vertices.size()+" from import_dxf_block and 2011_NAVTEQ__BLDG ");
			do_vertex =true;
		}
		if ((dxfKey.compareTo( "100")==0 ) && (dxfValue.compareTo( "AcDb2dVertex")==0 )) {
			//		if ((dxfKey.compareTo( "8")==0 ) && (dxfValue.compareTo( "VERTEX")==0 )) {


			vertexPoint = pLine.addVertex();
//			System.out.println("added Vertex dxfValue.compareTo( 'AcDb2dVertex')==0");
			//						System.out.println(/*dxf_polyline.size()+*/" #Polylline has Length = "+pLine.vertices.size() + "from import_dxf_block and AcDb2dVertex ");
			do_vertex =true;
		}

		if (pLine!=null) {
			if (pLine.vertices.size()==0 && do_vertex== true) {
			}
		}

		if (do_vertex) {
			if (dxfKey.compareTo( "10")==0 ) 
			{
				//				System.out.println(vertexPoint.x);
				if(vertexPoint == null)
				{
					vertexPoint = pLine.addVertex();
//					System.out.println("vertex added do_vertex && 10");	
				}
				vertexPoint.x = parseFloat(dxfValue); 
				if (vertexPoint.x>dxf_maxx && setBounds && vertexPoint.x!=0) dxf_maxx = vertexPoint.x; 
				if (vertexPoint.x<dxf_minx && setBounds && vertexPoint.x!=0) dxf_minx = vertexPoint.x;  
				//								System.out.println("Polyline Vertex x "+ pLine.vertices.size() +" : "+dxfValue); 
			}
			else if (dxfKey.compareTo( "20")==0 ) 
			{
				vertexPoint.y = -parseFloat(dxfValue); 
				if (vertexPoint.y>dxf_maxy && setBounds && vertexPoint.y!=0) dxf_maxy = vertexPoint.y; 
				if (vertexPoint.y<dxf_miny && setBounds && vertexPoint.y!=0) dxf_miny = vertexPoint.y; 
				//				System.out.println("Polyline Vertex y"+ pLine.vertices.size() +": "+dxfValue); 
			}
			else if (dxfKey.compareTo( "30")==0 ) 
			{
				vertexPoint.z = parseFloat(dxfValue);  
				//				System.out.println("Polyline Vertex z: "+dxfValue); 
			}
		}
	}

	public void import_mesh(String skey, String sval, boolean setBounds)
	{
		if ((skey.compareTo( "0")==0 ) && (sval.compareTo( "VERTEX")==0 )) {
			//seen a vertex opener . . . will this be mesh vertex or a face?
			do_AcDbPolyFaceMeshVertex = false;
			do_AcDbFaceRecord = false;
			do_vertex = true;
		}
		if (do_vertex) {
			//			System.out.println("I'm here!");
			if ((skey.compareTo( "100")==0 ) && (sval.compareTo( "AcDbPolyFaceMeshVertex")==0 )) {
				do_AcDbPolyFaceMeshVertex = true;
				vertexPoint = mm.addVertex();
				System.out.println("vertex added AcDbPolyFaceMeshVertex");
			}
			else if ((skey.compareTo( "100")==0 ) && (sval.compareTo( "AcDbFaceRecord")==0 )) {
				do_AcDbFaceRecord = true;
				ff = mm.addFace();
			}

			//if this will be a mesh vertex we are looking for the coordinates
			//if this is a face, we look for the vertices indices and the 'hidden' - which is an adjacency info at the same time!!
			if (do_AcDbPolyFaceMeshVertex) {
				//we DO need float accuracy here!
				if (skey.compareTo( "10")==0 ) 
				{ 
					vertexPoint.x = parseFloat(sval); 
					if (vertexPoint.x>dxf_maxx && setBounds) dxf_maxx = vertexPoint.x; 
					if (vertexPoint.x<dxf_minx && setBounds) dxf_minx = vertexPoint.x;  
					//					System.out.println("Mesh Vertex x: "+sval); 
				}
				else if (skey.compareTo( "20")==0 ) 
				{
					vertexPoint.y = -parseFloat(sval); 
					if (vertexPoint.y>dxf_maxy && setBounds) dxf_maxy = vertexPoint.y; 
					if (vertexPoint.y<dxf_miny && setBounds) dxf_miny = vertexPoint.y; 
					//					System.out.println("Mesh Vertex y: "+sval); 
				}
				else if (skey.compareTo( "30")==0 ) 
				{
					vertexPoint.z = parseFloat(sval);  
					//					System.out.println("Mesh Vertex z: "+sval); 
				}
			}
			else if (do_AcDbFaceRecord) {
				if (skey.compareTo( "71")==0 ) { ff.addVertexRef(0, parseInt(sval));  /*System.out.println("Face vertex 1: "+sval);*/ }
				else if (skey.compareTo( "72")==0 ) { ff.addVertexRef(1, parseInt(sval)); /* System.out.println("Face vertex 2: "+sval);*/ }
				else if (skey.compareTo( "73")==0 ) { ff.addVertexRef(2, parseInt(sval)); /* System.out.println("Face vertex 3: "+sval);*/ }
				else if (skey.compareTo( "74")==0 ) { ff.addVertexRef(3, parseInt(sval)); /* System.out.println("Face vertex 4: "+sval);*/ }
			}
		}
		System.out.println("MAXX = "+dxf_maxx);
		System.out.println("MAXY = "+dxf_maxy);
		System.out.println("MINX = "+dxf_minx);
		System.out.println("MINY = "+dxf_miny);
	}

	Polyline addPolyline() 
	{
		Polyline p = new Polyline();
		if (polyline == null) {
			System.out.println("dxf_polyline = null");
		}
		polyline.addElement(p);
		return (Polyline)polyline.elementAt(polyline.size()-1);
	}

	//	building addBuilding() {
	//
	//		building b = new building(x, y,  size, p, buildings, obstacles, pedestrians, urbanModel);
	//		buildings.addElement(b);
	//		System.out.println(buildings.size());
	//		return buildings.elementAt(buildings.size()-1);
	//	}
}
