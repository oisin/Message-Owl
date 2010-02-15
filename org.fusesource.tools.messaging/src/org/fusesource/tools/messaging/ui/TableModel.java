package org.fusesource.tools.messaging.ui;

public interface TableModel {
   
    public String getPreferenceKey();    
    
    public String  getDefaultPreferenceValue();
    
    public void saveModel();
    
}
