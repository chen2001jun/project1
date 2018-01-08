package com.lld360.cnc.core.utils;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: dhc
 * Date: 2016-09-01 17:22
 */
public class DocUtils {
    /**
     * 将Office文档转换为PDF文件
     *
     * @param converter 文档转换器
     * @param docFile   Office文件（doc,docx,xls,xlsx,ppt,pptx,txt等)
     * @param pdfFile   需要生成的PDF文件
     * @return 生成后的PDF文件
     */
    public static File convertOffice2Pdf(DocumentConverter converter, File docFile, String pdfFile) {
        if (docFile.exists()) {
            File pf = new File(pdfFile);
            if ((pf.getParentFile().exists() && pf.getParentFile().isDirectory()) || pf.getParentFile().mkdirs()) {
                DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();
                DocumentFormat doc = formatReg.getFormatByFileExtension("odt");
                DocumentFormat pdf = formatReg.getFormatByFileExtension("pdf");
                converter.convert(docFile, doc, pf, pdf);
                return pf;
            }
        }
        return null;
    }

    /**
     * 转换PDF文件为图片
     *
     * @param pdfFile   PDF文件
     * @param imagePath 图片文件目录
     * @param pages     最多生成页
     * @return 图片列表
     */
    public static List<String> convertPdf2Images(File pdfFile, String imagePath, int pages) {
        List<String> images = new ArrayList<>();

        if (pdfFile.exists()) {
            float scale = 2f; // 放大倍数

            try {
                PDDocument document = PDDocument.load(pdfFile);
                File imgDir = new File(imagePath);
                if ((imgDir.exists() && imgDir.isDirectory()) || imgDir.mkdirs()) {
                    PDFRenderer renderer = new PDFRenderer(document);
                    for (int i = 0; i < Math.min(document.getNumberOfPages(), pages); i++) {
                        BufferedImage image = renderer.renderImage(i, scale, ImageType.RGB);
                        String imgFile = String.format("%s/%02d_%s.jpg", imagePath, i + 1, RandomStringUtils.randomAlphanumeric(6));
                        File img = new File(imgFile);
                        ImageIO.write(image, "jpg", img);
                        images.add(img.getName());
                        image.flush();
                    }
                    document.close();
                    return images;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    /**
     * 将Office文档转换为图片
     *
     * @param converter  文档转换器
     * @param docFile    Office文件（doc,docx,xls,xlsx,ppt,pptx,txt等)
     * @param imagesPath 图片文件目录
     * @param pages      最多生成页
     * @param keepPdf    是否保留中间PDF文件
     * @return 图片列表
     */
    public static List<String> convertOffice2Images(DocumentConverter converter, File docFile, String imagesPath, int pages, boolean keepPdf) {
        if (docFile.exists()) {
            String pdfFile = docFile.getParentFile().getAbsolutePath() + "/" + docFile.getName() + ".pdf";
            File pdf = convertOffice2Pdf(converter, docFile, pdfFile);
            List<String> images = convertPdf2Images(pdf, imagesPath, pages);
            if (!keepPdf) {
                FileUtils.deleteQuietly(pdf);
            }
            return images;
        }
        return null;
    }

    /**
     * 获取TXT文件的字符编码
     *
     * @param txt txt文件
     * @return 文件编码
     * @throws IOException 文件不存在或读取失败
     */
    public static String getTxtFileCharset(File txt) throws IOException {
        FileInputStream fis = new FileInputStream(txt);
        BufferedInputStream bin = new BufferedInputStream(fis);
        int p = (bin.read() << 8) + bin.read();
        bin.close();
        fis.close();
        switch (p) {
            case 12783:
            case 58240:
            case 61371:
                return "UTF-8";
            case 2608:
                return "Unicode";
            case 12298:
                return "UTF-16BE";
            case 33139:
                return "Shift-JIS";
            default:
                return "GBK";
        }
    }
}
