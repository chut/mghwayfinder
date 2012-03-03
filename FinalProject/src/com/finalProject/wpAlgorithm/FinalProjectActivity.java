package com.finalProject.wpAlgorithm;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class FinalProjectActivity extends Activity {
    
	private  final int INFINITY = Integer.MAX_VALUE;
	 node A, B, C, D, E, F, G, H, I;											//individual nodes			(used for example)
	
	 ArrayList<node> S = new ArrayList<node>();									//list of settled nodes		(shortest distance found)
	 ArrayList<node> Q = new ArrayList<node>();									//list of unsettled nodes	(distances not yet found)
	 ArrayList<node> map = new ArrayList<node>();								//full array of all nodes	(used for filling UI element)
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        createMap();															//FILLS OUT MAP
        
        //UI ELEMENTS
		Button go = (Button)this.findViewById(R.id.goButton);
		final Spinner start = (Spinner)this.findViewById(R.id.spinStart);
		final Spinner end = (Spinner)this.findViewById(R.id.spinEnd);
		final TextView path = (TextView)this.findViewById(R.id.path);
		
		//FILL SPINNERS
		ArrayAdapter<node> adapter = new ArrayAdapter<node>(this, android.R.layout.simple_spinner_item, map);
		start.setAdapter(adapter);
		end.setAdapter(adapter);
		
		go.setOnClickListener(new OnClickListener() {
			public void onClick(View v1) {
			
				node PATHSTART, PATHEND;
				ArrayList<node> P = new ArrayList<node>();						//FINAL PATH
				String s;
				
				PATHSTART = (node)start.getSelectedItem();
				PATHEND = (node)end.getSelectedItem();
	
				dijkstra(PATHSTART,PATHEND);
		
				P.add(PATHEND);													//initialize end node
				while(P.get(0) != PATHSTART){									//loop backwards from end node until beginning node
					P.add(0, P.get(0).getPreviousNode());						//reverse stacking of nodes
				}
		
				s = P.get(0).toString();										//first step in path
		
				for(int i = 1; i < P.size(); i++){
					s = s + " -> " + P.get(i).toString();
				}
				path.setText(s);
				
				
				//CLEAR ALL PREVIOUS DATA FOR NEXT LOOP
				P.clear();												//this needs to be cleared so when the loop runs again, it is starting with a fresh path
				S.clear();												//not sure that this needs to be cleared, I was running into issues and chose the shotgun approach
				Q.clear();												//not sure that this needs to be cleared, I was running into issues and chose the shotgun approach
				map.clear();											//I don't know why, but the spinner elements were having issues - this fixed it.
				s = "";													//reset path string
				resetNodes();											//THIS IS CRITCAL
				createMap();
			}
		});
    }
	
	public void dijkstra(node start, node goal){
		
		node u;															//node place holder in the loop
		
		Q.add(start);													//starts by adding the starting point to the Q of unsettled nodes 	(EMPTY BEFORE ADD)
		start.setBestDistance(0);										//initializes starting distance of the start node to 0				(BEST DISTANCE FROM STARTING POINT = 0)
		
		while(!Q.isEmpty()){											//loops so long as there are elements in Q 							(ELEMENTS ARE REMOVED FROM Q IN getMinimumNode() AND ADDED in relaxNeighbors())
			u = getMinimumNode();										//set u to the min node distance in ArrayList Q
			S.add(u);													//add u to the ArrayList S											(NODES WITH MINIMUM DISTANCES FOUND)
			relaxNeighbors(u);											//tests neighbor nodes, see function below							
		}
	}
	
	public void relaxNeighbors(node v){
		node o = null;
		int dist;
		for(int i = 0; i < v.getNeighbors().size(); i++){						//loop through neighbors of node v
			o = v.getNeighbors().get(i);
			if(!S.contains(o)) { 												//only look at neighbors NOT in S
				dist = cDistance(v, o);											//calculate distance between v and o
				if(o.getBestDistance() > (v.getBestDistance() + dist)){			//shorter distance found
					o.setBestDistance(dist + v.getBestDistance());				//set best distance of node o
					o.setPNode(v);												//set best previous node to v
					Q.add(o);													//add node o to Q
				}
			}
		}
	}
	
	public int cDistance(node a, node b){								//calculates distances on the fly, could be stored to reduce calculations		(I SUGGEST KEEPING THIS FOR OTHER REASONS)
		double x = a.getX()-b.getX();
		double y = a.getY()-b.getY();
		return (int)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public node getMinimumNode(){										//returns the node from the arrayList Q with the smallest distance from the starting point
		node out = null;
		int min = INFINITY;
		for(int i = 0; i < Q.size(); i++){
			if(Q.get(i).getBestDistance() < min){
				min = Q.get(i).getBestDistance();
				out = Q.get(i);
			}
		}
		Q.remove(out);															//removes the minimum node from Q
		return out;
	}
	
	public void resetNodes(){
		for(node it:map){
			it.reset();
		}
	}
	
	
	public void createMap(){
		
		// INITIALIZING THE NODES FOR THE MAP <-> THIS COULD BE LOOPED OUT OF A DB
		A = new node('A',0,0);
		B = new node('B',0,1);
		C = new node('C',1,1);
		D = new node('D',1,3);
		E = new node('E',2,1);
		F = new node('F',2,0);
		G = new node('G',1,0);
		H = new node('H',0,3);
		I = new node('I',0,4);
		
		//CREATING CONNECTIONS BETWEEN NODES
		A.addNeighbor(B);
		B.addNeighbor(A);
		B.addNeighbor(C);
		B.addNeighbor(H);
		C.addNeighbor(B);
		C.addNeighbor(D);
		C.addNeighbor(E);
		D.addNeighbor(C);
		D.addNeighbor(E);
		D.addNeighbor(H);
		E.addNeighbor(C);
		E.addNeighbor(D);
		E.addNeighbor(F);
		F.addNeighbor(E);
		F.addNeighbor(G);
		G.addNeighbor(F);
		H.addNeighbor(B);
		H.addNeighbor(D);
		H.addNeighbor(I);
		I.addNeighbor(H);
		
		map.add(A);
		map.add(B);
		map.add(C);
		map.add(D);
		map.add(E);
		map.add(F);
		map.add(G);
		map.add(H);
		map.add(I);
		
	}
}
