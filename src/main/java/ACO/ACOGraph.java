package ACO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;



public class ACOGraph {
	
	ACOGraph(){
		
	}
	
	ACOGraph(int max, List<Branch> br){
		
		_total_agent = 100;
		_max_cycle = 10;
		_nodes = new ArrayList<BranchNode>();
		_ants = new ArrayList<Ant>();
		for(int i=0; i<_total_agent; i++) {
			Ant ant = new Ant(max);
			_ants.add(ant);
		}
		
		_alpha = 1.0;
		_beta = 1.0;
		_rho = 0.5;
		_Q = 1.0;
		
		set_node(max);
		
		set_branch(br);
		
		logger.info("success to generate the graph");
		
	}
	
	
	public void set_node(int max){
		
		for(int i=0; i<max; i++) {
			BranchNode tmp = new BranchNode(i, _alpha, _beta, _rho);
			_nodes.add(tmp);
		}
		
	}
	
	public void set_branch(List<Branch> br) {
		
		Branch tmp;
		for(Iterator<Branch> it=br.iterator(); it.hasNext();) {
			
			tmp = it.next();
			int _start = tmp.get_start();
			int _end = tmp.get_end();
			double _dis = tmp.get_dis();
			
			_nodes.get(_start).add_branch(_end, 1.0/_dis);
			_nodes.get(_end).add_branch(_start, 1.0/_dis);
			
		}
		
	}
	
	public void set_alpha(double alpha) {
		
		if(alpha < 0) {
			logger.error("negative alpha");
			return;
		}
		
		_alpha = alpha;
		
	}

	public void set_beta(double beta) {

		if(beta < 0) {
			logger.error("negative beta");
			return;
		}

		_beta = beta;

	}
	
	public List<Integer> get_final_path(){
		
		List<Integer> result = new ArrayList<Integer>();
		
		int next;
		int i = 1;
		int tmp = 0;
		result.add(Integer.valueOf(0));
		
		while(i < _nodes.size()) {
			
			next = _nodes.get(tmp).guess_next_node(result);
			
			if(next < 0) {
				logger.error("cannot guess next node");
				return result;
			} else {
				result.add(Integer.valueOf(next));
			}
			
			tmp = next;
			i++;
			
		}
		
		return result;
		
	}
	
	public void start() {
		
		logger.info("start with alpha " + _alpha);
		logger.info("start with beta " + _beta);
		
		int i = 0;
		while(i < _max_cycle) {
			
			logger.info(i + "th loop");

			for(int j=0; j<_total_agent; j++) {
				_ants.get(j).init();
			}
			
			for(int j=0; j<_total_agent; j++) {

				traveling(_ants.get(j));

			}
			
			update_pheromon();
			
			i++;

		}
		
		logger.info("end of the loop");
		
	}
	
	private void traveling(Ant _a) {
		
		while(!_a.check_end()) {
			
			int next = _a.get_next(_nodes.get(_a.get_cur()));
			
			if(next < 0) {
				logger.error("invaild next node");
				return;
			} else {
				
				_a.record_node(next);
				
			}
			
		}
		
	}
		
	private void update_pheromon() {
		
		accumulate_pheromon();
		
		BranchNode tmp_node;
		for(Iterator<BranchNode> it=_nodes.iterator(); it.hasNext();) {
			
			tmp_node = it.next();
			if(tmp_node.update_evap_pheromon() < 0) {
				logger.error("invalid pheromon");
			}
		}
		
	}
	
	private void accumulate_pheromon() {
		
		Ant tmp_ant;
		List<Integer> path_his;
		
		Integer cur_node = Integer.valueOf(0);
		Integer next_node;
		double total_dis;
		
		for(Iterator<Ant> it=_ants.iterator(); it.hasNext();) {
			
			tmp_ant = it.next();
			path_his =  tmp_ant.get_path();
			
			total_dis = tmp_ant.get_path_length();
			
			cur_node = Integer.valueOf(0);
			for(int i=1; i<path_his.size(); i++) {
				
				next_node = path_his.get(i);
				_nodes.get(cur_node.intValue()).set_pheromon(next_node.intValue(), _Q/total_dis);
				cur_node = next_node;
				
			}
			
		}
		
	}
	
	
	private static final Logger logger = LoggerFactory.getLogger(ACOGraph.class);
	
	private List<BranchNode> _nodes;
	
	private int _total_agent;
	
	private int _max_cycle;
	
	private List<Ant> _ants;
	
	private double _alpha;
	private double _beta;
	private double _rho;
	private double _Q;

}
