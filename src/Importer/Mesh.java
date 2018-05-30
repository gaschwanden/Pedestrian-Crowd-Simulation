package Importer;

import java.util.Vector;
import processing.core.PVector;

public class Mesh {
	
		Vector vertices;
		Vector faces;

		public Mesh()
		{
			vertices = new Vector();
			faces = new Vector();
		}

		public void draw(dxfImporter _p)
		{
			System.out.println(vertices.size());
			for (int i = 0; i < faces.size(); i++) {
				Face f = (Face)faces.elementAt(i);
				f.draw(_p);
			}
		}
		
		Face addFace() //adder function that returns a pointer!
		{
			Face f = new Face(this);
			faces.add(f);
			return (Face)faces.elementAt(faces.size()-1);
		}
		PVector addVertex() //adder function that returns a pointer!
		{
			PVector v = new PVector();
			vertices.add(v);
			return (PVector)vertices.elementAt(vertices.size()-1);
		}
}

	