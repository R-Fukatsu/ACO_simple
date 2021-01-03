package ACO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;

import java.util.List;
import java.util.ArrayList;


public class ReadConfig {
	
	ReadConfig(){
		
		_branches = new ArrayList<Branch>();
		
		Configurations configs = new Configurations();
		try{
			
		    XMLConfiguration config = configs.xml("config.xml");
		    _all_nodes = config.getInt("nodes");
		    logger.info("all nodes are " + _all_nodes);
		    
		    List<HierarchicalConfiguration<ImmutableNode>> connection_list = config.configurationsAt("connections");
		    for(HierarchicalConfiguration<ImmutableNode> connection : connection_list) {
		    	
		    	int _start = connection.getInt("start") - 1;
		    	int _end = connection.getInt("end") - 1;
		    	double _dis = connection.getDouble("dis");
		    	
		    	if(_start<0 || _all_nodes-1<_start) {
		    		logger.error("invalid start node");
		    	}
		    	if(_end<0 || _all_nodes-1<_end) {
		    		logger.error("invalid end node");
		    	}
		    	
		    	Branch b = new Branch(_start, _end, _dis);
		    	_branches.add(b);
		    	
		    }
		    
	    	logger.info("read branch information");

		    
		}
		catch (ConfigurationException cex) {
		
			logger.error("Fail to read config file");
	    
		}
	}
	
	public int get_nodes() {
		return _all_nodes;
	}
	
	public List<Branch> get_branch(){
		return _branches;
	}
	
	
	private static final Logger logger = LoggerFactory.getLogger(ReadConfig.class);
	
	private int _all_nodes;
	private List<Branch> _branches;
	
}
