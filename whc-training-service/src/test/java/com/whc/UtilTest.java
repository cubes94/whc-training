package com.whc;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.*;
import org.junit.Test;

import java.util.Map;

/**
 * 工具类测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月16 17:35
 */
public class UtilTest {

    @Test
    public void utilTest() throws Exception {
        // 设置为只读权限
        WriterProperties writerProperties = new WriterProperties();
        writerProperties.setStandardEncryption(null, null,
                EncryptionConstants.ALLOW_PRINTING, EncryptionConstants.STANDARD_ENCRYPTION_128);
        PdfReader pdfReader = new PdfReader("C:\\Users\\whc\\Desktop\\1.pdf");
        PdfWriter pdfWriter = new PdfWriter("C:\\Users\\whc\\Desktop\\2.pdf", writerProperties);
        PdfDocument readerDocumentPdf = new PdfDocument(pdfReader);
        PdfDocument writerDocumentPdf = new PdfDocument(pdfWriter);
        readerDocumentPdf.copyPagesTo(1, 3, writerDocumentPdf, new PdfPageFormCopier());
        readerDocumentPdf.copyPagesTo(3, 3, writerDocumentPdf, new PdfPageFormCopier());
        readerDocumentPdf.copyPagesTo(3, 3, writerDocumentPdf, new PdfPageFormCopier());
        readerDocumentPdf.close();
        writerDocumentPdf.close();
//        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
//        Map<String, PdfFormField> fields = form.getFormFields();
//
//        // 设置表单域不可编辑
//        form.flattenFields();
    }

    /**
     * 使用pdf 模板生成 pdf 文件，并设置为只读pdf
     *
     * @param templatePath 模板文件路径
     * @param newPDFPath   目标文件路径（覆盖）
     * @param param        参数
     */
    public static void fillTemplate(String templatePath, String newPDFPath, Map<String, String> param) throws Exception {

        try {
            // 设置为只读权限
            WriterProperties writerProperties = new WriterProperties();
            writerProperties.setStandardEncryption(null, null,
                    EncryptionConstants.ALLOW_PRINTING, EncryptionConstants.STANDARD_ENCRYPTION_128);
            PdfWriter pdfWriter = new PdfWriter(newPDFPath, writerProperties);
            PdfDocument pdf = new PdfDocument(new PdfReader(templatePath), pdfWriter);
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
            Map<String, PdfFormField> fields = form.getFormFields();

            // 获取文本域名称，填充文本域
            for (Map.Entry<String, String> entry : param.entrySet()) {
                if (fields.containsKey(entry.getKey())) {
                    PdfFormField formField = fields.get(entry.getKey());
                    formField.setValue(entry.getValue());
                }
            }
            // 设置表单域不可编辑
            form.flattenFields();
            pdf.close();
        } catch (Exception e) {
            throw e;
        }
    }

}
