package ACO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.algorithms.layout.FRLayout;


import org.apache.commons.collections15.Transformer;

import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Stroke;
import javax.swing.JFrame;

import java.util.List;
import java.util.Iterator;


public class Result {

	Result(){
		
	}
	
	Result(int num){
		
		_total = num;
		_graph = new UndirectedSparseGraph<Integer, Integer>();
		
		logger.info("set the nodes");	
		for(int i=0; i<num; i++) {
			_graph.addVertex(i);
		}
		
	}
	
	public void set_branch(List<Branch> br) {
		
		logger.info("set the branches");
		
		int _start;
		int _end;
		Branch tmp;
		for(Iterator<Branch> it=br.iterator(); it.hasNext();) {
			tmp = it.next();
			_start = tmp.get_start();
			_end = tmp.get_end();
			_graph.addEdge(10000+_start*100+_end, _start, _end);
		}
		
	}
	
	public void set_final_path(List<Integer> path) {
		_final_path = path;
	}
	
	public void show() {
		_layout = new FRLayout<Integer, Integer>(_graph);
		_panel = new BasicVisualizationServer<Integer, Integer>(_layout, new Dimension(500, 500));
		
		final Stroke edgeStroke1 = new BasicStroke(10.0f);        
		final Stroke edgeStroke2 = new BasicStroke(1.0f);        
		Transformer<Integer, Stroke> edgeStroke = new Transformer<Integer, Stroke>() {
		    public Stroke transform(Integer s) {
		    	int start = _final_path.get(0).intValue();
		    	for(int i=1; i<_final_path.size(); i++) {
		    		if(s.intValue() ==  10000+start*100+_final_path.get(i).intValue() ||
		    				s.intValue() ==  10000+_final_path.get(i).intValue()*100+start) {
		    			return edgeStroke1;
		    		}
		    		start = _final_path.get(i).intValue();
		    	}
		    	return edgeStroke2;
		    }
		};
		
		_panel.getRenderContext().setEdgeStrokeTransformer(edgeStroke);;

		_frame = new JFrame("Graph View: Manual Layout");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.getContentPane().add(_panel);
		_frame.pack();
		_frame.setVisible(true);
	}
	
	
	private static final Logger logger = LoggerFactory.getLogger(Result.class);
	
	private Graph<Integer, Integer> _graph;
	private int _total;
	private Layout<Integer, Integer> _layout;
	private BasicVisualizationServer<Integer, Integer> _panel;
	private JFrame _frame;
	private List<Integer> _final_path;
	
}
