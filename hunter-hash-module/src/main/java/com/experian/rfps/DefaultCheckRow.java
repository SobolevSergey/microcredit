package com.experian.rfps;

import com.experian.rfps.ICheckRow;

public class DefaultCheckRow implements ICheckRow {
   public boolean check(String[] row) {
      return true;
   }
}
