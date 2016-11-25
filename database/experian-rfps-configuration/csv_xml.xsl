<?xml version='1.0' encoding='windows-1251' ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="urn:mclsoftware.co.uk:hunterII">
	<xsl:output encoding="UTF-8"/>
	<xsl:template match="/">
		<BATCH xmlns="urn:mclsoftware.co.uk:hunterII">
			<HEADER>
				<COUNT>
					<xsl:value-of select="count(BATCH/ROWS/ROW)"/>
				</COUNT>
				<ORIGINATOR>
					<xsl:value-of select="BATCH/ROWS/STATIC_FIELDS/ORIGINATOR/@v"/>
			    </ORIGINATOR>
				<xsl:if test="BATCH/ROWS/STATIC_FIELDS/SUPPRESS/@v='Y'">
			    <SUPPRESS>
			    <xsl:value-of select="BATCH/ROWS/STATIC_FIELDS/SUPPRESS/@v"/>
			    </SUPPRESS>
				</xsl:if>
				<xsl:if test="BATCH/ROWS/STATIC_FIELDS/RESULT/@v='Y'">
			     <RESULT>
			    <xsl:value-of select="BATCH/ROWS/STATIC_FIELDS/RESULT/@v"/>
			    </RESULT>
				</xsl:if>
			</HEADER>
			<SUBMISSIONS>
				<xsl:for-each select="BATCH/ROWS">

						<xsl:apply-templates/>

				</xsl:for-each>
			</SUBMISSIONS>
		</BATCH>
	</xsl:template>
	<xsl:template match="ROW">
	<SUBMISSION>
		<IDENTIFIER>
			<xsl:value-of select="IDENTIFIER/@v"/>
		</IDENTIFIER>
		<PRODUCT>
			<xsl:value-of select="PRODUCT/@v"/>
		</PRODUCT>
		<CLASSIFICATION>
			<xsl:value-of select="CLASSIFICATION/@v"/>
		</CLASSIFICATION>
		<DATE>
			<xsl:value-of select="DATE/@v"/>
		</DATE>
		<xsl:if test="count(APP_DATE)>0">
			<APP_DATE>
				<xsl:value-of select="APP_DATE/@v"/>
			</APP_DATE>
		</xsl:if>
		<MA>
		    <xsl:if test="count(M_STATUS)>0">
			<STATUS>
				<xsl:value-of select="M_STATUS/@v"/>
			</STATUS>
			</xsl:if>
			<FNAME>
				<xsl:value-of select="M_FNAME/@v"/>
			</FNAME>
			<xsl:if test="count(M_PNAME)>0">
			<PNAME>
				<xsl:value-of select="M_PNAME/@v"/>
			</PNAME>
			</xsl:if>
			<xsl:if test="count(M_LNAME)>0">
			<LNAME>
				<xsl:value-of select="M_LNAME/@v"/>
			</LNAME>
			</xsl:if>
			<DOB>
				<xsl:value-of select="M_DOB/@v"/>
			</DOB>
			<xsl:if test="count(M_PER_INN)>0">
			<PER_INN>
				<xsl:value-of select="M_PER_INN/@v"/>
			</PER_INN>
			</xsl:if>
			<xsl:if test="count(M_PREV_FN)>0">
			<PREV_FN>
				<xsl:value-of select="M_PREV_FN/@v"/>
			</PREV_FN>
			</xsl:if>
			<xsl:if test="count(M_PREV_PN)>0">
			<PREV_PN>
				<xsl:value-of select="M_PREV_PN/@v"/>
			</PREV_PN>
			</xsl:if>
			<xsl:if test="count(M_PREV_LN)>0">
			<PREV_LN>
				<xsl:value-of select="M_PREV_LN/@v"/>
			</PREV_LN>
			</xsl:if>
			<xsl:if test="count(M_INC_TM)>0">
			<INC_TM>
				<xsl:value-of select="M_INC_TM/@v"/>
			</INC_TM>
			</xsl:if>
			<xsl:if test="count(M_INC_DOC)>0">
			<INC_DOC>
				<xsl:value-of select="M_INC_DOC/@v"/>
			</INC_DOC>
			</xsl:if>
			<xsl:if test="count(M_INC_ADD)>0">
			<INC_ADD>
				<xsl:value-of select="M_INC_ADD/@v"/>
			</INC_ADD>
			</xsl:if>
			<xsl:if test="count(M_PAS_NO)>0">
			<MA_PAS>
				 <xsl:if test="count(M_PAS_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_PAS_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="M_PAS_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(M_PAS_DATE)>0">
				<DOC_DATE>
					<xsl:value-of select="M_PAS_DATE/@v"/>
				</DOC_DATE>
				</xsl:if>
			</MA_PAS>
			</xsl:if>
			<xsl:if test="count(M_D1_NO)>0">
			<MA_DOC>
				 <xsl:if test="count(M_D1_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_D1_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="M_D1_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(M_D1_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="M_D1_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>				
				<DOT>
					<xsl:value-of select="M_D1_DOT/@v"/>
				</DOT>
			</MA_DOC>
			</xsl:if>
			<xsl:if test="count(M_D2_NO)>0">
			<MA_DOC>
				 <xsl:if test="count(M_D2_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_D2_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="M_D2_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(M_D2_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="M_D2_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>				
				<DOT>
					<xsl:value-of select="M_D2_DOT/@v"/>
				</DOT>
			</MA_DOC>
			</xsl:if>
			<xsl:if test="count(M_SPO_FNAME)>0">
			<MA_SPO>
				<xsl:if test="count(M_SPO_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_SPO_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<FNAME>
					<xsl:value-of select="M_SPO_FNAME/@v"/>
				</FNAME>
				<xsl:if test="count(M_SPO_PNAME)>0">
				<PNAME>
					<xsl:value-of select="M_SPO_PNAME/@v"/>
				</PNAME>
				</xsl:if>
				<LNAME>
					<xsl:value-of select="M_SPO_LNAME/@v"/>
				</LNAME>
				<DOB>
					<xsl:value-of select="M_SPO_DOB/@v"/>
				</DOB>
			</MA_SPO>
			</xsl:if>
			<MA_RAD>
				<xsl:if test="count(M_R_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_R_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(M_R_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="M_R_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="M_R_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="M_R_REGION/@v"/>
				</REGION>
				<xsl:if test="count(M_R_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="M_R_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(M_R_CITY)>0">
				<CITY>
					<xsl:value-of select="M_R_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(M_R_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="M_R_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(M_R_STREET)>0">
				<STREET>
					<xsl:value-of select="M_R_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(M_R_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="M_R_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(M_R_BUILD)>0">
				<BUILD>
					<xsl:value-of select="M_R_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(M_R_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="M_R_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(M_R_APART)>0">
				<APART>
					<xsl:value-of select="M_R_APART/@v"/>
				</APART>
				</xsl:if>
			</MA_RAD>
			<xsl:if test="count(M_R_TEL_NUM)>0">
			<MA_RTEL>
				<xsl:if test="count(M_R_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_R_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
			    <TEL_NUM>
				<xsl:value-of select="M_R_TEL_NUM/@v"/>
				</TEL_NUM>
			</MA_RTEL>
			</xsl:if>
			<xsl:if test="count(M_C_REGION)>0">
			<MA_CAD>
				<xsl:if test="count(M_C_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_C_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(M_C_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="M_C_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
	
				<POST_CODE>
					<xsl:value-of select="M_C_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="M_C_REGION/@v"/>
				</REGION>
				<xsl:if test="count(M_C_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="M_C_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(M_C_CITY)>0">
				<CITY>
					<xsl:value-of select="M_C_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(M_C_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="M_C_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(M_C_STREET)>0">
				<STREET>
					<xsl:value-of select="M_C_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(M_C_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="M_C_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(M_C_BUILD)>0">
				<BUILD>
					<xsl:value-of select="M_C_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(M_C_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="M_C_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(M_C_APART)>0">
				<APART>
					<xsl:value-of select="M_C_APART/@v"/>
				</APART>
				</xsl:if>
			</MA_CAD>
			</xsl:if>
			<xsl:if test="count(M_C_TEL_NUM)>0">
			<MA_CTEL>
				<xsl:if test="count(M_C_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_C_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="M_C_TEL_NUM/@v"/>
				</TEL_NUM>
			</MA_CTEL>
			</xsl:if>
			<xsl:if test="count(M_T_REGION)>0">
			<MA_TAD>
				<xsl:if test="count(M_T_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_T_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(M_T_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="M_T_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="M_T_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="M_T_REGION/@v"/>
				</REGION>
				<xsl:if test="count(M_T_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="M_T_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(M_T_CITY)>0">
				<CITY>
					<xsl:value-of select="M_T_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(M_T_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="M_T_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(M_T_STREET)>0">
				<STREET>
					<xsl:value-of select="M_T_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(M_T_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="M_T_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(M_T_BUILD)>0">
				<BUILD>
					<xsl:value-of select="M_T_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(M_T_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="M_T_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(M_T_APART)>0">
				<APART>
					<xsl:value-of select="M_T_APART/@v"/>
				</APART>
				</xsl:if>
			</MA_TAD>
			</xsl:if>
			<xsl:if test="count(M_1_TEL_NUM)>0">
			<MA_MTEL>
				<xsl:if test="count(M_1_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_1_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="M_1_TEL_NUM/@v"/>
				</TEL_NUM>
			</MA_MTEL>
			<xsl:if test="count(M_2_TEL_NUM)>0">
			<MA_MTEL>
				<xsl:if test="count(M_2_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_2_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="M_2_TEL_NUM/@v"/>
				</TEL_NUM>
			</MA_MTEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(M_E_ORG_NAME)>0">
			<MA_EMP>
				<xsl:if test="count(M_EMP_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_EMP_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<ORG_NAME>
					<xsl:value-of select="M_E_ORG_NAME/@v"/>
				</ORG_NAME>
				<xsl:if test="count(M_E_INN)>0">
				<INN>
					<xsl:value-of select="M_E_INN/@v"/>
				</INN>
				</xsl:if>
				<xsl:if test="count(M_JOT)>0">
				<JOT>
					<xsl:value-of select="M_JOT/@v"/>
				</JOT>
				</xsl:if>
			</MA_EMP>
			</xsl:if>
			<xsl:if test="count(M_E_TEL_NUM)>0">
			<MA_ETEL>
				<xsl:if test="count(M_E_TEL1_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_E_TEL1_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="M_E_TEL_NUM/@v"/>
				</TEL_NUM>
			</MA_ETEL>
			<xsl:if test="count(M_E_TEL_NUM_2)>0">
			<MA_ETEL>
				<xsl:if test="count(M_E_TEL2_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_E_TEL2_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="M_E_TEL_NUM_2/@v"/>
				</TEL_NUM>
			</MA_ETEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(M_E_REGION)>0">
			<MA_EAD>
				<xsl:if test="count(M_EAD_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_EAD_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(M_E_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="M_E_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="M_E_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="M_E_REGION/@v"/>
				</REGION>
				<xsl:if test="count(M_E_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="M_E_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(M_E_CITY)>0">
				<CITY>
					<xsl:value-of select="M_E_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(M_E_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="M_E_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(M_E_STREET)>0">
				<STREET>
					<xsl:value-of select="M_E_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(M_E_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="M_E_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(M_E_BUILD)>0">
				<BUILD>
					<xsl:value-of select="M_E_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(M_E_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="M_E_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(M_E_APART)>0">
				<APART>
					<xsl:value-of select="M_E_APART/@v"/>
				</APART>
				</xsl:if>
			</MA_EAD>
			</xsl:if>
			<xsl:if test="count(M_EMAIL)>0">
			<MA_EMA>
				<xsl:if test="count(M_EMA_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_EMA_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<EMAIL>
					<xsl:value-of select="M_EMAIL/@v"/>
				</EMAIL>				
			</MA_EMA>
			</xsl:if>
			<xsl:if test="count(M_CAR_REG)>0">
			<MA_VEH>
				<xsl:if test="count(M_VEH_STATUS)>0">
					<STATUS>
					<xsl:value-of select="M_VEH_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<CAR_REG>
					<xsl:value-of select="M_CAR_REG/@v"/>
				</CAR_REG>
				<xsl:if test="count(M_VIN)>0">
				<VIN>
					<xsl:value-of select="M_VIN/@v"/>
				</VIN>
				</xsl:if>
			</MA_VEH>
			</xsl:if>
		</MA>
		<xsl:if test="count(A_FNAME)>0">
				<xsl:call-template name="A0"/>
						<xsl:if test="count(A1_FNAME)>0">
				<xsl:call-template name="A1"/>
						<xsl:if test="count(A2_FNAME)>0">
				<xsl:call-template name="A2"/>
						<xsl:if test="count(A3_FNAME)>0">
				<xsl:call-template name="A3"/>
						<xsl:if test="count(A4_FNAME)>0">
				<xsl:call-template name="A4"/>
						<xsl:if test="count(A5_FNAME)>0">
				<xsl:call-template name="A5"/>
		</xsl:if>
		</xsl:if>
		</xsl:if>
		</xsl:if>
		</xsl:if>
		</xsl:if>
		</SUBMISSION>
	</xsl:template>

<xsl:template name="A0">
	<AP>
			<xsl:if test="count(A_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_STATUS/@v"/>
					</STATUS>
			</xsl:if>
			<FNAME>
				<xsl:value-of select="A_FNAME/@v"/>
			</FNAME>
			<xsl:if test="count(A_PNAME)>0">
			<PNAME>
				<xsl:value-of select="A_PNAME/@v"/>
			</PNAME>
			</xsl:if>
			<xsl:if test="count(A_LNAME)>0">
			<LNAME>
				<xsl:value-of select="A_LNAME/@v"/>
			</LNAME>
			</xsl:if>
			<DOB>
				<xsl:value-of select="A_DOB/@v"/>
			</DOB>
			<xsl:if test="count(A_PER_INN)>0">
			<PER_INN>
				<xsl:value-of select="A_PER_INN/@v"/>
			</PER_INN>
			</xsl:if>
			<xsl:if test="count(A_PREV_FN)>0">
			<PREV_FN>
				<xsl:value-of select="A_PREV_FN/@v"/>
			</PREV_FN>
			</xsl:if>
			<xsl:if test="count(A_PREV_PN)>0">
			<PREV_PN>
				<xsl:value-of select="A_PREV_PN/@v"/>
			</PREV_PN>
			</xsl:if>
			<xsl:if test="count(A_PREV_LN)>0">
			<PREV_LN>
				<xsl:value-of select="A_PREV_LN/@v"/>
			</PREV_LN>
			</xsl:if>
			<xsl:if test="count(A_INC_TM)>0">
			<INC_TM>
				<xsl:value-of select="A_INC_TM/@v"/>
			</INC_TM>
			</xsl:if>
			<xsl:if test="count(A_INC_DOC)>0">
			<INC_DOC>
				<xsl:value-of select="A_INC_DOC/@v"/>
			</INC_DOC>
			</xsl:if>
			<xsl:if test="count(A_INC_ADD)>0">
			<INC_ADD>
				<xsl:value-of select="A_INC_ADD/@v"/>
			</INC_ADD>
			</xsl:if>
			<xsl:if test="count(A_TYPE)>0">
			<APP_TYPE>
				<xsl:value-of select="A_TYPE/@v"/>
			</APP_TYPE>
			</xsl:if>
			<xsl:if test="count(A_PAS_NO)>0">
			<A_PAS>
				<xsl:if test="count(A_PAS_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_PAS_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A_PAS_NO/@v"/>
				</DOC_NUMBER>
				<DOC_DATE>
					<xsl:value-of select="A_PAS_DATE/@v"/>
				</DOC_DATE>
			</A_PAS>
			</xsl:if>
			<xsl:if test="count(A_D1_NO)>0">
			<A_DOC>
				<xsl:if test="count(A_D1_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_D1_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A_D1_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(A_D1_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="A_D1_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>
				<DOT>
					<xsl:value-of select="A_D1_DOT/@v"/>
				</DOT>
			</A_DOC>
			</xsl:if>
			<xsl:if test="count(A_D2_NO)>0">
			<A_DOC>
				<xsl:if test="count(A_D2_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_D2_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A_D2_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(A_D2_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="A_D2_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>
				<DOT>
					<xsl:value-of select="A_D2_DOT/@v"/>
				</DOT>
			</A_DOC>
			</xsl:if>
			<xsl:if test="count(A_SPO_FNAME)>0">
			<A_SPO>
				<xsl:if test="count(A_SPO_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_SPO_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<FNAME>
					<xsl:value-of select="A_SPO_FNAME/@v"/>
				</FNAME>
				<xsl:if test="count(A_SPO_PNAME)>0">
				<PNAME>
					<xsl:value-of select="A_SPO_PNAME/@v"/>
				</PNAME>
				</xsl:if>
				<xsl:if test="count(A_SPO_LNAME)>0">
				<LNAME>
					<xsl:value-of select="A_SPO_LNAME/@v"/>
				</LNAME>
				</xsl:if>
				<DOB>
					<xsl:value-of select="A_SPO_DOB/@v"/>
				</DOB>
			</A_SPO>
			</xsl:if>

			<A_RAD>
				<xsl:if test="count(A_R_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_R_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(A_R_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A_R_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A_R_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A_R_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A_R_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A_R_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A_R_CITY)>0">
				<CITY>
					<xsl:value-of select="A_R_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A_R_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A_R_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A_R_STREET)>0">
				<STREET>
					<xsl:value-of select="A_R_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A_R_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A_R_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A_R_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A_R_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A_R_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A_R_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A_R_APART)>0">
				<APART>
					<xsl:value-of select="A_R_APART/@v"/>
				</APART>
				</xsl:if>
			</A_RAD>
			<xsl:if test="count(A_R_APART)>0">
			<A_RTEL>
				<xsl:if test="count(A_R_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_R_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A_R_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_RTEL>
			</xsl:if>
			<xsl:if test="count(A_C_REGION)>0">
			<A_CAD>
				<xsl:if test="count(A_C_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_C_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(A_C_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A_C_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A_C_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A_C_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A_C_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A_C_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A_C_CITY)>0">
				<CITY>
					<xsl:value-of select="A_C_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A_C_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A_C_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A_C_STREET)>0">
				<STREET>
					<xsl:value-of select="A_C_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A_C_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A_C_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A_C_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A_C_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A_C_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A_C_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A_C_APART)>0">
				<APART>
					<xsl:value-of select="A_C_APART/@v"/>
				</APART>
				</xsl:if>
			</A_CAD>
			</xsl:if>
			<xsl:if test="count(A_C_TEL_NUM)>0">
			<A_CTEL>
				<xsl:if test="count(A_C_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_C_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A_C_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_CTEL>
			</xsl:if>
			<xsl:if test="count(A_T_REGION)>0">
			<A_TAD>
				<xsl:if test="count(A_T_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_T_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(A_T_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A_T_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A_T_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A_T_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A_T_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A_T_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A_T_CITY)>0">
				<CITY>
					<xsl:value-of select="A_T_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A_T_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A_T_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A_T_STREET)>0">
				<STREET>
					<xsl:value-of select="A_T_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A_T_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A_T_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A_T_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A_T_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A_T_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A_T_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A_T_APART)>0">
				<APART>
					<xsl:value-of select="A_T_APART/@v"/>
				</APART>
				</xsl:if>
			</A_TAD>
			</xsl:if>
			<xsl:if test="count(A_1_TEL_NUM)>0">
			<A_MTEL>
				<xsl:if test="count(A_1_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_1_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A_1_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_MTEL>
			<xsl:if test="count(A_2_TEL_NUM)>0">
			<A_MTEL>
				<xsl:if test="count(A_2_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_2_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A_2_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_MTEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(A_E_ORG_NAME)>0">
			<A_EMP>
				<xsl:if test="count(A_EMP_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_EMP_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<ORG_NAME>
					<xsl:value-of select="A_E_ORG_NAME/@v"/>
				</ORG_NAME>
	            <xsl:if test="count(A_E_INN)>0">
				<INN>
					<xsl:value-of select="A_E_INN/@v"/>
				</INN>
				</xsl:if>
				<xsl:if test="count(A_JOT)>0">
				<JOT>
					<xsl:value-of select="A_JOT/@v"/>
				</JOT>
				</xsl:if>

			</A_EMP>
			</xsl:if>
			<xsl:if test="count(A_E_TEL_NUM)>0">
			<A_ETEL>
				<xsl:if test="count(A_E_TEL1_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_E_TEL1_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A_E_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_ETEL>
			<xsl:if test="count(A_E_TEL_NUM_2)>0">
			<A_ETEL>
				<xsl:if test="count(A_E_TEL2_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_E_TEL2_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A_E_TEL_NUM_2/@v"/>
				</TEL_NUM>
			</A_ETEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(A_E_REGION)>0">
			<A_EAD>
				<xsl:if test="count(A_EAD_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_EAD_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(A_E_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A_E_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A_E_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A_E_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A_E_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A_E_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A_E_CITY)>0">
				<CITY>
					<xsl:value-of select="A_E_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A_E_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A_E_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A_E_STREET)>0">
				<STREET>
					<xsl:value-of select="A_E_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A_E_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A_E_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A_E_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A_E_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A_E_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A_E_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A_E_APART)>0">
				<APART>
					<xsl:value-of select="A_E_APART/@v"/>
				</APART>
				</xsl:if>
			</A_EAD>
			</xsl:if>
			<xsl:if test="count(A_EMAIL)>0">
			<A_EMA>
				<xsl:if test="count(A_EMA_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_EMA_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<EMAIL>
					<xsl:value-of select="A_EMAIL/@v"/>
				</EMAIL>
			</A_EMA>
			</xsl:if>
			<xsl:if test="count(A_CAR_REG)>0">
			<A_VEH>
				<xsl:if test="count(A_VEH_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A_VEH_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<CAR_REG>
					<xsl:value-of select="A_CAR_REG/@v"/>
				</CAR_REG>
				<xsl:if test="count(A_VIN)>0">
				<VIN>
					<xsl:value-of select="A_VIN/@v"/>
				</VIN>
				</xsl:if>
			</A_VEH>
			</xsl:if>
		</AP>		
</xsl:template>

<xsl:template name="A1">
	<AP>
			<xsl:if test="count(A1_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_STATUS/@v"/>
					</STATUS>
			</xsl:if>
			<FNAME>
				<xsl:value-of select="A1_FNAME/@v"/>
			</FNAME>
			<xsl:if test="count(A1_PNAME)>0">
			<PNAME>
				<xsl:value-of select="A1_PNAME/@v"/>
			</PNAME>
			</xsl:if>
			<xsl:if test="count(A1_LNAME)>0">
			<LNAME>
				<xsl:value-of select="A1_LNAME/@v"/>
			</LNAME>
			</xsl:if>
			<DOB>
				<xsl:value-of select="A1_DOB/@v"/>
			</DOB>
			<xsl:if test="count(A1_PER_INN)>0">
			<PER_INN>
				<xsl:value-of select="A1_PER_INN/@v"/>
			</PER_INN>
			</xsl:if>
			<xsl:if test="count(A1_PREV_FN)>0">
			<PREV_FN>
				<xsl:value-of select="A1_PREV_FN/@v"/>
			</PREV_FN>
			</xsl:if>
			<xsl:if test="count(A1_PREV_PN)>0">
			<PREV_PN>
				<xsl:value-of select="A1_PREV_PN/@v"/>
			</PREV_PN>
			</xsl:if>
			<xsl:if test="count(A1_PREV_LN)>0">
			<PREV_LN>
				<xsl:value-of select="A1_PREV_LN/@v"/>
			</PREV_LN>
			</xsl:if>
			<xsl:if test="count(A1_INC_TM)>0">
			<INC_TM>
				<xsl:value-of select="A1_INC_TM/@v"/>
			</INC_TM>
			</xsl:if>
			<xsl:if test="count(A1_INC_DOC)>0">
			<INC_DOC>
				<xsl:value-of select="A1_INC_DOC/@v"/>
			</INC_DOC>
			</xsl:if>
			<xsl:if test="count(A1_INC_ADD)>0">
			<INC_ADD>
				<xsl:value-of select="A1_INC_ADD/@v"/>
			</INC_ADD>
			</xsl:if>
			<xsl:if test="count(A1_TYPE)>0">
			<APP_TYPE>
				<xsl:value-of select="A1_TYPE/@v"/>
			</APP_TYPE>
			</xsl:if>
			<xsl:if test="count(A1_PAS_NO)>0">
			<A_PAS>
				<xsl:if test="count(A1_PAS_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_PAS_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A1_PAS_NO/@v"/>
				</DOC_NUMBER>
				<DOC_DATE>
					<xsl:value-of select="A1_PAS_DATE/@v"/>
				</DOC_DATE>
			</A_PAS>
			</xsl:if>
			<xsl:if test="count(A1_D1_NO)>0">
			<A_DOC>
				<xsl:if test="count(A1_D1_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_D1_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A1_D1_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(A1_D1_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="A1_D1_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>
				<DOT>
					<xsl:value-of select="A1_D1_DOT/@v"/>
				</DOT>
			</A_DOC>
			</xsl:if>
			<xsl:if test="count(A1_D2_NO)>0">
			<A_DOC>
				<xsl:if test="count(A1_D2_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_D2_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A1_D2_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(A1_D2_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="A1_D2_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>
				<DOT>
					<xsl:value-of select="A1_D2_DOT/@v"/>
				</DOT>
			</A_DOC>
			</xsl:if>
			<xsl:if test="count(A1_SPO_FNAME)>0">
			<A_SPO>
				<xsl:if test="count(A1_SPO_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_SPO_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<FNAME>
					<xsl:value-of select="A1_SPO_FNAME/@v"/>
				</FNAME>
				<xsl:if test="count(A1_SPO_PNAME)>0">
				<PNAME>
					<xsl:value-of select="A1_SPO_PNAME/@v"/>
				</PNAME>
				</xsl:if>
				<xsl:if test="count(A1_SPO_LNAME)>0">
				<LNAME>
					<xsl:value-of select="A1_SPO_LNAME/@v"/>
				</LNAME>
				</xsl:if>
				<DOB>
					<xsl:value-of select="A1_SPO_DOB/@v"/>
				</DOB>
			</A_SPO>
			</xsl:if>

			<A_RAD>
				<xsl:if test="count(A1_R_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_R_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(A1_R_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A1_R_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A1_R_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A1_R_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A1_R_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A1_R_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A1_R_CITY)>0">
				<CITY>
					<xsl:value-of select="A1_R_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A1_R_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A1_R_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A1_R_STREET)>0">
				<STREET>
					<xsl:value-of select="A1_R_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A1_R_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A1_R_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A1_R_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A1_R_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A1_R_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A1_R_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A1_R_APART)>0">
				<APART>
					<xsl:value-of select="A1_R_APART/@v"/>
				</APART>
				</xsl:if>
			</A_RAD>
			<xsl:if test="count(A1_R_APART)>0">
			<A_RTEL>
				<xsl:if test="count(A1_R_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_R_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A1_R_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_RTEL>
			</xsl:if>
			<xsl:if test="count(A1_C_REGION)>0">
			<A_CAD>
				<xsl:if test="count(A1_C_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_C_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(A1_C_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A1_C_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A1_C_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A1_C_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A1_C_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A1_C_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A1_C_CITY)>0">
				<CITY>
					<xsl:value-of select="A1_C_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A1_C_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A1_C_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A1_C_STREET)>0">
				<STREET>
					<xsl:value-of select="A1_C_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A1_C_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A1_C_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A1_C_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A1_C_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A1_C_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A1_C_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A1_C_APART)>0">
				<APART>
					<xsl:value-of select="A1_C_APART/@v"/>
				</APART>
				</xsl:if>
			</A_CAD>
			</xsl:if>
			<xsl:if test="count(A1_C_TEL_NUM)>0">
			<A_CTEL>
				<xsl:if test="count(A1_C_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_C_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A1_C_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_CTEL>
			</xsl:if>
			<xsl:if test="count(A1_T_REGION)>0">
			<A_TAD>
				<xsl:if test="count(A1_T_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_T_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(A1_T_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A1_T_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A1_T_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A1_T_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A1_T_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A1_T_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A1_T_CITY)>0">
				<CITY>
					<xsl:value-of select="A1_T_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A1_T_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A1_T_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A1_T_STREET)>0">
				<STREET>
					<xsl:value-of select="A1_T_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A1_T_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A1_T_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A1_T_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A1_T_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A1_T_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A1_T_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A1_T_APART)>0">
				<APART>
					<xsl:value-of select="A1_T_APART/@v"/>
				</APART>
				</xsl:if>
			</A_TAD>
			</xsl:if>
			<xsl:if test="count(A1_1_TEL_NUM)>0">
			<A_MTEL>
				<xsl:if test="count(A1_1_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_1_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A1_1_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_MTEL>
			<xsl:if test="count(A1_2_TEL_NUM)>0">
			<A_MTEL>
				<xsl:if test="count(A1_2_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_2_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A1_2_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_MTEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(A1_E_ORG_NAME)>0">
			<A_EMP>
				<xsl:if test="count(A1_EMP_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_EMP_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<ORG_NAME>
					<xsl:value-of select="A1_E_ORG_NAME/@v"/>
				</ORG_NAME>
				<xsl:if test="count(A1_E_INN)>0">
				<INN>
					<xsl:value-of select="A1_E_INN/@v"/>
				</INN>
				</xsl:if>
				<xsl:if test="count(A1_JOT)>0">
				<JOT>
					<xsl:value-of select="A1_JOT/@v"/>
				</JOT>
				</xsl:if>				
			</A_EMP>
			</xsl:if>
			<xsl:if test="count(A1_E_TEL_NUM)>0">
			<A_ETEL>
				<xsl:if test="count(A1_E_TEL1_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_E_TEL1_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A1_E_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_ETEL>
			<xsl:if test="count(A1_E_TEL_NUM_2)>0">
			<A_ETEL>
				<xsl:if test="count(A1_E_TEL2_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_E_TEL2_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A1_E_TEL_NUM_2/@v"/>
				</TEL_NUM>
			</A_ETEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(A1_E_REGION)>0">
			<A_EAD>
				<xsl:if test="count(A1_EAD_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_EAD_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(A1_E_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A1_E_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A1_E_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A1_E_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A1_E_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A1_E_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A1_E_CITY)>0">
				<CITY>
					<xsl:value-of select="A1_E_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A1_E_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A1_E_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A1_E_STREET)>0">
				<STREET>
					<xsl:value-of select="A1_E_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A1_E_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A1_E_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A1_E_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A1_E_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A1_E_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A1_E_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A1_E_APART)>0">
				<APART>
					<xsl:value-of select="A1_E_APART/@v"/>
				</APART>
				</xsl:if>
			</A_EAD>
			</xsl:if>
			<xsl:if test="count(A1_EMAIL)>0">
			<A_EMA>
				<xsl:if test="count(A1_EMA_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_EMA_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<EMAIL>
					<xsl:value-of select="A1_EMAIL/@v"/>
				</EMAIL>
			</A_EMA>
			</xsl:if>
			<xsl:if test="count(A1_CAR_REG)>0">
			<A_VEH>
				<xsl:if test="count(A1_VEH_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A1_VEH_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<CAR_REG>
					<xsl:value-of select="A1_CAR_REG/@v"/>
				</CAR_REG>
				<xsl:if test="count(A1_VIN)>0">
				<VIN>
					<xsl:value-of select="A1_VIN/@v"/>
				</VIN>
				</xsl:if>
			</A_VEH>
			</xsl:if>
		</AP>		
</xsl:template>

<xsl:template name="A2">
	<AP>
			<xsl:if test="count(A2_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A2_STATUS/@v"/>
					</STATUS>
			</xsl:if>
			<FNAME>
				<xsl:value-of select="A2_FNAME/@v"/>
			</FNAME>
			<xsl:if test="count(A2_PNAME)>0">
			<PNAME>
				<xsl:value-of select="A2_PNAME/@v"/>
			</PNAME>
			</xsl:if>
			<xsl:if test="count(A2_LNAME)>0">
			<LNAME>
				<xsl:value-of select="A2_LNAME/@v"/>
			</LNAME>
			</xsl:if>
			<DOB>
				<xsl:value-of select="A2_DOB/@v"/>
			</DOB>
			<xsl:if test="count(A2_PER_INN)>0">
			<PER_INN>
				<xsl:value-of select="A2_PER_INN/@v"/>
			</PER_INN>
			</xsl:if>
			<xsl:if test="count(A2_PREV_FN)>0">
			<PREV_FN>
				<xsl:value-of select="A2_PREV_FN/@v"/>
			</PREV_FN>
			</xsl:if>
			<xsl:if test="count(A2_PREV_PN)>0">
			<PREV_PN>
				<xsl:value-of select="A2_PREV_PN/@v"/>
			</PREV_PN>
			</xsl:if>
			<xsl:if test="count(A2_PREV_LN)>0">
			<PREV_LN>
				<xsl:value-of select="A2_PREV_LN/@v"/>
			</PREV_LN>
			</xsl:if>
			<xsl:if test="count(A2_INC_TM)>0">
			<INC_TM>
				<xsl:value-of select="A2_INC_TM/@v"/>
			</INC_TM>
			</xsl:if>
			<xsl:if test="count(A2_INC_DOC)>0">
			<INC_DOC>
				<xsl:value-of select="A2_INC_DOC/@v"/>
			</INC_DOC>
			</xsl:if>
			<xsl:if test="count(A2_INC_ADD)>0">
			<INC_ADD>
				<xsl:value-of select="A2_INC_ADD/@v"/>
			</INC_ADD>
			</xsl:if>
			<xsl:if test="count(A2_TYPE)>0">
			<APP_TYPE>
				<xsl:value-of select="A2_TYPE/@v"/>
			</APP_TYPE>
			</xsl:if>
			<xsl:if test="count(A2_PAS_NO)>0">
			<A_PAS>
				<xsl:if test="count(A2_PAS_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_PAS_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A2_PAS_NO/@v"/>
				</DOC_NUMBER>
				<DOC_DATE>
					<xsl:value-of select="A2_PAS_DATE/@v"/>
				</DOC_DATE>
			</A_PAS>
			</xsl:if>
			<xsl:if test="count(A2_D1_NO)>0">
			<A_DOC>
				<xsl:if test="count(A2_D1_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_D1_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A2_D1_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(A2_D1_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="A2_D1_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>
				<DOT>
					<xsl:value-of select="A2_D1_DOT/@v"/>
				</DOT>
			</A_DOC>
			</xsl:if>
			<xsl:if test="count(A2_D2_NO)>0">
			<A_DOC>
				<xsl:if test="count(A2_D2_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_D2_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A2_D2_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(A2_D2_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="A2_D2_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>
				<DOT>
					<xsl:value-of select="A2_D2_DOT/@v"/>
				</DOT>
			</A_DOC>
			</xsl:if>
			<xsl:if test="count(A2_SPO_FNAME)>0">
			<A_SPO>
				<xsl:if test="count(A2_SPO_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_SPO_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<FNAME>
					<xsl:value-of select="A2_SPO_FNAME/@v"/>
				</FNAME>
				<xsl:if test="count(A2_SPO_PNAME)>0">
				<PNAME>
					<xsl:value-of select="A2_SPO_PNAME/@v"/>
				</PNAME>
				</xsl:if>
				<xsl:if test="count(A2_SPO_LNAME)>0">
				<LNAME>
					<xsl:value-of select="A2_SPO_LNAME/@v"/>
				</LNAME>
				</xsl:if>
				<DOB>
					<xsl:value-of select="A2_SPO_DOB/@v"/>
				</DOB>
			</A_SPO>
			</xsl:if>

			<A_RAD>
				<xsl:if test="count(A2_R_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_R_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<xsl:if test="count(A2_R_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A2_R_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A2_R_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A2_R_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A2_R_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A2_R_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A2_R_CITY)>0">
				<CITY>
					<xsl:value-of select="A2_R_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A2_R_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A2_R_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A2_R_STREET)>0">
				<STREET>
					<xsl:value-of select="A2_R_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A2_R_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A2_R_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A2_R_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A2_R_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A2_R_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A2_R_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A2_R_APART)>0">
				<APART>
					<xsl:value-of select="A2_R_APART/@v"/>
				</APART>
				</xsl:if>
			</A_RAD>
			<xsl:if test="count(A2_R_APART)>0">
			<A_RTEL>
				<xsl:if test="count(A2_R_TEL_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_R_TEL_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A2_R_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_RTEL>
			</xsl:if>
			<xsl:if test="count(A2_C_REGION)>0">
			<A_CAD>
				<xsl:if test="count(A2_C_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_C_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<xsl:if test="count(A2_C_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A2_C_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A2_C_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A2_C_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A2_C_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A2_C_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A2_C_CITY)>0">
				<CITY>
					<xsl:value-of select="A2_C_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A2_C_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A2_C_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A2_C_STREET)>0">
				<STREET>
					<xsl:value-of select="A2_C_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A2_C_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A2_C_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A2_C_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A2_C_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A2_C_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A2_C_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A2_C_APART)>0">
				<APART>
					<xsl:value-of select="A2_C_APART/@v"/>
				</APART>
				</xsl:if>
			</A_CAD>
			</xsl:if>
			<xsl:if test="count(A2_C_TEL_NUM)>0">
			<A_CTEL>
				<xsl:if test="count(A2_C_TEL_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_C_TEL_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A2_C_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_CTEL>
			</xsl:if>
			<xsl:if test="count(A2_T_REGION)>0">
			<A_TAD>
				<xsl:if test="count(A2_T_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_T_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<xsl:if test="count(A2_T_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A2_T_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A2_T_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A2_T_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A2_T_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A2_T_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A2_T_CITY)>0">
				<CITY>
					<xsl:value-of select="A2_T_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A2_T_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A2_T_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A2_T_STREET)>0">
				<STREET>
					<xsl:value-of select="A2_T_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A2_T_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A2_T_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A2_T_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A2_T_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A2_T_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A2_T_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A2_T_APART)>0">
				<APART>
					<xsl:value-of select="A2_T_APART/@v"/>
				</APART>
				</xsl:if>
			</A_TAD>
			</xsl:if>
			<xsl:if test="count(A2_1_TEL_NUM)>0">
			<A_MTEL>
				<xsl:if test="count(A2_1_TEL_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_1_TEL_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A2_1_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_MTEL>
			<xsl:if test="count(A2_2_TEL_NUM)>0">
			<A_MTEL>
				<xsl:if test="count(A2_2_TEL_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_2_TEL_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A2_2_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_MTEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(A2_E_ORG_NAME)>0">
			<A_EMP>
				<xsl:if test="count(A2_EMP_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_EMP_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<ORG_NAME>
					<xsl:value-of select="A2_E_ORG_NAME/@v"/>
				</ORG_NAME>
				<xsl:if test="count(A2_E_INN)>0">
				<INN>
					<xsl:value-of select="A2_E_INN/@v"/>
				</INN>
				</xsl:if>
				<xsl:if test="count(A2_JOT)>0">
				<JOT>
					<xsl:value-of select="A2_JOT/@v"/>
				</JOT>
				</xsl:if>				
			</A_EMP>
			</xsl:if>
			<xsl:if test="count(A2_E_TEL_NUM)>0">
			<A_ETEL>
				<xsl:if test="count(A2_E_TEL1_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_E_TEL1_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A2_E_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_ETEL>
			<xsl:if test="count(A2_E_TEL_NUM_2)>0">
			<A_ETEL>
				<xsl:if test="count(A2_E_TEL2_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_E_TEL2_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A2_E_TEL_NUM_2/@v"/>
				</TEL_NUM>
			</A_ETEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(A2_E_REGION)>0">
			<A_EAD>
				<xsl:if test="count(A2_EAD_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_EAD_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<xsl:if test="count(A2_E_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A2_E_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A2_E_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A2_E_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A2_E_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A2_E_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A2_E_CITY)>0">
				<CITY>
					<xsl:value-of select="A2_E_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A2_E_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A2_E_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A2_E_STREET)>0">
				<STREET>
					<xsl:value-of select="A2_E_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A2_E_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A2_E_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A2_E_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A2_E_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A2_E_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A2_E_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A2_E_APART)>0">
				<APART>
					<xsl:value-of select="A2_E_APART/@v"/>
				</APART>
				</xsl:if>
			</A_EAD>
			</xsl:if>
			<xsl:if test="count(A2_EMAIL)>0">
			<A_EMA>
				<xsl:if test="count(A2_EMA_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_EMA_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<EMAIL>
					<xsl:value-of select="A2_EMAIL/@v"/>
				</EMAIL>
			</A_EMA>
			</xsl:if>
			<xsl:if test="count(A2_CAR_REG)>0">
			<A_VEH>
				<xsl:if test="count(A2_VEH_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A2_VEH_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<CAR_REG>
					<xsl:value-of select="A2_CAR_REG/@v"/>
				</CAR_REG>
				<xsl:if test="count(A2_VIN)>0">
				<VIN>
					<xsl:value-of select="A2_VIN/@v"/>
				</VIN>
				</xsl:if>
			</A_VEH>
			</xsl:if>
		</AP>		
</xsl:template>

<xsl:template name="A3">
	<AP>
			<xsl:if test="count(A3_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A3_STATUS/@v"/>
					</STATUS>
			</xsl:if>
			<FNAME>
				<xsl:value-of select="A3_FNAME/@v"/>
			</FNAME>
			<xsl:if test="count(A3_PNAME)>0">
			<PNAME>
				<xsl:value-of select="A3_PNAME/@v"/>
			</PNAME>
			</xsl:if>
			<xsl:if test="count(A3_LNAME)>0">
			<LNAME>
				<xsl:value-of select="A3_LNAME/@v"/>
			</LNAME>
			</xsl:if>
			<DOB>
				<xsl:value-of select="A3_DOB/@v"/>
			</DOB>
			<xsl:if test="count(A3_PER_INN)>0">
			<PER_INN>
				<xsl:value-of select="A3_PER_INN/@v"/>
			</PER_INN>
			</xsl:if>
			<xsl:if test="count(A3_PREV_FN)>0">
			<PREV_FN>
				<xsl:value-of select="A3_PREV_FN/@v"/>
			</PREV_FN>
			</xsl:if>
			<xsl:if test="count(A3_PREV_PN)>0">
			<PREV_PN>
				<xsl:value-of select="A3_PREV_PN/@v"/>
			</PREV_PN>
			</xsl:if>
			<xsl:if test="count(A3_PREV_LN)>0">
			<PREV_LN>
				<xsl:value-of select="A3_PREV_LN/@v"/>
			</PREV_LN>
			</xsl:if>
			<xsl:if test="count(A3_INC_TM)>0">
			<INC_TM>
				<xsl:value-of select="A3_INC_TM/@v"/>
			</INC_TM>
			</xsl:if>
			<xsl:if test="count(A3_INC_DOC)>0">
			<INC_DOC>
				<xsl:value-of select="A3_INC_DOC/@v"/>
			</INC_DOC>
			</xsl:if>
			<xsl:if test="count(A3_INC_ADD)>0">
			<INC_ADD>
				<xsl:value-of select="A3_INC_ADD/@v"/>
			</INC_ADD>
			</xsl:if>
			<xsl:if test="count(A3_TYPE)>0">
			<APP_TYPE>
				<xsl:value-of select="A3_TYPE/@v"/>
			</APP_TYPE>
			</xsl:if>
			<xsl:if test="count(A3_PAS_NO)>0">
			<A_PAS>
				<xsl:if test="count(A3_PAS_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_PAS_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A3_PAS_NO/@v"/>
				</DOC_NUMBER>
				<DOC_DATE>
					<xsl:value-of select="A3_PAS_DATE/@v"/>
				</DOC_DATE>
			</A_PAS>
			</xsl:if>
			<xsl:if test="count(A3_D1_NO)>0">
			<A_DOC>
				<xsl:if test="count(A3_D1_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_D1_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A3_D1_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(A3_D1_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="A3_D1_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>
				<DOT>
					<xsl:value-of select="A3_D1_DOT/@v"/>
				</DOT>
			</A_DOC>
			</xsl:if>
			<xsl:if test="count(A3_D2_NO)>0">
			<A_DOC>
				<xsl:if test="count(A3_D2_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_D2_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A3_D2_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(A3_D2_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="A3_D2_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>
				<DOT>
					<xsl:value-of select="A3_D2_DOT/@v"/>
				</DOT>
			</A_DOC>
			</xsl:if>
			<xsl:if test="count(A3_SPO_FNAME)>0">
			<A_SPO>
				<xsl:if test="count(A3_SPO_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_SPO_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<FNAME>
					<xsl:value-of select="A3_SPO_FNAME/@v"/>
				</FNAME>
				<xsl:if test="count(A3_SPO_PNAME)>0">
				<PNAME>
					<xsl:value-of select="A3_SPO_PNAME/@v"/>
				</PNAME>
				</xsl:if>
				<xsl:if test="count(A3_SPO_LNAME)>0">
				<LNAME>
					<xsl:value-of select="A3_SPO_LNAME/@v"/>
				</LNAME>
				</xsl:if>
				<DOB>
					<xsl:value-of select="A3_SPO_DOB/@v"/>
				</DOB>
			</A_SPO>
			</xsl:if>

			<A_RAD>
				<xsl:if test="count(A3_R_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_R_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<xsl:if test="count(A3_R_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A3_R_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A3_R_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A3_R_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A3_R_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A3_R_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A3_R_CITY)>0">
				<CITY>
					<xsl:value-of select="A3_R_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A3_R_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A3_R_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A3_R_STREET)>0">
				<STREET>
					<xsl:value-of select="A3_R_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A3_R_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A3_R_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A3_R_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A3_R_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A3_R_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A3_R_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A3_R_APART)>0">
				<APART>
					<xsl:value-of select="A3_R_APART/@v"/>
				</APART>
				</xsl:if>
			</A_RAD>
			<xsl:if test="count(A3_R_APART)>0">
			<A_RTEL>
				<xsl:if test="count(A3_R_TEL_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_R_TEL_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A3_R_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_RTEL>
			</xsl:if>
			<xsl:if test="count(A3_C_REGION)>0">
			<A_CAD>
				<xsl:if test="count(A3_C_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_C_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<xsl:if test="count(A3_C_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A3_C_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A3_C_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A3_C_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A3_C_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A3_C_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A3_C_CITY)>0">
				<CITY>
					<xsl:value-of select="A3_C_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A3_C_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A3_C_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A3_C_STREET)>0">
				<STREET>
					<xsl:value-of select="A3_C_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A3_C_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A3_C_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A3_C_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A3_C_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A3_C_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A3_C_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A3_C_APART)>0">
				<APART>
					<xsl:value-of select="A3_C_APART/@v"/>
				</APART>
				</xsl:if>
			</A_CAD>
			</xsl:if>
			<xsl:if test="count(A3_C_TEL_NUM)>0">
			<A_CTEL>
				<xsl:if test="count(A3_C_TEL_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_C_TEL_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A3_C_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_CTEL>
			</xsl:if>
			<xsl:if test="count(A3_T_REGION)>0">
			<A_TAD>
				<xsl:if test="count(A3_T_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_T_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<xsl:if test="count(A3_T_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A3_T_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A3_T_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A3_T_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A3_T_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A3_T_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A3_T_CITY)>0">
				<CITY>
					<xsl:value-of select="A3_T_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A3_T_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A3_T_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A3_T_STREET)>0">
				<STREET>
					<xsl:value-of select="A3_T_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A3_T_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A3_T_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A3_T_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A3_T_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A3_T_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A3_T_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A3_T_APART)>0">
				<APART>
					<xsl:value-of select="A3_T_APART/@v"/>
				</APART>
				</xsl:if>
			</A_TAD>
			</xsl:if>
			<xsl:if test="count(A3_1_TEL_NUM)>0">
			<A_MTEL>
				<xsl:if test="count(A3_1_TEL_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_1_TEL_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A3_1_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_MTEL>
			<xsl:if test="count(A3_2_TEL_NUM)>0">
			<A_MTEL>
				<xsl:if test="count(A3_2_TEL_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_2_TEL_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A3_2_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_MTEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(A3_E_ORG_NAME)>0">
			<A_EMP>
				<xsl:if test="count(A3_EMP_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_EMP_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<ORG_NAME>
					<xsl:value-of select="A3_E_ORG_NAME/@v"/>
				</ORG_NAME>
				<xsl:if test="count(A3_E_INN)>0">
				<INN>
					<xsl:value-of select="A3_E_INN/@v"/>
				</INN>
				</xsl:if>
				<xsl:if test="count(A3_JOT)>0">
				<JOT>
					<xsl:value-of select="A3_JOT/@v"/>
				</JOT>
				</xsl:if>				
			</A_EMP>
			</xsl:if>
			<xsl:if test="count(A3_E_TEL_NUM)>0">
			<A_ETEL>
				<xsl:if test="count(A3_E_TEL1_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_E_TEL1_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A3_E_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_ETEL>
			<xsl:if test="count(A3_E_TEL_NUM_2)>0">
			<A_ETEL>
				<xsl:if test="count(A3_E_TEL2_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_E_TEL2_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A3_E_TEL_NUM_2/@v"/>
				</TEL_NUM>
			</A_ETEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(A3_E_REGION)>0">
			<A_EAD>
				<xsl:if test="count(A3_EAD_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_EAD_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<xsl:if test="count(A3_E_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A3_E_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A3_E_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A3_E_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A3_E_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A3_E_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A3_E_CITY)>0">
				<CITY>
					<xsl:value-of select="A3_E_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A3_E_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A3_E_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A3_E_STREET)>0">
				<STREET>
					<xsl:value-of select="A3_E_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A3_E_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A3_E_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A3_E_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A3_E_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A3_E_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A3_E_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A3_E_APART)>0">
				<APART>
					<xsl:value-of select="A3_E_APART/@v"/>
				</APART>
				</xsl:if>
			</A_EAD>
			</xsl:if>
			<xsl:if test="count(A3_EMAIL)>0">
			<A_EMA>
				<xsl:if test="count(A3_EMA_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_EMA_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<EMAIL>
					<xsl:value-of select="A3_EMAIL/@v"/>
				</EMAIL>
			</A_EMA>
			</xsl:if>
			<xsl:if test="count(A3_CAR_REG)>0">
			<A_VEH>
				<xsl:if test="count(A3_VEH_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A3_VEH_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<CAR_REG>
					<xsl:value-of select="A3_CAR_REG/@v"/>
				</CAR_REG>
				<xsl:if test="count(A3_VIN)>0">
				<VIN>
					<xsl:value-of select="A3_VIN/@v"/>
				</VIN>
				</xsl:if>
			</A_VEH>
			</xsl:if>
		</AP>		
</xsl:template>
<xsl:template name="A4">
	<AP>
			<xsl:if test="count(A4_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A4_STATUS/@v"/>
					</STATUS>
			</xsl:if>
			<FNAME>
				<xsl:value-of select="A4_FNAME/@v"/>
			</FNAME>
			<xsl:if test="count(A4_PNAME)>0">
			<PNAME>
				<xsl:value-of select="A4_PNAME/@v"/>
			</PNAME>
			</xsl:if>
			<xsl:if test="count(A4_LNAME)>0">
			<LNAME>
				<xsl:value-of select="A4_LNAME/@v"/>
			</LNAME>
			</xsl:if>
			<DOB>
				<xsl:value-of select="A4_DOB/@v"/>
			</DOB>
			<xsl:if test="count(A4_PER_INN)>0">
			<PER_INN>
				<xsl:value-of select="A4_PER_INN/@v"/>
			</PER_INN>
			</xsl:if>
			<xsl:if test="count(A4_PREV_FN)>0">
			<PREV_FN>
				<xsl:value-of select="A4_PREV_FN/@v"/>
			</PREV_FN>
			</xsl:if>
			<xsl:if test="count(A4_PREV_PN)>0">
			<PREV_PN>
				<xsl:value-of select="A4_PREV_PN/@v"/>
			</PREV_PN>
			</xsl:if>
			<xsl:if test="count(A4_PREV_LN)>0">
			<PREV_LN>
				<xsl:value-of select="A4_PREV_LN/@v"/>
			</PREV_LN>
			</xsl:if>
			<xsl:if test="count(A4_INC_TM)>0">
			<INC_TM>
				<xsl:value-of select="A4_INC_TM/@v"/>
			</INC_TM>
			</xsl:if>
			<xsl:if test="count(A4_INC_DOC)>0">
			<INC_DOC>
				<xsl:value-of select="A4_INC_DOC/@v"/>
			</INC_DOC>
			</xsl:if>
			<xsl:if test="count(A4_INC_ADD)>0">
			<INC_ADD>
				<xsl:value-of select="A4_INC_ADD/@v"/>
			</INC_ADD>
			</xsl:if>
			<xsl:if test="count(A4_TYPE)>0">
			<APP_TYPE>
				<xsl:value-of select="A4_TYPE/@v"/>
			</APP_TYPE>
			</xsl:if>
			<xsl:if test="count(A4_PAS_NO)>0">
			<A_PAS>
				<xsl:if test="count(A4_PAS_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_PAS_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A4_PAS_NO/@v"/>
				</DOC_NUMBER>
				<DOC_DATE>
					<xsl:value-of select="A4_PAS_DATE/@v"/>
				</DOC_DATE>
			</A_PAS>
			</xsl:if>
			<xsl:if test="count(A4_D1_NO)>0">
			<A_DOC>
				<xsl:if test="count(A4_D1_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_D1_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A4_D1_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(A4_D1_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="A4_D1_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>
				<DOT>
					<xsl:value-of select="A4_D1_DOT/@v"/>
				</DOT>
			</A_DOC>
			</xsl:if>
			<xsl:if test="count(A4_D2_NO)>0">
			<A_DOC>
				<xsl:if test="count(A4_D2_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_D2_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A4_D2_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(A4_D2_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="A4_D2_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>
				<DOT>
					<xsl:value-of select="A4_D2_DOT/@v"/>
				</DOT>
			</A_DOC>
			</xsl:if>
			<xsl:if test="count(A4_SPO_FNAME)>0">
			<A_SPO>
				<xsl:if test="count(A4_SPO_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_SPO_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<FNAME>
					<xsl:value-of select="A4_SPO_FNAME/@v"/>
				</FNAME>
				<xsl:if test="count(A4_SPO_PNAME)>0">
				<PNAME>
					<xsl:value-of select="A4_SPO_PNAME/@v"/>
				</PNAME>
				</xsl:if>
				<xsl:if test="count(A4_SPO_LNAME)>0">
				<LNAME>
					<xsl:value-of select="A4_SPO_LNAME/@v"/>
				</LNAME>
				</xsl:if>
				<DOB>
					<xsl:value-of select="A4_SPO_DOB/@v"/>
				</DOB>
			</A_SPO>
			</xsl:if>

			<A_RAD>
				<xsl:if test="count(A4_R_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_R_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<xsl:if test="count(A4_R_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A4_R_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A4_R_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A4_R_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A4_R_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A4_R_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A4_R_CITY)>0">
				<CITY>
					<xsl:value-of select="A4_R_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A4_R_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A4_R_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A4_R_STREET)>0">
				<STREET>
					<xsl:value-of select="A4_R_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A4_R_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A4_R_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A4_R_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A4_R_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A4_R_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A4_R_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A4_R_APART)>0">
				<APART>
					<xsl:value-of select="A4_R_APART/@v"/>
				</APART>
				</xsl:if>
			</A_RAD>
			<xsl:if test="count(A4_R_APART)>0">
			<A_RTEL>
				<xsl:if test="count(A4_R_TEL_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_R_TEL_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A4_R_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_RTEL>
			</xsl:if>
			<xsl:if test="count(A4_C_REGION)>0">
			<A_CAD>
				<xsl:if test="count(A4_C_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_C_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<xsl:if test="count(A4_C_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A4_C_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A4_C_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A4_C_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A4_C_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A4_C_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A4_C_CITY)>0">
				<CITY>
					<xsl:value-of select="A4_C_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A4_C_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A4_C_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A4_C_STREET)>0">
				<STREET>
					<xsl:value-of select="A4_C_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A4_C_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A4_C_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A4_C_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A4_C_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A4_C_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A4_C_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A4_C_APART)>0">
				<APART>
					<xsl:value-of select="A4_C_APART/@v"/>
				</APART>
				</xsl:if>
			</A_CAD>
			</xsl:if>
			<xsl:if test="count(A4_C_TEL_NUM)>0">
			<A_CTEL>
				<xsl:if test="count(A4_C_TEL_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_C_TEL_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A4_C_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_CTEL>
			</xsl:if>
			<xsl:if test="count(A4_T_REGION)>0">
			<A_TAD>
				<xsl:if test="count(A4_T_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_T_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<xsl:if test="count(A4_T_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A4_T_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A4_T_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A4_T_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A4_T_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A4_T_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A4_T_CITY)>0">
				<CITY>
					<xsl:value-of select="A4_T_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A4_T_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A4_T_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A4_T_STREET)>0">
				<STREET>
					<xsl:value-of select="A4_T_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A4_T_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A4_T_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A4_T_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A4_T_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A4_T_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A4_T_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A4_T_APART)>0">
				<APART>
					<xsl:value-of select="A4_T_APART/@v"/>
				</APART>
				</xsl:if>
			</A_TAD>
			</xsl:if>
			<xsl:if test="count(A4_1_TEL_NUM)>0">
			<A_MTEL>
				<xsl:if test="count(A4_E_TEL1_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_E_TEL1_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A4_1_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_MTEL>
			<xsl:if test="count(A4_2_TEL_NUM)>0">
			<A_MTEL>
				<xsl:if test="count(A4_E_TEL2_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_E_TEL2_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A4_2_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_MTEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(A4_E_ORG_NAME)>0">
			<A_EMP>
				<xsl:if test="count(A4_EMP_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_EMP_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<ORG_NAME>
					<xsl:value-of select="A4_E_ORG_NAME/@v"/>
				</ORG_NAME>
				<xsl:if test="count(A4_E_INN)>0">
				<INN>
					<xsl:value-of select="A4_E_INN/@v"/>
				</INN>
				</xsl:if>
				<xsl:if test="count(A4_JOT)>0">
				<JOT>
					<xsl:value-of select="A4_JOT/@v"/>
				</JOT>
				</xsl:if>				
			</A_EMP>
			</xsl:if>
			<xsl:if test="count(A4_E_TEL_NUM)>0">
			<A_ETEL>
				<xsl:if test="count(A4_E_TEL1_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_E_TEL1_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A4_E_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_ETEL>
			<xsl:if test="count(A4_E_TEL_NUM_2)>0">
			<A_ETEL>
				<xsl:if test="count(A4_E_TEL2_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_E_TEL2_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A4_E_TEL_NUM_2/@v"/>
				</TEL_NUM>
			</A_ETEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(A4_E_REGION)>0">
			<A_EAD>
				<xsl:if test="count(A4_EAD_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_EAD_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<xsl:if test="count(A4_E_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A4_E_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A4_E_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A4_E_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A4_E_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A4_E_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A4_E_CITY)>0">
				<CITY>
					<xsl:value-of select="A4_E_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A4_E_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A4_E_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A4_E_STREET)>0">
				<STREET>
					<xsl:value-of select="A4_E_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A4_E_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A4_E_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A4_E_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A4_E_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A4_E_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A4_E_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A4_E_APART)>0">
				<APART>
					<xsl:value-of select="A4_E_APART/@v"/>
				</APART>
				</xsl:if>
			</A_EAD>
			</xsl:if>
			<xsl:if test="count(A4_EMAIL)>0">
			<A_EMA>
				<xsl:if test="count(A4_EMA_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_EMA_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<EMAIL>
					<xsl:value-of select="A4_EMAIL/@v"/>
				</EMAIL>
			</A_EMA>
			</xsl:if>
			<xsl:if test="count(A4_CAR_REG)>0">
			<A_VEH>
				<xsl:if test="count(A4_VEH_STATUS)>0">
						<STATUS>
						<xsl:value-of select="A4_VEH_STATUS/@v"/>
						</STATUS>
				</xsl:if>
				<CAR_REG>
					<xsl:value-of select="A4_CAR_REG/@v"/>
				</CAR_REG>
				<xsl:if test="count(A4_VIN)>0">
				<VIN>
					<xsl:value-of select="A4_VIN/@v"/>
				</VIN>
				</xsl:if>
			</A_VEH>
			</xsl:if>
		</AP>		
</xsl:template>
<xsl:template name="A5">
	<AP>
			<xsl:if test="count(A5_STATUS)>0">
				<STATUS>
				<xsl:value-of select="A5_STATUS/@v"/>
				</STATUS>
			</xsl:if>
			<FNAME>
				<xsl:value-of select="A5_FNAME/@v"/>
			</FNAME>
			<xsl:if test="count(A5_PNAME)>0">
			<PNAME>
				<xsl:value-of select="A5_PNAME/@v"/>
			</PNAME>
			</xsl:if>
			<xsl:if test="count(A5_LNAME)>0">
			<LNAME>
				<xsl:value-of select="A5_LNAME/@v"/>
			</LNAME>
			</xsl:if>
			<DOB>
				<xsl:value-of select="A5_DOB/@v"/>
			</DOB>
			<xsl:if test="count(A5_PER_INN)>0">
			<PER_INN>
				<xsl:value-of select="A5_PER_INN/@v"/>
			</PER_INN>
			</xsl:if>
			<xsl:if test="count(A5_PREV_FN)>0">
			<PREV_FN>
				<xsl:value-of select="A5_PREV_FN/@v"/>
			</PREV_FN>
			</xsl:if>
			<xsl:if test="count(A5_PREV_PN)>0">
			<PREV_PN>
				<xsl:value-of select="A5_PREV_PN/@v"/>
			</PREV_PN>
			</xsl:if>
			<xsl:if test="count(A5_PREV_LN)>0">
			<PREV_LN>
				<xsl:value-of select="A5_PREV_LN/@v"/>
			</PREV_LN>
			</xsl:if>
			<xsl:if test="count(A5_INC_TM)>0">
			<INC_TM>
				<xsl:value-of select="A5_INC_TM/@v"/>
			</INC_TM>
			</xsl:if>
			<xsl:if test="count(A5_INC_DOC)>0">
			<INC_DOC>
				<xsl:value-of select="A5_INC_DOC/@v"/>
			</INC_DOC>
			</xsl:if>
			<xsl:if test="count(A5_INC_ADD)>0">
			<INC_ADD>
				<xsl:value-of select="A5_INC_ADD/@v"/>
			</INC_ADD>
			</xsl:if>
			<xsl:if test="count(A5_TYPE)>0">
			<APP_TYPE>
				<xsl:value-of select="A5_TYPE/@v"/>
			</APP_TYPE>
			</xsl:if>
			<xsl:if test="count(A5_PAS_NO)>0">
			<A_PAS>
			<xsl:if test="count(A5_PAS_STATUS)>0">
				<STATUS>
				<xsl:value-of select="A5_PAS_STATUS/@v"/>
				</STATUS>
			</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A5_PAS_NO/@v"/>
				</DOC_NUMBER>
				<DOC_DATE>
					<xsl:value-of select="A5_PAS_DATE/@v"/>
				</DOC_DATE>
			</A_PAS>
			</xsl:if>
			<xsl:if test="count(A5_D1_NO)>0">
			<A_DOC>
				<xsl:if test="count(A5_D1_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_D1_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A5_D1_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(A5_D1_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="A5_D1_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>
				<DOT>
					<xsl:value-of select="A5_D1_DOT/@v"/>
				</DOT>
			</A_DOC>
			</xsl:if>
			<xsl:if test="count(A5_D2_NO)>0">
			<A_DOC>
				<xsl:if test="count(A5_D2_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_D2_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<DOC_NUMBER>
					<xsl:value-of select="A5_D2_NO/@v"/>
				</DOC_NUMBER>
				<xsl:if test="count(A5_D2_DATE)>0">
					<DOC_DATE>
						<xsl:value-of select="A5_D2_DATE/@v"/>
					</DOC_DATE>
				</xsl:if>
				<DOT>
					<xsl:value-of select="A5_D2_DOT/@v"/>
				</DOT>
			</A_DOC>
			</xsl:if>
			<xsl:if test="count(A5_SPO_FNAME)>0">
			<A_SPO>
				<xsl:if test="count(A5_SPO_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_SPO_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<FNAME>
					<xsl:value-of select="A5_SPO_FNAME/@v"/>
				</FNAME>
				<xsl:if test="count(A5_SPO_PNAME)>0">
				<PNAME>
					<xsl:value-of select="A5_SPO_PNAME/@v"/>
				</PNAME>
				</xsl:if>
				<xsl:if test="count(A5_SPO_LNAME)>0">
				<LNAME>
					<xsl:value-of select="A5_SPO_LNAME/@v"/>
				</LNAME>
				</xsl:if>
				<DOB>
					<xsl:value-of select="A5_SPO_DOB/@v"/>
				</DOB>
			</A_SPO>
			</xsl:if>

			<A_RAD>
				<xsl:if test="count(A5_R_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_R_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(A5_R_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A5_R_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A5_R_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A5_R_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A5_R_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A5_R_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A5_R_CITY)>0">
				<CITY>
					<xsl:value-of select="A5_R_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A5_R_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A5_R_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A5_R_STREET)>0">
				<STREET>
					<xsl:value-of select="A5_R_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A5_R_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A5_R_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A5_R_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A5_R_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A5_R_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A5_R_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A5_R_APART)>0">
				<APART>
					<xsl:value-of select="A5_R_APART/@v"/>
				</APART>
				</xsl:if>
			</A_RAD>
			<xsl:if test="count(A5_R_APART)>0">
			<A_RTEL>
				<xsl:if test="count(A5_R_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_R_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A5_R_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_RTEL>
			</xsl:if>
			<xsl:if test="count(A5_C_REGION)>0">
			<A_CAD>
				<xsl:if test="count(A5_C_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_C_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(A5_C_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A5_C_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A5_C_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A5_C_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A5_C_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A5_C_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A5_C_CITY)>0">
				<CITY>
					<xsl:value-of select="A5_C_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A5_C_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A5_C_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A5_C_STREET)>0">
				<STREET>
					<xsl:value-of select="A5_C_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A5_C_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A5_C_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A5_C_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A5_C_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A5_C_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A5_C_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A5_C_APART)>0">
				<APART>
					<xsl:value-of select="A5_C_APART/@v"/>
				</APART>
				</xsl:if>
			</A_CAD>
			</xsl:if>
			<xsl:if test="count(A5_C_TEL_NUM)>0">
			<A_CTEL>
				<xsl:if test="count(A5_C_TEL_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_C_TEL_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A5_C_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_CTEL>
			</xsl:if>
			<xsl:if test="count(A5_T_REGION)>0">
			<A_TAD>
				<xsl:if test="count(A5_T_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_T_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(A5_T_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A5_T_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A5_T_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A5_T_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A5_T_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A5_T_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A5_T_CITY)>0">
				<CITY>
					<xsl:value-of select="A5_T_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A5_T_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A5_T_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A5_T_STREET)>0">
				<STREET>
					<xsl:value-of select="A5_T_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A5_T_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A5_T_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A5_T_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A5_T_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A5_T_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A5_T_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A5_T_APART)>0">
				<APART>
					<xsl:value-of select="A5_T_APART/@v"/>
				</APART>
				</xsl:if>
			</A_TAD>
			</xsl:if>
			<xsl:if test="count(A5_1_TEL_NUM)>0">
			<A_MTEL>
				<xsl:if test="count(A5_E_TEL1_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_E_TEL1_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A5_1_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_MTEL>
			<xsl:if test="count(A5_2_TEL_NUM)>0">
			<A_MTEL>
				<xsl:if test="count(A5_E_TEL2_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_E_TEL2_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A5_2_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_MTEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(A5_E_ORG_NAME)>0">
			<A_EMP>
				<xsl:if test="count(A5_EMP_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_EMP_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<ORG_NAME>
					<xsl:value-of select="A5_E_ORG_NAME/@v"/>
				</ORG_NAME>
				<xsl:if test="count(A5_E_INN)>0">
				<INN>
					<xsl:value-of select="A5_E_INN/@v"/>
				</INN>
				</xsl:if>
				<xsl:if test="count(A5_JOT)>0">
				<JOT>
					<xsl:value-of select="A5_JOT/@v"/>
				</JOT>
				</xsl:if>				
			</A_EMP>
			</xsl:if>
			<xsl:if test="count(A5_E_TEL_NUM)>0">
			<A_ETEL>
				<xsl:if test="count(A5_E_TEL1_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_E_TEL1_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A5_E_TEL_NUM/@v"/>
				</TEL_NUM>
			</A_ETEL>
			<xsl:if test="count(A5_E_TEL_NUM_2)>0">
			<A_ETEL>
				<xsl:if test="count(A5_E_TEL2_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_E_TEL2_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<TEL_NUM>
					<xsl:value-of select="A5_E_TEL_NUM_2/@v"/>
				</TEL_NUM>
			</A_ETEL>
			</xsl:if>
			</xsl:if>
			<xsl:if test="count(A5_E_REGION)>0">
			<A_EAD>
				<xsl:if test="count(A5_EAD_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_EAD_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<xsl:if test="count(A5_E_COUNTRY)>0">
				<COUNTRY>
					<xsl:value-of select="A5_E_COUNTRY/@v"/>
				</COUNTRY>
				</xsl:if>
				<POST_CODE>
					<xsl:value-of select="A5_E_POST_CODE/@v"/>
				</POST_CODE>
				<REGION>
					<xsl:value-of select="A5_E_REGION/@v"/>
				</REGION>
				<xsl:if test="count(A5_E_DISTRICT)>0">
				<DISTRICT>
					<xsl:value-of select="A5_E_DISTRICT/@v"/>
				</DISTRICT>
				</xsl:if>
				<xsl:if test="count(A5_E_CITY)>0">
				<CITY>
					<xsl:value-of select="A5_E_CITY/@v"/>
				</CITY>
				</xsl:if>
				<xsl:if test="count(A5_E_SETTLEMENT)>0">
				<SETTLEMENT>
					<xsl:value-of select="A5_E_SETTLEMENT/@v"/>
				</SETTLEMENT>
				</xsl:if>
				<xsl:if test="count(A5_E_STREET)>0">
				<STREET>
					<xsl:value-of select="A5_E_STREET/@v"/>
				</STREET>
				</xsl:if>
				<xsl:if test="count(A5_E_HOUSE)>0">
				<HOUSE>
					<xsl:value-of select="A5_E_HOUSE/@v"/>
				</HOUSE>
				</xsl:if>
				<xsl:if test="count(A5_E_BUILD)>0">
				<BUILD>
					<xsl:value-of select="A5_E_BUILD/@v"/>
				</BUILD>
				</xsl:if>
				<xsl:if test="count(A5_E_CONSTR)>0">
				<CONSTR>
					<xsl:value-of select="A5_E_CONSTR/@v"/>
				</CONSTR>
				</xsl:if>
				<xsl:if test="count(A5_E_APART)>0">
				<APART>
					<xsl:value-of select="A5_E_APART/@v"/>
				</APART>
				</xsl:if>
			</A_EAD>
			</xsl:if>
			<xsl:if test="count(A5_EMAIL)>0">
			<A_EMA>
				<xsl:if test="count(A5_EMA_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_EMA_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<EMAIL>
					<xsl:value-of select="A5_EMAIL/@v"/>
				</EMAIL>
			</A_EMA>
			</xsl:if>
			<xsl:if test="count(A5_CAR_REG)>0">
			<A_VEH>
				<xsl:if test="count(A5_VEH_STATUS)>0">
					<STATUS>
					<xsl:value-of select="A5_VEH_STATUS/@v"/>
					</STATUS>
				</xsl:if>
				<CAR_REG>
					<xsl:value-of select="A5_CAR_REG/@v"/>
				</CAR_REG>
				<xsl:if test="count(A5_VIN)>0">
				<VIN>
					<xsl:value-of select="A5_VIN/@v"/>
				</VIN>
				</xsl:if>
			</A_VEH>
			</xsl:if>
		</AP>		
</xsl:template>
</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="csv_xml" userelativepaths="yes" externalpreview="no" url="input_file_csv.xml" htmlbaseurl="" outputurl="" processortype="jaxp" useresolver="yes" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" validateoutput="no" validator="internal" customvalidator="">
			<advancedProp name="sInitialMode" value=""/>
			<advancedProp name="bXsltOneIsOkay" value="true"/>
			<advancedProp name="bSchemaAware" value="true"/>
			<advancedProp name="bXml11" value="false"/>
			<advancedProp name="iValidation" value="0"/>
			<advancedProp name="bExtensions" value="true"/>
			<advancedProp name="iWhitespace" value="0"/>
			<advancedProp name="sInitialTemplate" value=""/>
			<advancedProp name="bTinyTree" value="true"/>
			<advancedProp name="bWarnings" value="true"/>
			<advancedProp name="bUseDTD" value="false"/>
			<advancedProp name="iErrorHandling" value="fatal"/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="input_file.xsd" destSchemaRoot="BATCH" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no">
			<SourceSchema srcSchemaPath="input_csv.xml" srcSchemaRoot="BATCH" AssociatedInstance="file:///d:/Work/Проекты/Experian/Модуль нормализации/input_csv.xml" loaderFunction="document" loaderFunctionUsesURI="no"/>
			<SourceSchema srcSchemaPath="input_csv.xsd" srcSchemaRoot="BATCH" AssociatedInstance="" loaderFunction="document" loaderFunctionUsesURI="no"/>
		</MapperInfo>
		<MapperBlockPosition>
			<template match="/">
				<block path="a:BATHCH/a:HEADER/a:COUNT/xsl:value-of" x="82" y="152"/>
				<block path="a:BATHCH/a:SUBMISSIONS/xsl:for-each" x="42" y="38"/>
				<block path="a:BATHCH/a:SUBMISSIONS/xsl:for-each/SUBMISSION/xsl:apply-templates" x="122" y="38"/>
			</template>
			<template match="SUBMISSION"></template>
			<template match="ROW"></template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
-->