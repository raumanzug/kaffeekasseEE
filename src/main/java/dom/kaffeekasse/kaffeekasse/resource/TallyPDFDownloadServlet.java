/*
 * (C) 2022; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.kaffeekasse.kaffeekasse.resource;

import dom.kaffeekasse.kaffeekasse.ApplicationConfiguration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * provides download link for tally pdf.
 *
 * @author jfischer
 * @version $Id: $Id
 */
@ApplicationScoped
@WebServlet("/tally.pdf")
public class TallyPDFDownloadServlet extends HttpServlet {

    @Inject
    private ApplicationConfiguration applicationConfiguration;

    @Inject
    private ServletContext servletContext;

    @Inject
    private TallyPDFGenerator tallyPdfGenerator;

    /**
     * retrieve url string where tally pdf can be downloaded.
     *
     * @return url string.
     */
    public String getDownloadUrl() {
        return this.servletContext.getContextPath() + "/tally.pdf";
    }

    /** {@inheritDoc} */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename=\"tally.pdf\"");

        ByteArrayOutputStream intermediateStore = new ByteArrayOutputStream();
        tallyPdfGenerator.generateTallyPDF(intermediateStore);
        resp.setContentLength(intermediateStore.size());
        try (
                 OutputStream httpStream = resp.getOutputStream();) {
            intermediateStore.writeTo(httpStream);
            httpStream.flush();
        } catch (IOException ex) {
            this.applicationConfiguration.logger().severe(ex.getMessage());
        }

    }

}
