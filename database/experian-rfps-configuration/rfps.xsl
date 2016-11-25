<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:a="urn:mclsoftware.co.uk:hunterII" xmlns="urn:mclsoftware.co.uk:hunterII">
	<xsl:output method="xml" encoding="UTF-8"/> 
	<xsl:template match="/">
		<BATCH>
			<HEADER>
				<COUNT>
					<xsl:value-of select="a:BATCH/a:HEADER/a:COUNT"/>
				</COUNT>
				<ORIGINATOR>
					<xsl:value-of select="a:BATCH/a:HEADER/a:ORIGINATOR"/>
				</ORIGINATOR>
				<xsl:if test="a:BATCH/a:HEADER/a:SUPPRESS='Y'">
					<SUPPRESS>
						<xsl:value-of select="a:BATCH/a:HEADER/a:SUPPRESS"/>
					</SUPPRESS>
				</xsl:if>
				<xsl:if test="a:BATCH/a:HEADER/a:RESULT='Y'">
					<RESULT>
						<xsl:value-of select="a:BATCH/a:HEADER/a:RESULT"/>
					</RESULT>
				</xsl:if>
			</HEADER>
			<SUBMISSIONS>
				<xsl:apply-templates select="a:BATCH/a:SUBMISSIONS"/>
			</SUBMISSIONS>
		</BATCH>
	</xsl:template>
	<xsl:template match="a:A_EAD|a:A_TAD|a:A_CAD|a:A_RAD|a:MA_EAD|a:MA_TAD|a:MA_CAD|a:MA_RAD">
		<xsl:call-template name="idStatus"/>
		<xsl:for-each select="a:COUNTRY">
			<COUNTRY>
				<xsl:value-of select="."/>
			</COUNTRY>
		</xsl:for-each>
		<xsl:if test="string-length(a:POST_CODE)>0">
			<POST_CODE>
				<xsl:value-of select="a:POST_CODE"/>
			</POST_CODE>
		</xsl:if>		
		<REGION>
			<xsl:value-of select="a:REGION"/>
		</REGION>
		<xsl:for-each select="a:DISTRICT">
			<DISTRICT>
				<xsl:value-of select="."/>
			</DISTRICT>
		</xsl:for-each>
		<xsl:choose>
			<xsl:when test="string-length(a:CITY)>0">
				<AHK1>
					<xsl:value-of select="concat(a:STREET,a:CITY)"/>
				</AHK1>
			</xsl:when>
			<xsl:otherwise>
				<AHK1></AHK1>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="string-length(a:SETTLEMENT)>0">
				<AHK2>
					<xsl:value-of select="concat(a:STREET,a:SETTLEMENT)"/>
				</AHK2>
			</xsl:when>
			<xsl:otherwise>
				<AHK2></AHK2>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:for-each select="a:HOUSE">
			<HOUSE>
				<xsl:value-of select="."/>
			</HOUSE>
		</xsl:for-each>
		<!--xsl:for-each select="a:BUILD">
			<BUILD>
				<xsl:value-of select="concat(.,../a:CONSTR)"/>
			</BUILD>
		</xsl:for-each>
		<xsl:for-each select="a:CONSTR">
			<CONSTR>
				<xsl:value-of select="concat(.,../a:BUILD)"/>
			</CONSTR>
		</xsl:for-each-->
		<xsl:if test="a:CONSTR or a:BUILD">
			<BUILD>
				<xsl:value-of select="concat(a:BUILD,a:CONSTR)"/>
			</BUILD>
			<CONSTR>
				<xsl:value-of select="concat(a:CONSTR,a:BUILD)"/>
			</CONSTR>
		</xsl:if>	
		<xsl:for-each select="a:APART">
			<APART>
				<xsl:value-of select="."/>
			</APART>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="a:A_EMA|a:MA_EMA">
		<xsl:call-template name="idStatus"/>
		<EMAIL>
			<xsl:value-of select="a:EMAIL"/>
		</EMAIL>
	</xsl:template>
	<xsl:template match="a:A_ETEL|a:A_MTEL|a:A_CTEL|a:A_RTEL|a:MA_ETEL|a:MA_MTEL|a:MA_CTEL|a:MA_RTEL">
		<xsl:call-template name="idStatus"/>
		<TEL_NUM>
			<xsl:value-of select="a:TEL_NUM"/>
		</TEL_NUM>
	</xsl:template>
	<xsl:template match="a:A_VEH|a:MA_VEH">
		<xsl:call-template name="idStatus"/>
		<CAR_REG>
			<xsl:value-of select="a:CAR_REG"/>
		</CAR_REG>
		<xsl:for-each select="a:VIN">
			<VIN>
				<xsl:value-of select="."/>
			</VIN>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="a:A_SPO|a:MA_SPO">
		<xsl:call-template name="idStatus"/>
		<PHK1>
			<xsl:value-of select="concat(a:FNAME,a:LNAME)"/>
		</PHK1>
		<xsl:if test="string-length(concat(a:FNAME,a:PNAME))>0">
			<PHK2>
				<xsl:value-of select="concat(a:FNAME,a:PNAME)"/>
			</PHK2>
		</xsl:if>
		<LNAME>
			<xsl:value-of select="a:LNAME"/>
		</LNAME>
		<DOB>
			<xsl:value-of select="a:DOB"/>
		</DOB>
	</xsl:template>
	<xsl:template match="a:A_DOC|a:MA_DOC">
		<xsl:call-template name="idStatus"/>
                <DHK1>
                    <xsl:choose>
                        <xsl:when test="a:DOT!=5">
                            <xsl:value-of select="a:DOC_NUMBER"/>
                        </xsl:when>
                        <xsl:when test="a:DOT=5 and a:DOC_NUMBER and a:DOC_NUMBER!='DUMMY' and a:DOC_DATE and string-length(a:DOC_NUMBER)>0 and string-length(a:DOC_DATE)>0">
                            <xsl:value-of select="concat(a:DOC_NUMBER,a:DOC_DATE)"/>
                        </xsl:when>  
                        <xsl:otherwise> 
                            <xsl:value-of select="'DUMMY'"/>
                        </xsl:otherwise>
                    </xsl:choose> 
                </DHK1>
		<xsl:for-each select="a:DOT">
			<DOT>
				<xsl:value-of select="."/>
			</DOT>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="a:A_PAS|a:MA_PAS">
		<xsl:call-template name="idStatus"/>
                <DHK1>
                    <xsl:value-of select="concat(a:DOC_NUMBER,a:DOC_DATE)"/>
                </DHK1>                                                  
	</xsl:template>
	<xsl:template match="a:A_EMP|a:MA_EMP">
		<xsl:call-template name="idStatus"/>
		<ORG_NAME>
			<xsl:value-of select="a:ORG_NAME"/>
		</ORG_NAME>
		<xsl:for-each select="a:JOT">
			<JOT>
				<xsl:value-of select="."/>
			</JOT>
		</xsl:for-each>
		<xsl:for-each select="a:INN">
			<INN>
				<xsl:value-of select="."/>
			</INN>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="a:MA|a:AP">
		<xsl:call-template name="idStatus"/>            
                <xsl:for-each select="a:MA_PAS|a:A_PAS"> 
                    <PER_INDEX>
                        <xsl:choose>
                            <xsl:when test="string-length(../a:PNAME)>2">
                                <xsl:value-of select="concat(translate(../a:LNAME,'АЕИОУЭЮЯЫ',''),translate(../a:FNAME,'АЕИОУЭЮЯЫ',''),translate(../a:PNAME,'АЕИОУЭЮЯЫ',''),substring(../a:PNAME,string-length(../a:PNAME)-1),../a:DOB,substring(a:DOC_NUMBER,1,2))" />                                   
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="concat(translate(../a:LNAME,'АЕИОУЭЮЯЫ',''),translate(../a:FNAME,'АЕИОУЭЮЯЫ',''),translate(../a:PNAME,'АЕИОУЭЮЯЫ',''),../a:PNAME,../a:DOB,substring(a:DOC_NUMBER,1,2))" />                                
                            </xsl:otherwise>
                        </xsl:choose>          
                    </PER_INDEX>
                </xsl:for-each>                         
		<PHK1>
			<xsl:value-of select="concat(a:FNAME,a:LNAME)"/>
		</PHK1>
		<xsl:if test="string-length(concat(a:FNAME,a:PNAME))>0">
			<PHK2>
				<xsl:value-of select="concat(a:FNAME,a:PNAME)"/>
			</PHK2>
		</xsl:if>
		<xsl:if test="string-length(a:LNAME)>0">
			<LNAME>
				<xsl:value-of select="a:LNAME"/>
			</LNAME>
		</xsl:if>
		<DOB>
			<xsl:value-of select="a:DOB"/>
		</DOB>
		<xsl:for-each select="a:PER_INN">
			<PER_INN>
				<xsl:value-of select="."/>
			</PER_INN>
		</xsl:for-each>
		<xsl:if test="string-length(concat(a:PREV_FN,a:PREV_LN))>0">
			<PREV_FL>
				<xsl:value-of select="concat(a:PREV_FN,a:PREV_LN)"/>
			</PREV_FL>
		</xsl:if>
		<xsl:if test="string-length(concat(a:PREV_FN,a:PREV_PNPREV_PN))>0">
			<PREV_FP>
				<xsl:value-of select="concat(a:PREV_FN,a:PREV_PN)"/>
			</PREV_FP>
		</xsl:if>
		<xsl:if test="string-length(a:PREV_LN)>0">
			<PREV_LN>
				<xsl:value-of select="a:PREV_LN"/>
			</PREV_LN>
		</xsl:if>
		<xsl:for-each select="a:INC_TM">
			<INC_TM>
				<xsl:value-of select="."/>
			</INC_TM>
		</xsl:for-each>
		<xsl:for-each select="a:INC_DOC">
			<INC_DOC>
				<xsl:value-of select="."/>
			</INC_DOC>
		</xsl:for-each>
		<xsl:for-each select="a:INC_ADD">
			<INC_ADD>
				<xsl:value-of select="."/>
			</INC_ADD>
		</xsl:for-each>    
	</xsl:template>
	<xsl:template match="a:SUBMISSION">
		<xsl:for-each select="@NotificationRqd">
			<xsl:attribute name="NotificationRqd"><xsl:value-of select="."/></xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="@subID">
			<xsl:attribute name="subID"><xsl:value-of select="."/></xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="a:SUB_HEADER">
			<SUB_HEADER>
				<NOTES>
					<xsl:apply-templates select="./a:NOTES"/>
				</NOTES>
			</SUB_HEADER>
		</xsl:for-each>
		<IDENTIFIER>
			<xsl:value-of select="a:IDENTIFIER"/>
		</IDENTIFIER>
		<PRODUCT>
			<xsl:value-of select="a:PRODUCT"/>
		</PRODUCT>
		<xsl:for-each select="a:CLASSIFICATION">
			<CLASSIFICATION>
				<xsl:value-of select="."/>
			</CLASSIFICATION>
		</xsl:for-each>
		<DATE>
			<xsl:value-of select="a:DATE"/>
		</DATE>
		<xsl:for-each select="a:APP_DATE">
			<APP_DATE>
				<xsl:value-of select="."/>
			</APP_DATE>
		</xsl:for-each> 
                <!--xsl:for-each select="a:HM_VERSION"-->
                <HM_VERSION>
                    <xsl:value-of select="HM_VERSION" />
                </HM_VERSION>
                <!--/xsl:for-each-->
		<xsl:for-each select="a:MA">
			<MA>
				<xsl:apply-templates select="."/>
				<xsl:for-each select="a:MA_PAS">
					<MA_PAS>
						<xsl:apply-templates select="."/>
					</MA_PAS>                               
				</xsl:for-each>
				<xsl:for-each select="a:MA_DOC">
					<MA_DOC>
						<xsl:apply-templates select="."/>
					</MA_DOC>
				</xsl:for-each>
				<xsl:for-each select="a:MA_SPO">
					<MA_SPO>
						<xsl:apply-templates select="."/>
					</MA_SPO>
				</xsl:for-each>
				<MA_RAD>
					<xsl:apply-templates select="a:MA_RAD"/>
				</MA_RAD>
				<xsl:for-each select="a:MA_RTEL">
					<MA_RTEL>
						<xsl:apply-templates select="."/>
					</MA_RTEL>
				</xsl:for-each>
				<xsl:for-each select="a:MA_CAD">
					<MA_CAD>
						<xsl:apply-templates select="."/>
					</MA_CAD>
				</xsl:for-each>
				<xsl:for-each select="a:MA_CTEL">
					<MA_CTEL>
						<xsl:apply-templates select="."/>
					</MA_CTEL>
				</xsl:for-each>
				<xsl:for-each select="a:MA_TAD">
					<MA_TAD>
						<xsl:apply-templates select="."/>
					</MA_TAD>
				</xsl:for-each>
				<xsl:for-each select="a:MA_MTEL">
					<MA_MTEL>
						<xsl:apply-templates select="."/>
					</MA_MTEL>
				</xsl:for-each>
				<xsl:for-each select="a:MA_EMP">
					<MA_EMP>
						<xsl:apply-templates select="."/>
					</MA_EMP>
				</xsl:for-each>
				<xsl:for-each select="a:MA_ETEL">
					<MA_ETEL>
						<xsl:apply-templates select="."/>
					</MA_ETEL>
				</xsl:for-each>
				<xsl:for-each select="a:MA_EAD">
					<MA_EAD>
						<xsl:apply-templates select="."/>
					</MA_EAD>
				</xsl:for-each>
				<xsl:for-each select="a:MA_EMA">
					<MA_EMA>
						<xsl:apply-templates select="."/>
					</MA_EMA>
				</xsl:for-each>
				<xsl:for-each select="a:MA_VEH">
					<MA_VEH>
						<xsl:apply-templates select="."/>
					</MA_VEH>
				</xsl:for-each>

                                <xsl:for-each select="a:MA_EMP">
                                    <xsl:if test="a:JOT and a:JOT!='1' and a:JOT!='Owner' and a:JOT!='Self Employed' and a:JOT!='5' and a:JOT!='2' and a:JOT!='99'">
                                        <xsl:for-each select="../a:MA_ETEL">
                                            <xsl:if test="string-length(a:TEL_NUM) &lt; 12 and string-length(a:TEL_NUM) &gt; 10">
                                                <MA_ETEL_O>
                                                    <TEL_NUM>
                                                        <xsl:value-of select="a:TEL_NUM" />
                                                    </TEL_NUM>                                         
                                                </MA_ETEL_O>
                                                <MA_ETEL_M>
                                                    <TEL_NUM_MASK>
                                                        <xsl:value-of select="substring(a:TEL_NUM, 1, 5)" />
                                                    </TEL_NUM_MASK>
                                                </MA_ETEL_M>
                                                <MA_ETEL_M>
                                                    <TEL_NUM_MASK>
                                                        <xsl:value-of select="substring(a:TEL_NUM, 1, 6)" />
                                                    </TEL_NUM_MASK>
                                                </MA_ETEL_M>
                                                <MA_ETEL_M>
                                                    <TEL_NUM_MASK>
                                                        <xsl:value-of select="substring(a:TEL_NUM, 1, 7)" />
                                                    </TEL_NUM_MASK>
                                                </MA_ETEL_M>
                                                <MA_ETEL_M>
                                                    <TEL_NUM_MASK>
                                                        <xsl:value-of select="substring(a:TEL_NUM, 1, 8)" />
                                                    </TEL_NUM_MASK>
                                                </MA_ETEL_M>
                                                <MA_ETEL_M>
                                                    <TEL_NUM_MASK>
                                                        <xsl:value-of select="substring(a:TEL_NUM, 1, 9)" />
                                                    </TEL_NUM_MASK> 
                                                </MA_ETEL_M>
                                                <MA_ETEL_M>
                                                    <TEL_NUM_MASK>
                                                        <xsl:value-of select="substring(a:TEL_NUM, 1, 10)" />
                                                    </TEL_NUM_MASK>     
                                                </MA_ETEL_M>
                                            </xsl:if>
                                        </xsl:for-each>
                                    </xsl:if>
                                </xsl:for-each>                                                                                                                                         
			</MA>
		</xsl:for-each>
		<xsl:for-each select="a:AP">
			<AP>
				<xsl:apply-templates select="."/>
				<xsl:for-each select="a:APP_TYPE">
					<APP_TYPE>
						<xsl:value-of select="."/>
					</APP_TYPE>
				</xsl:for-each>
				<xsl:for-each select="a:A_PAS">
					<A_PAS>
						<xsl:apply-templates select="."/>
					</A_PAS>
				</xsl:for-each>
				<xsl:for-each select="a:A_DOC">
					<A_DOC>
						<xsl:apply-templates select="."/>
					</A_DOC>
				</xsl:for-each>
				<xsl:for-each select="a:A_SPO">
					<A_SPO>
						<xsl:apply-templates select="."/>
					</A_SPO>
				</xsl:for-each>
				<xsl:for-each select="a:A_RAD">
					<A_RAD>
						<xsl:apply-templates select="."/>
					</A_RAD>
				</xsl:for-each>
				<xsl:for-each select="a:A_RTEL">
					<A_RTEL>
						<xsl:apply-templates select="."/>
					</A_RTEL>
				</xsl:for-each>
				<xsl:for-each select="a:A_CAD">
					<A_CAD>
						<xsl:apply-templates select="."/>
					</A_CAD>
				</xsl:for-each>
				<xsl:for-each select="a:A_CTEL">
					<A_CTEL>
						<xsl:apply-templates select="."/>
					</A_CTEL>
				</xsl:for-each>
				<xsl:for-each select="a:A_TAD">
					<A_TAD>
						<xsl:apply-templates select="."/>
					</A_TAD>
				</xsl:for-each>
				<xsl:for-each select="a:A_MTEL">
					<A_MTEL>
						<xsl:apply-templates select="."/>
					</A_MTEL>
				</xsl:for-each>
				<xsl:for-each select="a:A_EMP">
					<A_EMP>
						<xsl:apply-templates select="."/>
					</A_EMP>
				</xsl:for-each>
				<xsl:for-each select="a:A_ETEL">
					<A_ETEL>
						<xsl:apply-templates select="."/>
					</A_ETEL>
				</xsl:for-each>
				<xsl:for-each select="a:A_EAD">
					<A_EAD>
						<xsl:apply-templates select="."/>
					</A_EAD>
				</xsl:for-each>
				<xsl:for-each select="a:A_EMA">
					<A_EMA>
						<xsl:apply-templates select="."/>
					</A_EMA>
				</xsl:for-each>
				<xsl:for-each select="a:A_VEH">
					<A_VEH>
						<xsl:apply-templates select="."/>
					</A_VEH>
				</xsl:for-each>
                                <xsl:for-each select="a:A_EMP">
                                    <xsl:if test="a:JOT and a:JOT!='1' and a:JOT!='Owner' and a:JOT!='Self Employed' and a:JOT!='5' and a:JOT!='2' and a:JOT!='99'">
                                        <xsl:for-each select="../a:A_ETEL">
                                            <xsl:if test="string-length(a:TEL_NUM) &lt; 12 and string-length(a:TEL_NUM) &gt; 10">
                                                <A_ETEL_O>
                                                    <TEL_NUM>
                                                        <xsl:value-of select="a:TEL_NUM" />
                                                    </TEL_NUM>                                         
                                                </A_ETEL_O>
                                                <A_ETEL_M>
                                                    <TEL_NUM_MASK>
                                                        <xsl:value-of select="substring(a:TEL_NUM, 1, 5)" />
                                                    </TEL_NUM_MASK>
                                                </A_ETEL_M>
                                                <A_ETEL_M>
                                                    <TEL_NUM_MASK>
                                                        <xsl:value-of select="substring(a:TEL_NUM, 1, 6)" />
                                                    </TEL_NUM_MASK>
                                                </A_ETEL_M>
                                                <A_ETEL_M>
                                                    <TEL_NUM_MASK>
                                                        <xsl:value-of select="substring(a:TEL_NUM, 1, 7)" />
                                                    </TEL_NUM_MASK>
                                                </A_ETEL_M>
                                                <A_ETEL_M>
                                                    <TEL_NUM_MASK>
                                                        <xsl:value-of select="substring(a:TEL_NUM, 1, 8)" />
                                                    </TEL_NUM_MASK>
                                                </A_ETEL_M>
                                                <A_ETEL_M>
                                                    <TEL_NUM_MASK>
                                                        <xsl:value-of select="substring(a:TEL_NUM, 1, 9)" />
                                                    </TEL_NUM_MASK> 
                                                </A_ETEL_M>
                                                <A_ETEL_M>
                                                    <TEL_NUM_MASK>
                                                        <xsl:value-of select="substring(a:TEL_NUM, 1, 10)" />
                                                    </TEL_NUM_MASK>     
                                                </A_ETEL_M>
                                            </xsl:if>
                                        </xsl:for-each>
                                    </xsl:if>
                                </xsl:for-each>      
			</AP>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="a:SUBMISSIONS">
		<xsl:for-each select="a:SUBMISSION">
			<SUBMISSION>
				<xsl:apply-templates select="."/>
			</SUBMISSION>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="a:NOTES">
		<xsl:for-each select="a:NOTE">
			<NOTE>
				<xsl:attribute name="privacyLevel"><xsl:value-of select="@privacyLevel"/></xsl:attribute>
				<xsl:attribute name="type"><xsl:value-of select="@type"/></xsl:attribute>
			</NOTE>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="idStatus">
		<xsl:for-each select="@entID">
			<xsl:attribute name="entID"><xsl:value-of select="."/></xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="a:STATUS">
			<STATUS>
				<xsl:value-of select="."/>
			</STATUS>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>

<!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="RFPS" userelativepaths="yes" externalpreview="no" url="XSL_CSV_OUT.xml" htmlbaseurl="" outputurl="" processortype="saxon8" useresolver="yes" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""
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
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="output_file.xsd" destSchemaRoot="BATCH" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no">
			<SourceSchema srcSchemaPath="input_file.xsd" srcSchemaRoot="BATCH" AssociatedInstance="" loaderFunction="document" loaderFunctionUsesURI="no"/>
		</MapperInfo>
		<MapperBlockPosition>
			<template match="/">
				<block path="BATCH/HEADER/xsl:if/&gt;[0]" x="197" y="142"/>
				<block path="BATCH/HEADER/xsl:if/&gt;[0]/count[0]" x="151" y="136"/>
				<block path="BATCH/HEADER/xsl:if" x="243" y="144"/>
				<block path="BATCH/HEADER/xsl:if[1]/&gt;[0]" x="237" y="160"/>
				<block path="BATCH/HEADER/xsl:if[1]/&gt;[0]/count[0]" x="191" y="154"/>
				<block path="BATCH/HEADER/xsl:if[1]" x="283" y="162"/>
				<block path="BATCH/SUBMISSIONS/xsl:apply-templates" x="243" y="105"/>
			</template>
			<template match="A_EAD|A_TAD|A_CAD|A_RAD|MA_EAD|MA_TAD|MA_CAD|MA_RAD">
				<block path="xsl:attribute/xsl:value-of" x="129" y="83"/>
				<block path="a:STATUS/xsl:value-of" x="169" y="83"/>
				<block path="a:COUNTRY/xsl:value-of" x="89" y="83"/>
				<block path="a:POST_CODE/xsl:value-of" x="49" y="83"/>
				<block path="a:REGION/xsl:value-of" x="129" y="43"/>
				<block path="a:DISTRICT/xsl:value-of" x="169" y="43"/>
				<block path="a:AHK1/xsl:value-of" x="89" y="43"/>
				<block path="a:AHK2/xsl:value-of" x="49" y="43"/>
				<block path="a:HOUSE/xsl:value-of" x="129" y="123"/>
				<block path="a:BUILD/xsl:value-of" x="169" y="123"/>
				<block path="a:CONSTR/xsl:value-of" x="89" y="123"/>
				<block path="a:APART/xsl:value-of" x="49" y="123"/>
			</template>
			<template match="A_EMA|MA_EMA">
				<block path="xsl:attribute/xsl:value-of" x="129" y="153"/>
				<block path="a:STATUS/xsl:value-of" x="169" y="153"/>
				<block path="a:EMAIL/xsl:value-of" x="89" y="153"/>
			</template>
			<template match="A_PAS|MA_PAS">
				<block path="xsl:attribute/xsl:value-of" x="129" y="85"/>
				<block path="a:STATUS/xsl:value-of" x="169" y="85"/>
				<block path="a:DHK1/xsl:value-of" x="89" y="85"/>
			</template>
			<template match="MA|AP">
				<block path="xsl:attribute/xsl:value-of" x="106" y="3"/>
				<block path="a:STATUS/xsl:value-of" x="106" y="3"/>
				<block path="a:PHK1/xsl:value-of" x="106" y="3"/>
				<block path="a:PHK2/xsl:value-of" x="106" y="3"/>
				<block path="a:LNAME/xsl:value-of" x="106" y="3"/>
				<block path="a:DOB/xsl:value-of" x="106" y="3"/>
				<block path="a:PER_INN/xsl:value-of" x="106" y="3"/>
				<block path="a:PREV_FL/xsl:value-of" x="106" y="3"/>
				<block path="a:PREV_FP/xsl:value-of" x="106" y="3"/>
				<block path="a:PREV_LN/xsl:value-of" x="106" y="3"/>
				<block path="a:INC_TM/xsl:value-of" x="106" y="3"/>
				<block path="a:INC_DOC/xsl:value-of" x="106" y="3"/>
				<block path="a:INC_ADD/xsl:value-of" x="106" y="3"/>
			</template>
			<template match="a:MA|a:AP">
				<block path="xsl:call-template" x="191" y="53"/>
				<block path="a:PHK1/xsl:value-of" x="231" y="53"/>
				<block path="a:PHK2/xsl:value-of" x="151" y="53"/>
				<block path="a:LNAME/xsl:value-of" x="111" y="53"/>
				<block path="a:DOB/xsl:value-of" x="71" y="53"/>
				<block path="xsl:for-each" x="191" y="13"/>
				<block path="xsl:for-each/a:PER_INN/xsl:value-of" x="31" y="53"/>
				<block path="a:PREV_FL/xsl:value-of" x="231" y="13"/>
				<block path="a:PREV_FP/xsl:value-of" x="151" y="13"/>
				<block path="a:PREV_LN/xsl:value-of" x="111" y="13"/>
				<block path="xsl:for-each[1]" x="31" y="13"/>
				<block path="xsl:for-each[1]/a:INC_TM/xsl:value-of" x="71" y="13"/>
				<block path="xsl:for-each[2]" x="191" y="53"/>
				<block path="xsl:for-each[2]/a:INC_DOC/xsl:value-of" x="191" y="53"/>
				<block path="xsl:for-each[3]" x="191" y="53"/>
				<block path="xsl:for-each[3]/a:INC_ADD/xsl:value-of" x="191" y="53"/>
			</template>
			<template match="a:A_SPO|a:MA_SPO">
				<block path="xsl:call-template" x="205" y="116"/>
				<block path="a:PHK1/xsl:value-of" x="245" y="116"/>
				<block path="xsl:if/&gt;[0]" x="79" y="114"/>
				<block path="xsl:if/&gt;[0]/string-length[0]" x="33" y="108"/>
				<block path="xsl:if/&gt;[0]/string-length[0]/concat[0]" x="0" y="106"/>
				<block path="xsl:if" x="125" y="116"/>
				<block path="xsl:if/a:PHK2/xsl:value-of" x="165" y="116"/>
				<block path="a:LNAME/xsl:value-of" x="85" y="116"/>
				<block path="a:DOB/xsl:value-of" x="45" y="116"/>
			</template>
			<template match="a:SUBMISSION">
				<block path="xsl:for-each" x="245" y="162"/>
				<block path="xsl:for-each/xsl:attribute/xsl:value-of" x="205" y="162"/>
				<block path="xsl:for-each[1]" x="125" y="180"/>
				<block path="xsl:for-each[1]/xsl:attribute/xsl:value-of" x="165" y="180"/>
				<block path="xsl:for-each[2]" x="205" y="198"/>
				<block path="xsl:for-each[2]/a:SUB_HEADER/a:NOTES/xsl:apply-templates" x="45" y="74"/>
				<block path="a:IDENTIFIER/xsl:value-of" x="205" y="114"/>
				<block path="a:PRODUCT/xsl:value-of" x="245" y="114"/>
				<block path="xsl:for-each[3]" x="125" y="114"/>
				<block path="xsl:for-each[3]/a:CLASSIFICATION/xsl:value-of" x="165" y="114"/>
				<block path="a:DATE/xsl:value-of" x="85" y="114"/>
				<block path="xsl:for-each[4]" x="205" y="74"/>
				<block path="xsl:for-each[4]/a:APP_DATE/xsl:value-of" x="45" y="114"/>
				<block path="xsl:for-each[5]" x="165" y="74"/>
				<block path="xsl:for-each[5]/a:MA/xsl:apply-templates" x="245" y="74"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each" x="245" y="34"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each/a:MA_PAS/xsl:apply-templates" x="205" y="34"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[1]" x="125" y="34"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[1]/a:MA_DOC/xsl:apply-templates" x="165" y="34"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[2]" x="45" y="34"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[2]/a:MA_SPO/xsl:apply-templates" x="85" y="34"/>
				<block path="xsl:for-each[5]/a:MA/a:MA_RAD/xsl:apply-templates" x="85" y="154"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[3]" x="245" y="194"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[3]/a:MA_RTEL/xsl:apply-templates" x="45" y="154"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[4]" x="45" y="194"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[4]/a:MA_CAD/xsl:apply-templates" x="85" y="194"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[5]" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[5]/a:MA_CTEL/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[6]" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[6]/a:MA_TAD/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[7]" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[7]/a:MA_MTEL/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[8]" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[8]/a:MA_EMP/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[9]" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[9]/a:MA_ETEL/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[10]" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[10]/a:MA_EAD/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[11]" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[11]/a:MA_EMA/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[12]" x="205" y="114"/>
				<block path="xsl:for-each[5]/a:MA/xsl:for-each[12]/a:MA_VEH/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]" x="85" y="74"/>
				<block path="xsl:for-each[6]/a:AP/xsl:apply-templates" x="125" y="74"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each/a:APP_TYPE/xsl:value-of" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/a:A_PAS/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[1]" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[1]/a:A_DOC/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[2]" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[2]/a:A_SPO/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[3]" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[3]/a:A_RAD/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[4]" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[4]/a:A_RTEL/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[5]" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[5]/a:A_CAD/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[6]" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[6]/a:A_CTEL/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[7]" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[7]/a:A_TAD/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[8]" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[8]/a:A_MTEL/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[9]" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[9]/a:A_EMP/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[10]" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[10]/a:A_ETEL/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[11]" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[11]/a:A_EAD/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[12]" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[12]/a:A_EMA/xsl:apply-templates" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[13]" x="205" y="114"/>
				<block path="xsl:for-each[6]/a:AP/xsl:for-each[13]/a:A_VEH/xsl:apply-templates" x="205" y="114"/>
			</template>
			<template match="a:A_EMP|a:MA_EMP">
				<block path="xsl:call-template" x="191" y="114"/>
				<block path="a:ORG_NAME/xsl:value-of" x="231" y="114"/>
				<block path="xsl:for-each" x="111" y="114"/>
				<block path="xsl:for-each/a:INN/xsl:value-of" x="151" y="114"/>
				<block path="xsl:for-each[1]" x="31" y="114"/>
				<block path="xsl:for-each[1]/a:JOT/xsl:value-of" x="71" y="114"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
-->