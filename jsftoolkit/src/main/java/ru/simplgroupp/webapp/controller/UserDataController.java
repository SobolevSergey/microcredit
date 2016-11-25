package ru.simplgroupp.webapp.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.simplgroupp.dao.interfaces.UsersDAO;
import ru.simplgroupp.ejb.BusinessEvent;
import ru.simplgroupp.interfaces.UsersBeanLocal;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BizAction;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.transfer.RefSystemFeature;
import ru.simplgroupp.transfer.Reference;
import ru.simplgroupp.transfer.Roles;
import ru.simplgroupp.transfer.Users;
import ru.simplgroupp.util.AppUtil;
import ru.simplgroupp.util.ReferenceUtil;
import ru.simplgroupp.util.UserUtil;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SessionScoped
@ManagedBean
@javax.enterprise.context.SessionScoped
public class UserDataController extends AbstractController implements Serializable {

    private static final long serialVersionUID = -2470228187645437971L;

    private static final Logger logger = LoggerFactory.getLogger(UserDataController.class);

    @EJB
    protected UsersBeanLocal userBean;

    @EJB
    UsersDAO usersDAO;

    private String userName;

    private Users user;

    private List<Reference> featuresEnabled;

    private List<BizAction> bizactEnabled;


    @PostConstruct
    public void init() {
        Principal pp = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        logger.info("Principal " + pp);

        if (pp == null) {
            userName = null;
            clearPerms();
        } else {
            userName = pp.getName();
        }
        logger.info("User name " + userName);
        if (StringUtils.isNotEmpty(userName)) {
            user = findUser();
            clearPerms();
            reloadPerms();
        }
    }

    public void reloadPerms(Roles arole) {
        if (user.getRoles().contains(arole)) {
            reloadPerms();
        }
    }

    private void clearPerms() {
        featuresEnabled = null;
        bizactEnabled = null;
    }

    private void reloadPerms() {
        List<Integer> roleIds = AppUtil.extractIds(user.getRoles());
        featuresEnabled = usersDAO.listRPermFeature(roleIds);
        bizactEnabled = usersDAO.listRPermAction(roleIds);
    }

    public void reloadUser() {
        if (user == null) {
            Principal pp = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
            if (pp == null) {
                userName = null;
            } else {
                userName = pp.getName();
            }

            user = findUser();
        } else {
            int userId = user.getId();

            user = userBean.getUser(userId,
                    Utils.setOf(Users.Options.INIT_ROLES, PeopleMain.Options.INIT_PEOPLE_PERSONAL));
            userName = user.getUserName();
        }

        reloadPerms();
    }

    public String getUserName() {
        return userName;
    }

    public boolean hasRoles(String[] roleNames, boolean bAll) {
        if (user == null) {
            return false;
        }

        return UserUtil.hasRoles(user.getRoles(), roleNames, bAll);
    }

    public boolean hasRole(int roleId) {
        if (user == null || user.getRoles() == null) {
            return false;
        }
        for (Roles role : user.getRoles()) {
            if (roleId == role.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRole(String roleName) {
        if (user == null || user.getRoles() == null) {
            return false;
        }
        for (Roles role : user.getRoles()) {
            if (roleName.equals(role.getName())) {
                return true;
            }
        }
        return false;
    }

    public Users getUser() {
        return user;
    }

    public String logout() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        return "logoutSuccess";
    }

    public List<SelectItem> getUsers() {
        List<Users> lst = userBean.listUsers(-1, -1, null, Utils.setOf(PeopleMain.Options.INIT_PEOPLE_PERSONAL), null,
                null, null, null, null);

        List<SelectItem> lstRes = new ArrayList<SelectItem>(lst.size());
        for (Users usr : lst) {
            SelectItem si = new SelectItem();
            si.setValue(usr.getId());
            si.setLabel(usr.getPeopleMain().getPeoplePersonalActive().getSurname());
            lstRes.add(si);
        }
        return lstRes;
    }

    public boolean isFeaturesEnabled(int[] codes, boolean bAll) {
        int n = 0;
        if (featuresEnabled != null) {
            for (Reference ref : featuresEnabled) {
                if (Arrays.binarySearch(codes, ref.getCodeInteger()) >= 0) {
                    // код найден
                    n++;
                }
                if (n > 0 && (!bAll)) {
                    return true;
                }
            }
            if (bAll) {
                return (n == codes.length);
            } else {
                return (n > 0);
            }
        }
        return false;
    }

    public boolean isFeatureEnabled(int code) {
        return (ReferenceUtil.find(featuresEnabled, code, null) != null);
    }

    public boolean isBizActionEnabled(int actId) {
        return (AppUtil.findById(bizactEnabled, actId) != null);
    }

    public void actionChangedEvent(@Observes BusinessEvent event) {
        if (event.getEventCode() != EventCode.ROLE_PERM_CHANGED && event.getEventCode() != EventCode.ROLE_DELETED) {
            return;
        }

        Roles role = (Roles) event.getBusinessObject();
        if (user.getRoles().contains(role)) {
            reloadPerms();
        }
    }

    protected Users findUser() {
        return userBean.findUserByLogin(userName.toLowerCase(),
                Utils.setOf(Users.Options.INIT_ROLES, PeopleMain.Options.INIT_PEOPLE_PERSONAL));
    }
    
	public boolean getCanViewContacts() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.PE_VIEW_CONTACT}, false);
	}    
	
	public boolean getCanViewPassport() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.PE_VIEW_PASSPORT}, false);
	} 	
	
	public boolean getCanViewAddress() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.PE_VIEW_ADDRESS}, false);
	}	
	
	public boolean getCanViewWorkContacts() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.PE_VIEW_WORK_CONTACT}, false);
	}
	
	public boolean getCanViewWorkAddress() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.PE_VIEW_WORK_ADDRESS}, false);
	}
	
	public boolean getCanViewAntiFraud() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_ANTIFRAUD}, false);
	}
	
	public boolean getCanViewHistory() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_HISTORY}, false);
	}
	
	public boolean getCanViewVariables() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_VARIABLE}, false);
	}	
	
	public boolean getCanViewCRData() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_DATA}, false);
	}	
	
	public boolean getCanViewEmployee() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_EMPLOYEE}, false);
	}
	
	public boolean getCanViewAdditional() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_ADDITIONAL}, false);
	}	

	public boolean getCanViewSummary() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_SUMMARY}, false);
	}
	
	public boolean getCanViewBlackList() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_BLACK_LIST}, false);
	}
	
	public boolean getCanViewLog() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_LOG}, false);
	}	

	public boolean getCanViewRequest() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_REQUEST}, false);
	}	

	public boolean getCanViewScoring() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_SCORING}, false);
	}
	
	public boolean getCanViewDebt() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_DEBT}, false);
	}	

	public boolean getCanViewNegative() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_NEGATIVE}, false);
	}	

	public boolean getCanViewPhonePay() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_PHONE_PAY}, false);
	}	
	
	public boolean getCanViewBehavior() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_BEHAVIOR}, false);
	}	
	
	public boolean getCanViewVerify() {
		return isFeaturesEnabled(new int[] {RefSystemFeature.CR_VIEW_VERIFY}, false);
	}		
}
