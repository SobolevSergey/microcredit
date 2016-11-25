package com.experian.rfps;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

public class BufferAppender extends AppenderSkeleton {
   private static final Logger log = Logger.getLogger(BufferAppender.class);
   private StringBuffer buffer = new StringBuffer();

   public BufferAppender(Layout layout) {
      this.layout = layout;
   }

   protected void append(LoggingEvent le) {
      this.buffer.append(this.layout.format(le));
   }

   public void close() {
   }

   public boolean requiresLayout() {
      return true;
   }

   public String getBuffer() {
      return this.buffer.toString();
   }

   public void reset() {
      this.buffer = new StringBuffer();
   }
}
