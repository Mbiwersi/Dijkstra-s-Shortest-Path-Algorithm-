/*Author Michael Biwersi
 * 
 * Given a text file as a command line args[0] that gives the number of Vertexes, a starting Vertex 
 * and the weight or cost given between
 * two vertexes(ie. EdgeNodes) this class finds the shortest or lowest cost
 *  to each Vertex from the starting Vertex using Dijkstra's shortest path
 *  
 *  Example File:
 *  4		//Number of Vertexes
 *  2		//Starting Vertex
 *  0 1 5	//Edge between Vertex 0 and 1 with a weight or cost of 5
 *  3 2 2
 *  0 3 7
 *  3 1 6
 *  
 *  End Example File:
 *  
 *  Output: The lowest cost from the starting Vertex to each other Vertex 
 */

import java.io.*;
import java.util.*;

public class Dijkstra {
	
	//Inner class for Vertex Nodes that store EdgeNodes it shares with other
	//Vertexes, the current shortest distance(initialized as inf) and the previous Vertex in
	//current shortest path.
	private class Vertex {
		private EdgeNode edges1;
		private EdgeNode edges2;
		private boolean known;
		private int distance;
		private int previous;
		
		private Vertex() {
			edges1 = null;
			edges2 = null;
			known = false;
			distance = Integer.MAX_VALUE;
			previous = -1;
		}
	}
	
	//Inner class that stores two Vertexes and the weight between the two
	private class EdgeNode {
		private int vertex1;  //array index in g
		private int vertex2;  //array index in g
		private EdgeNode next1;  //next node in edge list 1
		private EdgeNode next2;  //next node in edge list 2
		private int weight;
		
		private EdgeNode(int v1, int v2, EdgeNode e1, EdgeNode e2, int w) {
		//PRE: v1 < v2
			vertex1 = v1;
			vertex2 = v2;
			next1 = e1;
			next2 = e2;
			weight = w;
		}
	}
	
	private Vertex[] g; //array of vertices
	
	public Dijkstra(int size) {
		g = new Vertex[size];
		for(int i = 0; i < size; i++) {
			g[i] = new Vertex();
		}
	}
	
	public void addEdge(int v1, int v2, int w) {
	//PRE: v1 and v2 are legitimate vertices
	//(i.e. 0<= v1 < g.length and 0 <=v2 < g.length)
		if(v1<v2) {
			EdgeNode newEdge = new EdgeNode(v1,v2,g[v1].edges1, g[v2].edges2, w);
			g[v1].edges1 = newEdge;
			g[v2].edges2 = newEdge;
		}
		else {
			EdgeNode newEdge = new EdgeNode(v2,v1,g[v2].edges1, g[v1].edges2, w);
			g[v2].edges1 = newEdge;
			g[v1].edges2 = newEdge;
		}
	}
	
	//Finds the shortest Path to each Vertex using DijkstraHeap
	private int[] shortestPath(int j, DijkstraHeap d) {
		g[j].distance = 0;
		g[j].known = true;
		int numKnown = 1;
		EdgeNode currentEdge = g[j].edges1;
		while(currentEdge!=null) { 
		//Assigning the starting vertex and all adjacent knows to the edge weight previous to starting vertex
			g[currentEdge.vertex2].distance = currentEdge.weight;
			g[currentEdge.vertex2].previous = currentEdge.vertex1;
			d.insert(currentEdge.vertex2, g[currentEdge.vertex2].distance);
			currentEdge = currentEdge.next1;
		}
		EdgeNode currentEdge2 = g[j].edges2;
		while(currentEdge2!=null) { 
		//Assigning the starting vertex and all adjacent knows to the edge weight previous to starting vertex
			g[currentEdge2.vertex1].distance = currentEdge2.weight;
			g[currentEdge2.vertex1].previous = currentEdge2.vertex2;
			d.insert(currentEdge2.vertex1, g[currentEdge2.vertex1].distance);
			currentEdge2 = currentEdge2.next2;
		}
		while(numKnown<g.length) {
			int nextV = d.getMinNode();
			d.removeMin();
			g[nextV].known=true;
			EdgeNode edge = g[nextV].edges1;
			while(edge!=null) {
				if(g[edge.vertex2].distance==Integer.MAX_VALUE&&edge.vertex2!=j) {
					d.insert(edge.vertex2, (edge.weight+g[edge.vertex1].distance));
					g[edge.vertex2].distance = (g[edge.vertex1].distance+edge.weight);
					g[edge.vertex2].previous = edge.vertex1;
				}
				else if((g[edge.vertex1].distance+edge.weight)<g[edge.vertex2].distance&&edge.vertex2!=j) {
					g[edge.vertex2].distance = (g[edge.vertex1].distance+edge.weight);
					g[edge.vertex2].previous = edge.vertex1;
					d.decreaseKey(edge.vertex2, g[edge.vertex1].distance+edge.weight);
				}
				
				edge = edge.next1;
			}
			edge = g[nextV].edges2;
			while(edge!=null) {
				if(g[edge.vertex1].distance==Integer.MAX_VALUE&&edge.vertex1!=j) {
					d.insert(edge.vertex1, edge.weight+g[edge.vertex2].distance);
					g[edge.vertex1].distance = g[edge.vertex2].distance+edge.weight;
					g[edge.vertex1].previous = edge.vertex2;
				}
				else if((g[edge.vertex2].distance+edge.weight)<g[edge.vertex1].distance&&edge.vertex1!=j) {
					g[edge.vertex1].distance = g[edge.vertex2].distance+edge.weight;
					g[edge.vertex1].previous = edge.vertex2;
					d.decreaseKey(edge.vertex1, g[edge.vertex2].distance+edge.weight);
				}
				edge = edge.next2;
			}
			numKnown++;
		}
		
		int[] rtn = new int[g.length];
		for(int i=0; i<rtn.length; i++) {
			rtn[i] = g[i].distance;
		}
		return rtn;
	}
	
	//Testing method
	private void printEdges() {
		for(int i = 0; i < g.length; i++) {
			EdgeNode edge1 = g[i].edges1;
			System.out.print("Node "+i+" edge1 = ");
			while(edge1!=null) {
				System.out.print("{"+edge1.vertex1+", "+edge1.vertex2+", "+edge1.weight+"}, ");
				edge1 = edge1.next1;
			}
			EdgeNode edge = g[i].edges2;
			System.out.println();
			System.out.print("Node "+i+" edge2 = ");
			while(edge!=null) {
				System.out.print("{"+edge.vertex1+", "+edge.vertex2+", "+edge.weight+"}, ");
				edge = edge.next2;
			}
			System.out.println();
		}
	}
	
	//find and print the best routes from j to all other nodes in the graph
	public void printRoutes(int j) {
		DijkstraHeap p = new DijkstraHeap(g.length);
		int[] distances = this.shortestPath(j, p);
		for(int i = 0; i < distances.length; i++) {
			System.out.println("Vertex "+i+" shortest path has a distance of "+distances[i]+", prev = "+g[i].previous);
		}
		
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader b = new BufferedReader(new FileReader(args[0]));
		String line = b.readLine();
		int numNodes = Integer.parseInt(line);
		line = b.readLine();
		int source = Integer.parseInt(line);
		Dijkstra g = new Dijkstra(numNodes);
		line = b.readLine();
		while(line != null) {
			Scanner scan = new Scanner(line);
			g.addEdge(scan.nextInt(), scan.nextInt(), scan.nextInt());
			line = b.readLine();
		}
		//System.out.println("Printing the edges");
		//g.printEdges();//test method
		g.printRoutes(source);
	}

}
