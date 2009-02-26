package example.camel.jboss;

import java.io.IOException;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.List;
import java.util.LinkedList;
import org.apache.camel.util.ResolverUtil;
import org.apache.log4j.Logger;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;

public class JbossResolverUtil
    extends ResolverUtil
{
    private final static Logger log = Logger.getLogger(JbossResolverUtil.class);

    @Override
    protected void find(Test test, String packageName, ClassLoader loader) {
        Enumeration<URL> urls;
        try {
            urls = getResources(loader, packageName);
            if (!urls.hasMoreElements()) {
                log.trace("No URLs returned by classloader");
            }
        } catch (IOException ioe) {
            log.warn("Could not read package: " + packageName, ioe);
            return;
        }

        while (urls.hasMoreElements()) {
            URL url = null;
            try {
                url = urls.nextElement();
                List<String> classNames = getClassNames(url);
                loadImplementationsFromList(test, packageName, classNames);
            } catch (IOException e) {
                log.warn("Could not read entries in " + url, e);
            } catch (URISyntaxException ue) {
                log.warn("URI could not be parsed: " + packageName, ue);
            }
        }
    }

    private List<String> getClassNames(URL url) throws IOException, URISyntaxException {
        List<String> results = new LinkedList<String>();
        VFS vfs = VFS.getVFS(url);
        VirtualFile root = vfs.getRoot();
        List<VirtualFile> children = root.getChildrenRecursively();
        for (VirtualFile vf : children) {
            String pathName = vf.toURL().toExternalForm();
            if (pathName.endsWith(".class")) {
                results.add(vf.getPathName());
            }
        }
        return results;
    }

    private void loadImplementationsFromList(Test test, String parent, List<String> classNames) {
        for (String name : classNames) {
            StringBuilder builder = new StringBuilder();
            if (name != null) {
                name = name.trim();
                builder.append(parent).append("/").append(name);
                String packageOrClass = parent == null ? name : builder.toString();
                addIfMatching(test, packageOrClass);
            }
        }
    }
}
