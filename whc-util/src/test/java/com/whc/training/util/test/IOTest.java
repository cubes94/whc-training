package com.whc.training.util.test;

import com.alibaba.fastjson.JSON;
import com.whc.common.constants.CharsetConstants;
import com.whc.common.constants.Constants;
import com.whc.common.constants.FileConstants;
import com.whc.util.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * IO单元测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月19 17:46
 */
@Slf4j
public class IOTest {

    /**
     * 删除测试文件
     */
    @Test
    public void deleteAllTestFile() {
        deleteTestFile();
    }

    /**
     * JAVA IO: Pipes
     * <p>
     * 一个JVM中不同线程可以使用管道进行通讯
     * 传递字节数据{@link PipedInputStream} & {@link PipedOutputStream}
     * 传递字符数据{@link PipedReader} & {@link PipedWriter}
     */
    @Test
    public void testPipe() throws Exception {
        final PipedOutputStream outputStream = new PipedOutputStream();
        final PipedInputStream inputStream = new PipedInputStream(outputStream);
        Thread t1 = new Thread(() -> {
            try {
                outputStream.write(DEFAULT_DATA.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                int count;
                byte[] buffer = new byte[1024];
                StringBuilder sb = new StringBuilder();
                while ((count = inputStream.read(buffer)) != -1) {
                    sb.append(new String(buffer, 0, count, CharsetConstants.UTF8));
                }
                inputStream.close();
                log.info("读取数据：{}", sb);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();

        final PipedWriter writer = new PipedWriter();
        final PipedReader reader = new PipedReader(writer);
        Thread t3 = new Thread(() -> {
            try {
                writer.write(DEFAULT_DATA);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread t4 = new Thread(() -> {
            try {
                int data;
                StringBuilder sb = new StringBuilder();
                while ((data = reader.read()) != -1) {
                    sb.append((char) data);
                }
                reader.close();
                log.info("读取数据：{}", sb);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t3.start();
        t4.start();

        Thread.sleep(3000);
    }

    /**
     * JAVA IO: Networking
     * <p>
     * 网络传输，当两个进程建了了网络连接之后，可以通过流进行数据交互
     */
    @Test
    public void testNetworking() throws Exception {
        InputStream inputStream = new FileInputStream(createFile(FileConstants.FILE_EXT_TXT, DEFAULT_DATA));
        String content = StreamUtils.copyToString(inputStream, Charset.forName(CharsetConstants.UTF8));
        log.info("content: {}", content);
    }

    /**
     * JAVA IO: Byte & Char Arrays
     * <p>
     * 从内存读取字节数组{@link ByteArrayInputStream}和字符数组{@link CharArrayReader}
     * 往内存写入字节数组{@link ByteArrayOutputStream}和字符数组{@link CharArrayWriter}
     */
    @Test
    public void testByteAndCharArray() throws Exception {
        File txtFile = createFile(FileConstants.FILE_EXT_TXT, DEFAULT_DATA);
        // 读取字节数组
        byte[] bytes = FileUtils.readFileToByteArray(txtFile);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        List<Character> byteList = new ArrayList<>();
        StringBuilder byteSb = new StringBuilder();
        int data;
        while ((data = inputStream.read()) != -1) {
            byteList.add((char) data);
            byteSb.append((char) data);
        }
        inputStream.close();
        log.info("读取字节数组：{}", JSON.toJSONString(byteList.toArray()));
        // 读取字符数组
        char[] chars = FileUtils.readFileToString(txtFile, CharsetConstants.UTF8).toCharArray();
        Reader reader = new CharArrayReader(chars);
        List<Character> charList = new ArrayList<>();
        StringBuilder charSb = new StringBuilder();
        while ((data = reader.read()) != -1) {
            charList.add((char) data);
            charSb.append((char) data);
        }
        reader.close();
        log.info("读取字符数组：{}", JSON.toJSONString(charList.toArray()));
        // 写入字节数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(bytes);
        outputStream.write(bytes);
        outputStream.write(byteSb.toString().getBytes());
        log.info("写入字节数组：{}", new String(outputStream.toByteArray()));
        outputStream.close();
        // 写入字符数组
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        charArrayWriter.write(chars);
        charArrayWriter.write(chars);
        charArrayWriter.write(charSb.toString().toCharArray());
        log.info("写入字符数组：{}", new String(charArrayWriter.toCharArray()));
    }

    /**
     * JAVA IO: Buffer
     * <p>
     * 缓冲区
     * 默认缓冲区大小为8192字节，即8KB
     * {@link InputStream} & {@link BufferedInputStream}
     * {@link BufferedReader} & {@link BufferedWriter}
     */
    @Test
    public void testBuffer() throws Exception {
        File txtFile = createFile(FileConstants.FILE_EXT_TXT);

        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(txtFile));
        for (int i = 0; i < 10; i++) {
            outputStream.write(DEFAULT_DATA.getBytes());
        }
        // 需要手动flush确保写入到此输出流的数据真正写入到磁盘或网络中
        outputStream.flush();
        outputStream.close();

        InputStream inputStream = new BufferedInputStream(new FileInputStream(txtFile));
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[10240];
        int count;
        while ((count = inputStream.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, count));
        }
        log.info("读取数据：{}", sb);
        // 关闭的时候只需要关闭最外层的流
        inputStream.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile, true));
        for (int i = 0; i < 10; i++) {
            writer.write(DEFAULT_DATA);
        }
        writer.flush();
        writer.close();

        BufferedReader reader = new BufferedReader(new FileReader(txtFile));
        StringBuilder sb1 = new StringBuilder();
        char[] buffer1 = new char[1024];
        int count1;
        while ((count1 = reader.read(buffer1)) != -1) {
            sb1.append(new String(buffer1, 0, count1));
        }
        reader.close();
        log.info("读取数据：{}", sb1);
    }

    /**
     * JAVA IO: {@link RandomAccessFile}
     * <p>
     * 随机流
     */
    @Test
    public void testRandomAccessFile() throws Exception {
        String pathname = path + File.separator + DEFAULT_FILE_NAME + FileConstants.FILE_EXT_TXT;
        RandomAccessFile randomAccessFile = new RandomAccessFile(pathname, "rw");
        // 移动文件指针
        randomAccessFile.seek(200);
        long pointer = randomAccessFile.getFilePointer();
        log.info("指针所在位置：{}", pointer);
        // 写入数据
        for (int i = 0; i < 10; i++) {
            randomAccessFile.write(DEFAULT_DATA.getBytes());
        }
        randomAccessFile.seek(0);
        // 读取数据
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[1024];
        int count;
        while ((count = randomAccessFile.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, count, CharsetConstants.UTF8));
        }
        log.info("读取数据：{}", sb);
        randomAccessFile.close();
    }

    /**
     * JAVA IO: ObjectInputStream & ObjectOutputStream
     * <p>
     * 可以将{@link InputStream}包装到{@link ObjectInputStream}中，就可以从流中读取对象了
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testObjectStream() throws Exception {
        File txtFile = createFile(FileConstants.FILE_EXT_TXT);
        Response<Response<String>> response = Response.ok(Response.ok(DEFAULT_DATA));

        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(txtFile));
        outputStream.writeObject(response);
        outputStream.close();
        log.info("输出数据：{}", response);
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(txtFile));
        response = (Response<Response<String>>) inputStream.readObject();
        inputStream.close();
        log.info("读取数据：{}", response);
    }

    /**
     * JAVA IO: InputStreamReader & OutputStreamWriter
     * <p>
     * {@link InputStreamReader} & {@link OutputStreamWriter}
     * 字节流可以转换为字符流，类似适配器模式的思想
     */
    @Test
    public void testStreamReader() throws Exception {
        File txtFile = createFile(FileConstants.FILE_EXT_TXT);

        OutputStream outputStream = new FileOutputStream(txtFile);
        Writer writer = new OutputStreamWriter(outputStream, CharsetConstants.UTF8);
        writer.write(DEFAULT_DATA);
        writer.close();

        InputStream inputStream = new FileInputStream(txtFile);
        Reader reader = new InputStreamReader(inputStream, CharsetConstants.UTF8);
        int data;
        StringBuilder sb = new StringBuilder();
        while ((data = reader.read()) != -1) {
            sb.append((char) data);
        }
        reader.close();
        log.info("读取数据：{}", sb);
    }

    /**
     * JAVA IO: FileReader & FileWriter
     * <p>
     * {@link FileReader} & {@link FileWriter}
     * 以字符流的形式读取文件
     * FileWriter不能指定编码，可以通过OutputStreamWriter配合FileOutputStream使用
     */
    @Test
    public void testFileReader() throws Exception {
        File txtFile = createFile(FileConstants.FILE_EXT_TXT);

        Writer writer = new FileWriter(txtFile);
        writer.write(DEFAULT_DATA);
        writer.close();

        Reader reader = new FileReader(txtFile);
        int data;
        StringBuilder sb = new StringBuilder();
        while ((data = reader.read()) != -1) {
            sb.append((char) data);
        }
        reader.close();
        log.info("读取数据：{}", sb);
    }

    /**
     * JAVA IO: PushbackInputStream & PushbackReader
     * <p>
     * {@link PushbackInputStream} & {@link PushbackReader} 可以把读取到的字节（符）重新推回到InputStream
     */
    @Test
    public void testPushback() throws Exception {
        File txtFile = createFile(FileConstants.FILE_EXT_TXT, DEFAULT_DATA, CharsetConstants.GBK);

        // 可以设置推回缓冲区的大小，下面例子设置了8个字节的缓冲区
        int preDataLength = 8;
        PushbackInputStream inputStream = new PushbackInputStream(new FileInputStream(txtFile), preDataLength);
        byte[] bytes = new byte[preDataLength];
        int count = inputStream.read(bytes);
        if (count != -1) {
            String preDataStr = new String(bytes, 0, count, CharsetConstants.GBK);
            log.info("预读数据：{}", preDataStr);
            inputStream.unread(bytes);
        }
        log.info("读取数据：{}", StreamUtils.copyToString(inputStream, Charset.forName(CharsetConstants.GBK)));
        inputStream.close();

        // 8个字符的缓冲区
        PushbackReader reader = new PushbackReader(new FileReader(txtFile), preDataLength);
        char[] chars = new char[preDataLength];
        count = reader.read(chars);
        if (count != -1) {
            String preDataStr = new String(chars, 0, count);
            log.info("预读数据：{}", preDataStr);
            reader.unread(chars);
        }
        StringBuilder sb = new StringBuilder();
        while ((count = reader.read(chars)) != -1) {
            sb.append(new String(chars, 0, count));
        }
        log.info("读取数据：{}", sb);
        reader.close();
    }

    /**
     * JAVA IO: PrintWriter & PrintStream
     * <p>
     * {@link PrintWriter} & {@link PrintStream} 允许将格式化数据（而非它们的字节/符数据）写入到底层OutputStream中
     */
    @Test
    public void testPrint() throws Exception {
        File txtFile = createFile(FileConstants.FILE_EXT_TXT, DEFAULT_DATA, CharsetConstants.GBK);

        OutputStream outputStream = new FileOutputStream(txtFile, true);
        PrintStream printStream = new PrintStream(outputStream);
        printStream.println();
        printStream.println(Integer.MAX_VALUE);
        printStream.println(new Date());
        printStream.close();

        outputStream = new FileOutputStream(txtFile, true);
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println();
        printWriter.println(Integer.MAX_VALUE);
        printWriter.println(new Date());
        printWriter.close();
    }

    /**
     * JAVA IO: Other Stream
     * <p>
     * 其他字节流
     */
    @Test
    public void testOtherStream() throws Exception {
        File txtFile = createFile(FileConstants.FILE_EXT_TXT, DEFAULT_DATA, CharsetConstants.GBK);
        File xlsFile = createFile(FileConstants.FILE_EXT_XLS, StringUtils.reverse(DEFAULT_DATA));

        // SequenceInputStream把一个或者多个InputStream整合起来，形成一个逻辑连贯的输入流。
        InputStream inputStream1 = new FileInputStream(txtFile);
        InputStream inputStream2 = new FileInputStream(xlsFile);
        InputStream combinedInputStream = new SequenceInputStream(inputStream1, inputStream2);
        log.info("读取数据：{}", StreamUtils.copyToString(combinedInputStream, Charset.forName(CharsetConstants.UTF8)));
        combinedInputStream.close();
    }

    /**
     * JAVA IO: Other Reader
     * <p>
     * 其他字符流
     */
    @Test
    public void testOtherReader() throws Exception {
        File txtFile = createFile(FileConstants.FILE_EXT_TXT, multiData());

        // LineNumberReader记录了已读取数据行号的BufferedReader
        // 默认情况下，行号从0开始，当LineNumberReader读取到行终止符时，行号会递增（\n \r \n\r）
        // 可以通过getLineNumber()方法获取当前行号，通过setLineNumber()方法设置当前行数
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(txtFile));
        char[] buffer = new char[24];
        int count;
        StringBuilder sb = new StringBuilder();
        while ((count = lineNumberReader.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, count));
            sb.append("\n\r------------").append(lineNumberReader.getLineNumber()).append("-----------\n\r");
        }
        log.info("读取数据：{}", sb);

        // StringWriter能够以字符串的形式从Writer中获取写入到其中数据
        StringWriter stringWriter = new StringWriter();
        stringWriter.write(multiData());

        // StringReader能够将原始字符串转换成Reader
        StringReader stringReader = new StringReader(stringWriter.getBuffer().toString());

        // StreamTokenizer可以把输入流分解成一系列符号(推荐使用Reader构造对象)
        // ttype 读取到的符号的类型(字符，数字，或者行结尾符)
        // sval 如果读取到的符号是字符串类型，该变量的值就是读取到的字符串的值
        // nval 如果读取到的符号是数字类型，该变量的值就是读取到的数字的值
        StreamTokenizer tokenizer = new StreamTokenizer(stringReader);
        // TT_EOF流末尾，TT_EOL行末尾
        while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            switch (tokenizer.ttype) {
                case StreamTokenizer.TT_WORD:
                    log.info("读取字符串：{}", tokenizer.sval);
                case StreamTokenizer.TT_NUMBER:
                    log.info("读取数字：{}", tokenizer.nval);
                case StreamTokenizer.TT_EOL:
                    log.info("行末尾");
                default:
                    log.info("");
            }
        }
    }


    protected static final String DEFAULT_DATA = "开始 is END";

    protected static final String DEFAULT_FILE_NAME = "test_test";

    protected static String path;

    protected static String multiData() {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            content.append(DEFAULT_DATA).append("\n").append(DEFAULT_DATA).append(Integer.MAX_VALUE).append("\n\r");
        }
        return content.toString();
    }

    protected static File createFile(String fileExt) throws Exception {
        return createFile(fileExt, Constants.EMPTY_STRING);
    }

    protected static File createFile(String fileExt, String content) throws Exception {
        return createFile(fileExt, content, CharsetConstants.UTF8);
    }

    protected static File createFile(String fileExt, String content, String charset) throws Exception {
        String pathname = path + File.separator + DEFAULT_FILE_NAME + fileExt;
        File file = new File(pathname);
        if (!file.exists()) {
            FileUtils.touch(file);
        }
        FileUtils.writeStringToFile(file, content, charset);
        return file;
    }

    protected static void deleteTestFile() {
        File[] files = new File(path).listFiles();
        if (files != null && files.length > 0) {
            Arrays.stream(files).forEach(file -> {
                if (Objects.equals(file.getName().split("\\.")[0], DEFAULT_FILE_NAME)) {
                    log.info("delete {} : {}", file.getName(), file.delete());
                }
            });
        }
    }

    static {
        try {
            path = new File(Constants.EMPTY_STRING).getCanonicalPath();
            deleteTestFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
