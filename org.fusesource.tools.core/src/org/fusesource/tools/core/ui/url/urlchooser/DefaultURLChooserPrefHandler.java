package org.fusesource.tools.core.ui.url.urlchooser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

/**
 * To change this template use File | Settings | File Templates.
 */
public class DefaultURLChooserPrefHandler  {
    private static DefaultURLChooserPrefHandler urlChooserPrefHandler = null;
    private IPreferenceStore store = PlatformUI.getPreferenceStore();

    private DefaultURLChooserPrefHandler() {
    }

    public static DefaultURLChooserPrefHandler getInstance() {
        if (urlChooserPrefHandler == null)
            urlChooserPrefHandler = new DefaultURLChooserPrefHandler();
        return urlChooserPrefHandler;
    }

    public URL[] getURLs(String context) {
        List urls = _getURLs(context);
        return (URL[]) urls.toArray(new URL[urls.size()]);
    }

    private List _getURLs(String context) {
        return _getURLs(getURLStrings(context));
    }

    private List _getURLs(Set urlSet){
        List urls = new ArrayList(urlSet.size());
        for (Iterator iterator = urlSet.iterator(); iterator.hasNext();) {
            try {
                urls.add(new URL((String) iterator.next()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    private Set getURLStrings(String context) {
        String prevHistory = store.getString(context);
        StringTokenizer tokenizer = new StringTokenizer(prevHistory, ",");
        Set urlSet = new LinkedHashSet();
        while (tokenizer.hasMoreTokens()) {
            urlSet.add(tokenizer.nextToken());
        }
        return urlSet;
    }

    public void addURL(String context, URL url) {
        Set urlsSet = getURLStrings(context);
        List urls = _getURLs(urlsSet);
        String urlString = url.toString();
        if(urlsSet.contains(urlString)){
            if(urls.indexOf(url)==0)
        		return;//url passed is already at beginnning of list
        	urls.remove(url);
        }
        urls.add(0,url);
        addToPrefStore(context, urls);
    }

    private void addToPrefStore(String context, List urlList) {
        String commaSeparatedURLs = getCommaSeperatedURLs(urlList);
        store.putValue(context, commaSeparatedURLs);
    }

    private String getCommaSeperatedURLs(List urlList) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < urlList.size(); i++) {
            URL url = (URL) urlList.get(i);
            buffer.append(url.toString());
            if (i != (urlList.size() - 1))
                buffer.append(",");
        }
        return buffer.toString();
    }

    /**
     * removes the url from the list of urls stored against the passed context
     * @param context
     * @param url
     */
    public void removeURL(String context, URL url) {
        List list = _getURLs(context);
        list.remove(url);
        store.setValue(context, getCommaSeperatedURLs(list));
    }
}
