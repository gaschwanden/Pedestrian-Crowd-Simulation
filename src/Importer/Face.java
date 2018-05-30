package Importer;
import java.util.Vector;
import processing.core.*; 

public class Face {

	Vector Vrefs;
	Mesh parent;

	public Face (Mesh p)
	{
		parent = p;
		init();
	}

	void init()
	{
		Vrefs = new Vector();
	}

	void addVertexRef(int verticesindex, int index)
	{
		boolean b;
		if (index>0) b = false; else b = true;
		VertexRef vr = new VertexRef(PApplet.abs(index)-1, b);
		Vrefs.addElement(vr);
	}

	public void draw(dxfImporter _p)
	
	{
		PVector a,b;
		

		VertexRef va,vb;
		for (int i = 0; i < Vrefs.size(); i++)
		{
			va = (VertexRef)Vrefs.elementAt(i);
			if (i < Vrefs.size()-1) vb = (VertexRef)Vrefs.elementAt(i+1); else vb = (VertexRef)Vrefs.elementAt(0);
			if (va.hidden == true) _p.stroke(200,0,0); else _p.stroke(0);
			a = (PVector)parent.vertices.elementAt(va.v); 
			b = (PVector)parent.vertices.elementAt(vb.v);
			_p.line(a.x,a.y,a.z,b.x,b.y,b.z);
		}
	}
}
