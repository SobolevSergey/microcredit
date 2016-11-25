package com.experian.rfps;

import com.experian.rfps.Transformation;
import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleHash;
import com.experian.rfps.rules.IRuleNormalization;
import com.experian.rfps.rules.IRuleValidation;
import java.util.HashMap;
import java.util.Map;

public class Rule {
   public static final int RULE_TYPE_NORMALIZATION = 1;
   public static final int RULE_TYPE_VALIDATION = 2;
   public static final int RULE_TYPE_HASH = 4;
   public static final int RULE_TYPE_PREHASH = 8;
   public static final String STR_NORMALIZATION = "NORMALIZATION";
   public static final String STR_VALIDATION = "VALIDATION";
   public static final String STR_HASH = "HASH";
   public static final String STR_PREHASH = "PREHASH";
   private String description;
   private Transformation app;
   private String name;
   private String classname;
   private int type;
   private Object instance;
   private boolean isValid;
   private Class cls;
   private Map<String, String> parameters = new HashMap();

   public Rule(Transformation app, String name, String classname, String type, String description) throws Exception {
      this.Init(app, name, classname, type, description);
   }

   public Rule(Transformation app, String name, String classname, String type) throws Exception {
      this.Init(app, name, classname, type, "");
   }

   private void Init(Transformation app, String name, String classname, String type, String descr) throws Exception {
      this.description = descr;
      this.classname = classname;
      this.name = name;
      this.app = app;
      if("NORMALIZATION".equalsIgnoreCase(type)) {
         this.type = 1;
      }

      if("HASH".equalsIgnoreCase(type)) {
         this.type = 4;
      }

      if("PREHASH".equalsIgnoreCase(type)) {
         this.type = 8;
      }

      if("VALIDATION".equalsIgnoreCase(type)) {
         this.type = 2;
      }

      if(this.type == 0) {
         throw new Exception("Unknown type transformation" + type + " of rule " + name + ".");
      } else {
         this.isValid = false;
      }
   }

   public String getDescription() {
      return this.description;
   }

   public String getClassname() {
      return this.classname;
   }

   public void setClassname(String classname) {
      this.classname = classname;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public void putParameter(String name, String value) {
      this.parameters.put(name, value);
   }

   public String getParameter(String name) {
      return (String)this.parameters.get(name);
   }

   public boolean prepare() {
      if(this.isValid) {
         ((AbstractRule)this.instance).setApp(this.app);
         return ((AbstractRule)this.instance).prepare(this.parameters);
      } else {
         return false;
      }
   }

   public void load() throws Exception {
      if(this.name != null && this.classname != null) {
         try {
            this.cls = Class.forName(this.classname);
         } catch (ClassNotFoundException var2) {
            throw new Exception("Not found class " + this.classname + " of rule " + this.name + ".");
         }

         this.instance = this.cls.newInstance();
         ((AbstractRule)this.instance).setApp((Transformation)null);
         IRuleNormalization irule;
         switch(this.type) {
         case 1:
            irule = (IRuleNormalization)this.instance;
            if(irule == null) {
               throw new Exception("Not interface IRuleNormalization in class " + this.classname + " of rule " + this.name + ".");
            }
            break;
         case 2:
            IRuleValidation irule2 = (IRuleValidation)this.instance;
            if(irule2 == null) {
               throw new Exception("Not interface IRuleValidation in class " + this.classname + " of rule " + this.name + ".");
            }
            break;
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            throw new Exception("Error type (" + this.type + ") of rule " + this.name + ".");
         case 4:
            IRuleHash irule1 = (IRuleHash)this.instance;
            if(irule1 == null) {
               throw new Exception("Not interface IRuleHash in class " + this.classname + " of rule " + this.name + ".");
            }
            break;
         case 8:
            irule = (IRuleNormalization)this.instance;
            if(irule == null) {
               throw new Exception("Not interface IRuleNormalization in class " + this.classname + " of rule " + this.name + ".");
            }
         }

         this.isValid = true;
      } else {
         throw new Exception("Classname or name of rule equal null.");
      }
   }

   public Object invoke(Object inputObject) throws Exception {
      Object result = Boolean.FALSE;
      if(this.isValid) {
         IRuleNormalization irule;
         switch(this.type) {
         case 1:
            irule = (IRuleNormalization)this.instance;
            result = irule.transform(inputObject, this.parameters);
            break;
         case 2:
            IRuleValidation irule2 = (IRuleValidation)this.instance;
            result = Boolean.valueOf(irule2.validation(inputObject, this.parameters));
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            break;
         case 4:
            IRuleHash irule1 = (IRuleHash)this.instance;
            result = irule1.encode((String)inputObject, this.parameters);
            break;
         case 8:
            irule = (IRuleNormalization)this.instance;
            result = irule.transform(inputObject, this.parameters);
         }
      }

      return result;
   }
}
