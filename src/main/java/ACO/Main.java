package ACO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

	public static void main(String[] arg) {
		
		final Logger logger = LoggerFactory.getLogger(Main.class);
		
		logger.info("start to read a config file");
        ReadConfig config = new ReadConfig();
        
		logger.info("generate the graph");
        ACOGraph graph = new ACOGraph(config.get_nodes(), config.get_branch());
        graph.start();
		
        
        logger.info("generate the figure");
        Result result = new Result(config.get_nodes());
        result.set_branch(config.get_branch());
        result.set_final_path(graph.get_final_path());
        result.show();
        
	}
	
}
