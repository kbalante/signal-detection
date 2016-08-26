package my.vaadin.app;

import com.vaadin.server.*;
import com.vaadin.spring.server.SpringVaadinServlet;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;

/**
 * Add a new listener that adds a responsive viewport to the html head tag
 * to make the bootstrapped page responsive
 */
@Component("vaadinServlet")
public class SignalDetectionServlet extends SpringVaadinServlet {

    /**
     * Override servlet initialized to add a new listener that adds a responsive viewport
     * to the html head tag
     * @throws ServletException if there is an error
     */
    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener(new SessionInitListener() {

            /**
             * Session init
             * @param event session init event
             */
            @Override
            public void sessionInit(SessionInitEvent event) {
                // add a new listener
                event.getSession().addBootstrapListener(new BootstrapListener() {

                    /**
                     * Override this method but don't do anything
                     * @param bootstrapFragmentResponse bootstrap fragment response
                     */
                    @Override
                    public void modifyBootstrapFragment(BootstrapFragmentResponse bootstrapFragmentResponse) {
                    }

                    /**
                     * Add the responsive view port settings in the header so we can
                     * have a responsive page
                     * @param bootstrapPageResponse bootstrap response
                     */
                    @Override
                    public void modifyBootstrapPage(BootstrapPageResponse bootstrapPageResponse) {
                        bootstrapPageResponse.getDocument().head().prependElement("meta").attr("name", "viewport").
                                attr("content", "width=device-width");
                    }
                });
            }
        });
    }
}
