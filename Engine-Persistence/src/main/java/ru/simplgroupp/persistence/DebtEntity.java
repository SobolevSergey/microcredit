package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

import ru.simplgroupp.toolkit.common.Utils;

/**
 * Долги по налогам или судебному производству, свои и чужие
 */
public class DebtEntity extends ExtendedBaseEntity implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4269049718303538162L;
	protected Integer txVersion = 0;
	
	/**
	 * сумма, которую мы зафиксировали или которую вернули партнеры
	 */
	private Double amount;
	/**
	 * сумма штрафов и пошлин
	 */
	private Double amountPenalty;
	/**
	 * вид долга
	 */
	private String typeDebt;
	/**
	 * название долга
	 */
	private String nameDebt;
	/**
	 * код органа
	 */
	private Integer authorityCode;
	/**
	 * название органа
	 */
	private String authorityName;
	/**
	 * дата долга
	 */
	private Date dateDebt;
	/**
	 * дата принятия решения по долгу
	 */
	private Date dateDecision;
	/**
	 * вид органа по справочнику
	 */
	private ReferenceEntity authorityId;
	
	/**
	 * номер иска
	 */
	private String debtNumber;
	/**
	 * описание (свободный текст)
	 */
	private String comment;
	/**
	 * организация
	 */
	private OrganizationEntity organizationId;
	/**
     * кредит
     */
    private CreditEntity creditId;
    /**
     * активное состояние долга
     */
    private Integer isActive;
    
	public DebtEntity(){
		
	}
	
    public Double getAmount(){
		return amount;
	}
	
	public void setAmount(Double amount){
		this.amount=amount;
	}
	
	public String getTypeDebt(){
		return typeDebt;
	}
	
	public void setTypeDebt(String typeDebt){
		this.typeDebt=typeDebt;
	}
	
	public String getNameDebt(){
		return nameDebt;
	}
	
	public void setNameDebt(String nameDebt){
		this.nameDebt=nameDebt;
	}
	
	public Integer getAuthorityCode(){
	  return authorityCode;	
	}
	
	public void setAuthorityCode(Integer authorityCode){
		this.authorityCode=authorityCode;
	}
	
	public String getAuthorityName(){
	  return authorityName;	
	}
	
	public void setAuthorityName(String authorityName)	{
		this.authorityName=authorityName;
	}
	
	public Date getDateDebt(){
		return dateDebt;
	}
	
	public void setDateDebt(Date dateDebt){
		this.dateDebt=dateDebt;
	}
	
	public ReferenceEntity getAuthorityId(){
		return authorityId;
	}
	
	public void setAuthorityId(ReferenceEntity authorityId){
		this.authorityId=authorityId;
	}
	
	public String getDebtNumber() {
		return debtNumber;
	}

	public void setDebtNumber(String debtNumber) {
		this.debtNumber = debtNumber;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public OrganizationEntity getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(OrganizationEntity organizationId) {
		this.organizationId = organizationId;
	}

	
	public Date getDateDecision() {
		return dateDecision;
	}

	public void setDateDecision(Date dateDecision) {
		this.dateDecision = dateDecision;
	}
 
	public Double getAmountPenalty() {
		return amountPenalty;
	}

	public void setAmountPenalty(Double amountPenalty) {
		this.amountPenalty = amountPenalty;
	}

	public CreditEntity getCreditId() {
		return creditId;
	}

	public void setCreditId(CreditEntity creditId) {
		this.creditId = creditId;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	@Override
	public Boolean equalsContent(BaseEntity other) {
    	DebtEntity debt=(DebtEntity) other;
    	return Utils.equalsNull(amount,debt.getAmount())&&Utils.equalsNull(authorityCode,debt.getAuthorityCode())
    			&&Utils.equalsNull(authorityName,debt.getAuthorityName())&&Utils.equalsNull(authorityId,debt.getAuthorityId())
    			&&Utils.equalsNull(comment,debt.getComment())&&Utils.equalsNull(debtNumber,debt.getDebtNumber())
    			&&Utils.equalsNull(dateDebt!=null?dateDebt.getTime():null,debt.getDateDebt()!=null?debt.getDateDebt().getTime():null)
    			&&Utils.equalsNull(dateDecision!=null?dateDecision.getTime():null,debt.getDateDecision()!=null?debt.getDateDecision().getTime():null)
    			&&Utils.equalsNull(nameDebt,debt.getNameDebt())&&Utils.equalsNull(organizationId,debt.getOrganizationId());
    }
}
