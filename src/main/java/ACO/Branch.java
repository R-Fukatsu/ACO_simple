package ACO;

public class Branch {
	
	Branch(){
		_start = -1;
		_end = -1;
		_dis = -1.0;
	}
	
	Branch(int s, int e, double d){
		
		if(s == e) {
			_start = -1;
			_end = -1;
			_dis = -1.0;
			return;
		}
		
		_start = s;
		_end = e;
		_dis = d;
		
	}
	
	public int get_start(){
		return _start;
	}
	
	public int get_end(){
		return _end;
	}
	
	public double get_dis(){
		return _dis;
	}
	
	private int _start;
	private int _end;
	private double _dis;
	
}
