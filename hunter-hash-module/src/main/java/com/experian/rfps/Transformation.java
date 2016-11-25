package com.experian.rfps;

import com.experian.rfps.BufferAppender;
import com.experian.rfps.CSVFileReader;
import com.experian.rfps.CheckRow;
import com.experian.rfps.Domain;
import com.experian.rfps.ExtensionFilter;
import com.experian.rfps.Field;
import com.experian.rfps.Rule;
import com.experian.rfps.Searcher;
import com.experian.rfps.Table;
import com.experian.rfps.XMLLoader;
import com.experian.rfps.XSLTransformer;
import com.experian.rfps.config.RFPSDoc;
import com.experian.rfps.config.RFPSType;
import com.experian.rfps.rules.SimpleNamespaceContext;
import com.experian.rfps.transformation.DOMAINSType;
import com.experian.rfps.transformation.DOMAINType;
import com.experian.rfps.transformation.FIELDSType;
import com.experian.rfps.transformation.FIELDType;
import com.experian.rfps.transformation.PARAMETERSType;
import com.experian.rfps.transformation.PARAMETERType;
import com.experian.rfps.transformation.RULESType;
import com.experian.rfps.transformation.RULEType;
import com.experian.rfps.transformation.STEPType;
import com.experian.rfps.transformation.TABLESType;
import com.experian.rfps.transformation.TABLEType;
import com.experian.rfps.transformation.TRANSFORMATIONType;
import com.experian.rfps.transformation.WORKSType;
import com.experian.rfps.transformation.transformationDoc;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.varia.LevelRangeFilter;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Transformation {
   public static String defaultConfig = "rfps.xml";
   public static String configDir = "";
   public static String defaultLog = "log4j_rfps.xml";
   public static String mutexConfig = "mutexConfig";
   private String nameConfig;
   RFPSDoc configDoc;
   RFPSType config;
   public static String EXPERIAN_NAMESPACE = "urn:mclsoftware.co.uk:hunterII";
   private Map<String, Domain> domains;
   private Map<String, Table> tables;
   private Map<String, Rule> rules;
   private Map<String, Table> tablesOfvalues;
   private BufferAppender logAppender;
   public Document currentDoc;
   public Node currentNode;
   public static final String programVersion = "Version 1.0.3";
   public static final String version = "1.0.001";
   public static final String dateVersion = "2011.04.01";
   private String apiLog;
   private String inputIdentifier;
   private String inputFilePath;
   private String inputFileArchive;
   private String inputFileError;
   private String inputFileLog;
   private String outputFilePath;
   private String logPath;
   private String logPrefix;
   private Transformation.TypeCreation logCreation;
   private Transformation.TypeMode mode;
   private Transformation.ServiceStatus status;
   private int maxRows;
   private int maxValueRows;
   private int minValueRows;
   private int interval;
   private String xsdTransformation;
   private String xsdInput;
   private String xsdOutput;
   private String xmlTransformation;
   private String xslCSVtoInputXML;
   private String xslInputXmlOutputXml;
   private static Logger log;
   private static Logger syslog;
   private FileAppender sysappender;
   private FileAppender fileappender;
   private String fileName;
   private XMLLoader loaderInput;
   private XMLLoader loaderOutput;
   private XMLLoader loaderCSV;
   private XSLTransformer transformerXMLtoXML;
   private XSLTransformer transformerCSVtoXML;
   private XSLTransformer saverXML;
   private boolean isFindError;
   private Transformation.TypeProcess typeProcess;
   private Level gloLevel;
   private SimpleNamespaceContext namespaceContext;
   private Searcher searcher;
   public static boolean excludeHash = false;
   private String hmVersion;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$experian$rfps$Transformation$TypeCreation;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$experian$rfps$Transformation$TypeMode;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$experian$rfps$Transformation$ServiceStatus;

   public NamespaceContext getNameSpaceContext() {
      return this.namespaceContext;
   }

   public static String getStringTypeCreation(Transformation.TypeCreation value) {
      String result = null;
      switch($SWITCH_TABLE$com$experian$rfps$Transformation$TypeCreation()[value.ordinal()]) {
      case 1:
         result = "DAY";
         break;
      case 2:
         result = "MONTH";
         break;
      case 3:
         result = "QUARTER";
         break;
      case 4:
         result = "YEAR";
      }

      return result;
   }

   public static String getStringTypeMode(Transformation.TypeMode value) {
      String result = null;
      switch($SWITCH_TABLE$com$experian$rfps$Transformation$TypeMode()[value.ordinal()]) {
      case 1:
         result = "SERVICE";
         break;
      case 2:
         result = "CALLED";
      }

      return result;
   }

   public static String getStringServiceStatus(Transformation.ServiceStatus value) {
      String result = null;
      switch($SWITCH_TABLE$com$experian$rfps$Transformation$ServiceStatus()[value.ordinal()]) {
      case 1:
         result = "START";
         break;
      case 2:
         result = "WORKING";
         break;
      case 3:
         result = "STOP";
      }

      return result;
   }

   private void readTransformation() throws Exception {
      if(syslog.isTraceEnabled()) {
         syslog.trace("start readTransformation");
      }

      transformationDoc doc = new transformationDoc();
      TRANSFORMATIONType root = new TRANSFORMATIONType(doc.load(this.xmlTransformation));
      String ver = root.getVERSION().getValue();
      if(ver.compareTo("1.0.001") != 0) {
         String var15 = "Transformation of the data it will not be executed.The file versions " + ver + " is found. The demanded version " + "1.0.001" + ".";
         syslog.error(var15);
         throw new Exception(var15);
      } else {
         RULESType rulesXml = root.getRULES();

         for(int domainsXml = 0; domainsXml < rulesXml.getRULECount(); ++domainsXml) {
            RULEType tablesXML = rulesXml.getRULEAt(domainsXml);
            Rule i = new Rule(this, tablesXML.getNAME().getValue(), tablesXML.getCLASS2().getValue(), tablesXML.getTYPE().getValue(), tablesXML.hasDESCRIPTION()?tablesXML.getDESCRIPTION().getValue():"");
            i.load();
            this.rules.put(i.getName(), i);
            if(syslog.isTraceEnabled()) {
               syslog.trace("Added rule:" + i.getName());
            }

            if(tablesXML.getPARAMETERSCount() > 0) {
               PARAMETERSType tbl = tablesXML.getPARAMETERS();

               for(int table = 0; table < tbl.getPARAMETERCount(); ++table) {
                  PARAMETERType fields = tbl.getPARAMETERAt(table);
                  i.putParameter(fields.getNAME().getValue(), fields.getVALUE2().getValue());
               }
            }

            if(!i.prepare()) {
               String var22 = "The rule " + i.getName() + " isn\'t prepared for work.";
               syslog.error(var22);
               throw new Exception(var22);
            }
         }

         DOMAINSType var16 = root.getDOMAINS();

         for(int var17 = 0; var17 < var16.getDOMAINCount(); ++var17) {
            DOMAINType var18 = var16.getDOMAINAt(var17);
            Domain var21 = new Domain(var18.getNAME().getValue());
            this.domains.put(var21.getName(), var21);
            if(syslog.isTraceEnabled()) {
               syslog.trace("Added domain:" + var21.getName());
            }

            WORKSType var25 = var18.getWORKS();

            for(int var28 = 0; var28 < var25.getSTEPCount(); ++var28) {
               STEPType tableOfvalues = var25.getSTEPAt(var28);
               Rule fieldXml = (Rule)this.rules.get(tableOfvalues.getRULE().getValue());
               if(fieldXml == null) {
                  String domain = "For " + var21.getName() + " the rule " + tableOfvalues.getRULE().getValue() + " transformations isn\'t found";
                  syslog.error(domain);
                  throw new Exception(domain);
               }

               var21.addRule(tableOfvalues.getORDER().getValue(), fieldXml);
               if(syslog.isTraceEnabled()) {
                  syslog.trace("Added step order:" + tableOfvalues.getORDER().getValue() + " rule:" + fieldXml.getName());
               }
            }
         }

         TABLESType var19 = root.getTABLES();

         for(int var20 = 0; var20 < var19.getTABLECount(); ++var20) {
            TABLEType var23 = var19.getTABLEAt(var20);
            Table var24 = new Table(var23.getNAME().getValue().toUpperCase());
            this.tables.put(var24.getName(), var24);
            FIELDSType var26 = var23.getFIELDS();

            for(int var27 = 0; var27 < var26.getFIELDCount(); ++var27) {
               FIELDType var29 = var26.getFIELDAt(var27);
               Domain var31 = (Domain)this.domains.get(var29.getTYPE().getValue());
               if(var31 == null) {
                  String var32 = "For a field " + var29.getNAME().getValue() + " the type (domain)" + var29.getTYPE().getValue() + " isn\'t found.";
                  syslog.error(var32);
                  throw new Exception(var32);
               }

               Field field = new Field(var29.getNAME().getValue(), var31, var29.getORDER().intValue());
               var24.addField(field.getOrder(), field);
            }

            Table var30 = (Table)this.tablesOfvalues.get(var24.getName().toUpperCase());
            if(var30 != null) {
               var24.setAdditionFields(var30);
            }
         }

         this.xsdInput = root.getXSD().getINPUT().getValue();
         if(configDir != "") {
            this.xsdInput = configDir + System.getProperty("file.separator") + this.xsdInput;
         }

         if(syslog.isTraceEnabled()) {
            syslog.trace("xsdInput=" + this.xsdInput);
         }

         this.xsdOutput = root.getXSD().getOUTPUT().getValue();
         if(configDir != "") {
            this.xsdOutput = configDir + System.getProperty("file.separator") + this.xsdOutput;
         }

         if(syslog.isTraceEnabled()) {
            syslog.trace("xsdOutput=" + this.xsdOutput);
         }

         this.xslCSVtoInputXML = root.getXSL().getCSV_INPUTXML().getValue();
         if(configDir != "") {
            this.xslCSVtoInputXML = configDir + System.getProperty("file.separator") + this.xslCSVtoInputXML;
         }

         if(syslog.isTraceEnabled()) {
            syslog.trace("xslCSVtoInputXML=" + this.xslCSVtoInputXML);
         }

         this.xslInputXmlOutputXml = root.getXSL().getINPUTXML_OUTPUTXML().getValue();
         if(configDir != "") {
            this.xslInputXmlOutputXml = configDir + System.getProperty("file.separator") + this.xslInputXmlOutputXml;
         }

         if(syslog.isTraceEnabled()) {
            syslog.trace("xslInputXmlOutputXml=" + this.xslInputXmlOutputXml);
         }

         if(syslog.isTraceEnabled()) {
            syslog.trace("end readTransformation");
         }

         Logger.getRootLogger().removeAppender(this.sysappender);
      }
   }

   public Transformation() throws Exception {
      this.nameConfig = defaultConfig;
      this.configDoc = new RFPSDoc();
      this.domains = new HashMap();
      this.tables = new HashMap();
      this.rules = new HashMap();
      this.tablesOfvalues = new HashMap();
      this.apiLog = "common.log";
      this.logCreation = Transformation.TypeCreation.MONTH;
      this.maxRows = 0;
      this.maxValueRows = 1;
      this.minValueRows = 1;
      this.interval = 60;
      this.typeProcess = Transformation.TypeProcess.XML;
      this.searcher = new Searcher(System.getenv("classpath"));
      this.init();
   }

   private Transformation(String nameConfigFile) throws Exception {
      this.nameConfig = defaultConfig;
      this.configDoc = new RFPSDoc();
      this.domains = new HashMap();
      this.tables = new HashMap();
      this.rules = new HashMap();
      this.tablesOfvalues = new HashMap();
      this.apiLog = "common.log";
      this.logCreation = Transformation.TypeCreation.MONTH;
      this.maxRows = 0;
      this.maxValueRows = 1;
      this.minValueRows = 1;
      this.interval = 60;
      this.typeProcess = Transformation.TypeProcess.XML;
      this.searcher = new Searcher(System.getenv("classpath"));
      this.nameConfig = nameConfigFile;
      this.init();
   }

   private Transformation(String nameConfigFile, String configDirParam) throws Exception {
      System.out.println("Transformation### start constructor");
      this.nameConfig = defaultConfig;
      this.configDoc = new RFPSDoc();
      this.domains = new HashMap();
      this.tables = new HashMap();
      this.rules = new HashMap();
      this.tablesOfvalues = new HashMap();
      this.apiLog = "common.log";
      this.logCreation = Transformation.TypeCreation.MONTH;
      this.maxRows = 0;
      this.maxValueRows = 1;
      this.minValueRows = 1;
      this.interval = 60;
      this.typeProcess = Transformation.TypeProcess.XML;
      System.out.println("Transformation### System.getenv(classpath) - lower: " + System.getenv("classpath"));
      System.out.println("Transformation### System.getenv(CLASSPATH) - UPPER: " + System.getenv("CLASSPATH"));
      this.searcher = new Searcher(System.getenv("CLASSPATH"));
      this.nameConfig = nameConfigFile;
      configDir = configDirParam;
      this.init();
      System.out.println("Transformation### end constructor");
   }

   private synchronized void init() throws Exception {
      System.out.println("Transformation### start init");
      String nameLog = null;//this.searcher.findNameFile(defaultLog);
      System.out.println("Transformation### nameLog: " + nameLog);
      System.out.println("Transformation### configDir: " + configDir);
      if(!configDir.isEmpty()) {
         nameLog = configDir + System.getProperty("file.separator") + defaultLog;
      }
      System.out.println("Transformation### configDir: " + nameLog);

      DOMConfigurator.configure(nameLog);
      log = Logger.getLogger(Transformation.class);
      syslog = Logger.getLogger("syslog");
      this.logAppender = new BufferAppender(new PatternLayout("[%5p] %m at %l%n"));
      this.logAppender.addFilter(new LevelRangeFilter());
      this.logAppender.setName("LOG-BUFFER");
      Logger.getRootLogger().addAppender(this.logAppender);
      String var2 = mutexConfig;
      synchronized(mutexConfig) {
         this.readConfig();
         this.defineSysLog();
         this.readTransformation();
         this.loaderInput = XMLLoader.getInstance(this.xsdInput);
         this.loaderOutput = XMLLoader.getInstance(this.xsdOutput);
         this.loaderCSV = XMLLoader.getInstance();
         this.transformerXMLtoXML = XSLTransformer.getInstance(this.xslInputXmlOutputXml);
         this.transformerCSVtoXML = XSLTransformer.getInstance(this.xslCSVtoInputXML);
         this.saverXML = XSLTransformer.getInstance();
      }
      System.out.println("Transformation### end init");
   }

   public boolean makeTransformationInput(Document doc, int rules, Level lvl) {
      Level level = Logger.getRootLogger().getLevel();
      Logger.getRootLogger().setLevel(lvl);
      this.isFindError = false;
      this.currentDoc = doc;
      Element root = doc.getDocumentElement();
      NodeList children = root.getChildNodes();

      for(int i = 0; i < children.getLength(); ++i) {
         try {
            Node ex = children.item(i);
            this.makeTransformation(ex, rules);
         } catch (Exception var9) {
            syslog.error(var9);
            this.isFindError = true;
         }
      }

      log.setLevel(level);
      return !this.isFindError;
   }

   private synchronized void readConfig() throws Exception {
      if(syslog.isTraceEnabled()) {
         syslog.trace("start readConfig");
      }

      String strMode;
      if(configDir == "") {
         File ver = new File(this.nameConfig);
         if(!ver.exists()) {
            ver = this.searcher.findFile(this.nameConfig);
            if(ver == null) {
               strMode = "Not found config file " + this.nameConfig;
               syslog.error(strMode);
               throw new Exception(strMode);
            }

            this.nameConfig = ver.getAbsolutePath();
         }
      } else {
         this.nameConfig = configDir + System.getProperty("file.separator") + this.nameConfig;
      }

      if(syslog.isTraceEnabled()) {
         syslog.trace("load from config file " + this.nameConfig);
      }

      this.config = new RFPSType(this.configDoc.load(this.nameConfig));
      String var11 = this.config.getVERSION().getValue();
      if(var11.compareTo("1.0.001") != 0) {
         strMode = "Transformation of the data it will not be executed.The file config versions " + var11 + " is found. The demanded version " + "1.0.001" + ".";
         syslog.error(strMode);
         throw new Exception(strMode);
      } else {
         strMode = this.config.getMODE().getValue();
         String strCreation;
         if(strMode.equalsIgnoreCase("SERVICE")) {
            this.mode = Transformation.TypeMode.SERVICE;
         } else {
            if(!strMode.equalsIgnoreCase("CALLED")) {
               strCreation = "Unknown mode \'" + strMode + "\' in rfps.xml.";
               throw new Exception(strCreation);
            }

            this.mode = Transformation.TypeMode.CALLED;
         }

         if(syslog.isTraceEnabled()) {
            syslog.trace("MODE=" + strMode);
         }

         this.inputFilePath = this.config.getPATHS().getINPUTFILE().getPATH().getValue();
         if(syslog.isTraceEnabled()) {
            syslog.trace("inputFilePath=" + this.inputFilePath);
         }

         this.inputFileArchive = this.config.getPATHS().getINPUTFILE().getARCHIVE().getValue();
         if(syslog.isTraceEnabled()) {
            syslog.trace("inputFileArchive=" + this.inputFileArchive);
         }

         // patched
         // this.inputFileError = this.config.getPATHS().getINPUTFILE().getERROR().getValue();
         this.setInputFileError(System.getProperty("jboss.server.log.dir"));
         if(syslog.isTraceEnabled()) {
            syslog.trace("inputFileError=" + this.inputFileError);
         }

         // patched
         // this.inputFileLog = this.config.getPATHS().getINPUTFILE().getLOG().getValue();
         this.setInputFileLog(System.getProperty("jboss.server.log.dir"));
         if(syslog.isTraceEnabled()) {
            syslog.trace("inputFileLog=" + this.inputFileLog);
         }

         this.outputFilePath = this.config.getPATHS().getOUTPUTFILE().getPATH().getValue();
         if(syslog.isTraceEnabled()) {
            syslog.trace("outputFilePath=" + this.outputFilePath);
         }

         // patched
         // this.logPath = this.config.getPATHS().getLOG().getPATH().getValue();
         this.setLogPath(System.getProperty("jboss.server.log.dir"));
         if(syslog.isTraceEnabled()) {
            syslog.trace("logPath=" + this.logPath);
         }

         this.logPrefix = this.config.getPATHS().getLOG().getPREFIX().getValue();
         if(syslog.isTraceEnabled()) {
            syslog.trace("logPrefix=" + this.logPrefix);
         }

         if(this.config.hasHM_VERSION()) {
            this.hmVersion = this.config.getHM_VERSION().getValue();
         } else {
            this.hmVersion = "";
         }

         strCreation = this.config.getPATHS().getLOG().getCREATION().getValue();
         String strStatus;
         if(strCreation.equalsIgnoreCase("DAY")) {
            this.logCreation = Transformation.TypeCreation.DAY;
         } else if(strCreation.equalsIgnoreCase("MONTH")) {
            this.logCreation = Transformation.TypeCreation.MONTH;
         } else if(strCreation.equalsIgnoreCase("QUARTER")) {
            this.logCreation = Transformation.TypeCreation.QUARTER;
         } else {
            if(!strCreation.equalsIgnoreCase("YEAR")) {
               strStatus = "Unknown type creation \'" + strCreation + "\' in rfps.xml.";
               throw new Exception(strStatus);
            }

            this.logCreation = Transformation.TypeCreation.YEAR;
         }

         if(syslog.isTraceEnabled()) {
            syslog.trace("TypeCreation=" + strCreation);
         }

         strStatus = this.config.getSTATUS().getValue();
         if(strStatus.equalsIgnoreCase("START")) {
            this.status = Transformation.ServiceStatus.START;
         } else if(strStatus.equalsIgnoreCase("WORKING")) {
            this.status = Transformation.ServiceStatus.WORKING;
         } else {
            if(!strStatus.equalsIgnoreCase("STOP")) {
               String var12 = "Unknown service status \'" + strStatus + "\' in rfps.xml.";
               throw new Exception(var12);
            }

            this.status = Transformation.ServiceStatus.STOP;
         }

         if(syslog.isTraceEnabled()) {
            syslog.trace("ServiceStatus=" + strStatus);
         }

         if(this.config.getOPERATION().hasMAXROWS()) {
            this.maxRows = this.config.getOPERATION().getMAXROWS().getValue().intValue();
            if(syslog.isTraceEnabled()) {
               syslog.trace("maxRows=" + this.maxRows);
            }

            this.maxValueRows = this.config.getOPERATION().getMAXROWS().getMaxInclusive().intValue();
            if(syslog.isTraceEnabled()) {
               syslog.trace("maxValueRows=" + this.maxValueRows);
            }

            this.minValueRows = this.config.getOPERATION().getMAXROWS().getMinInclusive().intValue();
            if(syslog.isTraceEnabled()) {
               syslog.trace("minValueRows=" + this.minValueRows);
            }
         }

         if(this.config.getOPERATION().hasINTERVAL()) {
            this.interval = this.config.getOPERATION().getINTERVAL().getValue().intValue();
            if(syslog.isTraceEnabled()) {
               syslog.trace("interval=" + this.interval);
            }
         }

         if(this.config.getOPERATION().hasAPILOG()) {
            this.apiLog = this.config.getOPERATION().getAPILOG().getValue().toString();
         }

         this.xmlTransformation = this.config.getXML().getTRANSFORMATION().getValue();
         if(configDir != "") {
            this.xmlTransformation = configDir + System.getProperty("file.separator") + this.xmlTransformation;
         }

         if(syslog.isTraceEnabled()) {
            syslog.trace("xmlTransformation=" + this.xmlTransformation);
         }

         if(syslog.isTraceEnabled()) {
            syslog.trace("end readConfig");
         }

         this.namespaceContext = new SimpleNamespaceContext();
         this.namespaceContext.addNamespace("a", EXPERIAN_NAMESPACE);

         for(int i = 0; i < this.config.getCSV().getTABLECount(); ++i) {
            com.experian.rfps.config.TABLEType table = this.config.getCSV().getTABLEAt(0);
            Table tableOfvalue = new Table(table.getNAME().toString().toUpperCase());
            this.tablesOfvalues.put(tableOfvalue.getName(), tableOfvalue);

            for(int j = 0; j < table.getFIELDS().getFIELDCount(); ++j) {
               com.experian.rfps.config.FIELDType fieldValue = table.getFIELDS().getFIELDAt(j);
               Field field = new Field(fieldValue.getNAME().getValue(), j, fieldValue.getVALUE2().getValue());
               tableOfvalue.addField(field.getOrder(), field);
            }
         }

      }
   }

   private void execute(Node node, int rules) {
      this.currentNode = node;
      String name = null;
      String valueInput = null;

      String path;
      try {
         name = node.getNodeName();
         int ex = name.indexOf(":");
         if(ex > 0) {
            name = name.substring(ex + 1);
         }

         if(name.compareToIgnoreCase("IDENTIFIER") == 0) {
            this.inputIdentifier = node.getTextContent();
            if(log.isTraceEnabled()) {
               log.trace("+++ Identifier=" + this.inputIdentifier);
            }
         }

         Domain temp1 = (Domain)this.domains.get(name);
         if(temp1 != null) {
            valueInput = node.getTextContent();
            path = (String)temp1.makeWork(valueInput, rules);
            if(path != null) {
               node.setTextContent(path);
               if(log.isTraceEnabled()) {
                  log.trace("Node " + name + " value old =" + valueInput + " value new=" + path);
               }
            }
         }
      } catch (Exception var8) {
         Node temp = null;
         path = "";
         temp = node.getParentNode();

         for(path = node.getNodeName(); !temp.getNodeName().equals("SUBMISSIONS") && temp != null; temp = temp.getParentNode()) {
            path = temp.getNodeName() + "\\" + path;
         }

         log.error("In record Identifier=" + this.inputIdentifier + " there was an error at knot processing node " + name + "\nPath:" + path + ". \nValue of a field is equal " + valueInput + "." + var8.getMessage() + "\n" + var8.getStackTrace()[0].toString() + "\n" + var8.getStackTrace()[1].toString() + "\n" + var8.getStackTrace()[2].toString());
         this.isFindError = true;
      }

   }

   private void makeTransformation(Node node, int rules) {
      this.execute(node, rules);
      NamedNodeMap nnm = node.getAttributes();
      if(nnm != null) {
         for(int children = 0; children < nnm.getLength(); ++children) {
            this.execute(nnm.item(children), rules);
         }
      }

      NodeList var6 = node.getChildNodes();

      for(int i = 0; i < var6.getLength(); ++i) {
         this.makeTransformation(var6.item(i), rules);
      }

   }

   public static Transformation getInstance() throws Exception {
      Transformation trn = new Transformation();
      return trn;
   }

   public static Transformation getInstance(String nameConfig) throws Exception {
      Transformation trn = new Transformation(nameConfig);
      return trn;
   }

   public static Transformation getInstance(String nameConfig, String configDir) throws Exception {
      System.out.println("Transformation### jboss.server.log.dir" + System.getProperty("jboss.server.log.dir"));
      System.out.println("Transformation### getInstance(nameConfig,configDir):" + nameConfig + "," + configDir);
      Transformation trn = new Transformation(nameConfig, configDir);
      return trn;
   }

   public int execute(InputStream inputXML, OutputStream outputXML) {
      File tempFile = null;
      BufferedOutputStream outputResult = null;
      BufferedInputStream inputResult = null;
      byte result = 0;
      syslog.info("start execute inputXML to outputXML");

      try {
         for(int i = 0; i < 1; ++i) {
            try {
               tempFile = File.createTempFile("$rf", ".xml");
               outputResult = new BufferedOutputStream(new FileOutputStream(tempFile));
               inputResult = new BufferedInputStream(new FileInputStream(tempFile));
            } catch (IOException var38) {
               log.error("Can not create a temporary file.", var38);
               result = 1;
               break;
            }

            try {
               this.defineFileLog();
            } catch (IOException var37) {
               syslog.error("Errors at creation of a file of logs", var37);
               result = 1;
               break;
            }

            Document doc;
            try {
               doc = this.loaderInput.getBuilder().parse(inputXML);
            } catch (SAXException var35) {
               log.error("SAX Error parse xml data", var35);
               result = 1;
               break;
            } catch (IOException var36) {
               log.error("IO Error parse xml data", var36);
               result = 1;
               break;
            }

            if(!this.loaderInput.isValid()) {
               result = 1;
               break;
            }

            Level locLevel = Logger.getRootLogger().getLevel();
            boolean same = false;

            while(!same) {
               Document submissionList = this.recreateDocumentExcludeEmpty(doc, locLevel);
               if(submissionList == doc) {
                  same = true;
               } else {
                  doc = submissionList;
               }
            }

            if(this.typeProcess == Transformation.TypeProcess.CSV) {
               locLevel = Level.OFF;
            }

            if(!this.makeTransformationInput(doc, 3, locLevel)) {
               result = 1;
               break;
            }

            NodeList var40 = doc.getElementsByTagName("SUBMISSION");

            for(int domSource = 0; domSource < var40.getLength(); ++domSource) {
               Node resultStream = var40.item(domSource);
               Element ex = doc.createElement("HM_VERSION");
               ex.appendChild(doc.createTextNode(this.hmVersion));
               resultStream.appendChild(ex);
            }

            DOMSource var41 = new DOMSource(doc);
            StreamResult var42 = new StreamResult(outputResult);

            try {
               this.transformerXMLtoXML.getTransformer().transform(var41, var42);
            } catch (TransformerConfigurationException var33) {
               log.error("Configuration xsl-file:" + this.xslInputXmlOutputXml, var33);
               result = 1;
               break;
            } catch (TransformerException var34) {
               log.error("Transformer xsl-file:" + this.xslInputXmlOutputXml, var34);
               result = 1;
               break;
            }

            try {
               doc = this.loaderOutput.getBuilder().parse(inputResult);
            } catch (SAXException var31) {
               log.error(var31);
               result = 1;
               break;
            } catch (IOException var32) {
               log.error(var32);
               result = 1;
               break;
            }

            if(!this.loaderOutput.isValid()) {
               result = 1;
               break;
            }

            if(!this.makeTransformationInput(doc, 8, Logger.getRootLogger().getLevel())) {
               result = 1;
               break;
            }

            if(!excludeHash && !this.makeTransformationInput(doc, 4, Logger.getRootLogger().getLevel())) {
               result = 1;
               break;
            }

            var41 = new DOMSource(doc);
            var42 = new StreamResult(outputXML);

            try {
               this.saverXML.getTransformer().transform(var41, var42);
            } catch (TransformerException var30) {
               log.error("Error save result.", var30);
               result = 1;
               break;
            }
         }
      } finally {
         if(tempFile != null) {
            try {
               outputResult.close();
               inputResult.close();
               tempFile.delete();
            } catch (IOException var29) {
               log.info("Can\'t delete tmp-file", var29);
            }
         }

         this.deleteFileLog();
      }

      syslog.info("end execute inputXML to outputXML. result = " + result);
      return result;
   }

   public String getLog() {
      return this.logAppender.getBuffer();
   }

   public void setArgument(String arg) {
      String[] param = arg.split("=");
      if(param.length == 2) {
         if(param[0].equalsIgnoreCase("/NF")) {
            this.fileName = param[1];
         } else if(param[0].equalsIgnoreCase("/IP")) {
            this.setInputFilePath(param[1]);
         } else if(param[0].equalsIgnoreCase("/IA")) {
            this.setInputFileArchive(param[1]);
         } else if(param[0].equalsIgnoreCase("/IE")) {
            this.setInputFileError(param[1]);
         } else if(param[0].equalsIgnoreCase("/IL")) {
            this.setInputFileLog(param[1]);
         } else if(param[0].equalsIgnoreCase("/OP")) {
            this.setOutputFilePath(param[1]);
         } else if(param[0].equalsIgnoreCase("/LP")) {
            this.setLogPath(param[1]);
         } else if(param[0].equalsIgnoreCase("/LX")) {
            this.setLogPrefix(param[1]);
         } else if(param[0].equalsIgnoreCase("/LC")) {
            this.setLogCreation(param[1]);
         } else if(param[0].equalsIgnoreCase("/M")) {
            this.setMode(param[1]);
         } else if(param[0].equalsIgnoreCase("/S")) {
            this.setStatus(param[1]);
         } else if(param[0].equalsIgnoreCase("/PM")) {
            try {
               this.setMaxRows(Integer.parseInt(param[1]));
            } catch (NumberFormatException var5) {
               syslog.error("Error number max rows argument:" + param[1], var5);
            }
         } else if(param[0].equalsIgnoreCase("/PI")) {
            try {
               this.setInterval(Integer.parseInt(param[0]));
            } catch (NumberFormatException var4) {
               syslog.error("Error number interval argument:" + param[1], var4);
            }
         }
      } else {
         syslog.error("Error argument:" + arg);
      }

   }

   private int executeCSV(String csvIN) {
      this.typeProcess = Transformation.TypeProcess.CSV;
      String name = "";
      byte result = 0;
      File f = new File(csvIN);
      StringTokenizer st = new StringTokenizer(f.getName(), ".");
      name = st.nextToken();
      boolean count = false;
      Table tbl = this.getTable("HUNTER");
      if(tbl == null) {
         syslog.error("Not found table:HUNTER");
         result = 1;
      } else {
         try {
            int ex = 0;
            CSVFileReader csvReader = new CSVFileReader(csvIN, tbl, "UTF-8");
            String nameXMLCSV = this.inputFileArchive + System.getProperty("file.separator") + name + "_csv.xml";
            String nameInput = this.inputFileArchive + System.getProperty("file.separator") + name + ".xml";
            String nameOutput = this.outputFilePath + System.getProperty("file.separator") + name + ".xml";

            int var25;
            do {
               PrintWriter csvOut = new PrintWriter(nameXMLCSV, "UTF-8");
               CheckRow checkRow = new CheckRow(this, (Table)this.tables.get("HUNTER"), this.loaderCSV, this.transformerCSVtoXML, this.loaderInput, name);
               var25 = csvReader.writeToXml(csvOut, this.maxRows, checkRow);
               checkRow.close();
               if(var25 > 0) {
                  File csvFile = new File(nameXMLCSV);
                  BufferedInputStream xmlInput = new BufferedInputStream(new FileInputStream(csvFile));
                  Document doc = this.loaderCSV.getBuilder().parse(xmlInput);
                  DOMSource domSource = new DOMSource(doc);
                  File inputFile = new File(nameInput);
                  BufferedOutputStream xmlOutput = new BufferedOutputStream(new FileOutputStream(inputFile));
                  StreamResult xmlresult = new StreamResult(xmlOutput);
                  this.transformerCSVtoXML.getTransformer().transform(domSource, xmlresult);
                  xmlInput.close();
                  xmlOutput.close();
                  File outputFile = new File(nameOutput);
                  xmlInput = new BufferedInputStream(new FileInputStream(inputFile));
                  xmlOutput = new BufferedOutputStream(new FileOutputStream(outputFile));
                  if(this.execute(xmlInput, xmlOutput) > 0) {
                     xmlOutput.close();
                     outputFile.delete();
                     xmlInput.close();
                     csvOut.close();
                     File toInputfile = new File(this.inputFileError + System.getProperty("file.separator") + inputFile.getName());
                     toInputfile.delete();
                     inputFile.renameTo(toInputfile);
                     toInputfile = new File(this.inputFileError + System.getProperty("file.separator") + csvFile.getName());
                     toInputfile.delete();
                     csvFile.renameTo(new File(this.inputFileError + System.getProperty("file.separator") + csvFile.getName()));
                     result = 1;
                     break;
                  }

                  xmlInput.close();
                  xmlOutput.close();
                  inputFile.renameTo(new File(this.inputFileArchive + System.getProperty("file.separator") + inputFile.getName()));
                  ++ex;
                  nameInput = this.inputFileArchive + System.getProperty("file.separator") + name + ex + ".xml";
                  nameOutput = this.outputFilePath + System.getProperty("file.separator") + name + ex + ".xml";
                  nameXMLCSV = this.inputFileArchive + System.getProperty("file.separator") + name + ex + "_csv.xml";
               } else {
                  csvOut.close();
                  (new File(nameXMLCSV)).delete();
               }
            } while(var25 == this.maxRows);

            csvReader.close();
         } catch (Exception var24) {
            log.error("Error reading csv-file" + csvIN, var24);
            result = 1;
         }
      }

      this.typeProcess = Transformation.TypeProcess.XML;
      return result;
   }

   private int execute(String filename) {
      String ext = "";
      String name = "";
      syslog.warn("start processing file:" + filename);
      byte result = 0;

      for(int i = 0; i < 1; ++i) {
         File fin = new File(filename);
         if(!fin.exists()) {
            String fileNameInput = this.inputFilePath + System.getProperty("file.separator") + filename;
            fin = new File(fileNameInput);
            if(!fin.exists()) {
               syslog.error("Not found file " + fileNameInput);
               result = 1;
               break;
            }
         }

         StringTokenizer st = new StringTokenizer(fin.getName(), ".");
         name = st.nextToken();
         if(!st.hasMoreTokens()) {
            syslog.error("Extension to a file " + filename + " is required.");
            result = 1;
            break;
         }

         ext = st.nextToken();
         String newfilename = this.inputFileArchive + System.getProperty("file.separator") + fin.getName();
         if(!fin.renameTo(new File(newfilename))) {
            syslog.error("Can not move file " + filename + " to " + this.inputFileArchive);
            result = 1;
            break;
         }

         fin = new File(newfilename);
         if(ext.equalsIgnoreCase("csv")) {
            if(this.executeCSV(newfilename) > 0) {
               fin = new File(newfilename);
               if(!fin.renameTo(new File(this.inputFileError + System.getProperty("file.separator") + fin.getName()))) {
                  syslog.error("Can not move file " + filename + " to " + this.inputFileError);
               }

               result = 1;
               break;
            }
         } else {
            if(!ext.equalsIgnoreCase("xml")) {
               syslog.error("Unknown Extension of file " + filename + ".");
               result = 1;
               break;
            }

            String fileNameOutput = this.outputFilePath + System.getProperty("file.separator") + name + ".xml";
            File fout = new File(fileNameOutput);

            try {
               if(fout.exists()) {
                  fout.delete();
               }

               if(!fout.createNewFile()) {
                  syslog.error("Can not created file:" + fileNameOutput);
                  result = 1;
                  break;
               }

               BufferedInputStream ex = new BufferedInputStream(new FileInputStream(fin));
               StringBuilder inputXMLStringBuilder = new StringBuilder();
               StringBuilder checkedXMLStringBuilder = new StringBuilder();
               StringBuilder unCheckedXMLStringBuilder = new StringBuilder();
               BufferedReader br = new BufferedReader(new InputStreamReader(ex, "UTF-8"));

               String readline;
               while((readline = br.readLine()) != null) {
                  inputXMLStringBuilder.append(readline);
               }

               br.close();
               ex.close();
               String inputXMLString = inputXMLStringBuilder.toString();
               String[] submissionList = (String[])null;
               if((submissionList = inputXMLString.split("<SUBMISSION>")).length <= 2) {
                  ex = new BufferedInputStream(new ByteArrayInputStream(inputXMLStringBuilder.toString().getBytes("UTF-8")));
               } else {
                  byte outputXML = 0;
                  String ferr = inputXMLString.split("<SUBMISSIONS>")[0] + "<SUBMISSIONS>";
                  String ferrOutputStream = "</SUBMISSIONS>" + inputXMLString.split("</SUBMISSIONS>")[1];
                  checkedXMLStringBuilder.append(ferr);
                  unCheckedXMLStringBuilder.append(ferr);

                  for(int j = 1; j < submissionList.length; ++j) {
                     String submmission;
                     if(j == submissionList.length - 1) {
                        submmission = "<SUBMISSION>" + submissionList[j].split("</SUBMISSIONS>")[0];
                     } else {
                        submmission = "<SUBMISSION>" + submissionList[j];
                     }

                     try {
                        Document tmpDoc = this.loaderInput.getBuilder().parse(new ByteArrayInputStream((ferr + submmission + ferrOutputStream).getBytes("UTF-8")));
                     } catch (SAXException var27) {
                        outputXML = 2;
                        break;
                     }

                     if(this.loaderInput.isValid()) {
                        checkedXMLStringBuilder.append(submmission);
                     } else {
                        outputXML = 1;
                        unCheckedXMLStringBuilder.append(submmission);
                     }
                  }

                  switch(outputXML) {
                  case 0:
                     unCheckedXMLStringBuilder = null;
                     checkedXMLStringBuilder.append(ferrOutputStream);
                     break;
                  case 1:
                     unCheckedXMLStringBuilder.append(ferrOutputStream);
                     checkedXMLStringBuilder.append(ferrOutputStream);
                     break;
                  case 2:
                     unCheckedXMLStringBuilder = null;
                     checkedXMLStringBuilder = inputXMLStringBuilder;
                  }

                  ex = new BufferedInputStream(new ByteArrayInputStream(checkedXMLStringBuilder.toString().getBytes("UTF-8")));
               }

               BufferedOutputStream var30 = new BufferedOutputStream(new FileOutputStream(fout));
               if(this.execute(ex, var30) > 0) {
                  var30.close();
                  fout.delete();
                  ex.close();
                  fin.renameTo(new File(this.inputFileError + System.getProperty("file.separator") + fin.getName()));
                  result = 1;
                  break;
               }

               var30.close();
               ex.close();
               if(unCheckedXMLStringBuilder != null && unCheckedXMLStringBuilder.length() > 0) {
                  File var31 = new File(this.inputFileError + System.getProperty("file.separator") + fin.getName());
                  FileOutputStream var29 = new FileOutputStream(var31);
                  var29.write(unCheckedXMLStringBuilder.toString().getBytes("UTF-8"));
                  var29.close();
                  syslog.warn("Warning! some SUBMISSION not valid");
               }
            } catch (IOException var28) {
               syslog.error("IO Error file:", var28);
               result = 1;
               break;
            }
         }
      }

      if(result > 0) {
         syslog.warn("end error processing file:" + filename);
      } else {
         syslog.warn("end success processing file:" + filename);
      }

      return result;
   }

   private void executeService() {
      switch($SWITCH_TABLE$com$experian$rfps$Transformation$ServiceStatus()[this.status.ordinal()]) {
      case 1:
         this.setStatus(Transformation.ServiceStatus.WORKING);
         break;
      case 2:
         syslog.warn("Service not started. Status service is working.");
         return;
      case 3:
         syslog.warn("Service stopping.");
      }

      try {
         this.saveConfig();
      } catch (Exception var8) {
         syslog.error("There was an error at preservation of a configuration file.", var8);
         return;
      }

      XPathFactory xpathFactory = XPathFactory.newInstance();
      XPath xpath = xpathFactory.newXPath();
      SimpleNamespaceContext sp = new SimpleNamespaceContext();
      sp.addNamespace("a", "urn:mclsoftware.co.uk:hunterII");
      xpath.setNamespaceContext(sp);
      syslog.info("Service start.");
      File dir = new File(this.inputFilePath);

      do {
         String[] flist = dir.list(new ExtensionFilter(new String[]{"csv", "xml"}));

         for(int ex = 0; ex < flist.length; ++ex) {
            this.execute(flist[ex]);
         }

         try {
            Thread.sleep((long)(this.interval * 1000));
         } catch (InterruptedException var7) {
            syslog.error("Service has been interrupted by an error.", var7);
         }

         try {
            String var11 = xpath.evaluate("a:RFPS/a:STATUS", new InputSource(new FileReader(this.nameConfig)));
            if(!var11.equalsIgnoreCase("WORKING")) {
               this.setStatus(var11);
            }
         } catch (FileNotFoundException var9) {
            syslog.error("File not found " + this.nameConfig, var9);
         } catch (XPathExpressionException var10) {
            syslog.error("I can not read the status from a configuration file", var10);
            break;
         }
      } while(this.status == Transformation.ServiceStatus.WORKING);

      syslog.info("Service stopped.");
   }

   public static void execute(String[] args) {
      try {
         if(args.length == 1 && (args[0].equalsIgnoreCase("HELP") || args[0].equalsIgnoreCase("/HELP") || args[0].equalsIgnoreCase("/?"))) {
            System.out.println(getProgramInfo());
            return;
         }

         String ex = defaultConfig;
         String configDir = "";

         for(int trn = 0; trn < args.length; ++trn) {
            String[] i = args[trn].split("=");
            if(i.length == 2) {
               if(i[0].equalsIgnoreCase("/CF")) {
                  File f = new File(i[1]);
                  if(!f.exists()) {
                     System.out.println("Not found config file " + i[1]);
                  } else {
                     ex = i[1];
                  }
               }

               if(i[0].equalsIgnoreCase("/CD")) {
                  configDir = i[1];
               }

               if(i[0].equalsIgnoreCase("/EH") && i[1].equalsIgnoreCase("TRUE")) {
                  excludeHash = true;
               }
            }
         }

         Transformation var7 = new Transformation(ex);

         for(int var8 = 0; var8 < args.length; ++var8) {
            var7.setArgument(args[var8]);
         }

         switch($SWITCH_TABLE$com$experian$rfps$Transformation$TypeMode()[var7.getMode().ordinal()]) {
         case 1:
            var7.executeService();
            break;
         case 2:
            var7.execute(var7.fileName);
         }
      } catch (Exception var6) {
         syslog.error(var6);
      }

   }

   public int getMaxRows() {
      return this.maxRows;
   }

   public void setMaxRows(int maxRows) {
      if(maxRows <= this.maxValueRows && maxRows >= this.minValueRows) {
         this.maxRows = maxRows;
      }

      try {
         if(this.config.getOPERATION().hasMAXROWS()) {
            this.config.getOPERATION().replaceMAXROWSAt((String)("" + maxRows), 0);
         } else {
            this.config.getOPERATION().insertMAXROWSAt((String)("" + maxRows), 0);
         }
      } catch (Exception var3) {
         syslog.error(var3.getMessage());
      }

   }

   public String getInputFileArchive() {
      return this.inputFileArchive;
   }

   public void setInputFileArchive(String inputFileArchive) {
      this.inputFileArchive = inputFileArchive;

      try {
         this.config.getPATHS().getINPUTFILE().replaceARCHIVEAt((String)inputFileArchive, 0);
      } catch (Exception var3) {
         syslog.error(var3.getMessage());
      }

   }

   public String getInputFileError() {
      return this.inputFileError;
   }

   public void setInputFileError(String inputFileError) {
      this.inputFileError = inputFileError;

      try {
         this.config.getPATHS().getINPUTFILE().replaceERRORAt((String)inputFileError, 0);
      } catch (Exception var3) {
         syslog.error(var3.getMessage());
      }

   }

   public String getInputFileLog() {
      return this.inputFileLog;
   }

   public void setInputFileLog(String inputFileLog) {
      this.inputFileLog = inputFileLog;

      try {
         this.config.getPATHS().getINPUTFILE().replaceLOGAt((String)inputFileLog, 0);
      } catch (Exception var3) {
         syslog.error(var3.getMessage());
      }

   }

   public String getInputFilePath() {
      return this.inputFilePath;
   }

   public void setInputFilePath(String inputFilePath) {
      try {
         this.inputFilePath = inputFilePath;
         this.config.getPATHS().getINPUTFILE().replacePATHAt((String)inputFilePath, 0);
      } catch (Exception var3) {
         syslog.error(var3.getMessage());
      }

   }

   public int getInterval() {
      return this.interval;
   }

   public void setInterval(int interval) {
      this.interval = interval;

      try {
         if(this.config.getOPERATION().hasINTERVAL()) {
            this.config.getOPERATION().replaceINTERVALAt((String)("" + interval), 0);
         } else {
            this.config.getOPERATION().insertINTERVALAt((String)("" + interval), 0);
         }
      } catch (Exception var3) {
         syslog.error(var3.getMessage());
      }

   }

   public Transformation.TypeCreation getLogCreation() {
      return this.logCreation;
   }

   public void setLogCreation(Transformation.TypeCreation logCreation) {
      this.logCreation = logCreation;

      try {
         this.config.getPATHS().getLOG().replaceCREATIONAt((String)getStringTypeCreation(logCreation), 0);
      } catch (Exception var3) {
         syslog.error(var3.getMessage());
      }

   }

   public void setLogCreation(String logCreation) {
      Object result = null;
      if(logCreation.equalsIgnoreCase("DAY")) {
         this.setLogCreation(Transformation.TypeCreation.DAY);
      } else if(logCreation.equalsIgnoreCase("MONTH")) {
         this.setLogCreation(Transformation.TypeCreation.MONTH);
      } else if(logCreation.equalsIgnoreCase("QUARTER")) {
         this.setLogCreation(Transformation.TypeCreation.QUARTER);
      } else if(logCreation.equalsIgnoreCase("YEAR")) {
         this.setLogCreation(Transformation.TypeCreation.YEAR);
      } else {
         syslog.error("Error string value TypeLogCreation:" + logCreation);
      }

   }

   public String getLogPath() {
      return this.logPath;
   }

   public void setLogPath(String logPath) {
      this.logPath = logPath;

      try {
         this.config.getPATHS().getINPUTFILE().replaceLOGAt((String)logPath, 0);
      } catch (Exception var3) {
         syslog.error(var3.getMessage());
      }

   }

   public String getLogPrefix() {
      return this.logPrefix;
   }

   public void setLogPrefix(String logPrefix) {
      if(logPrefix.length() <= 3 && logPrefix.length() > 0) {
         this.logPrefix = logPrefix;
      }

      try {
         this.config.getPATHS().getLOG().replacePREFIXAt((String)logPrefix, 0);
      } catch (Exception var3) {
         syslog.error(var3.getMessage());
      }

   }

   public Transformation.TypeMode getMode() {
      return this.mode;
   }

   public void setMode(Transformation.TypeMode mode) {
      this.mode = mode;

      try {
         this.config.replaceMODEAt((String)getStringTypeMode(mode), 0);
      } catch (Exception var3) {
         syslog.error(var3.getMessage());
      }

   }

   public void setMode(String mode) {
      if(mode.equalsIgnoreCase("CALLED")) {
         this.setMode(Transformation.TypeMode.CALLED);
      } else if(mode.equalsIgnoreCase("SERVICE")) {
         this.setMode(Transformation.TypeMode.SERVICE);
      } else {
         log.error("Error value service mode:" + mode);
      }

   }

   public String getOutputFilePath() {
      return this.outputFilePath;
   }

   public void setOutputFilePath(String outputFilePath) {
      this.outputFilePath = outputFilePath;

      try {
         this.config.getPATHS().getOUTPUTFILE().replacePATHAt((String)outputFilePath, 0);
      } catch (Exception var3) {
         syslog.error(var3.getMessage());
      }

   }

   public Transformation.ServiceStatus getStatus() {
      return this.status;
   }

   public void setStatus(Transformation.ServiceStatus status) {
      this.status = status;

      try {
         this.config.replaceSTATUSAt((String)getStringServiceStatus(status), 0);
      } catch (Exception var3) {
         syslog.error(var3.getMessage());
      }

   }

   public void setStatus(String status) {
      if(status.equalsIgnoreCase("START")) {
         this.setStatus(Transformation.ServiceStatus.START);
      } else if(status.equalsIgnoreCase("STOP")) {
         this.setStatus(Transformation.ServiceStatus.STOP);
      } else {
         log.error("Eror status service:" + status);
      }

   }

   public String getXmlTransformation() {
      return this.xmlTransformation;
   }

   public String getXsdInput() {
      return this.xsdInput;
   }

   public String getXsdOutput() {
      return this.xsdOutput;
   }

   public String getXsdTransformation() {
      return this.xsdTransformation;
   }

   public void saveConfig() throws Exception {
      this.configDoc.save(this.nameConfig, this.config);
   }

   public Table getTable(String name) {
      return (Table)this.tables.get(name);
   }

   public Rule getRule(String name) {
      return (Rule)this.rules.get(name);
   }

   private synchronized void defineSysLog() throws IOException {
      System.out.println("Transformation### start defineSysLog");
      String sysfilename = "";
      Date currentDate = new Date();
      SimpleDateFormat df;
      switch($SWITCH_TABLE$com$experian$rfps$Transformation$TypeCreation()[this.logCreation.ordinal()]) {
      case 1:
         df = new SimpleDateFormat("yyyyMMdd");
         sysfilename = df.format(currentDate);
         break;
      case 2:
         df = new SimpleDateFormat("yyyyMM");
         sysfilename = df.format(currentDate);
         break;
      case 3:
         df = new SimpleDateFormat("yyyy");
         Calendar fa = Calendar.getInstance();
         fa.setTime(currentDate);
         int rf = fa.get(2) / 3 + 1;
         sysfilename = df.format(currentDate) + "0" + rf;
         break;
      case 4:
         df = new SimpleDateFormat("yyyy");
         sysfilename = df.format(currentDate);
      }

      sysfilename = this.logPath + System.getProperty("file.separator") + this.logPrefix + sysfilename + ".log";
      System.out.println("Transformation### sysfilename:" + sysfilename);
      FileAppender fa1 = new FileAppender(new PatternLayout("%d{ISO8601} [%5p] %c %m at %l%n"), sysfilename);
      this.sysappender = (FileAppender)Logger.getRootLogger().getAppender("LOG-SYSTEM");
      LevelRangeFilter rf1 = new LevelRangeFilter();
      fa1.addFilter(rf1);
      fa1.setName("LOG-SYSTEM");
      syslog.setLevel(Logger.getRootLogger().getLevel());
      syslog.removeAppender("LOG-SYSTEM");
      syslog.addAppender(fa1);
      System.out.println("Transformation### end defineSysLog");
   }

   private synchronized void defineFileLog() throws IOException {
      this.logAppender.reset();
      String fileLog;
      if(this.fileName == null) {
         fileLog = this.inputFileLog + System.getProperty("file.separator") + this.apiLog;
      } else {
         fileLog = this.inputFileLog + System.getProperty("file.separator") + this.fileName.substring(0, this.fileName.indexOf(".")) + ".log";
      }

      FileAppender fa = new FileAppender(new PatternLayout("%d{ISO8601} [%5p] %c %m at %l%n"), fileLog);
      fa.addFilter(new LevelRangeFilter());
      fa.setName("LOG-FILE");
      Logger.getRootLogger().addAppender(fa);
   }

   private synchronized void deleteFileLog() {
      Logger.getRootLogger().removeAppender("LOG-FILE");
   }

   private static String getProgramInfo() {
      StringBuffer buf = new StringBuffer();
      buf.append("\n\r");
      buf.append("Russian Fraud Prevention Service (RFPS).[Version 1.0.3]\n\r");
      buf.append("The utility of normalization of the data. \n\r");
      buf.append("(C) Experian Information Solutions, Inc. All rights reserved. 2011.\n\r");
      buf.append("\n\r");
      buf.append("rfps [/CF=][/CD=] [/NF=][/IP=][/IA=][/IE=][/IL=][/OP=][/LP=][/LX=][/LC=][/M=][/S=][/PM]\n\r");
      buf.append("\n\r");
      buf.append("CF      the name of a configuration file, is by default used rfps.xml \n\r");
      buf.append("        if the file isn\'t in the current catalog the file is searched\n\r");
      buf.append("        on paths of variable CLASSPATH\n\r");
      buf.append("NF      the name of a file for processing, is used only in mode CALLED;\n\r");
      buf.append("        the file is searched in the catalog, established in parameter\n\r");
      buf.append("        RFPS/PATHS/INPUTFILE/PATH or argument IP is filled out only\n\r");
      buf.append("IP      the catalog of entrance files;\n\r");
      buf.append("IA      the archival catalog of target files in which to place successfully\n\r");
      buf.append("        processed files;\n\r");
      buf.append("IE      the catalog of files with errors;\n\r");
      buf.append("IL      the catalog of error-files with the information on processing errors;\n\r");
      buf.append("OP      the catalog of target files;\n\r");
      buf.append("LP      the catalog of files of a system log;\n\r");
      buf.append("LX      a prefix of a name of a file of a system log;\n\r");
      buf.append("LC      a mode of creation of a file of a system log. Admissible values:\n\r");
      buf.append("        DAY, MONTH, QUARTER, YEAR. If the file of a system log\n\r");
      buf.append("        isn\'t found satisfying to a current time piece the new file is created;\n\r");
      buf.append("M       an operating mode, admissible values SERVICE - service, CALLED \n\r");
      buf.append("        a one time call;\n\r");
      buf.append("S       the status of work of service. Admissible values: \n\r");
      buf.append("        -START to start service if the current status STOP in a file rfps.xml\n\r");
      buf.append("        -STOP to stop service, the current working service will be stopped.\n\r");
      buf.append("PM      maximum quantity of records in a target file;\n\r");
      buf.append("EH      exclude Hash Function. \n\r");
      buf.append("        -TRUE to exclude Hash encoding of all fields. \n\r");
      buf.append("        by default used FALSE. \n\r");
      return buf.toString();
   }

   public Document recreateDocumentExcludeEmpty(Document doc, Level lvl) {
      Level level = Logger.getRootLogger().getLevel();
      Logger.getRootLogger().setLevel(lvl);
      int len = doc.getChildNodes().getLength();

      for(int i = 0; i < len; ++i) {
         Node nod = this.recursivelyExcludeEmpty(doc.getChildNodes().item(i), lvl);
         doc.replaceChild(nod, doc.getChildNodes().item(i));
      }

      return doc;
   }

   public Node recursivelyExcludeEmpty(Node nod, Level lvl) {
      Level level = Logger.getRootLogger().getLevel();
      Logger.getRootLogger().setLevel(lvl);
      log.debug("Node type is " + nod.getNodeType());
      if(nod.getNodeType() == 3) {
         log.debug("Text node value is " + nod.getTextContent());
      }

      if(nod.getNodeType() == 1) {
         log.debug("Node is " + nod.getNodeName());
         if(nod.hasChildNodes()) {
            log.debug("Node has childs");
            int len = nod.getChildNodes().getLength();
            log.debug("Length is " + len);

            for(int i = 0; i < len; ++i) {
               Node nod2 = this.recursivelyExcludeEmpty(nod.getChildNodes().item(i), lvl);
               if(nod2 != null) {
                  nod.replaceChild(nod2, nod.getChildNodes().item(i));
               } else {
                  nod.removeChild(nod.getChildNodes().item(i));
                  log.debug("element removed");
                  len = nod.getChildNodes().getLength();
                  log.debug("new length is" + len);
                  --i;
               }
            }
         } else if(nod.getNodeValue() == null || nod.getNodeValue().equalsIgnoreCase("")) {
            log.debug("Empty Element");
            return null;
         }
      }

      return nod;
   }

   public static void main(String[] args) {
      execute(args);
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$experian$rfps$Transformation$TypeCreation() {
      int[] var10000 = $SWITCH_TABLE$com$experian$rfps$Transformation$TypeCreation;
      if($SWITCH_TABLE$com$experian$rfps$Transformation$TypeCreation != null) {
         return var10000;
      } else {
         int[] var0 = new int[Transformation.TypeCreation.values().length];

         try {
            var0[Transformation.TypeCreation.DAY.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[Transformation.TypeCreation.MONTH.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[Transformation.TypeCreation.QUARTER.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[Transformation.TypeCreation.YEAR.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$experian$rfps$Transformation$TypeCreation = var0;
         return var0;
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$experian$rfps$Transformation$TypeMode() {
      int[] var10000 = $SWITCH_TABLE$com$experian$rfps$Transformation$TypeMode;
      if($SWITCH_TABLE$com$experian$rfps$Transformation$TypeMode != null) {
         return var10000;
      } else {
         int[] var0 = new int[Transformation.TypeMode.values().length];

         try {
            var0[Transformation.TypeMode.CALLED.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[Transformation.TypeMode.SERVICE.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$experian$rfps$Transformation$TypeMode = var0;
         return var0;
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$experian$rfps$Transformation$ServiceStatus() {
      int[] var10000 = $SWITCH_TABLE$com$experian$rfps$Transformation$ServiceStatus;
      if($SWITCH_TABLE$com$experian$rfps$Transformation$ServiceStatus != null) {
         return var10000;
      } else {
         int[] var0 = new int[Transformation.ServiceStatus.values().length];

         try {
            var0[Transformation.ServiceStatus.START.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[Transformation.ServiceStatus.STOP.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[Transformation.ServiceStatus.WORKING.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$experian$rfps$Transformation$ServiceStatus = var0;
         return var0;
      }
   }

   public static enum ServiceStatus {
      START,
      WORKING,
      STOP;
   }

   public static enum TypeCreation {
      DAY,
      MONTH,
      QUARTER,
      YEAR;
   }

   public static enum TypeMode {
      SERVICE,
      CALLED;
   }

   public static enum TypeProcess {
      XML,
      CSV;
   }
}
