package Importer;

import java.io.File;
import java.util.*;

import javax.swing.JFileChooser;

import UrbanModel.UrbanMap;
import UrbanModel.UrbanModel;

import Geometry.*;
import processing.core.*;

public class CSVimporter extends importer{
	private PVector vertexPoint;
	String[] data;
	Vector<building>[] BuildingFunctionContainer;
	building[] BuildingFunction;
	Vector<String> functions;

	PApplet p;
	UrbanMap UrbanMap;

	CSVimporter(){
		// defining where the file is
		String fileExtension = "csv";
		JFileChooser jf = new JFileChooser();
		jf.showOpenDialog(this);
		File fileCSV = jf.getSelectedFile();
		data = loadStrings(fileCSV);
		BuildingFunctionContainer = new Vector[data.length];
	}

	public void loadCSV() {
		/*for (int i = 0; i < data.length; i++) {
			String dataString = data[i];
			BuildingFunctionContainer[i] = parseCSV(dataString);
		}*/
	}

}