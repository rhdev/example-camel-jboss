package example.camel.jboss;

import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.TypeConverter;

public class JbossCamelContext
    extends DefaultCamelContext
{
    @Override
    protected TypeConverter createTypeConverter() {
        return new JbossTypeConverter(getInjector());
    }
}

