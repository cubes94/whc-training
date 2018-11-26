package com.whc.training.test.util;

import com.whc.common.constants.FileConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO单元测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月24 11:53
 */
@Slf4j
public class NIOTest extends IOTest {

    // 标准的IO基于字节流和字符流进行操作的，而NIO是基于通道（Channel）和缓冲区（Buffer）进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。
    // Java NIO可以让你非阻塞的使用IO，例如：当线程从通道读取数据到缓冲区时，线程还是可以进行其他事情。当数据被写入到缓冲区时，线程可以继续处理它。从缓冲区写入通道也类似
    // Java NIO引入了选择器的概念，选择器用于监听多个通道的事件（比如：连接打开，数据到达）。因此，单个的线程可以监听多个数据通道。

    /**
     * JAVA NIO: Channel & Buffer
     * <p>
     * {@link java.nio.channels.Channel}
     * Java NIO的通道类似流，但有几点不同
     * 1. 既可以从通道中读取数据，又可以写数据到通道，但流的读写通常是单向的
     * 2. 通道可以异步地读写
     * 3. 通道中的数据总是要先读到一个Buffer，或者总是要从一个Buffer中写入
     * <p>
     * {@link java.nio.Buffer}
     * 缓冲区是一块可以写入数据，然后可以从中读取数据的内存
     * 写入数据到Buffer->调用flip()方法(从写模式切换到读模式，将position设回0，并将limit设置成之前position的值)->从Buffer中读取数据->调用clear()(清空整个缓冲区)方法或者compact()(清除已经读过的数据)方法
     * Buffer三属性，capacity(容量)/position(当前位置)/limit(写模式下等于capacity，表示最多写数据量；读模式下等于写模式下最后的position，表示最多读数据量)
     */
    @Test
    public void testChannel() throws Exception {
        // FileChannel 从文件中读写数据。
        // 需要通过InputStream/OutputStream/RandomAccessFile获取FileChannel
        File file = createFile(FileConstants.FILE_EXT_TXT, multiData());

        RandomAccessFile randomAccessFile = new RandomAccessFile(file.getPath(), "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        // 跟缓冲区分配大小
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        StringBuilder sb = new StringBuilder();
        while (fileChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            sb.append(new String(byteBuffer.array()));
            byteBuffer.clear();
        }
        randomAccessFile.close();
        log.info("读取数据：{}", sb);

        // DatagramChannel 能通过UDP读写网络中的数据。

        // SocketChannel 能通过TCP读写网络中的数据。

        // ServerSocketChannel可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。
    }

    /**
     * JAVA NIO: Scatter/Gather
     * <p>
     * Scatter/Gather常用于将传输的数据分开处理的场合
     * scatter(分散)从channel中读取时将数据写入多个buffer中
     * gather(聚集)将多个buffer的数据写入同一个channel中
     */
    @Test
    public void testScatter$Gather() throws Exception {
        File file = createFile(FileConstants.FILE_EXT_TXT, multiData());
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileInputChannel = fileInputStream.getChannel();
        // Scattering Reads
        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body = ByteBuffer.allocate(1024);
        ByteBuffer[] bufferArray = new ByteBuffer[]{header, body};
        fileInputChannel.read(bufferArray);
        log.info("header:{}", new String(header.array()));
        log.info("body:{}", new String(body.array()));
        fileInputChannel.close();
        fileInputStream.close();

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        body.flip();
        header.flip();
        ByteBuffer[] bufferArray2 = {body, header};
        FileChannel fileOutputChannel = fileOutputStream.getChannel();
        fileOutputChannel.write(bufferArray2);
        fileOutputChannel.close();
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    /**
     * JAVA NIO: 通道之间的数据传输
     * <p>
     * 若两个通道中有一个是FileChannel，则可以直接从一方传数据到另一方
     */
    @Test
    public void transferData() throws Exception {
        File dataFile = createFile(FileConstants.FILE_EXT_SQL, multiData());
        FileInputStream fileInputStream = new FileInputStream(dataFile);

        File emptyFile = createFile(FileConstants.FILE_EXT_TXT);
        FileOutputStream fileOutputStream = new FileOutputStream(emptyFile);

        // transferFrom()可以将数据从源通道传输到FileChannel
        fileOutputStream.getChannel().transferFrom(fileInputStream.getChannel(), 0, fileInputStream.getChannel().size() / 2);
        // transferTo()可以将数据从FileChannel传输到其他的channel
        fileInputStream.getChannel().transferTo(0, fileInputStream.getChannel().size(), fileOutputStream.getChannel());

        fileInputStream.close();
        fileOutputStream.close();
    }
}
