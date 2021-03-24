package View.Configuration;

import Control.Connect.DbProvider;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;

public class ContextUtil {

    private static final String CONTEXT_ATTR_TEMPLATE_ENGINE = "com.collector.TemplateEngineInstance";
    private static final String CONTEXT_ATTR_DB_PROVIDER = "DbProvider";

    public static void storeTemplateEngine(ServletContext context, TemplateEngine engine) {
        context.setAttribute(CONTEXT_ATTR_TEMPLATE_ENGINE, engine);
    }

    public static TemplateEngine getTemplateEngine(ServletContext context) {
        return (TemplateEngine) context.getAttribute(CONTEXT_ATTR_TEMPLATE_ENGINE);
    }
    public static void storeDbProvider(ServletContext context, DbProvider dbProvider) {
        context.setAttribute(CONTEXT_ATTR_DB_PROVIDER, dbProvider);
    }

    public static DbProvider getDbProvider(ServletContext context) {
        return (DbProvider) context.getAttribute(CONTEXT_ATTR_DB_PROVIDER);
    }
}
