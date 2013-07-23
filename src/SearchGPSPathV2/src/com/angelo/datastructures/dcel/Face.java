/*
 * Author Moroni Angelo
 * This file is part of SeachGPSPath.

    SeachGPSPath is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SeachGPSPath is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SeachGPSPath.  If not, see <http://www.gnu.org/licenses/>. 
 */

package com.angelo.datastructures.dcel;

import com.angelo.voronoi.VPoint;

public class Face {
	private HalfEdge edge;
	private String nameFace;
	private VPoint sito;
	
	public Face(HalfEdge edge,String nameFace){
		setEdge(edge);
		setNameFace(nameFace);
		sito = null;
	}

	public HalfEdge getEdge() {
		return edge;
	}

	public void setEdge(HalfEdge edge) {
		this.edge = edge;
	}

	public String getNameFace() {
		return nameFace;
	}

	public void setNameFace(String nameFace) {
		this.nameFace = nameFace;
	}
	
	public String toString(){
		String toReturn;
		try{
			 toReturn= nameFace + sito.toString()+": ";
		}catch(NullPointerException ed){
			//we are in Diagram external face
			toReturn= nameFace + "(external): ";
		}
		HalfEdge e = edge;
		do{
			toReturn += e.getOrigin().toString();	
			e = e.getNext();
		}while(e!=edge);
		return toReturn;
	}

	public VPoint getSito() {
		return sito;
	}

	public void setSito(VPoint sito) {
		this.sito = sito;
	}
}
