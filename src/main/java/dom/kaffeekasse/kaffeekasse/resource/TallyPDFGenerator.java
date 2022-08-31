/*
 * (C) 2022; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.kaffeekasse.kaffeekasse.resource;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dom.kaffeekasse.kaffeekasse.ApplicationConfiguration;
import dom.kaffeekasse.kaffeekasse.dao.Dao;
import dom.kaffeekasse.kaffeekasse.dao.StateDao;
import dom.kaffeekasse.kaffeekasse.entity.AccountPeriod;
import dom.kaffeekasse.kaffeekasse.entity.Participant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.OutputStream;
import java.util.List;
import java.util.ResourceBundle;

/**
 * generates tally pdf.
 *
 * @author jfischer
 * @version $Id: $Id
 */
@ApplicationScoped
public class TallyPDFGenerator {

    @Inject
    private ApplicationConfiguration applicationConfiguration;

    @Inject
    private Dao dao;

    @Inject
    private StateDao stateDao;

    /**
     * width of left margin.
     */
    private static final int DOCUMENT_LEFT_MARGIN = 37;

    /**
     * width of right margin.
     */
    private static final int DOCUMENT_RIGHT_MARGIN = 37;

    /**
     * height of top margin.
     */
    private static final int DOCUMENT_TOP_MARGIN = 37;

    /**
     * height of bottom margin.
     */
    private static final int DOCUMENT_BOTTOM_MARGIN = 37;

    /**
     * width of participant's table column.
     */
    private static final float TABLE_PARTICIPANT_MARGIN = 71F;

    /**
     * width of deposit's table column.
     */
    private static final float TABLE_DEPOSIT_MARGIN = 31F;

    /**
     * width of table column which contains number of cups.
     */
    private static final float TABLE_NR_CUPS_MARGIN = 31F;

    /**
     * width of table column where participants can make strokes.
     */
    private static final float TABLE_TALLY_MARGIN = 239F;

    /**
     * generate pdf file containing a tally in which participants marks
     * the number of cups of coffee taken.
     *
     * @param ostream target to which pdf file is writtten.
     */
    @Transactional
    public void generateTallyPDF(OutputStream ostream) {

        ResourceBundle bundle = this.applicationConfiguration.bundle();
        
        AccountPeriod currentAccountPeriod
                = this.stateDao.getCurrentAccountPeriod();
        int price = currentAccountPeriod.getPrice();

        Rectangle pageSize = PageSize.A4;
        Document document
                = new Document(
                        pageSize,
                        DOCUMENT_LEFT_MARGIN,
                        DOCUMENT_RIGHT_MARGIN,
                        DOCUMENT_TOP_MARGIN,
                        DOCUMENT_BOTTOM_MARGIN);
        try {
            PdfWriter.getInstance(document, ostream);

            document.addCreator("kaffeekasseEE v0.1");

            document.open();

            {
                Paragraph coffeePriceParagraph
                        = new Paragraph(bundle.getString("pricePerCup") + ": " + price + " cent");
                document.add(coffeePriceParagraph);
            }

            {
                float[] margins = {
                    TABLE_PARTICIPANT_MARGIN,
                    TABLE_DEPOSIT_MARGIN,
                    TABLE_NR_CUPS_MARGIN,
                    TABLE_TALLY_MARGIN};

                PdfPTable table
                        = new PdfPTable(margins);
                table.setHorizontalAlignment(0);
                table.addCell(bundle.getString("participant"));
                table.addCell(bundle.getString("deposit"));
                table.addCell(bundle.getString("nrOfCups"));
                table.addCell(bundle.getString("tally"));

                List<Participant> participants = this.dao.listActiveParticipants();
                for (Participant participant : participants) {
                    table.addCell(participant.getName());
                    table.addCell("" + participant.getDeposit());
                    table.addCell("" + participant.getNrCups());
                    table.addCell("");
                }

                document.add(table);
            }

            document.close();
        } catch (DocumentException ex) {
            this.applicationConfiguration.logger().severe(ex.getMessage());
        }
    }
}
