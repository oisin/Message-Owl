package org.fusesource.tools.core.ui.url.urlchooser;

import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class URLChooserFilter extends LinkedHashMap
{
    public URLChooserFilter()
    {
    }

    public URLChooserFilter(String filter)
    {
        put(filter, filter);
    }
    
    public URLChooserFilter(String[] filter)
    {
        for(int i = 0; i < filter.length; i++)
            put(filter[i], filter[i]);
    }
    
    public String[] getFilterNames()
    {
        return (String[])values().toArray(new String[0]);
    }
    
    public String[] getFilterExtensions()
    {
        return (String[])keySet().toArray(new String[0]);
    }
    
    public Object put(Object key, Object value)
    {
        boolean equal = key.equals(value);
        
        StringTokenizer st = new StringTokenizer((String)key, ";");
        
        while(st.hasMoreTokens())
        {
            String token = st.nextToken();
        
            super.put(token, equal ? token : value);
        }
        return null;
    }
}
