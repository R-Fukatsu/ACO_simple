package ACO;

import java.lang.Integer;
import java.lang.Double;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.lang.Math;


public class BranchNode {
	
	BranchNode(){
		_connections = new ArrayList<Integer>();
		_phero = new ArrayList<Double>();
		_phero_accum = new ArrayList<Double>();
		_dis = new ArrayList<Double>();
		_start = -1;
		_alpha = 0.0;
		_beta = 0.0;
		_rho = 0.0;
		_Q = 0.0;
	}
	
	BranchNode(int start, double alpha, double beta, double rho){
		_connections = new ArrayList<Integer>();
		_phero = new ArrayList<Double>();
		_phero_accum = new ArrayList<Double>();
		_dis = new ArrayList<Double>();
		// 0 start
		_start = start;
		
		_alpha = alpha;
		_beta = beta;
		_rho = rho;
		_Q = 1.0;
	}
	
	public void add_branch(int end_node, double dis) {
		
		for(Iterator<Integer> it=_connections.iterator(); it.hasNext();) {
			
			if(end_node == it.next()) {
				return;
			}
			
		}
		
		_connections.add(end_node);
		_phero.add(Double.valueOf(0.0));
		_phero_accum.add(Double.valueOf(0.0));
		_dis.add(Double.valueOf(dis));
		
	}
	
	public int move_to_next(List<Integer> next_set) {
		
		boolean uniform_flag = true;
		
		for(Iterator<Double> it=_phero.iterator(); it.hasNext();) {
			
			if( Double.compare(it.next(), 0.0) != 0 ) {
				uniform_flag = false;
				break;
			}
			
		}
		
		
		if(uniform_flag) {
		
			Random rand = new Random();
			return rand.nextInt(next_set.size());
			
		} else {
			
			Random rand = new Random();
			double val = rand.nextDouble();
			
			int next = 0;
			double tmp_phero;
			double prob = 0.0;
			int next_node_num;
			int next_node_idx;
			double deno_prob = 0.0;
			double prv_prob = 0.0;
			double eta;
			
			for(Iterator<Integer> it=next_set.iterator(); it.hasNext();) {
			
				next_node_num = it.next().intValue();
				next_node_idx = find_idx(next_node_num);
				if(next_node_idx < 0) {
					return -1;
				}
				
				eta = _Q / _dis.get(next_node_idx);
				deno_prob += Math.pow(_phero.get(next_node_idx), _alpha)
							* Math.pow(eta, _beta);
			
			}
			
			while(next < next_set.size()) {

				tmp_phero = next_set.get(next).doubleValue();
				
				if( Double.compare(tmp_phero, 0.0) == 0 ) {
					return -1;
				}
				
				if( tmp_phero < 0.0 ) {
					return -1;
				}
				
				int idx = find_idx(next_set.get(next).intValue());
				eta = _Q / _dis.get(idx).doubleValue();
				prob = Math.pow(tmp_phero, _alpha) * Math.pow(eta, _beta) / deno_prob;
				
				if(val < prv_prob + prob) {
					break;
				}
				
				prv_prob += prob;
				next++;
				
			}
			
			return next;
		
		}
		
	}
	
	public List<Integer> get_connections(){
		return _connections;
	}
	
	public int update_evap_pheromon() {
		
		Double tmp;
		for(int i=0; i<_phero.size(); i++) {
			
			tmp = _phero.get(i);
			
			_phero.set(i, _rho*tmp + Double.valueOf(_phero_accum.get(i)));
			if(tmp.doubleValue() < 0) {
				return -1;
			}
			
			_phero_accum.set(i, Double.valueOf(0.0));
			
		}
		
		return 0;
		
	}
	
	public double get_distance(int idx) {
		return _dis.get(idx).doubleValue();
	}
	
	public void set_pheromon(int idx, double ph) {
		_phero_accum.set(find_idx(idx), ph);
	}
	
	public int guess_next_node(List<Integer> path) {
		
		List<Integer> set = new ArrayList<Integer>();
		boolean flag;
		
		for(int i=0; i<_connections.size(); i++) {
			
			flag = false;
					
			for(int j=0; j<path.size(); j++) {
				if(path.get(j).intValue() == _connections.get(i).intValue()) {
					flag = true;
					break;
				}
			}
			
			if(!flag) {
				set.add(Integer.valueOf(i));
			}
		}
		
		if(set.size() == 0) {
			return -1;
		}else if(set.size() == 1) {
			return _connections.get(set.get(0).intValue()).intValue();
		}

		
		Double highest = _phero.get(set.get(0).intValue());
		Double tmp;
		int idx = 0;
		int comp;
		
		for(int i=1; i<set.size(); i++) {
			
			tmp = _phero.get(set.get(i).intValue());
			comp = highest.compareTo(tmp);
			
			if(comp < 0) {
				highest = tmp;
				idx = i;
			} else if(comp == 0) {
				return -1;
			}
			
		}
		
		return _connections.get(set.get(idx).intValue()).intValue();
		
	}
	
	private int find_idx(int idx) {
		
		int node_idx = -1;
		for(int i=0; i<_connections.size(); i++) {
			if(_connections.get(i).intValue() == idx) {
				node_idx = i;
				break;
			}
		}
		
		return node_idx;
		
	}
	
	
	private List<Integer> _connections;
	private List<Double> _phero;
	private List<Double> _phero_accum;
	private List<Double> _dis;
	
	private int _start;
	
	private double _alpha;
	private double _beta;
	private double _rho;
	private double _Q;
}
