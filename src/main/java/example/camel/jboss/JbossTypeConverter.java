package example.camel.jboss;

import org.apache.camel.impl.converter.AnnotationTypeConverterLoader;
import org.apache.camel.impl.converter.AsyncProcessorTypeConverter;
import org.apache.camel.impl.converter.PropertyEditorTypeConverter;
import org.apache.camel.impl.converter.ToStringTypeConverter;
import org.apache.camel.impl.converter.ArrayTypeConverter;
import org.apache.camel.impl.converter.DefaultTypeConverter;
import org.apache.camel.spi.Injector;
public class JbossTypeConverter
    extends DefaultTypeConverter
{
    public JbossTypeConverter(Injector injector)
    {
        super(injector);
        getTypeConverterLoaders().clear();
        getTypeConverterLoaders().add(new AnnotationTypeConverterLoader(new JbossResolverUtil()));
    }
}
