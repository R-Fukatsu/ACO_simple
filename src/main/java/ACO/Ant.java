package ACO;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Integer;
import java.util.Objects;


public class Ant {
	
	Ant(){
		
	}
	
	Ant(int total) {
		
		_cur = 0;
		_idx = 0;
		_path = new ArrayList<Integer>(total);
		_path_len = 0.0;
		_path_num = total;
		
	}
	
	public void init() {
		
		_path.clear();
		
		_cur = 0;
		_path.add(Integer.valueOf(0));
		
		for(int i=1; i<_path_num; i++) {
			_path.add(Integer.valueOf(-1));
		}
		
		_path_len = 0.0;
		_idx = 0;
		
	}
	
	public boolean check_end() {
		
		Integer tmp = _path.get(_path.size()-1);
		
		if( tmp.intValue() == -1 ) {
			return false;
		} else {
			return true;
		}
		
	}
	
	public void record_node(int next_node) {
		
		_cur = next_node;
		_idx++;
		
		_path.set(_idx, next_node);
		
	}
	
	public int get_cur() {
		return _cur;
	}
	
	public int get_next(BranchNode cur_node) {
		
		List<Integer> connections = cur_node.get_connections();
		
		List<Integer> set = new ArrayList<Integer>();
		boolean same_flag;
		
		for(Iterator<Integer> itc=connections.iterator(); itc.hasNext();) {

			same_flag = false;
			
			Integer tmp = itc.next();
						
			for(Iterator<Integer> it=_path.iterator(); it.hasNext();) {
				
				Integer p_tmp = it.next();
				
				if(p_tmp.intValue() == -1) {
					break;
				}

				if(tmp.equals(p_tmp)) {
					same_flag = true;
					break;
				}
				

			}
						
			if(!same_flag) {
				set.add(tmp);
			}

		}
		
		if(set.size() == 1) {
			return set.get(0).intValue();
		}
		
		int result = cur_node.move_to_next(set);
		_path_len += cur_node.get_distance(result);
		return set.get(result).intValue();
		
	}
	
	public List<Integer> get_path(){
		return _path;
	}
	
	public double get_path_length() {
		return _path_len;
	}
	
	
	private int _cur;
	private int _idx;
	
	private List<Integer> _path;
	private double _path_len;
	private int _path_num;
	
}
