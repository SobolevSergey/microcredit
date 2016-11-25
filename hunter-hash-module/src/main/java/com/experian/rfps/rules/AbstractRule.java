package com.experian.rfps.rules;

import com.experian.rfps.Transformation;
import java.util.Map;

public abstract class AbstractRule {
   public Transformation app;
   protected boolean isPrepared = false;

   public void setApp(Transformation app) {
      this.app = app;
   }

   public abstract boolean prepare(Map var1);

   public boolean isPrepared() {
      return this.isPrepared;
   }
}
