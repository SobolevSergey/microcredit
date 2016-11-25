package ru.simplgroupp.workflow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.toolkit.common.Utils;

public class WorkflowObjectActionDef implements Serializable, Cloneable {

    private static final long serialVersionUID = -6773779690623507481L;
    
    private String businessObjectClass;

    private String signalRef;

    private String name;

    private String assignee;

    private String candidateGroups;

    private String candidateUsers;
    
    boolean bForProcess = true;
    
    boolean enabled = true;
    
    private String comment;
    
    private String runProcessDefKey;
    
    private HashMap<String,Object> variables;

    public WorkflowObjectActionDef() {
        super();
    }
    
    public WorkflowObjectActionDef(boolean bForProcess) {
        super();
        this.bForProcess = bForProcess;
    }    

    public WorkflowObjectActionDef(String aname, String aref) {
        super();
        name = aname;
        signalRef = aref;
    }

    public String getSignalRef() {
        return signalRef;
    }

    public void setSignalRef(String signalRef) {
        this.signalRef = signalRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof WorkflowObjectActionDef)) {
            return false;
        }

        WorkflowObjectActionDef other = (WorkflowObjectActionDef) obj;

        return Utils.equalsNull(other.signalRef, signalRef);
    }

    @Override
    public int hashCode() {
        return signalRef != null ? signalRef.hashCode() : 0;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(String candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    public String getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(String candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public WorkflowObjectActionDef copy() {
        WorkflowObjectActionDef aobj = null;
        try {
            aobj = (WorkflowObjectActionDef) this.clone();
        } catch (CloneNotSupportedException e) {
        }
        return aobj;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        WorkflowObjectActionDef def = new WorkflowObjectActionDef();
        def.name = this.name;
        def.signalRef = this.signalRef;
        def.assignee = this.assignee;
        def.candidateGroups = this.candidateGroups;
        def.candidateUsers = this.candidateUsers;
        def.businessObjectClass = this.businessObjectClass;
        def.bForProcess = this.bForProcess;
        return def;
    }

    public void loadFromString(String source) {
    	if (StringUtils.isEmpty(source))
    		return;
        String[] pairs = source.split(";");
        for (String pair : pairs) {
            int n = pair.indexOf("=");
            if (n < 0) {
                continue;
            }
            String sname = pair.substring(0, n);
            String svalue = pair.substring(n + 1);
            if ("name".equals(sname)) {
                if (StringUtils.isBlank(svalue)) {
                    name = null;
                } else {
                    name = svalue;
                }
            } else if ("signalRef".equals(sname)) {
                if (StringUtils.isBlank(svalue)) {
                    signalRef = null;
                } else {
                    signalRef = svalue;
                }
            } else if ("assignee".equals(sname)) {
                if (StringUtils.isBlank(svalue)) {
                    assignee = null;
                } else {
                    assignee = svalue;
                }
            } else if ("candidateGroups".equals(sname)) {
                if (StringUtils.isBlank(svalue)) {
                    candidateGroups = null;
                } else {
                    candidateGroups = svalue;
                }
            } else if ("candidateUsers".equals(sname)) {
                if (StringUtils.isBlank(svalue)) {
                    candidateUsers = null;
                } else {
                    candidateUsers = svalue;
                }
            } else if ("businessObjectClass".equals(sname)) {
                if (StringUtils.isBlank(svalue)) {
                	businessObjectClass = null;
                } else {
                	businessObjectClass = svalue;
                }
            } else if ("forProcess".equals(sname)) {
                if (StringUtils.isBlank(svalue)) {
                	bForProcess = true;
                } else {
                    bForProcess = Boolean.parseBoolean(svalue);
                }
            } 
        }
    }
    
    public boolean isForProcess() {
    	return bForProcess;
    }
    
    public String getAsString() {
    	return saveToString();
    }

    public String saveToString() {
        String sres = "name=" + name + ";signalRef=" + signalRef + ";forProcess=" + Boolean.toString(bForProcess);
        if (assignee != null) {
            sres = sres + ";assignee=" + assignee;
        }
        if (candidateGroups != null) {
            sres = sres + ";candidateGroups=" + candidateGroups;
        }
        if (candidateUsers != null) {
            sres = sres + ";candidateUsers=" + candidateUsers;
        }
        if (businessObjectClass != null) {
            sres = sres + ";businessObjectClass=" + businessObjectClass;
        } 
        return sres;
    }

	public String getBusinessObjectClass() {
		return businessObjectClass;
	}

	public void setBusinessObjectClass(String businessObjectClass) {
		this.businessObjectClass = businessObjectClass;
	}
	
	/**
	 * Возвращает имя процесса для обработки этого сигнала. Применяется только для действий над бизнес-объектом. 
	 * @return
	 */
	public String getProcessDefinitionKey() {
		if (bForProcess) {
			return null;
		}
		
		if (runProcessDefKey != null) {
			return runProcessDefKey;
		}
		
		if (StringUtils.isBlank(businessObjectClass)) {
			return null;
		}
		if (StringUtils.isBlank(signalRef)) {
			return null;
		}		
		
		String sdef = "proc" + businessObjectClass.substring( businessObjectClass.lastIndexOf('.') + 1 ) + signalRef;
		return sdef;
	}
	
	public static WorkflowObjectActionDef valueOf(String source) {
		WorkflowObjectActionDef def = new WorkflowObjectActionDef();
		def.loadFromString(source);
		return def;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return StringUtils.defaultString(businessObjectClass) + " " + StringUtils.defaultString(signalRef);
	}

	public String getRunProcessDefKey() {
		return runProcessDefKey;
	}

	public void setRunProcessDefKey(String runProcessDefKey) {
		this.runProcessDefKey = runProcessDefKey;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}
	
	public void clearVariables() {
		variables = null;
	}

	public void putVariables(Map<String, Object> source) {
		if (source == null || source.size() == 0) {
			return;
		}
		if (variables == null) {
			variables = new HashMap<String,Object>(source.size());
		}
		variables.putAll(source);
	}
}
