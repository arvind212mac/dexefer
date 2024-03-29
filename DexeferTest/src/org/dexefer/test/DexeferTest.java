/*
   Copyright 2012 Luis Román Gutiérrez

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.dexefer.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.dexefer.DXFColor;
import org.dexefer.DXFFile;
import org.dexefer.DXFPoint;
import org.dexefer.entities.AlignedDimension;
import org.dexefer.entities.Circle;
import org.dexefer.entities.Line;
import org.dexefer.entities.Text;
import org.dexefer.tables.VPort;

public class DexeferTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DXFPoint start = new DXFPoint(0, 0, 0);
		DXFPoint end = new DXFPoint(100, 100, 100);
		
		DXFFile file = new DXFFile();
		
		file.getTables().add(new VPort(new DXFPoint(0, 0), 1));
		
		Line line  =new Line(start,end);
		line.setColorNumber(DXFColor.CYAN.getColorCode());
		file.getEntities().add(line);
		
		Circle circle = new Circle(end, 50);
		circle.setColorNumber(DXFColor.RED.getColorCode());
		file.getEntities().add(circle);
		
		AlignedDimension aDim = new AlignedDimension(start, end);
		file.getEntities().add(aDim);
		
		Text text = new Text("Lorem ipsum dolor sit amet, consectetuer...", new DXFPoint(200,100));
		text.setTextHeight(10);
		text.setJustification(Text.JUSTIFICATION_CENTER);
		text.setVerticalAlignment(Text.VALIGN_TOP);
		file.getEntities().add(text);
		
		FileOutputStream oStream;
		try {
			oStream = new FileOutputStream(File.createTempFile("dexeferTest", ".dxf"));
			file.write(oStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}

	}

}
