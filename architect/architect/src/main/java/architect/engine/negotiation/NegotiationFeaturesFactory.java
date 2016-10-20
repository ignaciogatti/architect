package architect.engine.negotiation;

import org.apache.commons.lang.StringUtils;

import architect.engine.DBNegotiationExecutor;
import architect.engine.architecture.ArchitectureResults;
import architect.engine.negotiation.utility.ExponentialDecreasingUtility;
import architect.engine.negotiation.utility.ExponentialUtility;
import architect.engine.negotiation.utility.IUtilityFunction;
import architect.engine.negotiation.utility.LinearUtility;
import architect.engine.negotiation.zeuthen.IZeuthenFunction;
import architect.engine.negotiation.zeuthen.ProductIncreasingFunction;
import architect.engine.negotiation.zeuthen.WillignessRiskConflictFunction;
import architect.model.Scenario;

public class NegotiationFeaturesFactory {
	
	//THRESHOLD
	public final static String THRESHOLD_FIXED = "THRESHOLD_FIXED";
	public final static String THRESHOLD_PRIORITY = "THRESHOLD_PRIORITY";
	public final static String THRESHOLD_RESTRICTED = "THRESHOLD_RESTRICTED";
	
	//UTILITY
	public final static String LINEAR_UTILITY = "LINEAR_UTILITY";
	public final static String EXPONENTIAL_UTILITY = "EXPONENTIAL_UTILITY";
	public final static String EXPONENTIAL_DECREASING_UTILITY = "EXPONENTIAL_DECREASING_UTILITY";
	
	//ZEUTHEN
	public final static String WILLIGNESS_RISK_CONFLICT = "WILLIGNESS_RISK_CONFLICT";
	public final static String PRODUCT_INCREASING = "PRODUCT_INCREASING";
	
	public static Double generateThreshold(DBNegotiationExecutor dbot,String type){
		if (THRESHOLD_FIXED.equals(type)){
			return new Double(0.5d);
		}
		if (THRESHOLD_PRIORITY.equals(type)){
			double factor = 1 - (double) (dbot.getArchitectureAnalysis().getScenario().getPriority() / 10d);
			return new Double(factor);
		}
		if (THRESHOLD_RESTRICTED.equals(type)){
			return new Double(0.9d);
		}		
		return new Double(0);
	}
	
	public static IUtilityFunction generateUtility(Scenario scenario,ArchitectureResults actualResults,String typeAndValues){
		String[] arrayUtility = typeAndValues.split(",");
		String type = arrayUtility[0];
		try{
			if (LINEAR_UTILITY.equals(type)){
				if (arrayUtility.length==2){
					int a = (arrayUtility[1] != null) ? Integer.parseInt(arrayUtility[1]) : 1;
					double b = 1 - a * actualResults.getScenarioCost(scenario);
					return new LinearUtility(a,b);
				}
			}
			if (EXPONENTIAL_UTILITY.equals(type)){
				double b = (arrayUtility[1] != null) ? Double.parseDouble(arrayUtility[1]) : 1d;
				return new ExponentialUtility(b);
			}
			if (EXPONENTIAL_DECREASING_UTILITY.equals(type)){
				return new ExponentialDecreasingUtility(actualResults.getScenarioCost(scenario), scenario.getPriority());
			}
		}
		catch (NumberFormatException e){
			e.printStackTrace();
		}
		return new LinearUtility(1,0);
	}
	
	public static IZeuthenFunction generateZeuthen(String type){
		if (WILLIGNESS_RISK_CONFLICT.equals(type)){
			return new WillignessRiskConflictFunction();
		}
		if (PRODUCT_INCREASING.equals(type)){
			return new ProductIncreasingFunction();
		}
		return new WillignessRiskConflictFunction();
	}
}
