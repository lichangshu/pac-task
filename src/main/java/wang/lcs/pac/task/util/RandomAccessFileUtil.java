/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.util;

import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author changshu.li
 */
public class RandomAccessFileUtil {

	private static final Logger logger = LoggerFactory.getLogger(wang.lcs.pac.task.util.LogUncaughtExceptionHandler.class);

	public static InputStream translate2inputStream(RandomAccessFile access) throws IOException {
		return translate2inputStream(access, 0);
	}

	public static InputStream translate2inputStream(RandomAccessFile access, long position) throws IOException {
		PipedOutputStream out = new PipedOutputStream();
		PipedInputStream input = new PipedInputStream(out);
		Thread th = new ReadThread(access, out, position);
		th.setUncaughtExceptionHandler(new LogUncaughtExceptionHandler());
		th.start();
		return input;
	}

	public static OutputStream translate2outputStream(RandomAccessFile access) throws IOException {
		return translate2outputStream(access, 0);
	}

	public static OutputStream translate2outputStream(RandomAccessFile access, long position) throws IOException {
		PipedInputStream input = new PipedInputStream();
		final Thread th = new WriterThread(access, input, position);
		final PipedOutputStream out = new PipedOutputStream(input){

			@Override
			public void close() throws IOException {
				super.close();
				try {//等待将所有内容写入文件
					th.join();
				} catch (InterruptedException ex) {
					Thread.interrupted();
					throw new RuntimeException(ex);
				}
			}
		};
		th.setUncaughtExceptionHandler(new LogUncaughtExceptionHandler());
		th.start();
		return out;
	}

	private static class ReadThread extends Thread {

		private final RandomAccessFile access;
		private final PipedOutputStream out;
		private final long position;

		public ReadThread(RandomAccessFile access, PipedOutputStream out, long position) {
			this.access = access;
			this.out = out;
			this.position = position;
			this.setName("randomaccess2outputStream");
		}

		@Override
		public void run() {
			byte[] buffer = new byte[1];//10K
			try (OutputStream fo = out) {
				access.seek(position);
				while (true) {
					int len = access.read(buffer);
					if (len >= 0) {
						fo.write(buffer, 0, len);
						fo.flush();
					} else {
						break;
					}
				}
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	private static class WriterThread extends Thread {

		private final RandomAccessFile access;
		private final PipedInputStream input;
		private final long position;

		public WriterThread(RandomAccessFile access, PipedInputStream in, long position) {
			this.access = access;
			this.input = in;
			this.position = position;
		}

		@Override
		public void run() {
			byte[] buffer = new byte[1];//10K
			try (InputStream in = input) {
				access.seek(position);
				while (true) {
					int len = in.read(buffer);
					if (len >= 0) {
						access.write(buffer, 0, len);
					} else {
						break;
					}
				}
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

}
