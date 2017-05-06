package common.cfg;

import java.util.LinkedList;
import java.util.List;

public class Cfg
{
    String vars[];
    List<CfgProductionRule> R = new LinkedList<CfgProductionRule>();
    String start_var;
    String terminals[];
    
	public String[] getVars() {
		return vars;
	}
	public void setVars(String[] vars) {
		this.vars = vars;
	}
	public List<CfgProductionRule> getR() {
		return R;
	}
	public void setR(String[][] rules) {
		for (String[] rule : rules) {
			this.R.add(new CfgProductionRule(rule));
		}
	}
	public String getStart_var() {
		return start_var;
	}
	public void setStart_var(String start_var) {
		this.start_var = start_var;
	}
	public String[] getTerminals() {
		return terminals;
	}
	public void setTerminals(String[] terminals) {
		this.terminals = terminals;
	}
	
	public boolean terminalsContain(String mayt) {
		for (String term : terminals) {
			if (term.equals(mayt)) return true;
		}
		return false;
	}
	
	public boolean varsContain(String mayvar) {
		for (String var : vars) {
			if (var.equals(mayvar)) return true;
		}
		return false;
	}
}