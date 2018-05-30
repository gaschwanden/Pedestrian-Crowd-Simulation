package Importer;

import java.util.Vector;
import processing.core.*;
import Geometry.*;
import Geometry.building;

import UrbanModel.*;
import Importer.*;

public class dxfImporter extends importer{

	private boolean do_vertex;
	private boolean do_AcDbPolyFaceMeshVertex;
	private boolean do_AcDbFaceRecord;
	private boolean do_AcDbVertex;
	private boolean do_Block;
	private boolean sec_blocks;

	private PVector vertexPoint;
	private Polyline pLine;

	public float dxf_maxx;
	public float dxf_maxy;
	public float dxf_miny;
	public float dxf_minx;
	public Vector<Polyline> Dxf_polyline;
	private boolean do_polyline = false;
	private boolean add_points;
	private boolean do_point = false;
	private Vector<building> transportStops;
	UrbanModel urbanModel; 
	
	
	dxfImporter(String[] _textFile, UrbanModel _urbanModel){
//		do_AcDbVertex = false;
//		sec_blocks = false;
//		do_Block = false;
//	}
//
//
//	public void dxf(String[] _textFile){
		
		urbanModel = _urbanModel;
		String textFile[] = _textFile;

		String dxfValue, dxfKey;
//		do_vertex = false;
//		do_AcDbPolyFaceMeshVertex = false;
//		do_AcDbFaceRecord = false;
		do_AcDbVertex = false;
		sec_blocks = false;
		do_Block = false;
	
		// Cutting the textfile into pairs
	

		for (int i=0; i < textFile.length-1; i += 2) {// dxfKey is 1 // dxfValue is 2
			dxfValue = textFile[i+1].trim();
			if (dxfValue.equals("BLOCKS")) {
				sec_blocks = true;
			}
			

			if (sec_blocks == true) {
				dxfKey = textFile[i].trim();
				if((dxfKey.compareTo( "0")==0 ) && (dxfValue.compareTo( "POLYLINE")==0 )) {
					pLine = addPolyline();
					do_polyline = true;
				}
				if((dxfKey.compareTo( "8")==0 ) && (dxfValue.compareTo( "Transport_Stops")==0 )) {
					do_point  = true;
					import_point(dxfKey,dxfValue);
				}
				
				
				if(do_polyline) {
					if((dxfValue.compareTo("VERTEX") ==0) && (dxfKey.compareTo( "0")==0 )) {
						add_points=true;
					}
				}
				

				if(add_points) {
					if((dxfValue.compareTo("2011_NAVTEQ__BLDG") == 0) && (dxfKey.compareTo( "8") == 0 )) {
						do_Block = true;
					}
				}
				if(do_Block) {
					import_Block(dxfKey,dxfValue);

				}
			}
		}
	}
	
	
	
	private void import_point(String dxfKey, String dxfValue) {
		// TODO Auto-generated method stub
		transportStops = new Vector<building>();
//		transportStops.addElement(new Geometry.building(x, y, size, this, buildings, obstacles, (Vector<MovingAgents.pedestrians>) pedestrians, urbanModel))
	}



	private void import_Block(String skey, String sval) {
		if ((skey.compareTo( "8")==0 ) && (sval.compareTo( "2011_NAVTEQ__BLDG")==0 )) {
			vertexPoint = pLine.addVertex();
//			System.out.println(dxf_polyline.size()+" #Polylline has Length = "+pLine.vertices.size());
			do_vertex =true;
		}
		if (do_vertex) {
			
			if (skey.compareTo( "10")==0 ) 
			{
				vertexPoint.x = parseFloat(sval); 
				if (vertexPoint.x>dxf_maxx) dxf_maxx = vertexPoint.x; 
				if (vertexPoint.x<dxf_minx) dxf_minx = vertexPoint.x;  
				if (vertexPoint.x > 1.0) System.out.println(pLine.vertices.size()+" Polyline Vertex x: "+sval); 
				}
			else if (skey.compareTo( "20")==0 ) 
			{
				vertexPoint.y = -parseFloat(sval); 
				if (vertexPoint.y>dxf_maxy) dxf_maxy = vertexPoint.y; 
				if (vertexPoint.y<dxf_miny) dxf_miny = vertexPoint.y; 
//				System.out.println("Polyline Vertex y: "+sval); 
			}
			else if (skey.compareTo( "30")==0 ) 
			{
				vertexPoint.z = parseFloat(sval);  
//				System.out.println("Polyline Vertex z: "+sval); 
				}
		}
	}
}
