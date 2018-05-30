package Importer;

public class VertexRef {
	
		int v;
		boolean hidden; //'hidden' vertices are inside-lying triangulations of a polygon face
		VertexRef(int _a, boolean h)
		{
			v=_a;
			hidden = h;
		}
	
}
