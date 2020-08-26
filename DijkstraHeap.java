//Author Michael Biwersi

public class DijkstraHeap {
	/*
	 * Implements a binary head where the heap rule is the value in the
	 * parent nodes is less than or equal to the values in the child nodes.
	 * In addition to the standard binary head implementation to the class
	 * keeps an array of locations of items in the heap so an item can be
	 * quickly found for the decreaseKey operation
	 */
	
	private class Item { //implements
		private int node;
		private int distance; //the priority
		
		private Item(int n, int d) {
			node = n;
			distance = d;
		}
	}
	
	private Item items[];
	private int locations[]; //if i is in the heap the location of node i
								// is in location[i] otherwise location[i] is -1
	private int size; //current number of elements in the heap
	
	public DijkstraHeap(int s) {
		items = new Item[s+1];
		locations = new int[s];
		//initially no nodes have been inserted
		for(int i = 0; i < locations.length; i++) 
			locations[i] = -1;
		size = 0;
	}
	
	public void removeMin() {
	//PRE: getSize() != 0
		//removes the highest priority item in the queue
		int loc = this.getMinNode();
		items[locations[loc]] = null;
		locations[loc] = -1;
		for(int i=1; i<items.length-1; i++) {
			items[i] = items[i+1];
		}
		items[size] = null;
		size--;
		for(int i=0; i<locations.length; i++) {
			Item item = items[i+1];
			if(item!=null) 
				locations[items[i+1].node] = i+1;
		}
	}
	
	public int getMinNode() {
	//PRE: getSize !=0
	//returns the highest priority node
		if(items[1]!=null) {
			return items[1].node;
		}
		else {
			int smallestDis = Integer.MAX_VALUE;
			int nextVertex = -1;
			for(int i =0; i<locations.length; i++) {
				int loc = locations[i];
				//System.out.println("loc = "+loc);
				if(loc !=-1) {
					if(items[loc].distance<smallestDis) {
						smallestDis = items[locations[i]].distance;
						nextVertex = items[locations[i]].node;
					}
				}
			}
			return nextVertex;
		}
	}
	

	public int getMinDistance() {
	//PRE: getSize()!=0
	//returns the distance of the highest priority node
		return items[locations[this.getMinNode()]].distance;
	}
	
	public boolean full() {
	//return true if the heap is full otherwise return false
		if(this.size==this.locations.length)
			return true;
		else
			return false;
	}
	
	public void insert(int n, int d) {
	//PRE !full() and !inserted(n)
		Item item = new Item(n, d);
		if(size==0) {
		//Making root
			items[1] = item;
			locations[n] = 1;
		}
		else {
			int index = 1;
			while(index<items.length) {
				if(items[index]==null) {
					items[index] = item;
					locations[n] = index;
					index = items.length;
				}
				else if(items[index].distance>item.distance) {
					Item temp = items[index];
					items[index] = item;
					item = temp;
				}
				index++;
			}
			for(int i=0; i<locations.length; i++) {
				item = items[i+1];
				if(item!=null) 
					locations[items[i+1].node] = i+1;
			}
		}
		size++;
	}
	
	public void decreaseKey(int n, int d) {
	//PRE: inserted(n) and d < the distance associated with n
	//replace the current distance associated with n with d
	//and adjust the heap
		items[locations[n]].distance = d;
		if(locations[n] !=1) {
			int index = locations[n];
			Item item = items[index];
			Item prev = items[index-1];
			while(index>1&&item.distance<prev.distance) {
				items[index] = prev;
				items[index-1] = item;
				locations[prev.node] = index;
				locations[item.node] = index-1;
				prev = items[index-2];
				index--;
				item = items[index];
			}
		}
		
	}
	
	public int getSize() {
	//return the number of items in the heap
		return this.size;
	}
	
	public boolean inserted(int n) {
	//return true if n has been inserted otherwise return false
		if(locations[n]!=-1)
			return false;
		else
			return true;
		
	}
	
	public void printQueue() {
	//Testing method
		System.out.println("locations array:");
		for(int i =0; i<locations.length; i++) {
			System.out.println("index "+i+"["+locations[i]+"]");
		}
		for(int i=0; i<items.length; i++) {
			if(items[i]!=null) {
				System.out.println("("+items[i].node+", "+items[i].distance+")");
			}
			else {
				System.out.println("(index "+i+", -99)");
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Tests
		DijkstraHeap d = new DijkstraHeap(5);
		d.insert(3, 3);
		d.insert(1, 2);
		d.decreaseKey(3, 1);
		d.insert(4, 8);
		d.printQueue();
		d.insert(0, 4);
		d.printQueue();
		d.insert(2, 7);
		d.printQueue();
		d.decreaseKey(4, 0);
		d.printQueue();
		d.decreaseKey(2, 1);
		d.printQueue();
		d.removeMin();
		d.printQueue();
	}

}
