package View.Configuration;

import Control.Connect.DbProvider;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ThymeleafConfig implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sceInternal = sce.getServletContext();
        sceInternal.setRequestCharacterEncoding("UTF-8");
        sceInternal.setResponseCharacterEncoding("UTF-8");

        TemplateEngine engine = templateEngine(sceInternal);

        ContextUtil.storeTemplateEngine(sceInternal, engine);

        ContextUtil.storeDbProvider(sceInternal, new DbProvider());
    }

    public void contextDestroyed(ServletContextEvent sce) {
        DbProvider dbProvider = ContextUtil.getDbProvider(sce.getServletContext());
        dbProvider.disconnect();
    }

    private TemplateEngine templateEngine(ServletContext servletContext) {
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver(servletContext));
        return engine;
    }

    private ITemplateResolver templateResolver(ServletContext servletContext) {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(servletContext);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }
}
